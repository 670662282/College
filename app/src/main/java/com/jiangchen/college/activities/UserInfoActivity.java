package com.jiangchen.college.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.jiangchen.college.AssistantTool.Code;
import com.jiangchen.college.Log.LogUtil;
import com.jiangchen.college.R;
import com.jiangchen.college.entity.Result;
import com.jiangchen.college.entity.User;
import com.jiangchen.college.https.BaseRequestCallBack;
import com.jiangchen.college.https.XUtils;
import com.jiangchen.college.views.TitleView;
import com.jiangchen.college.views.UserInfoView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Dell- on 2015/12/9 0009.
 */
public class UserInfoActivity extends BaseActivity {

    @ViewInject(R.id.userinfo_t)
    private TitleView titleView;
    @ViewInject(R.id.userinfo_photo)
    private UserInfoView userPhoto;
    @ViewInject(R.id.userinfo_name)
    private UserInfoView userName;
    @ViewInject(R.id.userinfo_nick)
    private UserInfoView userNick;
    @ViewInject(R.id.userinfo_sex)
    private UserInfoView userSex;
    @ViewInject(R.id.userinfo_save)
    private Button btSave;
    private InputStream is;
    private int size;

    private User user;
    private int gender = -1;
    private boolean needUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        ViewUtils.inject(this);
        titleView.setImageBackClickListener(clickLis);
        btSave.setOnClickListener(clickLis);
        userPhoto.setOnClickListener(clickLis);
        userName.setOnClickListener(clickLis);
        userNick.setOnClickListener(clickLis);
        userSex.setOnClickListener(clickLis);
    }

    @Override
    protected void onResume() {
        initData();
        super.onResume();
    }

    //初始化数据 获取用户的信息
    private void initData() {
        user = ((MyApp) getApplication()).getUser();
        if (user == null) {
            XUtils.show(R.string.need_login);
            finish();
            return;
        }
        LogUtil.i("=======TAG=====", user.toString());
        userName.setText(user.getName() == null ? "" : user.getName());
        userNick.setText(user.getNick() == null ? "" : user.getNick());
        userSex.setText(user.getGender() == 0 ? getString(R.string.boy) : getString(R.string.gril));
        userPhoto.setRightImageUri(user.getPhotoUrl());

    }

    private View.OnClickListener clickLis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img_back:
                    finish();
                    break;
                case R.id.userinfo_save:
                    save();
                    break;
                case R.id.userinfo_photo:
                    showPhoto();
                    break;
                case R.id.userinfo_sex:
                    showSex();
                    break;
                default:
                    break;
            }
        }
    };

    //上传数据到服务端
    private void save()  {

        String nName = userName.getText().trim();
        String nNick = userNick.getText().trim();

        RequestParams params = new RequestParams();
        params.addBodyParameter("uid", String.valueOf(user.getUid()));


        if (is != null) {
            needUpdate = true;
            params.addBodyParameter("photourl", is, size, "aa.jpg");
        }
        if (!TextUtils.isEmpty(nName) && nName != user.getName() ) {
            needUpdate = true;
            params.addBodyParameter("name", nName);
        }
        if (!TextUtils.isEmpty(nNick) && nNick != user.getNick()) {
            needUpdate = true;
            params.addBodyParameter("nick", nNick);
        }

        if (gender != -1 && gender != (user.getGender())) {
            needUpdate = true;
            params.addBodyParameter("gender", String.valueOf(gender));
        }

        if (needUpdate) {
            XUtils.send(XUtils.UPDATE, params, new BaseRequestCallBack<Result<User>>() {
                @Override
                public void success(Result<User> data) {
                    if (data.state == Result.STATE_SUC) {
                        ((MyApp) getApplication()).setUser(data.data);
                        setResult(Code.RESP_UPDATE_USERINFO);
                        XUtils.show("保存成功");
                    } else {
                        XUtils.show("上传失败");
                    }
                }
            }, true);


        } else {
            XUtils.show("请改变");
        }
        if (is != null){
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        finish();
    }


    //Intent意图打开本地图库
    private void showPhoto() {
        Intent in = new Intent(Intent.ACTION_PICK)
                .setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(in, Code.REQUEST_IMG_PHOTO);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null && resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == Code.REQUEST_IMG_PHOTO) {
            Uri uri = data.getData();
            //获得当前的内容提供者实例 查询
            Cursor cursor = getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                toCutPhoto(path);
            }
            if (cursor != null){
                cursor.close();
            }
        } else if (requestCode == Code.REQ_CUT_PHOTO) {
            Bitmap bitmap = data.getParcelableExtra("data");
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 70, bos);
            //获取流的长度
            size = bos.size();
            is = new ByteArrayInputStream(bos.toByteArray());
            userPhoto.setRightImage(bitmap);

        }
    }

    //图片裁剪
    public void toCutPhoto(String path) {
        Intent in = new Intent("com.android.camera.action.CROP");
        //设置数据和格式
        in.setDataAndType(Uri.fromFile(new File(path)), "image/*");
        //设置在开启的Intent中设置显示的VIEW可裁剪
        in.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        in.putExtra("aspectX", 1);
        in.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        in.putExtra("outputX", 200);
        in.putExtra("outputY", 200);
        //返回数据
        in.putExtra("return-data", true);

        startActivityForResult(in, Code.REQ_CUT_PHOTO);
    }

    private void showSex() {
        new AlertDialog.Builder(this).setItems(new String[]{"男", "女"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        gender = 0;
                        userSex.setText(R.string.boy);
                        break;
                    case 1:
                        gender = 1;
                        userSex.setText(R.string.gril);
                        break;
                    default:
                        break;
                }
            }
        }).show();

    }


    public static void startActivityForResult(Activity activity) {

        Intent in = new Intent(activity, UserInfoActivity.class);

        activity.startActivityForResult(in, Code.REQ_UPDATE_USERINFO);
    }


}

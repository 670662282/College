package com.jiangchen.college.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiangchen.college.Log.LogUtil;
import com.jiangchen.college.R;
import com.jiangchen.college.entity.Result;
import com.jiangchen.college.entity.User;
import com.jiangchen.college.https.BaseRequestCallBack;
import com.jiangchen.college.https.XUtils;
import com.jiangchen.college.utils.MyAdapter;
import com.jiangchen.college.views.DiaLogFeedBack;
import com.jiangchen.college.views.TitleView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by Dell- on 2015/12/5 0005.
 * 个人中心
 */
public class CenterActivity extends BaseActivity {

    @ViewInject(R.id.center_userinfo)
    private RelativeLayout userInfo;
    @ViewInject(R.id.center_title)
    private TitleView titleView;
    @ViewInject(R.id.center_nick)
    private TextView nickname;
    @ViewInject(R.id.center_account)
    private TextView account;
    @ViewInject(R.id.center_school)
    private TextView school;
    @ViewInject(R.id.center_photo)
    private ImageView userPhoto;
    @ViewInject(R.id.center_listview)
    private ListView centerListView;
    @ViewInject(R.id.center_unlogin)
    private Button centerUnlogin;

    private User user;
    private DiaLogFeedBack dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center);
        ViewUtils.inject(this);
        titleView.setImageBackClickListener(clickLis);
        centerUnlogin.setOnClickListener(clickLis);
        userInfo.setOnClickListener(clickLis);

        user = ((MyApp) getApplication()).getUser();

        if (user != null) {
            LogUtil.e("TGA----PhotoUrl---------", user.toString());
            account.setText(user.getPhone() == null ?
                    (user.getEmail() == null ? "账号：未设置" : "账号：" + user.getEmail())
                    : "账号：" + user.getPhone());
            nickname.setText(user.getNick() == null ? "昵称：未设置" : "昵称：" + user.getNick());
            school.setText(user.getSchool() == null ? "学校：未设置" : "学校：" + user.getSchool());
            if (user.getPhotoUrl() != null && user.getPhotoUrl().length() > 0) {
                XUtils.display(userPhoto, user.getPhotoUrl());
            }
        }

        centerListView.setAdapter(new MyAdapter(this));
        centerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtil.i("TAG", position + "==================");
                switch (position) {
                    case 0:
                        XUtils.show("校内信息");
                        break;
                    case 1:
                        XUtils.show("账号与安全");
                        break;
                    case 2:
                        XUtils.show("系统设置");
                        break;
                    case 3:
                        XUtils.show("意见反馈");
                        showFeckBack();
                        break;
                    default:
                        break;
                }
            }
        });


    }

    private void showFeckBack() {

        dialog = new DiaLogFeedBack(this);
        //实现一个自己定义的监听器
        dialog.setOnClickListener(new DiaLogFeedBack.OnClickListener() {
            @Override
            public void onClick(DialogInterface diglog, String text) {
                feckBack(text);
            }
        });
        dialog.show();
    }

    private void feckBack(String content) {
        if (content.length() < 15) {
            XUtils.show(R.string.text_minlength);
            return;
        }

        if (user == null) {
            XUtils.show(R.string.need_login);
            finish();
            return;
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("uid", String.valueOf(user.getUid()));
        params.addBodyParameter("content", content);

        XUtils.send(XUtils.FEEDBACK, params, new BaseRequestCallBack<Result<Boolean>>() {

            @Override
            public void success(Result<Boolean> data) {

                if (data.descrpit != null && data.descrpit.length() > 0) {
                    XUtils.show(data.descrpit);
                }
                if (dialog != null && data.data) {
                    XUtils.show(R.string.feedback_success);
                    dialog.dismiss();

                }
            }
        }, true);


    }

    private View.OnClickListener clickLis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img_back:
                    finish();
                    break;
                case R.id.center_unlogin:
                    XUtils.show("退出账号");
                    ((MyApp) getApplication()).setUser(null);
                    finish();
                    break;
                case R.id.center_userinfo:
                    XUtils.show("前往个人中心");
                    UserInfoActivity.startActivity(CenterActivity.this);
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    public static void startActivity(Context context) {
        Intent in = new Intent(context, CenterActivity.class);
        context.startActivity(in);
    }
}

package com.jiangchen.college.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;

import com.jiangchen.college.AssistantTool.Matchers;
import com.jiangchen.college.AssistantTool.MyTextWatcher;
import com.jiangchen.college.R;
import com.jiangchen.college.entity.Result;
import com.jiangchen.college.entity.User;
import com.jiangchen.college.https.BaseRequestCallBack;
import com.jiangchen.college.https.XUtils;
import com.jiangchen.college.views.MEditText;
import com.jiangchen.college.views.TitleView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by Dell- on 2015/12/13 0013.
 */
public class RemailActivity extends BaseActivity {
    @ViewInject(R.id.email_title)
    private TitleView title;
    @ViewInject(R.id.email_title)
    private MEditText etInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        ViewUtils.inject(this);
        title.setRightClickListener(clickLis);
        title.setImageBackClickListener(clickLis);
        etInput.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 4) {
                    title.setTextRightEnable(true);
                } else {
                    title.setTextRightEnable(false);
                }
            }
        });
    }

    private View.OnClickListener clickLis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img_back:
                    finish();
                    break;
                case R.id.text_right:
                    signUp();
                    break;
            }
        }
    };


    private void signUp() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.DiglogFeedBack);
        dialog.setCancelable(false);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        String email = etInput.getText();
        User user = ((MyApp)getApplication()).getUser();
        if (user != null){
            XUtils.show(R.string.need_login);
            return ;
        }

        if (!email.matches(Matchers.EMAIL_MATCH)){
            dialog.setMessage("邮件格式错误，必须加@符号");
            dialog.show();
            return;
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("uid", String.valueOf(user.getUid()));
        params.addBodyParameter("email", email);

        XUtils.send(XUtils.UPDATE, params, new BaseRequestCallBack<Result<User>>() {
            @Override
            public void success(Result<User> data) {
                if (data.state == Result.STATE_SUC){
                    if (data.data != null){
                        ((MyApp) getApplication()).setUser(data.data);
                        dialog.setMessage("已经验证成功");
                        dialog.show();
                    }else{

                    }
                }
                finish();
            }
        }, true);

    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, RemailActivity.class));
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}

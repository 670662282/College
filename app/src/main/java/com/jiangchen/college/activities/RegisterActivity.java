package com.jiangchen.college.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jiangchen.college.AssistantTool.Matchers;
import com.jiangchen.college.R;
import com.jiangchen.college.entity.Result;
import com.jiangchen.college.https.BaseRequestCallBack;
import com.jiangchen.college.https.XUtils;
import com.jiangchen.college.views.MEditText;
import com.jiangchen.college.views.TitleView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by Dell- on 2015/12/5 0005.
 */
public class RegisterActivity extends BaseActivity {

    @ViewInject(R.id.reg_title)
    private TitleView title;
    @ViewInject(R.id.reg_account)
    private MEditText mEmail;
    @ViewInject(R.id.reg_pwd)
    private MEditText mPwd;
    @ViewInject(R.id.reg_repwd)
    private MEditText mRePwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ViewUtils.inject(this);
        //null ?
        title.setRightClickListener(onClickLis);
        title.setImageBackClickListener(onClickLis);


    }


    private View.OnClickListener onClickLis = new View.OnClickListener() {

        @Override
        public void onClick(View v) {


            switch (v.getId()) {
                case R.id.img_back:
                    finish();
                case R.id.text_right:
                    signUp();
                    break;
                default:
                    break;

            }


        }
    };

    private void signUp() {
        String phone = getIntent().getStringExtra("phone");
        String email = mEmail.getText().trim();
        String pwd = mPwd.getText().trim();
        String repwd = mRePwd.getText().trim();

        if (!email.matches(Matchers.EMAIL_MATCH)) {
            XUtils.show(R.string.email_format_error);
            return;
        }

        if (!pwd.matches(Matchers.PWD_MATHCH)) {
            XUtils.show(R.string.pwd_format_error);
            return;
        }

        if (!pwd.equals(repwd)) {
            XUtils.show(getString(R.string.pwd_repwd_error));
            return;
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("phone", phone);
        params.addBodyParameter("email", email);
        params.addBodyParameter("pwd", pwd);

        XUtils.send(XUtils.REG, params, new BaseRequestCallBack<Result<Boolean>>() {

            @Override
            public void success(Result<Boolean> data) {
                XUtils.show(data.descrpit);
                if (data.data) {
                    finish();
                }

            }
        }, true);
    }

    public static void startActivity(Context context, String phone) {
        Intent in = new Intent(context, RegisterActivity.class);
        in.putExtra("phone", phone);
        context.startActivity(in);
    }

}

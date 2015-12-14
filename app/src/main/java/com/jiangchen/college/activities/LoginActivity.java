package com.jiangchen.college.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jiangchen.college.AssistantTool.Code;
import com.jiangchen.college.AssistantTool.Matchers;
import com.jiangchen.college.Log.LogUtil;
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
import com.lidroid.xutils.view.annotation.event.OnClick;

//@ViewInject是利用反射原理将控件和事件绑定，节省诸如 findViewById 这样的代码
public class LoginActivity extends BaseActivity {
    @ViewInject(R.id.login_title)
    private TitleView titleView;
    @ViewInject(R.id.login_account)
    private MEditText mAccount;
    @ViewInject(R.id.login_pwd)
    private MEditText mPwd;
    @ViewInject(R.id.login_sign_in)
    private Button btSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //xUtils包下的ViewUtils类 自动绑定resId
        ViewUtils.inject(this);

        //设置监听器
        btSignIn.setOnClickListener(clickLis);
        titleView.setRightClickListener(clickLis);
        titleView.setImageBackClickListener(clickLis);

    }

    private View.OnClickListener clickLis = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.img_back:
                    finish();
                    break;
                case R.id.img_right:
                    break;
                case R.id.login_sign_in:
                    login();
                    break;
                case R.id.text_right:
                    break;
                default:
                    break;
            }

        }
    };

    @OnClick({R.id.login_fast_regeister, R.id.login_reset_pwd})
    public void onClick(View view) {
        switch (view.getId()) {
            //跳转验证界面
            case R.id.login_fast_regeister:
                ValidateActivity.startActivity(this, Code.ACTION_REG);
                break;
            case R.id.login_reset_pwd:
                ValidateActivity.startActivity(this, Code.ACTION_RESETPWD);
                break;
            default:
                break;
        }
    }


    private void login() {
        String account = mAccount.getText().trim();
        String pwd = mPwd.getText().trim();
        RequestParams params = new RequestParams();

        //检测或匹配手机
        if (account.matches(Matchers.PHONE_MATCH)) {
            params.addBodyParameter("account", account);
            //或匹配邮箱
        } else if (account.matches(Matchers.EMAIL_MATCH)) {
            params.addBodyParameter("account", account);
            //都不匹配则账号格式错误 提示并且退出
        } else {
            XUtils.show(R.string.xaccount);
            return;
        }

        //检测密码长度
        if (pwd.matches(Matchers.PWD_MATHCH)) {
            params.addBodyParameter("pwd", pwd);
        } else {
            XUtils.show(R.string.xpwd);
            return;
        }


        XUtils.send(XUtils.LOGIN, params, new BaseRequestCallBack<Result<User>>() {
            @Override
            public void success(Result<User> data) {

                if (data.descrpit != null) {
                    XUtils.show(data.descrpit);
                }
                if (data.state == Result.STATE_SUC) {
                    //把user的信息存入
                    ((MyApp) getApplication()).setUser(data.data);
                    // 如果 传过来的Intent()值为true 跳转CenterActivity
                    if (getIntent().getBooleanExtra("isToCenter", true)) {
                        CenterActivity.startActivity(LoginActivity.this);
                        finish();
                    } else {
                        XUtils.show("登录成功");
                        finish();
                    }
                } else {
                    XUtils.show(R.string.error_body_param);
                    LogUtil.e("College", "======params error=====");
                }

            }
        }, true);
    }


    public static void startActivity(Context context){
        context.startActivity(new Intent(context, LoginActivity.class));
    }


}

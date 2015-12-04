package com.jiangchen.college.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jiangchen.college.R;
import com.jiangchen.college.https.XUtils;
import com.jiangchen.college.views.MEditText;
import com.jiangchen.college.views.TitleView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;

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

    private void login() {
        String account = mAccount.getText();
        String pwd = mPwd.getText();
        RequestParams params = new RequestParams();

        //检测或匹配手机
        if (account.matches("^1(3|4|5|7|8)d{9}$")) {
            params.addBodyParameter("u.phone", account);
            //或匹配邮箱
        } else if (account.matches("^\\w+@\\w+\\.(com|cn).(cn)?$")) {
            params.addBodyParameter("u.email", account);
            //都不匹配则账号格式错误 提示并且退出
        } else {
            XUtils.show(R.string.xaccount);
            return;
        }

        //检测密码长度
        if (pwd.matches("^\\w{6,20}$")) {
            params.addBodyParameter("u.pwd", pwd);
        } else {
            XUtils.show(R.string.xpwd);
            return;
        }
    }

}

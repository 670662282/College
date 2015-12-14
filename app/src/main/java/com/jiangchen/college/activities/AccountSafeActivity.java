package com.jiangchen.college.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jiangchen.college.AssistantTool.Matchers;
import com.jiangchen.college.Log.LogUtil;
import com.jiangchen.college.R;
import com.jiangchen.college.entity.User;
import com.jiangchen.college.https.XUtils;
import com.jiangchen.college.views.TitleView;
import com.jiangchen.college.views.UserInfoView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by Dell- on 2015/12/12 0012.
 */
public class AccountSafeActivity extends BaseActivity {

    @ViewInject(R.id.safeset_title)
    private TitleView safeTitle;
    @ViewInject(R.id.safeset_phone)
    private UserInfoView safePhone;
    @ViewInject(R.id.safeset_email)
    private UserInfoView safeEmail;
    @ViewInject(R.id.safeset_updatepwd)
    private UserInfoView updatePwd;

    private User user;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_safe);
        ViewUtils.inject(this);
        safeTitle.setImageBackClickListener(clickLis);
        safePhone.setOnClickListener(clickLis);
        safeEmail.setOnClickListener(clickLis);
        updatePwd.setOnClickListener(clickLis);

    }


    @Override
    protected void onResume() {
        init();
        super.onResume();
    }

    public void init() {
        user = ((MyApp) getApplication()).getUser();

        if (user == null) {
            XUtils.show(R.string.need_login);
            return;
        }
        phone = user.getPhone();
        String email = user.getEmail();

        LogUtil.e("phone---------------", phone);
        safePhone.setText(phone == null ? "未绑定" : Matchers.replace(phone));
        safeEmail.setText(email == null ? "未绑定" : email);

    }


    private View.OnClickListener clickLis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img_back:
                    finish();
                    break;
                case R.id.safeset_phone:
                    UpdatePhoneEmailActivity.startActivity(AccountSafeActivity.this, true, phone);
                    break;
                case R.id.safeset_email:
                    UpdatePhoneEmailActivity.startActivity(AccountSafeActivity.this, false, phone);
                    break;
                case R.id.safeset_updatepwd:
                    RepwdActivity.startActivity(AccountSafeActivity.this, user.getToken());
                    break;

            }
        }
    };


    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, AccountSafeActivity.class));
    }
}

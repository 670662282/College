package com.jiangchen.college.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jiangchen.college.R;
import com.jiangchen.college.views.TitleView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by Dell- on 2015/12/5 0005.
 * 重置密码界面
 */
public class ResetPwdActivity extends BaseActivity {

    @ViewInject(R.id.resetpwd_title)
    private TitleView mTitleView;
    @ViewInject(R.id.resetpwd_email)
    private TextView textEmail;
    @ViewInject(R.id.bt_resetpwd)
    private Button btReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        setContentView(R.layout.activity_resetpwd);
        mTitleView.setImageBackClickListener(clickLis);
        btReset.setOnClickListener(clickLis);


    }
    private View.OnClickListener clickLis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.img_back:
                    finish();
                    break;
                case R.id.bt_resetpwd:
                    sendEmail();
                    break;
                default:
                    break;
            }
        }
    };

    private void sendEmail() {
        String phone = getIntent().getStringExtra("phone");
    }


    public static void  startActivity(Context context, String phone){
        Intent in  = new Intent(context, ResetPwdActivity.class);
        in.putExtra("phone", phone);
        context.startActivity(in);
    }

}

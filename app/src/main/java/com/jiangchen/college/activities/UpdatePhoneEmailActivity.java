package com.jiangchen.college.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.view.View;
import android.widget.TextView;

import com.jiangchen.college.AssistantTool.BaseEventHandler;
import com.jiangchen.college.AssistantTool.Code;
import com.jiangchen.college.AssistantTool.CodeTimeTask;
import com.jiangchen.college.AssistantTool.Matchers;
import com.jiangchen.college.AssistantTool.MyTextWatcher;
import com.jiangchen.college.Log.LogUtil;
import com.jiangchen.college.R;
import com.jiangchen.college.https.XUtils;
import com.jiangchen.college.utils.Loading;
import com.jiangchen.college.views.MEditText;
import com.jiangchen.college.views.TitleView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import cn.smssdk.SMSSDK;

/**
 * Created by Dell- on 2015/12/13 0013.
 */
public class UpdatePhoneEmailActivity extends BaseActivity {

    @ViewInject(R.id.update_title)
    private TitleView titleView;
    @ViewInject(R.id.update_phone)
    private TextView tvPhone;
    @ViewInject(R.id.update_code)
    private MEditText etCode;
    @ViewInject(R.id.update_get_code)
    private TextView tvCode;

    private String phone;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                XUtils.show((String) msg.obj);
            }
            // what 1表示通知
            if (msg.what == 1) {
                //msg.arg1 从子线程传过来的String资源id  （验证码已经发送）
                XUtils.show(msg.arg1);

            }
            //验证码发送失败
            if (msg.arg2 == 1) {
                CodeTimeTask.getInstance().taskCancel();
            }

            super.handleMessage(msg);
        }

    };

    BaseEventHandler eventHandler = new BaseEventHandler(handler) {
        @Override
        public void subSuccess() {
            if (getIntent().getBooleanExtra("isPhone", false)) {
                //验证新手机
                ValidateActivity.startActivity(UpdatePhoneEmailActivity.this, Code.ACTION_UPDATEPHONE);
            } else {
                //验证邮箱
                RemailActivity.startActivity(UpdatePhoneEmailActivity.this);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化SMSSDK
        SMSSDK.initSDK(this, Code.KEY, Code.SECRET);
        setContentView(R.layout.activity_updateaccount);
        ViewUtils.inject(this);
        phone = getIntent().getStringExtra("phone");

        //注册SMSSDK
        SMSSDK.registerEventHandler(eventHandler);
        titleView.setImageBackClickListener(clickLis);
        titleView.setRightClickListener(clickLis);
        tvCode.setOnClickListener(clickLis);

        //已经处理的手机号
        tvPhone.setText(Matchers.replace(phone));
        //获得验证码
        getCode();
        tvCode.addTextChangedListener(codeWather);


        if (CodeTimeTask.getInstance().isRun()) {
            CodeTimeTask.getInstance().startTimer(tvCode);
        }


    }

    //code监听 当code输入4位 title右侧显示 确定按钮
    private MyTextWatcher codeWather = new MyTextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                LogUtil.e("length", "length:"+s.length());
                titleView.setTextRightVisibility(View.VISIBLE);
            } else {
                LogUtil.e("length", "length:"+s.length());
                titleView.setTextRightVisibility(View.GONE);
            }
        }
    };

    private View.OnClickListener clickLis = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img_back:
                    finish();
                    break;
                case R.id.text_right:
                    String code = etCode.getText().trim();
                    Loading.show();
                    SMSSDK.submitVerificationCode("86", phone, code);
                    finish();
                    break;
                case R.id.update_get_code:
                    getCode();
                    break;
            }
        }
    };

    //获取验证码
    private void getCode() {
        SMSSDK.getVerificationCode("86", phone);
        CodeTimeTask.getInstance().startTimer(tvCode);

    }

    @Override
    protected void onDestroy() {
        SMSSDK.unregisterEventHandler(eventHandler);
        super.onDestroy();
    }


    public static void startActivity(Context context, boolean isPhone, String phone) {
        context.startActivity(new Intent(context, UpdatePhoneEmailActivity.class)
                .putExtra("isPhone", isPhone).putExtra("phone", phone));
    }
}

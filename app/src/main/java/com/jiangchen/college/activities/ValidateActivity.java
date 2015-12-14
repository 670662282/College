package com.jiangchen.college.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jiangchen.college.AssistantTool.Code;
import com.jiangchen.college.AssistantTool.CodeTimeTask;
import com.jiangchen.college.AssistantTool.Matchers;
import com.jiangchen.college.AssistantTool.MyTextWatcher;
import com.jiangchen.college.Log.LogUtil;
import com.jiangchen.college.R;
import com.jiangchen.college.entity.Result;
import com.jiangchen.college.entity.User;
import com.jiangchen.college.https.BaseRequestCallBack;
import com.jiangchen.college.https.XUtils;
import com.jiangchen.college.utils.Loading;
import com.jiangchen.college.views.MEditText;
import com.jiangchen.college.views.TitleView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by Dell- on 2015/12/5 0005.
 * 验证手机 获取验证码界面
 */
public class ValidateActivity extends BaseActivity {

    private static String phone;
    @ViewInject(R.id.vaildate_title)
    private TitleView title;
    @ViewInject(R.id.vaildate_phone)
    private MEditText mPhone;
    @ViewInject(R.id.vaildate_code)
    private MEditText mCode;
    @ViewInject(R.id.vaildate_get_code)
    private TextView btToGetCode;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化SMSSDK
        SMSSDK.initSDK(this, Code.KEY, Code.SECRET);

        setContentView(R.layout.activity_vaildate);
        ViewUtils.inject(this);
        title.setImageBackClickListener(clickLis);
        title.setRightClickListener(clickLis);
        btToGetCode.setOnClickListener(clickLis);

        mPhone.addTextChangedListener(phoneWatcher);
        mCode.addTextChangedListener(codeWather);
        //注册SMSSDK
        SMSSDK.registerEventHandler(eventHandler);

        if (phone != null) {
            mPhone.setText(phone);
        }

        if (CodeTimeTask.getInstance().isRun()) {
            CodeTimeTask.getInstance().startTimer(btToGetCode);
        }

    }

    //这里EventHandler开了子线程
    private EventHandler eventHandler = new EventHandler() {
        // data 返回的json数据
        @Override
        public void afterEvent(int event, int result, Object data) {
            super.afterEvent(event, result, data);
            //结果成功完成
            if (result == SMSSDK.RESULT_COMPLETE) {
                //发送验证码成功
                if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    handler.sendMessage(handler.obtainMessage(1, R.string.code_send_suc, 0));
                    //提交验证码成功  对话框消失 跳转界面
                } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    Loading.dismiss();


                    switch (getIntent().getIntExtra("action", -1)) {
                        case Code.ACTION_REG:
                            RegisterActivity.startActivity(ValidateActivity.this, phone);
                            break;
                        case Code.ACTION_RESETPWD:
                            ResetPwdActivity.startActivity(ValidateActivity.this, phone);
                            break;
                        case Code.ACTION_UPDATEPHONE:
                            RequestParams params = new RequestParams();
                            int uid =  ((MyApp) getApplication()).getUser().getUid();
                            params.addBodyParameter("uid", String.valueOf(uid));
                            params.addBodyParameter("phone", phone);
                            XUtils.send(XUtils.UPDATE, params, new BaseRequestCallBack<Result<User>>() {
                                @Override
                                public void success(Result<User> data) {

                                    if (data.state == Result.STATE_SUC){
                                        if (data.data != null){
                                            ((MyApp) getApplication()).setUser(data.data);
                                            XUtils.show(R.string.update_phone_suc);
                                        }else{

                                        }

                                    }

                                }
                            }, true);
                            break;

                        default:
                            break;
                    }



                } else {
                    XUtils.show("cuowu");
                }
                //结果失败
            } else {
                LogUtil.e("college", "===code error====" + JSON.toJSONString(data));
                //发送验证码 失败
                Loading.dismiss();
                if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    handler.sendMessage(handler.obtainMessage(1, R.string.code_send_fail, 1));

                    //提交验证码 失败
                } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    handler.sendMessage(handler.obtainMessage(1, R.string.validate_code_error, 0));

                }

            }
            //验证结束后关闭
            finish();

        }
    };

    @Override
    protected void onDestroy() {
        SMSSDK.unregisterEventHandler(eventHandler);
        super.onDestroy();
    }

    //phone文本监听 当输入手机号码匹配 PHONE_MATCH正则规则 显示获取验证码
    private MyTextWatcher phoneWatcher = new MyTextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().matches(Matchers.PHONE_MATCH)) {
                //btToGetCode Enabled默认属性为false
                btToGetCode.setEnabled(true);
            } else {
                btToGetCode.setEnabled(false);
            }
        }
    };

    //code监听 当code输入4位 title右侧显示 确定按钮
    private MyTextWatcher codeWather = new MyTextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 4) {
                title.setTextRightVisibility(View.VISIBLE);
            } else {
                title.setTextRightVisibility(View.GONE);
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
                    String code = mCode.getText().trim();
                    Loading.show();
                    SMSSDK.submitVerificationCode("86", phone, code);
                    break;
                case R.id.vaildate_get_code:
                    getCode();
                    break;
            }
        }
    };

    //获取验证码
    private void getCode() {
        phone = mPhone.getText().toString().trim();
        if (phone.matches(Matchers.PHONE_MATCH)) {
            SMSSDK.getVerificationCode("86", phone);
            CodeTimeTask.getInstance().startTimer(btToGetCode);
        }
    }

    public static void startActivity(Context context, int action) {
        Intent in = new Intent(context, ValidateActivity.class)
                .putExtra("action", action);
        context.startActivity(in);
    }
}

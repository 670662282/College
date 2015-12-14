package com.jiangchen.college.AssistantTool;

import android.os.Handler;

import com.alibaba.fastjson.JSON;
import com.jiangchen.college.Log.LogUtil;
import com.jiangchen.college.R;
import com.jiangchen.college.https.XUtils;
import com.jiangchen.college.utils.Loading;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by Dell- on 2015/12/13 0013.
 */
public abstract class BaseEventHandler extends EventHandler {


    public abstract void subSuccess();
    private Handler handler;

    public BaseEventHandler(Handler handler){
        this.handler = handler;
    }


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
                subSuccess();
            }else{
                XUtils.show("发生错误");
            }
            //结果失败
        } else {
            LogUtil.e("college", "===code error====" + JSON.toJSONString(data));
            //发送验证码 失败
            if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                handler.sendMessage(handler.obtainMessage(1, R.string.code_send_fail, 1));

                //提交验证码 失败
            } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                handler.sendMessage(handler.obtainMessage(1, R.string.validate_code_error, 0));

            }

        }
        //关闭Loading
        Loading.dismiss();

    }

}

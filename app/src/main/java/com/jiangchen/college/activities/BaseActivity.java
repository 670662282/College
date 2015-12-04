package com.jiangchen.college.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;

import com.jiangchen.college.AssistantTool.ActivityController;
import com.jiangchen.college.R;
import com.jiangchen.college.https.XUtils;

/**
 * Created by Dell- on 2015/12/3 0003.
 */
public class BaseActivity extends FragmentActivity{

    private boolean isExit = false;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityController.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityController.removeActivity(this);
    }

    //捕获返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK){
            //当判断当前Activity是MainActivity 按2次返回键 退出
            if (getClass().getName().equals(MainActivity.class.getName())) {
                if (!isExit) {
                    isExit = true;
                    XUtils.show(R.string.exit);
                    //2秒后设置isExit为false
                    handler.postDelayed(r, 2000);
                } else {
                    ActivityController.removeAllActivity();
                }
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    private  Runnable r  = new Runnable() {
        @Override
        public void run() {
            isExit = false;
        }
    };
}

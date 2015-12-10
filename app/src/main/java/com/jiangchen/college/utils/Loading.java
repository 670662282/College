package com.jiangchen.college.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

import com.jiangchen.college.R;

/**
 * Created by Dell- on 2015/12/5 0005.
 * 网络请求对话框  封装了dialog对话框
 */
public class Loading {
    private static Context mContext;
    private static Dialog dialog;

    public static void init(Context context) {
        mContext = context;
    }


    public static void show() {

        if (dialog == null) {
            dialog = new AlertDialog.Builder(mContext).create();
            dialog.setCanceledOnTouchOutside(false);
            Window w = dialog.getWindow();
            //需要设置全局的对话框 才能显示
            w.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            dialog.show();
            dialog.setContentView(R.layout.layout_loading);

        } else {
            dialog.show();
        }

    }


    public static void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public static boolean isShowing(){
        return dialog == null ? false : dialog.isShowing();
    }

    //释放资源
    public static void  destroy(){
        dismiss();
        if (dialog != null){
            dialog = null;
        }
        mContext = null;
    }

}

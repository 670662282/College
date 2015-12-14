package com.jiangchen.college.https;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created by Dell- on 2015/12/13 0013.
 */
public class SharedPreUtils {

    private static final String NAME = "college.spf";

    public static void changeMode(Context context, boolean isNight) {
        SharedPreferences shared = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();

        if (shared.getBoolean("isNight", false) != isNight) {

            editor.putBoolean("isNight", isNight);
            editor.commit();
            //发送广播
            context.sendBroadcast(new Intent("com.jiangchen.MODE_CHANGED"));
        }


    }
}

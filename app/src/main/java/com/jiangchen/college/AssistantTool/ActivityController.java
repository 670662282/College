package com.jiangchen.college.AssistantTool;

import android.app.Activity;
import android.os.Process;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell- on 2015/12/3 0003.
 * 辅助工具类 收集所有activity
 */
public class ActivityController {

    private static List<Activity> activities = new ArrayList<Activity>();



    public static void addActivity(Activity activity){
        activities.add(activity);
    }

    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }


    public static void removeAllActivity(){
        for (Activity activity : activities){
            activity.finish();
        }
        //结束当前进程
        android.os.Process.killProcess(Process.myPid());
    }

}

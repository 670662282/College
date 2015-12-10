package com.jiangchen.college.activities;

import android.app.Application;

import com.jiangchen.college.entity.User;
import com.jiangchen.college.https.XUtils;
import com.jiangchen.college.utils.Loading;

/**
 * Created by Dell- on 2015/12/5 0005.
 */
public class MyApp extends Application {

    private User user;

    @Override
    public void onCreate() {
        super.onCreate();
        Loading.init(this);
        XUtils.init(this);
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

package com.jiangchen.college.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.jiangchen.college.R;
import com.jiangchen.college.views.UserInfoView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by Dell- on 2015/12/9 0009.
 */
public class UserInfoActivity extends BaseActivity {

    @ViewInject(R.id.userinfo_photo)
    private UserInfoView userPhoto;
    @ViewInject(R.id.userinfo_name)
    private UserInfoView userName;
    @ViewInject(R.id.userinfo_nick)
    private UserInfoView userNick;
    @ViewInject(R.id.userinfo_sex)
    private UserInfoView userSex;
    @ViewInject(R.id.userinfo_save)
    private Button btSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        ViewUtils.inject(this);
    }


    public static void startActivity(Context context){

        Intent in = new Intent(context, UserInfoActivity.class);

        context.startActivity(in);
    }

    

}

package com.jiangchen.college.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.jiangchen.college.R;
import com.jiangchen.college.https.XUtils;
import com.jiangchen.college.views.TitleView;
import com.jiangchen.college.views.UserInfoView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnCompoundButtonCheckedChange;

/**
 * Created by Dell- on 2015/12/13 0013.
 */
public class SettingActivity extends BaseActivity {

    @ViewInject(R.id.set_title)
    private TitleView title;
    @ViewInject(R.id.set_mode_cb)
    private CheckBox set_mode_cb;
    @ViewInject(R.id.set_version)
    private UserInfoView version;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ViewUtils.inject(this);
        title.setImageBackClickListener(clickLis);
        version.setOnClickListener(clickLis);


    }

    @OnCompoundButtonCheckedChange(R.id.set_mode_cb)
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked) {

        }
    }


    private View.OnClickListener clickLis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img_back:
                    finish();
                    break;
                case R.id.set_version:
                    XUtils.show("这已经是最新的版本");
                    break;
            }
        }
    };


    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, SettingActivity.class));
    }
}

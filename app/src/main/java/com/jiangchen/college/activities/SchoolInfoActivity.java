package com.jiangchen.college.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.jiangchen.college.AssistantTool.Code;
import com.jiangchen.college.Log.LogUtil;
import com.jiangchen.college.R;
import com.jiangchen.college.entity.Result;
import com.jiangchen.college.entity.User;
import com.jiangchen.college.https.BaseRequestCallBack;
import com.jiangchen.college.https.XUtils;
import com.jiangchen.college.views.TitleView;
import com.jiangchen.college.views.UserInfoView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.ResType;
import com.lidroid.xutils.view.annotation.ResInject;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.Calendar;

/**
 * Created by Dell- on 2015/12/11 0011.
 * 学校信息  可以优化
 */
public class SchoolInfoActivity extends BaseActivity {

    @ViewInject(R.id.school_title)
    private TitleView STitle;
    @ViewInject(R.id.school_role)
    private UserInfoView SRole;
    @ViewInject(R.id.school_name)
    private UserInfoView SSchoolName;
    @ViewInject(R.id.school_department)
    private UserInfoView SDepartment;
    @ViewInject(R.id.school_class)
    private UserInfoView SClass;
    @ViewInject(R.id.school_year)
    private UserInfoView Syear;
    @ViewInject(R.id.school_save)
    private Button btSave;

    @ResInject(id = R.array.role, type = ResType.StringArray)
    private String[] roles;
    private String[] years = new String[30];
    private User user;
    private boolean needUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schoolinfo);
        ViewUtils.inject(this);
        STitle.setImageBackClickListener(clickLis);
        btSave.setOnClickListener(clickLis);
        Syear.setOnClickListener(clickLis);
        SSchoolName.setOnClickListener(clickLis);
        SRole.setOnClickListener(clickLis);

        init();

    }


    public void init() {
        user = ((MyApp) (getApplication())).getUser();
        if (user == null) {
            XUtils.show(R.string.need_login);
            finish();
            return;
        }

        if (user.getRoleId() != -1) {
            SRole.setText(roles[user.getRoleId() - 1]);
            SRole.setValues(user.getRoleId());
        }

        if (!TextUtils.isEmpty(user.getSchool())) {
            SSchoolName.setText(user.getSchool());
        }

        if (!TextUtils.isEmpty(user.getDepartment())) {
            SDepartment.setText(user.getDepartment());

        }
        if (!TextUtils.isEmpty(user.getGradeClass())) {
            SClass.setText(user.getGradeClass());

        }
        if (!TextUtils.isEmpty(user.getYear())) {
            Syear.setText(user.getYear());
        }


    }

    private View.OnClickListener clickLis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img_back:
                    finish();
                    break;
                case R.id.school_save:
                    save();
                    break;
                case R.id.school_year:
                    showYear();
                    break;
                case R.id.school_role:
                    showRole();
                    break;
                case R.id.school_name:
                    //跳转到选择学校界面
                    SchoolSelectActivity.startActivityForResult(SchoolInfoActivity.this);
                    break;
                default:
                    break;
            }
        }
    };

    //保存信息并上传到服务端
    private void save() {
        String role = SRole.getText().toString().trim();
        String school = SSchoolName.getText().toString().trim();
        String department = SDepartment.getText().toString().trim();
        String gradeclass = SClass.getText().toString().trim();
        String year = Syear.getText().toString().trim();

        RequestParams params = new RequestParams();
        params.addBodyParameter("uid", String.valueOf(user.getUid()));

        if (!TextUtils.isEmpty(role) && role != roles[user.getRoleId() - 1]) {
            needUpdate = true;
            params.addBodyParameter("roleid", String.valueOf(SRole.getValues()));
        }
        if (!TextUtils.isEmpty(school) && school != user.getSchool()) {
            needUpdate = true;
            params.addBodyParameter("school", school);
        }

        if (!TextUtils.isEmpty(department) && department != user.getDepartment()) {
            needUpdate = true;
            params.addBodyParameter("department", department);
        }
        if (!TextUtils.isEmpty(gradeclass) && gradeclass != user.getGradeClass()) {
            needUpdate = true;
            params.addBodyParameter("gradeclass", gradeclass);
        }
        if (!TextUtils.isEmpty(year) && year != user.getYear()) {
            LogUtil.e("year", year);
            needUpdate = true;
            params.addBodyParameter("year", year);
        }

        if (needUpdate) {
            XUtils.send(XUtils.UPDATE, params, new BaseRequestCallBack<Result<User>>() {
                @Override
                public void success(Result<User> data) {
                    if (data.state == Result.STATE_SUC) {
                        ((MyApp) getApplication()).setUser(data.data);
                        XUtils.show("保存成功");
                        //设置返回码
                        setResult(Code.RESP_UPDATE_SHCOLLINFO);
                    } else {
                        XUtils.show("上传失败");
                    }
                }
            }, true);


        } else {
            XUtils.show("请改变");
        }

        finish();
    }


    //弹出选择年份的对话框
    private void showYear() {

        if (years[0] == null) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);

            for (int i = 0; i < years.length; i++) {
                years[i] = String.format("%dyear", year - i);
            }
        }


        new AlertDialog.Builder(this).setItems(years, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //设置职位
                Syear.setText(years[which]);
            }
        }).show();
    }

    //弹出选择职业的对话框
    private void showRole() {

        new AlertDialog.Builder(this).setItems(roles, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //设置职位
                SRole.setValues(which + 1);
                //显示职位
                SRole.setText(roles[which]);

            }
        }).show();

    }


    public static void startActivityForResult(Activity activity) {

        Intent in = new Intent(activity, SchoolInfoActivity.class);
        activity.startActivityForResult(in, Code.REQ_UPDATE_SHCOLLINFO);
    }


    //获取SchoolSelectActivity界面传来的值
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data != null && resultCode == Code.RESP_SELECT_SCHOOL) {
            String name = data.getStringExtra("name");
            SSchoolName.setText(name);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}

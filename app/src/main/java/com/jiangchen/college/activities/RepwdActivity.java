package com.jiangchen.college.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jiangchen.college.AssistantTool.ActivityController;
import com.jiangchen.college.AssistantTool.Matchers;
import com.jiangchen.college.R;
import com.jiangchen.college.entity.Result;
import com.jiangchen.college.https.BaseRequestCallBack;
import com.jiangchen.college.https.XUtils;
import com.jiangchen.college.views.MEditText;
import com.jiangchen.college.views.TitleView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by Dell- on 2015/12/12 0012.
 */
public class RepwdActivity extends BaseActivity {

    @ViewInject(R.id.repwd_title)
    private TitleView titleView;
    @ViewInject(R.id.repwd_oldpwd)
    private MEditText oldPwd;
    @ViewInject(R.id.repwd_pwd)
    private MEditText newPwd;
    @ViewInject(R.id.repwd_repwd)
    private MEditText reNewPWd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repwd);
        ViewUtils.inject(this);
        titleView.setImageBackClickListener(clickLis);
        titleView.setRightClickListener(clickLis);
    }


    private View.OnClickListener clickLis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img_back:
                    finish();
                    break;
                case R.id.text_right:
                    updateOk();
                    break;
            }
        }
    };

    private void updateOk() {
        String oPwd = oldPwd.getText();
        String nPwd = newPwd.getText();
        String repwd = reNewPWd.getText();

        if (oPwd == null){
            XUtils.show("不能为空");
            return ;
        }

        if (!nPwd.matches(Matchers.PWD_MATHCH)) {
            XUtils.show(R.string.pwd_format_error);
            return;
        }

        if (!repwd.equals(repwd)) {
            XUtils.show(getString(R.string.pwd_repwd_error));
            return;
        }

        String token = getIntent().getStringExtra("token");
        RequestParams params = new RequestParams();
        params.addBodyParameter("token", token);
        params.addBodyParameter("oldpwd", oPwd);
        params.addBodyParameter("pwd", nPwd);
        params.addBodyParameter("repwd", repwd);


        XUtils.send(XUtils.RESETPWD, params, new BaseRequestCallBack<Result<Boolean>>() {

            @Override
            public void success(Result<Boolean> data) {
                if (data.state == Result.STATE_SUC) {
                    if (data.data) {
                        XUtils.show("检测到密码已经修改，为了安全，请重新登录");
                        ActivityController.removeAllActivity();
                        LoginActivity.startActivity(RepwdActivity.this);
                    } else {
                        XUtils.show(data.descrpit);
                    }
                }

            }
        }, true);
    }


    public static void startActivity(Context context, String token) {
        context.startActivity(new Intent(context, RepwdActivity.class)
                .putExtra("token", token));
    }

}

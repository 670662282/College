package com.jiangchen.college.https;

import com.alibaba.fastjson.JSON;
import com.jiangchen.college.AssistantTool.Matchers;
import com.jiangchen.college.Log.LogUtil;
import com.jiangchen.college.R;
import com.jiangchen.college.utils.Loading;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by Dell- on 2015/12/5 0005.
 * 请求回调抽象类 抽象出success方法
 */
public abstract class BaseRequestCallBack<T> extends RequestCallBack<String> {

    private Type type;

    //利用反射获取泛型T的类型 ！？
    public BaseRequestCallBack() {
        Type superClass = this.getClass().getGenericSuperclass();
        this.type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
    }

    @Override
    public void onSuccess(ResponseInfo<String> responseInfo) {
        Loading.dismiss();
        if (responseInfo != null) {
            String json = responseInfo.result;
            if (json.matches(Matchers.JSON_MATHCH)) {
                T t = JSON.parseObject(json, type);
                if (t != null) {
                    success(t);
                } else {
                    XUtils.show(R.string.no_data);
                }
            } else {
                XUtils.show(R.string.data_format_error);
            }
        } else {
            XUtils.show(R.string.data_load_error);
        }

    }

    @Override
    public void onFailure(HttpException error, String msg) {
        Loading.dismiss();
        XUtils.show(R.string.network_error);
        LogUtil.e("College", "======error=====" + msg);
        error.printStackTrace();
        failure();
    }


    public abstract void success(T data);

    public void failure() {

    }
}

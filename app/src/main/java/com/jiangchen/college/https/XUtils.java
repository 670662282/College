package com.jiangchen.college.https;

import android.content.Context;
import android.widget.ImageView;
import android.widget.Toast;

import com.jiangchen.college.R;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * Created by Dell- on 2015/12/1 0001.
 * XUtils
 */

public class XUtils {
    //web网络服务端地址
    private static final String U = "http//:localhost:8080/college_online";
    private static BitmapUtils bitmapUtils;
    private static HttpUtils httpUtils;
    private static DbUtils dbUtils;
    private static Context context;
    private static HttpHandler httpHandler;

    //初始化工作 完成各个工具类对象的初始化
    private static void init(Context context) {
        XUtils.context = context;
        //初始化bitmapUtils 配置读取时候图片  读取失败图片 设置自动缓存到本地
        if (bitmapUtils == null) {
            bitmapUtils = new BitmapUtils(context);
            bitmapUtils.configDefaultLoadingImage(R.drawable.logo_tr);
            bitmapUtils.configDefaultLoadFailedImage(R.drawable.no_data);
            bitmapUtils.configMemoryCacheEnabled(true);

        }
        //初始化http连接工具类
        if (httpUtils == null) {
            httpUtils = new HttpUtils();
        }
        //初始化数据库工具类
        if (dbUtils == null) {
            dbUtils = DbUtils.create(context, "college.db");
        }

    }
    //泛型类   封装了send Get方法
    public static <T> void send(String url, RequestCallBack<T> callBack, boolean isLoading){
        send(url, null, callBack, isLoading);
    }



    //泛型类   封装了send Post请求方法
    public static <T> void send(String url, RequestParams params, RequestCallBack<T> callBack, boolean isLoading) {
        //如果正在加载数据 则弹出对话框
        if (isLoading) {

        }
        if (params == null) {
            httpHandler = httpUtils.send(HttpRequest.HttpMethod.GET, U + url, callBack);
        } else {
            httpHandler = httpUtils.send(HttpRequest.HttpMethod.POST, U + url, params, callBack);

        }
    }
    //暂停请求
    public static void cancel() {
        if (httpHandler != null) {
            httpHandler.cancel();
            httpHandler = null;
        }

    }

    //封装加载图片的方法
    public static void display(ImageView img, String url) {
        bitmapUtils.display(img, url);
    }


    public static void show(int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }


}

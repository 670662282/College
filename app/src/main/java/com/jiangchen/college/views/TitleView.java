package com.jiangchen.college.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiangchen.college.R;
import com.jiangchen.college.https.XUtils;

/**
 * Created by Dell- on 2015/12/1 0001.
 * 标题自定义控件
 */
public class TitleView extends RelativeLayout {

    private  TextView textTitle;
    private  TextView textRight;
    private ImageView imageRight;
    private ImageView imageBack;
    private String currentUrl;

    public TitleView(Context context) {
        super(context);
        init(null);

    }

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }


    public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs){
                
        LayoutInflater.from(getContext()).inflate(R.layout.title_layout, this);
        textTitle = (TextView) findViewById(R.id.text_title);
        textRight = (TextView) findViewById(R.id.text_right);

        imageBack = (ImageView)findViewById(R.id.img_back);
        imageRight = (ImageView)findViewById(R.id.img_right);

        if  (attrs == null){
            return;
        }


       TypedArray array =  getContext().obtainStyledAttributes(attrs, R.styleable.TitleView);
       //获得数组的总数
        final int N =  array.getIndexCount();

        for (int i = 0 ; i < N; i++){
            int index = array.getIndex(i);

            switch (index){
                case R.styleable.TitleView_tv_back_visibility:
                    setVisibiliable(imageBack, array.getInt(index, 0));
                    break;
                case R.styleable.TitleView_tv_img_right_back_visibility:
                    setVisibiliable(imageRight, array.getInt(index, 2));
                    break;
                case R.styleable.TitleView_tv_right:
                    textRight.setText(array.getString(index));
                    break;
                case R.styleable.TitleView_tv_title:
                    textTitle.setText(array.getString(index));
                    break;
                case R.styleable.TitleView_tv_tv_right_visibility:
                    setVisibiliable(textRight, array.getInt(index, 2));
                    break;

                default:
                    break;
            }
        }

    }


    private  void setVisibiliable(View v, int visiable){

        switch (visiable){
            case 0:

                v.setVisibility(View.VISIBLE);
                break;
            case 1:
                v.setVisibility(View.INVISIBLE);
                break;
            case 2:
                v.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }


    public void setTextTitle(String str){
        textTitle.setText(str);
    }

    public void setTextRight(String str){
        textRight.setText(str);
    }

    public void setImageBackClickListener(View.OnClickListener l){
        imageBack.setOnClickListener(l);
    }

    //设置布局右侧的控件监听器 2 个控件同时只会显示一个
    public void setRightClickListener(View.OnClickListener l){
        imageRight.setOnClickListener(l);
        textRight.setOnClickListener(l);
    }

    //如果地址不发生改变就不设置Image
    public void setImageUrl(String url){

        if (currentUrl.equals(url)){
            return ;
        }
        currentUrl = url;
        XUtils.display(imageRight, currentUrl);


    }

}

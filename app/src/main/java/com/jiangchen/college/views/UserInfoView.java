package com.jiangchen.college.views;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiangchen.college.R;


/**
 * Created by Dell- on 2015/12/9 0009.
 */
public class UserInfoView extends RelativeLayout implements View.OnClickListener {

    private static final int VISIABLE = 0;
    private static final int INVISIABLE = 1;
    private static final int GONE = 2;
    private boolean etEnable;

    private OnClickListener l;

    private TextView label;
    private EditText etInput;
    private ImageView imgView;
    private ImageView next;

    public UserInfoView(Context context) {
        super(context);
        init(null);
    }

    public UserInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public UserInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }


    private void init(AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.userinfo_layout, this);
        label = (TextView) findViewById(R.id.userinfo_label);
        etInput = (EditText) findViewById(R.id.userinfo_info);
        imgView = (ImageView) findViewById(R.id.userinfo_img);
        next = (ImageView) findViewById(R.id.userinfo_next);


        etInput.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    setEnable(false);
                }
            }
        });

        if (attrs == null) {
            return;
        }


        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.UserInfoView);

        final int N = array.getIndexCount();

        for (int i = 0; i < N; i++) {
            int index = array.getIndex(i);

            switch (index) {
                case R.styleable.UserInfoView_userinfo_img_next_visibility:
                    setVisiable(next, array.getInt(index, 0));
                    break;
                case R.styleable.UserInfoView_userinfo_img_visibility:
                    setVisiable(imgView, array.getInt(index, 0));
                    break;
                case R.styleable.UserInfoView_userinfo_et_enable:
                    etEnable = array.getBoolean(index, false);
                    break;
                case R.styleable.UserInfoView_usefinfo_label_text:
                    label.setText(array.getString(index));
                    break;
                case R.styleable.UserInfoView_userinfo_et_init:
                    setHint(array.getString(index));
                    break;
                case R.styleable.UserInfoView_userinfo_et_input_text:
                    setText(array.getString(index));
                    break;
                default:
                    break;
            }

        }


    }





    public void setEnable(boolean enable) {
        etInput.setEnabled(enable);
    }

    public void setText(String text) {
        etInput.setText(text);
    }

    public String getText() {
        return etInput.getText().toString().trim();
    }

    public void setHint(String text) {
        etInput.setHint(text);
    }

    public void setVisiable(View view, int visibility) {

        switch (visibility) {
            case VISIABLE:
                view.setVisibility(View.VISIBLE);
                break;
            case INVISIABLE:
                view.setVisibility(View.INVISIBLE);
                break;
            case GONE:
                view.setVisibility(View.GONE);
                break;
            default:
                break;
        }

    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        this.l = l;
    }

    @Override
    public void onClick(View v) {
        //当编辑框被禁用 和监听接口l 不等于空的时候
        if (!etEnable && l != null) {
            l.onClick(this);
        } else {
            setEnable(true);
           // 强制etInput焦点获取
            etInput.requestFocus();
        }
    }
}

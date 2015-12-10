package com.jiangchen.college.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jiangchen.college.AssistantTool.MyTextWatcher;
import com.jiangchen.college.R;


/**
 * Created by Dell- on 2015/12/2 0002.
 * 编辑框自定义控件 EditText
 */
public class MEditText extends RelativeLayout implements View.OnTouchListener, View.OnFocusChangeListener, View.OnClickListener {

    private RelativeLayout rlRoot;
    private EditText etInput;
    private ImageView imgEye;
    private ImageView imgDel;
    private ImageView imgLable;
    private boolean eyeEnable;
    private boolean delEnable;

    private int paddingTop;
    private int paddingBottom;

    public static final int TYPE_TEXT = 0;
    public static final int TYPE_PASSWORD = 1;
    public static final int TYPE_NUMBER = 2;

    public MEditText(Context context) {
        super(context);
        init(null);
    }

    public MEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    //控件的 初始化工作
    public void init(AttributeSet attrs) {
        //获取当前资源Dimens文件下的paddingBottom和paddingTop值
        paddingBottom = getContext().getResources().getDimensionPixelSize(R.dimen.mEdit_padding_bottom);
        paddingTop = getContext().getResources().getDimensionPixelSize(R.dimen.mEdit_padding_top);

        LayoutInflater.from(getContext()).inflate(R.layout.layout_medit, this);
        rlRoot = (RelativeLayout) findViewById(R.id.medit_root);
        etInput = (EditText) findViewById(R.id.medit_input);
        imgDel = (ImageView) findViewById(R.id.medit_del);
        imgEye = (ImageView) findViewById(R.id.medit_eye);
        imgLable = (ImageView) findViewById(R.id.medit_lable);

        //设置按下状态监听器
        etInput.setOnFocusChangeListener(this);
        //编辑框添加公有的文本改变 监听器
        etInput.addTextChangedListener(delWatcher);
        //imgDel 设置点击监听器 点击的时候清空编辑框里面的内容
        imgDel.setOnClickListener(this);
        //设置 触摸监听器
        imgEye.setOnTouchListener(this);

        if (attrs == null) {
            return;
        }
        //获得所有的控件Xml里面自定义的属性 循环遍历来初始化自定义控件的属性
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MEditText);
        final int N = typedArray.getIndexCount();

        for (int i = 0; i < N; i++) {
            //得到每个自定义属性的资源id
            int index = typedArray.getIndex(i);
            switch (index) {
                case R.styleable.MEditText_me_del_enable:
                    delEnable = typedArray.getBoolean(index, false);
                    break;
                case R.styleable.MEditText_me_eye_enable:
                    eyeEnable = typedArray.getBoolean(index, false);
                    break;
                case R.styleable.MEditText_me_hint:
                    setHint(typedArray.getString(index));
                    break;
                case R.styleable.MEditText_me_lable_src:
                    imgLable.setImageDrawable(typedArray.getDrawable(index));
                    break;
                case R.styleable.MEditText_me_text:
                    setText(typedArray.getString(index));
                    break;
                case R.styleable.MEditText_me_input_type:
                    setTextType(typedArray.getInt(index, 0));
                    break;
                case R.styleable.MEditText_me_maxlength:
                    int max = typedArray.getInt(index, -1);
                    if (max != -1) {
                        setMaxLength(max);
                    }
                    break;
                default:
                    break;

            }
        }


    }

    //默认的文本删除 监听
    private MyTextWatcher delWatcher = new MyTextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            //文本改变后 文本长度大于0显示del控件
            if (delEnable && s.length() > 0) {
                imgDel.setVisibility(View.VISIBLE);
            } else if (delEnable) {
                imgDel.setVisibility(View.GONE);
            }
        }
    };

    //设置编辑框的最大长度
    public void setMaxLength(int maxLength) {
        etInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});

    }

    public String getText() {
        return etInput.getText().toString().trim();
    }

    public void setText(String text) {
        etInput.setText(text);
    }

    public void setHint(String text) {
        etInput.setHint(text);
    }

    //点击清除编辑框的内容
    @Override
    public void onClick(View v) {
        etInput.getText().clear();
    }

    public void setTextType(int type) {

        switch (type) {
            case TYPE_TEXT:
                etInput.setInputType(InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_TEXT_VARIATION_NORMAL);
                break;
            case TYPE_PASSWORD:
                etInput.setInputType(InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
            case TYPE_NUMBER:
                etInput.setInputType(InputType.TYPE_CLASS_NUMBER
                        | InputType.TYPE_NUMBER_VARIATION_NORMAL);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //按下的时候 设置文本类型为正常类型
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            setTextType(TYPE_TEXT);
            //return true是拦截按下的动作
            return true;
        }
        //抬起的时候 设置文本类型为密码类型
        if (event.getAction() == MotionEvent.ACTION_UP) {
            setTextType(TYPE_PASSWORD);
            return true;
        }
        return false;
    }

    //根据是否按下来是设置 del和eye（imageView控件）的显示和隐藏
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        //设置选中状态
        rlRoot.setSelected(hasFocus);


       // etInput.setSelected(hasFocus);
        //按下的时候 当编辑框长度大于0 显示imgdel控件
        if (delEnable && hasFocus && etInput.getText().length() > 0) {
            imgDel.setVisibility(View.VISIBLE);
            //当按下的时候编辑框内容为空 隐藏控件
        } else if (delEnable && hasFocus) {
            imgDel.setVisibility(View.GONE);
        }

        //当eyeEnable 按下编辑框的时候才设置显示 控件
        if (eyeEnable && hasFocus) {
            imgEye.setVisibility(View.VISIBLE);
        } else if (eyeEnable) {
            imgEye.setVisibility(View.GONE);
        }

    }


//    //当图片大小改变
//    //对图片的宽度进行调整 使其保持一致
//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//
//        int regulation = h - paddingBottom - paddingTop;
//        //设置当前布局中各imageView控件的宽
//        imgDel.getLayoutParams().width = regulation;
//        imgLable.getLayoutParams().width = regulation;
//        imgEye.getLayoutParams().width = regulation;
//
//
//
//    }

    //为编辑框添加新的文本监听器
    public void addTextChangedListener(TextWatcher Watcher) {
        etInput.addTextChangedListener(Watcher);
    }


}

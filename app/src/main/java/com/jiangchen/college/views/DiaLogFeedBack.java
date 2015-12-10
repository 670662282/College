package com.jiangchen.college.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jiangchen.college.AssistantTool.MyTextWatcher;
import com.jiangchen.college.R;

/**
 * Created by Dell- on 2015/12/9 0009.
 */
public class DiaLogFeedBack extends Dialog implements View.OnClickListener {

    private EditText etInput;
    private TextView inputLength;
    private Button btSend;
    private final int MAXLENGTH = 150;
    private OnClickListener l;

    public DiaLogFeedBack(Context context) {
        this(context, R.style.DiglogFeedBack);
    }

    public DiaLogFeedBack(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected DiaLogFeedBack(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_feedback);

        etInput = (EditText) findViewById(R.id.dialog_etInput);
        inputLength = (TextView) findViewById(R.id.dia_tv_input_length);
        btSend = (Button) findViewById(R.id.dialog_send);

        btSend.setOnClickListener(this);

        etInput.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > MAXLENGTH) {
                    s.delete(MAXLENGTH, s.length());
                } else {
                    inputLength.setText(String.valueOf(MAXLENGTH - s.length()));
                }
            }
        });


    }

    @Override
    public void onClick(View v) {
        if (l != null) {
            l.onClick(this, etInput.getText().toString().trim());
        }
    }


    //自定义一个接口监听器
    public interface OnClickListener {
        void onClick(DialogInterface diglog, String text);

    }

    //设置一个监听器
    public void setOnClickListener(OnClickListener l){
        this.l = l;
    }



}

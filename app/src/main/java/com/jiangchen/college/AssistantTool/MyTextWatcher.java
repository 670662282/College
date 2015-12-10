package com.jiangchen.college.AssistantTool;

import android.text.TextWatcher;

/**
 * Created by Dell- on 2015/12/5 0005.
 * 抽象类 实现了TextWatcher文本改变监听接口
 */
public abstract  class MyTextWatcher implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }
}

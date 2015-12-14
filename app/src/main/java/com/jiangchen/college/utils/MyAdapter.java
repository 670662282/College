package com.jiangchen.college.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiangchen.college.R;

/**
 * Created by Dell- on 2015/12/8 0008.
 */
public class MyAdapter extends BaseAdapter {

    final String[] str = {"校内信息", "账号与安全", "系统设置", "意见反馈"};
    final int[] resId = {R.drawable.icon_mail, R.drawable.icon_lock, R.drawable.icon_set, R.drawable.icon_write};

    private Context context;


    public MyAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return str.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;


        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, null);
            viewHolder.centerIv = (ImageView) convertView.findViewById(R.id.list_item_iv);
            viewHolder.centerTv = (TextView) convertView.findViewById(R.id.list_item_tv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.centerTv.setText(str[position]);
        viewHolder.centerIv.setImageResource(resId[position]);


        return convertView;
    }


    static class ViewHolder {

        public TextView centerTv;
        public ImageView centerIv;

    }
}

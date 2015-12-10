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

        ViewHolder viewHolder = null;
        View view = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.list_item, null);
            viewHolder.centerIv = (ImageView) view.findViewById(R.id.list_item_iv);
            viewHolder.centerTv = (TextView) view.findViewById(R.id.list_item_tv);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }


        viewHolder.centerTv.setText(str[position]);
        viewHolder.centerIv.setImageResource(resId[position]);


        return view;
    }


    private class ViewHolder {

        TextView centerTv;
        ImageView centerIv;

    }
}

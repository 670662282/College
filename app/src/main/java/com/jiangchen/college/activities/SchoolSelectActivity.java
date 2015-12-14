package com.jiangchen.college.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.jiangchen.college.AssistantTool.Code;
import com.jiangchen.college.AssistantTool.Matchers;
import com.jiangchen.college.AssistantTool.MyTextWatcher;
import com.jiangchen.college.Log.LogUtil;
import com.jiangchen.college.R;
import com.jiangchen.college.https.XUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Dell- on 2015/12/11 0011.
 */
public class SchoolSelectActivity extends BaseActivity {

    @ViewInject(R.id.select_etInput)
    private EditText etInput;
    @ViewInject(R.id.select_back)
    private ImageView imgBack;
    @ViewInject(R.id.select_search)
    private ImageView imgSearch;
    @ViewInject(R.id.select_items)
    private ListView items;

    private SuggestionSearch search;
    private SuggestAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_selectschool);
        ViewUtils.inject(this);
        search = SuggestionSearch.newInstance();
        search.setOnGetSuggestionResultListener(new OnGetSuggestionResultListener() {
            @Override
            public void onGetSuggestionResult(SuggestionResult suggestionResult) {
                //如果不为空 而且没有错误
                if (suggestionResult != null && suggestionResult.error == SearchResult.ERRORNO.NO_ERROR) {
                    List<SuggestionResult.SuggestionInfo> infos = suggestionResult.getAllSuggestions();
                    if (infos != null) {
                        //像适配器里面添加数据
                        adapter.addAll(infos);
                    }
                }
            }
        });

        etInput.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    searchSuggest();
                }
            }
        });

        //为listView设置点击事件 获得点击事件的那一项数据 用intent传送原来的Activity
        items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SuggestionResult.SuggestionInfo info = adapter.getItem(position);
                Intent in = new Intent();
                in.putExtra("name", info.key);
                in.putExtra("area", info.city + " " + info.district);
                setResult(Code.RESP_SELECT_SCHOOL, in);
                finish();
            }
        });

        adapter = new SuggestAdapter();
        items.setAdapter(adapter);
    }


    //查找学校
    private void searchSuggest() {

        String content = etInput.getText().toString().trim();
        if (content != null) {
            SuggestionSearchOption option = new SuggestionSearchOption();
            option.city("");
            option.keyword(content);
            search.requestSuggestion(option);
        }
    }


    @OnClick({R.id.select_back, R.id.select_search})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.select_back:
                finish();
                break;
            case R.id.select_search:
                XUtils.show("开始查找");
                searchSuggest();
                break;
            default:
                break;
        }

    }


    public static void startActivityForResult(Activity activity) {
        Intent in = new Intent(activity, SchoolSelectActivity.class);
        activity.startActivityForResult(in, Code.REQ_SELECT_SCHOOL);
    }


    class SuggestAdapter extends BaseAdapter {

        List<SuggestionResult.SuggestionInfo> list = new ArrayList<>();

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public SuggestionResult.SuggestionInfo getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;

            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView =  LayoutInflater.from(SchoolSelectActivity.this).inflate(R.layout.list_item_suggest, null);
                viewHolder.area = (TextView) convertView.findViewById(R.id.suggest_search_area);
                viewHolder.name = (TextView) convertView.findViewById(R.id.suggest_search_name);
                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            //把适配器里面的数据线到界面上
            SuggestionResult.SuggestionInfo info = getItem(position);
            if (info != null) {
                viewHolder.name.setText(info.key);
                viewHolder.area.setText(info.city + " " + info.district);
            }


            return convertView;
        }

        //获取迭代器 遍历 集合中所有元素 正则匹配 过滤掉不需要的数据
        public void addAll(List<SuggestionResult.SuggestionInfo> infos) {

            Iterator<SuggestionResult.SuggestionInfo> it = infos.iterator();

            while (it.hasNext()) {
                SuggestionResult.SuggestionInfo info = it.next();
                if (!info.key.matches(Matchers.SCHOOL_MATHCH) || TextUtils.isEmpty(info.city)) {
                    it.remove();
                    LogUtil.e("key", info.key);
                }
            }

            if (infos.size() <= 0) {
                return;
            }
            //清除之前的
            list.clear();
            list.addAll(infos);
            notifyDataSetChanged();
        }


        class ViewHolder {
            public TextView name;
            public TextView area;
        }
    }
}

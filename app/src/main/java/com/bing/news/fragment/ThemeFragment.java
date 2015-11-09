package com.bing.news.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bing.news.R;
import com.bing.news.activity.DetailActivity;
import com.bing.news.adapter.GridViewAdapter;
import com.bing.news.adapter.ListViewAdapter;
import com.bing.news.config.AppConfig;
import com.bing.news.domain.LastStories;
import com.bing.news.domain.Stories;
import com.bing.news.domain.ThemeStories;
import com.bing.news.utils.AsyncHttpUtils;
import com.bing.news.utils.Constants;
import com.bing.news.utils.LogUtils;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Description 主题新闻
 * Created by bing on 2015/11/3.
 */
public class ThemeFragment extends Fragment {
    private static final int WHAT = 0x021;//判断消息类型
    private static final int WHAT_REFRESH = 0x022;//下拉刷新
    private static final int WHAT_LOAD_MORE = 0x023;//上拉加载更多
    private SwipeRefreshLayout refreshLayout;//下拉刷新控件
    private ListView themeListView;//新闻列表
    private ListViewAdapter listViewAdapter;//新闻列表 适配器
    private GridViewAdapter gridViewAdapter;//主编布局 适配器
    private ImageView topImage;//顶部图片布局
    private TextView titleText;//顶部新闻描述
    private GridView gridView;//推荐者布局控件
    private Context context;
    private Handler handler;
    private int theme_Id;//主题id
    private ArrayList<Stories> storiesArrayList;
    private ArrayList<ThemeStories.Editor> editorList;
    private LinearLayout editorLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_theme, container, false);
        context = getActivity();
        Bundle bundle = getArguments();
        theme_Id = bundle.getInt("id");
        initView(view);
        initRefreshLayoutListener();
        initData();
        initListener();
        initListViewListener();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == WHAT) {
                    Gson gson = new Gson();
                    ThemeStories themeStories = gson.fromJson(msg.obj.toString(), ThemeStories.class);
                    Picasso.with(context).load(themeStories.background).into(topImage);
                    titleText.setText(themeStories.description);
                    storiesArrayList = themeStories.stories;
                    listViewAdapter = new ListViewAdapter(context, storiesArrayList);
                    themeListView.setAdapter(listViewAdapter);

                    if (themeStories.editors != null) {
                        editorList = themeStories.editors;
                        gridViewAdapter = new GridViewAdapter(context, editorList);
                        gridView.setAdapter(gridViewAdapter);
                    }
                } else if (msg.what == WHAT_REFRESH) {//下拉刷新
                    Gson gson = new Gson();
                    ThemeStories themeStories = gson.fromJson(msg.obj.toString(), ThemeStories.class);
                    Picasso.with(context).load(themeStories.background).into(topImage);
                    titleText.setText(themeStories.description);
                    storiesArrayList.clear();
                    for (int i = 0; i < themeStories.stories.size(); i++) {
                        storiesArrayList.add(themeStories.stories.get(i));
                    }
                    listViewAdapter.notifyDataSetChanged();
                } else if (msg.what == WHAT_LOAD_MORE) {//上拉加载更多
                    Gson gson = new Gson();
                    LastStories lastStories = gson.fromJson(msg.obj.toString(), LastStories.class);
                    for (int i = 0; i < lastStories.stories.size(); i++) {
                        storiesArrayList.add(lastStories.stories.get(i));
                    }
                    listViewAdapter.notifyDataSetChanged();
                }
            }
        };
        return view;
    }

    private void initView(View view) {
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.layout_theme_refresh);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        themeListView = (ListView) view.findViewById(R.id.lv_theme_news);
        View header = View.inflate(context, R.layout.news_image_header, null);
        topImage = (ImageView) header.findViewById(R.id.iv_theme_news_bg);
        titleText = (TextView) header.findViewById(R.id.tv_theme_news_title);
        gridView = (GridView) header.findViewById(R.id.gridview_header_editor);
        editorLayout = (LinearLayout) header.findViewById(R.id.layout_theme_editor);
        themeListView.addHeaderView(header, null, false);
    }

    private void initListener() {
        gridView.setEnabled(false);
        gridView.setClickable(false);
        editorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "clicked", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 下拉刷新
     */
    private void initRefreshLayoutListener() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void initListViewListener() {
        /**
         * item点击监听
         */
        themeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String read_Id = AppConfig.getInstance().getIsRead();
                if (!read_Id.contains("" + storiesArrayList.get(position - 1).id)) {
                    read_Id = read_Id + "," + storiesArrayList.get(position - 1).id;
                    AppConfig.getInstance().setIsRead(read_Id);
                }
                //设置阅读状态
                TextView textView = (TextView) view.findViewById(R.id.tv_main_news_list);
                textView.setTextColor(Color.GRAY);

                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt("id", storiesArrayList.get(position - 1).id);//position-1:listview设置了header
                intent.putExtra("bundle", bundle);
                intent.setClass(context, DetailActivity.class);
                startActivity(intent);
            }
        });
        /**
         * 上拉加载更多
         */
        themeListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int count = storiesArrayList.size() - 1;
                String url = Constants.THEME_URL_CONTENT + theme_Id + "/before/" + storiesArrayList.get(count).id;
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && themeListView.getLastVisiblePosition() == themeListView.getCount() - 1) {
                    AsyncHttpUtils.doHttpRequestForJson(AsyncHttpUtils.GET, url, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Message message = handler.obtainMessage();
                            message.what = WHAT_LOAD_MORE;
                            message.obj = response;
                            handler.sendMessage(message);
                        }
                    });
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    /**
     * 加载数据
     */
    private void initData() {
        AsyncHttpUtils.doHttpRequestForJson(AsyncHttpUtils.GET, Constants.THEME_URL_CONTENT + theme_Id, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Message msg = handler.obtainMessage();
                msg.what = WHAT;
                msg.obj = response;
                handler.sendMessage(msg);
            }
        });
    }
}

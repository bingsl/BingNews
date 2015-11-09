package com.bing.news.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bing.news.R;
import com.bing.news.activity.DetailActivity;
import com.bing.news.adapter.ListViewAdapter;
import com.bing.news.adapter.ViewPagerAdapter;
import com.bing.news.config.AppConfig;
import com.bing.news.domain.LastStories;
import com.bing.news.domain.Latest;
import com.bing.news.domain.Stories;
import com.bing.news.domain.TopStories;
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
 * Description 主页
 * Created by bing on 2015/11/3.
 */
public class MainFragment extends Fragment {
    /**
     * message.what 类型
     */
    private static final int WHAT_LATEST = 0x001;//加载数据
    private static final int WHAT_REFRESH = 0x002;//下拉刷新
    private static final int WHAT_UPDATE_DOT = 0x003;//更新圆点状态
    private static final int WHAT_LOAD_BEFORE = 0x004;//上拉加载更多
    private Context context;
    private SwipeRefreshLayout refreshLayout;
    private LinearLayout dotLayout;
    /**
     * 列表新闻
     */
    private ListView listView;
    private ListViewAdapter listViewAdapter;
    /**
     * 顶部轮播新闻
     */
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    /**
     * 服务器返回数据
     */
    private Latest latest;
    private LastStories lastStories;
    private String date;
    private Handler handler;
    private Gson gson;
    private ArrayList<Stories> storiesList;
    private ArrayList<TopStories> topStoriesList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        listView = (ListView) view.findViewById(R.id.lv_main_news);
        final View header = View.inflate(context, R.layout.news_list_header, null);
        viewPager = (ViewPager) header.findViewById(R.id.vp_main_top_news);
        listView.addHeaderView(header);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.layout_swiperefresh);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        dotLayout = (LinearLayout) header.findViewById(R.id.layout_dot);
        initDot();
        initData();
        initRefreshLayoutListener();
        initViewPagerListener();
        initListViewListener();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == WHAT_LATEST) {//初次加载数据
                    gson = new Gson();
                    latest = gson.fromJson(msg.obj.toString(), Latest.class);
                    storiesList = latest.stories;
                    topStoriesList = latest.top_stories;
                    date = latest.date;
                    storiesList.add(0, new Stories(date));//日期
                    listViewAdapter = new ListViewAdapter(context, storiesList);
                    viewPagerAdapter = new ViewPagerAdapter(context, topStoriesList);
                    listView.setAdapter(listViewAdapter);
                    viewPager.setAdapter(viewPagerAdapter);
                    viewPager.setCurrentItem(100);
                    handler.sendEmptyMessageDelayed(WHAT_UPDATE_DOT, 5000);
                } else if (msg.what == WHAT_REFRESH) {//下拉刷新
                    storiesList.clear();
                    topStoriesList.clear();
                    latest = gson.fromJson(msg.obj.toString(), Latest.class);
                    storiesList.add(0, new Stories(latest.date));//日期
                    for (int i = 0; i < latest.stories.size(); i++) {
                        storiesList.add(latest.stories.get(i));
                    }
                    for (int i = 0; i < latest.top_stories.size(); i++) {
                        topStoriesList.add(latest.top_stories.get(i));
                    }
                    date = latest.date;
                    listViewAdapter.notifyDataSetChanged();
                    viewPagerAdapter.notifyDataSetChanged();
                } else if (msg.what == WHAT_UPDATE_DOT) {//更新顶部圆点状态
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    updateDots();
                    handler.sendEmptyMessageDelayed(WHAT_UPDATE_DOT, 5000);
                } else if (msg.what == WHAT_LOAD_BEFORE) {//上拉加载更多
                    lastStories = gson.fromJson(msg.obj.toString(), LastStories.class);
                    storiesList.add(new Stories(lastStories.date));//日期
                    for (int i = 0; i < lastStories.stories.size(); i++) {
                        storiesList.add(lastStories.stories.get(i));
                    }
                    date = lastStories.date;
                    listViewAdapter.notifyDataSetChanged();
                }
            }
        };
        return view;
    }


    private void initListViewListener() {
        /**
         * listView点击监听
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (storiesList.get(position - 1).title != null) {//判断是新闻还是日期
                    String read_Id = AppConfig.getInstance().getIsRead();
                    if (!read_Id.contains("" + storiesList.get(position - 1).id)) {
                        read_Id = read_Id + "," + storiesList.get(position - 1).id;
                        AppConfig.getInstance().setIsRead(read_Id);
                    }
                    //设置阅读状态
                    TextView textView = (TextView) view.findViewById(R.id.tv_main_news_list);
                    textView.setTextColor(Color.GRAY);

                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", storiesList.get(position - 1).id);//position-1:listview设置了header
                    intent.putExtra("bundle", bundle);
                    intent.setClass(context, DetailActivity.class);
                    startActivity(intent);
                } else {
                    view.setEnabled(false);
                }
            }
        });
        /**
         * 上拉加载更多
         */
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && listView.getLastVisiblePosition() == listView.getCount() - 1) {
                    AsyncHttpUtils.doHttpRequestForJson(AsyncHttpUtils.GET, Constants.BEFORE_NEWS_URL + date, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Message message = handler.obtainMessage();
                            message.what = WHAT_LOAD_BEFORE;
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
     * viewPager滑动监听
     */
    private void initViewPagerListener() {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                updateDots();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 加载数据
     */
    private void initData() {
        storiesList = new ArrayList<>();
        topStoriesList = new ArrayList<>();
        AsyncHttpUtils.doHttpRequestForJson(AsyncHttpUtils.GET, Constants.NEW_NEWS_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Message message = handler.obtainMessage();
                message.what = WHAT_LATEST;
                message.obj = response;
                handler.sendMessage(message);
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
                AsyncHttpUtils.doHttpRequestForJson(AsyncHttpUtils.GET, Constants.NEW_NEWS_URL, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Message message = handler.obtainMessage();
                        message.what = WHAT_REFRESH;
                        message.obj = response;
                        handler.sendMessage(message);
                    }
                });
                refreshLayout.setRefreshing(false);
            }
        });
    }

    /**
     * 圆点布局
     */
    private void initDot() {
        for (int i = 0; i < 5; i++) {
            View view = new View(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(12, 12);
            if (i != 0) {
                layoutParams.leftMargin = 10;//圆点间距
            }
            view.setLayoutParams(layoutParams);
            view.setBackgroundResource(R.drawable.dot_selector);
            dotLayout.addView(view);
        }
    }

    /**
     * 更新圆点状态
     */
    public void updateDots() {
        int currentItem = viewPager.getCurrentItem() % latest.top_stories.size();
        for (int i = 0; i < dotLayout.getChildCount(); i++) {
            dotLayout.getChildAt(i).setEnabled(i == currentItem);
        }
    }
}

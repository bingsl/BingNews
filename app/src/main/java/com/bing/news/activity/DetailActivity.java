package com.bing.news.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;

import com.bing.news.R;
import com.bing.news.fragment.DetailFragment;
import com.bing.news.utils.AsyncHttpUtils;
import com.bing.news.utils.Constants;
import com.bing.news.utils.LogUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Description 新闻详细信息
 * Created by bing on 2015/11/6.
 */
public class DetailActivity extends BaseActivity {
    private Toolbar toolbar;

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar_detail);
        toolbar.setNavigationIcon(R.mipmap.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setBackgroundColor(getResources().getColor(R.color.reside_top));
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        initToolBarListener();
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.layout_frame_detail, detailFragment, null).commit();
    }

    private void initToolBarListener() {
        /**
         * 返回监听
         */
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityTaskStack.remove(DetailActivity.this);
                finish();
            }
        });
        /**
         * item点击监听
         */
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_share://分享
                        break;
                    case R.id.action_collect://收藏
                        break;
                    case R.id.action_comment://点评
                        break;
                    case R.id.action_praise://赞
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }
}

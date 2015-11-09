package com.bing.news.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bing.news.R;
import com.bing.news.activity.DetailActivity;
import com.bing.news.domain.TopStories;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Description 顶部viewpager适配器
 * Created by bing on 2015/11/6.
 */
public class ViewPagerAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<TopStories> topStoriesList;
    private TextView textView;
    private ImageView imageView;

    public ViewPagerAdapter(Context context, ArrayList<TopStories> topStoriesList) {
        this.context = context;
        this.topStoriesList = topStoriesList;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = View.inflate(context, R.layout.news_list_header_item, null);
        textView = (TextView) view.findViewById(R.id.tv_top_news);
        imageView = (ImageView) view.findViewById(R.id.iv_top_news);
        textView.setText(topStoriesList.get(position % 5).title);
        Picasso.with(context).load(topStoriesList.get(position % 5).image).into(imageView);
        container.addView(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt("id", topStoriesList.get(position % 5).id);
                bundle.putString("title", topStoriesList.get(position % 5).title);
                bundle.putString("image", topStoriesList.get(position % 5).image);
                intent.putExtra("bundle", bundle);
                intent.setClass(context, DetailActivity.class);
                context.startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}

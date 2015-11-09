package com.bing.news.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bing.news.R;
import com.bing.news.domain.ThemeStories;
import com.bing.news.view.CircleImage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Description 主编，推荐者 布局适配器
 * Created by bing on 2015/11/8.
 */
public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ThemeStories.Editor> editorList;

    public GridViewAdapter(Context context, ArrayList<ThemeStories.Editor> editorList) {
        this.context = context;
        this.editorList = editorList;
    }

    @Override
    public int getCount() {
        return editorList.size();
    }

    @Override
    public Object getItem(int position) {
        return editorList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(context, R.layout.editor_grid_item, null);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_editor_grid);
        Picasso.with(context).load(editorList.get(position).avatar).transform(new CircleImage()).into(imageView);
        return convertView;
    }
}

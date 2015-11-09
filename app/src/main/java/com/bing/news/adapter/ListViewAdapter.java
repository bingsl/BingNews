package com.bing.news.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bing.news.R;
import com.bing.news.config.AppConfig;
import com.bing.news.domain.Stories;
import com.bing.news.utils.DateUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Description 主页列表适配器
 * Created by bing on 2015/11/6.
 */
public class ListViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Stories> storiesList;
    private ViewHolder viewHolder;

    public ListViewAdapter(Context context, ArrayList<Stories> storiesList) {
        this.context = context;
        this.storiesList = storiesList;
    }

    /**
     * 判断listview item 类型
     *
     * @param position
     * @return 0 日期，1 新闻
     */
    @Override
    public int getItemViewType(int position) {
        return (storiesList.get(position).title == null) ? 0 : 1;//根据新闻标题来判断要加载的item类型
    }

    /**
     * listview item 种类
     *
     * @return 总共有两种，新闻简略信息和日期
     */
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return storiesList.size();
    }

    @Override
    public Object getItem(int position) {
        return storiesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position) == 0) {//加载日期
            String dateString = storiesList.get(position).getDate();
            String string = dateString.substring(0, 4) + "-" + dateString.substring(4, 6) + "-" + dateString.substring(6, dateString.length());
            String week = DateUtil.getWeek(string);
            String dateText = dateString.substring(4, 6) + "月" + dateString.substring(6, dateString.length()) + "日 " + week;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(context, R.layout.news_list_item_date, null);
                viewHolder.dateText = (TextView) convertView.findViewById(R.id.tv_news_list_date);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            if (string.equals(DateUtil.getDateTime(DateUtil.FOR_YEAR_DAY))) {
                dateText = "今日热闻";
            }
            viewHolder.dateText.setText(dateText);
        } else if (getItemViewType(position) == 1) {//加载新闻
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(context, R.layout.news_list_item, null);
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_main_news_list);
                viewHolder.titleText = (TextView) convertView.findViewById(R.id.tv_main_news_list);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            //是否阅读过当前新闻
            String read_id = AppConfig.getInstance().getIsRead();
            if (read_id.contains("" + storiesList.get(position).id)) {
                viewHolder.titleText.setTextColor(Color.GRAY);
            } else {
                viewHolder.titleText.setTextColor(Color.BLACK);
            }

            viewHolder.titleText.setText(storiesList.get(position).title);
            if (storiesList.get(position).images != null) {
                Picasso.with(context).load(storiesList.get(position).images[0]).into(viewHolder.imageView);//如果有多张图片，加载第一张
            }
        }
        return convertView;
    }

    public static class ViewHolder {
        private ImageView imageView;//新闻缩略图
        private TextView titleText;//新闻标题
        private TextView dateText;//日期
    }
}
package com.bing.news.domain;

import java.util.ArrayList;

/**
 * Description 最新新闻
 * Created by bing on 2015/11/5.
 * http://news-at.zhihu.com/api/4/news/latest
 */
public class Latest {
    /**
     * 日期
     */
    public String date;
    /**
     * 列表新闻
     */
    public ArrayList<Stories> stories;
    /**
     * 顶部新闻
     */
    public ArrayList<TopStories> top_stories;

    @Override
    public String toString() {
        return "Latest{" +
                "date='" + date + '\'' +
                ", stories=" + stories +
                ", top_stories=" + top_stories +
                '}';
    }
}

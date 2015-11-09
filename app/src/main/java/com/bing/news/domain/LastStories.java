package com.bing.news.domain;

import java.util.ArrayList;

/**
 * Description 加载过往的数据   before
 * Created by bing on 2015/11/5.
 * http://news.at.zhihu.com/api/4/news/before/20131119 主页新闻
 * http://news-at.zhihu.com/api/4/theme/4/before/7266397 主题新闻
 */
public class LastStories {
    public String date;
    public ArrayList<Stories> stories;

    @Override
    public String toString() {
        return "LastStories{" +
                "date='" + date + '\'' +
                ", stories=" + stories +
                '}';
    }
}

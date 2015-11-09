package com.bing.news.domain;

import java.util.Arrays;

/**
 * Description 列表新闻封装类
 * Created by bing on 2015/11/5.
 */
public class Stories {
    /**
     * 图片链接集合
     */
    public String[] images;
    public int id;
    public String title;
    private String date;//listview加载日期

    public Stories(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Stories{" +
                "images=" + Arrays.toString(images) +
                ", id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}

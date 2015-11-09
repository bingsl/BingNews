package com.bing.news.domain;

/**
 * Description 顶部轮播新闻封装类
 * Created by bing on 2015/11/5.
 */
public class TopStories {
    /**
     * 图片链接
     */
    public String image;
    public int id;
    /**
     * 新闻标题
     */
    public String title;

    @Override
    public String toString() {
        return "TopStories{" +
                "image='" + image + '\'' +
                ", id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}

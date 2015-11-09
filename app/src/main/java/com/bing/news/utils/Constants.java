package com.bing.news.utils;

/**
 * Description
 * Created by bing on 2015/11/2.
 */
public class Constants {

    public static final String SHARED_PREFERENCE = "shared_preference";//sharedPrefreences文件名
    public static final String IS_READ = "is_read"; //listView item 是否被点击过

    //-----------------API接口-------------------
    public static final String BASE_URL = "http://news-at.zhihu.com/api/4";
    /***
     * 欢迎界面图片接口
     */
    public static final String SPLASH_IMAGE_URL = BASE_URL + "/start-image/1080*1776";
    /**
     * 最新消息接口
     */
    public static final String NEW_NEWS_URL = BASE_URL + "/news/latest";
    /**
     * 过往新闻
     */
    public static final String BEFORE_NEWS_URL = BASE_URL + "/news/before/";
    /**
     * 新闻详细信息
     */
    public static final String DETAIL_NEWS = BASE_URL + "/news/";
    /**
     * 主题列表
     */
    public static final String THEME_URL_LIST = BASE_URL + "/themes";
    /**
     * 主题日报内容
     */
    public static final String THEME_URL_CONTENT = BASE_URL + "/theme/";
}

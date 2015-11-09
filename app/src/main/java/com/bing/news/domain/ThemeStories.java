package com.bing.news.domain;

import java.util.ArrayList;

/**
 * Description 主题新闻列表
 * Created by bing on 2015/11/7.
 * http://news-at.zhihu.com/api/4/theme/8
 */
public class ThemeStories {

    public ArrayList<Stories> stories;//新闻信息
    public String description;//顶部新闻描述
    public String background;//顶部新闻图片
    public String name;//主题名称
    public ArrayList<Editor> editors;//主编，推荐者
    public String image_source;//顶部图片来源

    public class Editor {
        public String url;//详细信息链接
        public String bio;//简介
        public int id;
        public String avatar;//图片链接
        public String name;//用户名
    }

    @Override
    public String toString() {
        return "ThemeStories{" +
                "stories=" + stories +
                ", description='" + description + '\'' +
                ", background='" + background + '\'' +
                ", name='" + name + '\'' +
                ", image_source='" + image_source + '\'' +
                '}';
    }
}

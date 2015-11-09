package com.bing.news.domain;

import java.util.ArrayList;

/**
 * Description 新闻主题列表
 * Created by bing on 2015/11/2.
 * http://news-at.zhihu.com/api/4/themes
 */
public class Theme {
    public ArrayList<ThemeItem> others;

    public static class ThemeItem {
        public String thumbnail;//图片链接
        public String description;//描述
        public int id;//新闻主题id
        public String name;//主题名称

        @Override
        public String toString() {
            return "ThemeItem{" +
                    "thumbnail='" + thumbnail + '\'' +
                    ", description='" + description + '\'' +
                    ", id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Theme{" +
                "others=" + others +
                '}';
    }
}

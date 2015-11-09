package com.bing.news.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.bing.news.Application;
import com.bing.news.utils.Constants;
/**
 * Description app相关配置
 * Created by bing on 2015/11/2.
 */
public class AppConfig {
    private static AppConfig instance;
    private SharedPreferences sharedPrefs;

    private AppConfig() {
        sharedPrefs = Application.getInstance().getSharedPreferences(Constants.SHARED_PREFERENCE, Context.MODE_PRIVATE);
    }

    /**
     * 单例
     *
     * @return
     */
    public static AppConfig getInstance() {
        if (instance == null) {
            synchronized (AppConfig.class) {
                if (instance == null) {
                    instance = new AppConfig();
                }
            }
        }
        return instance;
    }

    /**
     * listView item是否被点击，标识新闻是否被阅读过
     *
     * @param id 新闻id
     * @return
     */
    public boolean setIsRead(String id) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(Constants.IS_READ, id);
        return editor.commit();
    }

    public String getIsRead() {
        return sharedPrefs.getString(Constants.IS_READ, "");
    }
}

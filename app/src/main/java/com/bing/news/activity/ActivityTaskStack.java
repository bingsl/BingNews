package com.bing.news.activity;

import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * Description 管理activity
 * Created by bing on 2015/11/2.
 */
public class ActivityTaskStack {
    public static ArrayList<BaseActivity> arrayList = new ArrayList<>();

    /**
     * 添加activity
     *
     * @param baseActivity
     */
    public static void add(BaseActivity baseActivity) {
        arrayList.add(baseActivity);
    }

    /**
     * 删除activity
     *
     * @param baseActivity
     */
    public static void remove(BaseActivity baseActivity) {
        arrayList.remove(baseActivity);
    }

    /**
     * 退出程序时清空activity
     */
    public static void clear() {
        for (AppCompatActivity activity : arrayList) {
            if (activity != null) {
                activity.finish();
            }
        }
        arrayList.clear();
    }
}

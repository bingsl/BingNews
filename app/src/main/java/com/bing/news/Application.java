package com.bing.news;

import com.bing.news.activity.ActivityTaskStack;

/**
 * Description
 * Created by bing on 2015/11/2.
 */
public class Application extends android.app.Application {
    private static Application _Application;

    @Override
    public void onCreate() {
        super.onCreate();
        _Application = this;
    }

    public static Application getInstance() {
        return _Application;
    }

    /**
     * 退出App
     */
    public static void exitApp() {
        ActivityTaskStack.clear();
        System.exit(0);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}

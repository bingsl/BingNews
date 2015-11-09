package com.bing.news.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Description
 * Created by bing on 2015/11/2.
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected AppCompatActivity context;
    public FragmentManager fragmentManager;
    private Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        super.onCreate(savedInstanceState);
        ActivityTaskStack.add(this);
        context = this;
        fragmentManager = getSupportFragmentManager();
        onActivityCreate(savedInstanceState);

    }
    protected abstract void onActivityCreate(Bundle savedInstanceState);

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityTaskStack.remove(this);
    }
}

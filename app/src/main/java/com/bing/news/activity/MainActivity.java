package com.bing.news.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bing.news.Application;
import com.bing.news.R;
import com.bing.news.fragment.MainFragment;

public class MainActivity extends BaseActivity {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private long firstTime = 0;//记录按下返回键时间

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        initView();
        initToolbarListener();
        fragmentManager.beginTransaction().replace(R.id.layout_content, new MainFragment(), null).commit();
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        drawerLayout = (DrawerLayout) findViewById(R.id.layout_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.reside_top));
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.mipmap.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void initToolbarListener() {
        /**
         * 打开侧滑菜单
         */
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        /**
         * toolbar item监听
         */
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_settings:
                        Toast.makeText(context, "setting", Toast.LENGTH_LONG).show();
                        break;
                }
                return true;
            }
        });
    }

    /**
     * 关闭侧滑菜单
     */
    public void closeResideMenu() {
        drawerLayout.closeDrawers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * getFragmentManager
     *
     * @return fragmentManager
     */
    public FragmentManager getMyFragmentManager() {
        return fragmentManager;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    /**
     * 返回键监听
     */
    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - firstTime > 2000) {
            firstTime = currentTime;
            Toast.makeText(context, R.string.one_more_time_exit, Toast.LENGTH_LONG).show();
        } else {
            Application.exitApp();
        }
    }
}

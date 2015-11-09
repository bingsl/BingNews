package com.bing.news.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bing.news.R;
import com.bing.news.utils.AsyncHttpUtils;
import com.bing.news.utils.Constants;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Description 欢迎界面
 * Created by bing on 2015/11/2.
 */
public class SplashActivity extends Activity {
    private ImageView imageView;
    private TextView textView;
    private ImageView logoImg;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        initView();
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 5000);
    }

    private void initView() {
        imageView = (ImageView) findViewById(R.id.iv_splash);
        textView = (TextView) findViewById(R.id.tv_splash);
        logoImg = (ImageView) findViewById(R.id.iv_splash_logo);
        //缩放动画
        Animation animation = new ScaleAnimation(1f, 1.2f, 1f, 1.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setFillAfter(true);//动画停留在最后一帧
        animation.setDuration(5000);
        imageView.startAnimation(animation);
        AsyncHttpUtils.doHttpRequestForJson(AsyncHttpUtils.GET, Constants.SPLASH_IMAGE_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.has("text")) {
                        textView.setText(response.getString("text"));
                    }
                    Picasso.with(SplashActivity.this).load(response.getString("img")).into(imageView);
                    logoImg.setImageResource(R.mipmap.splash_logo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}

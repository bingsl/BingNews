package com.bing.news.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bing.news.R;
import com.bing.news.utils.AsyncHttpUtils;
import com.bing.news.utils.Constants;
import com.bing.news.utils.LogUtils;
import com.bing.news.view.CircleImage;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Description 新闻详细信息
 * Created by bing on 2015/11/8.
 */
public class DetailFragment extends Fragment {
    private static final int WHAT = 0x011;
    private WebView webView;
    private ImageView topImage;//顶部图片
    private TextView imageSourceText;//图片来源
    private TextView titleText;//新闻标题
    private ImageView editorImage;//推荐者图片
    private RelativeLayout topLayout;//顶部布局
    private Handler handler;
    private Context context;
    private String shareUrl;

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_news, container, false);
        context = getActivity();
        initView(view);
        initData();
        return view;
    }

    /**
     * 初始化view
     */
    private void initView(View view) {
        topImage = (ImageView) view.findViewById(R.id.iv_detail_news_bg);
        webView = (WebView) view.findViewById(R.id.webview_detail_news);
        imageSourceText = (TextView) view.findViewById(R.id.tv_detail_img_source);
        titleText = (TextView) view.findViewById(R.id.tv_detail_news_title);
        editorImage = (ImageView) view.findViewById(R.id.lv_detail_news_presenter);
        topLayout = (RelativeLayout) view.findViewById(R.id.layout_detail_news_top);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == WHAT) {
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    parseData(jsonObject);
                }
            }
        };
    }

    /**
     * 解析服务端返回的数据
     *
     * @param jsonObject 服务器返回的数据
     */
    private void parseData(JSONObject jsonObject) {
        try {
            if (jsonObject.has("image")) {
                Picasso.with(context).load(jsonObject.getString("image")).into(topImage);
                titleText.setText(jsonObject.getString("title"));
            } else {
                topLayout.setVisibility(View.GONE);
            }
            if (jsonObject.has("body")) {
                String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/css/news.css\" type=\"text/css\">";
                String html = "<html><head>" + css + "</head><body>" + jsonObject.getString("body") + "</body></html>";
                html = html.replace("<div class=\"img-place-holder\">", "");
                webView.loadDataWithBaseURL("x-data://base", html, "text/html", "UTF-8", null);
            } else {
                webView.setVisibility(View.GONE);
            }
            if (jsonObject.has("recommenders")) {
                JSONArray jsonArray = jsonObject.getJSONArray("recommenders");
                JSONObject object = jsonArray.getJSONObject(0);
                String url = object.getString("avatar");
                Picasso.with(context).load(url).transform(new CircleImage()).into(editorImage);
            }
            if (jsonObject.has("image_source")) {
                imageSourceText.setText(jsonObject.getString("image_source"));
            }
            if (jsonObject.has("share_url")) {
                shareUrl = jsonObject.getString("share_url");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Bundle bundle = getArguments();
        int id = bundle.getInt("id");
        AsyncHttpUtils.doHttpRequestForJson(AsyncHttpUtils.GET, Constants.DETAIL_NEWS + id, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Message message = handler.obtainMessage();
                message.what = WHAT;
                message.obj = response;
                handler.sendMessage(message);
            }
        });
    }

}

package com.bing.news.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bing.news.R;
import com.bing.news.activity.MainActivity;
import com.bing.news.domain.Theme;
import com.bing.news.utils.AsyncHttpUtils;
import com.bing.news.utils.Constants;
import com.bing.news.utils.LogUtils;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Description 侧滑菜单
 * Created by bing on 2015/11/2.
 */
public class ResideMenuFragment extends Fragment implements View.OnClickListener {
    private TextView loginText;//登录
    private TextView collectText;//我的收藏
    private TextView downloadText;//离线下载
    private TextView mainPageText;//主页
    private RecyclerView recyclerView;//主题列表
    private Theme theme;
    private List<Theme.ThemeItem> themeList;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reside_menu, container, false);
        context = getActivity();
        initView(view);
        initListener();
        return view;
    }

    private void initView(View view) {
        loginText = (TextView) view.findViewById(R.id.tv_reside_login);
        collectText = (TextView) view.findViewById(R.id.tv_reside_collect);
        downloadText = (TextView) view.findViewById(R.id.tv_reside_download);
        mainPageText = (TextView) view.findViewById(R.id.tv_reside_mainpage);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_reside);
        themeList = new ArrayList<>();
        AsyncHttpUtils.doHttpRequestForJson(AsyncHttpUtils.GET, Constants.THEME_URL_LIST, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Gson gson = new Gson();
                theme = gson.fromJson(response.toString(), Theme.class);
                /**获取新闻主题列表内容**/
                themeList = theme.others;
                recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
                recyclerView.setAdapter(new Adapter(context, themeList));
            }
        });
    }

    private void initListener() {
        loginText.setOnClickListener(this);
        collectText.setOnClickListener(this);
        downloadText.setOnClickListener(this);
        mainPageText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_reside_login:
                break;
            case R.id.tv_reside_collect:
                break;
            case R.id.tv_reside_download:
                break;
            case R.id.tv_reside_mainpage:
                ((MainActivity) context).closeResideMenu();
                break;
        }
    }

    public static class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
        public List<Theme.ThemeItem> values;
        public Context context;

        public Adapter(Context context, List<Theme.ThemeItem> values) {
            this.context = context;
            this.values = values;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reside_recycler_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.textView.setText(values.get(position).name);
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) context).getToolbar().setTitle(values.get(position).name);
                    ThemeFragment themeFragment = new ThemeFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", values.get(position).id);
                    themeFragment.setArguments(bundle);
                    ((MainActivity) context).getMyFragmentManager().beginTransaction().replace(R.id.layout_content, themeFragment, null).commit();
                    ((MainActivity) context).closeResideMenu();
                }
            });
        }

        @Override
        public int getItemCount() {
            return values.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public View view;
            public TextView textView;

            public ViewHolder(View itemView) {
                super(itemView);
                view = itemView;
                textView = (TextView) view.findViewById(R.id.tv_reside_recycler_item);
            }
        }
    }
}

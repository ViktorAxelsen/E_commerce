package com.viktor.e_commerce;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.viktor.e_commerce.Adapter.CollectionAdapter;
import com.viktor.e_commerce.Util.HttpUtil;
import com.viktor.e_commerce.Util.Utility;
import com.viktor.e_commerce.gson.Item;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CollectionActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "CollectionActivity";

    private Button back;

    private SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView recyclerView;

    private CollectionAdapter adapter;

    private List<Item> collections = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        //取控件
        back = (Button)findViewById(R.id.back);
        recyclerView = (RecyclerView)findViewById(R.id.recycle_view);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        //监听
        back.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestCollection();
            }
        });
        //数据
        swipeRefreshLayout.setRefreshing(true);
        requestCollection();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new CollectionAdapter(collections);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void requestCollection(){
        String url = ROOT + "get_user_collection/" + userId;
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CollectionActivity.this, "获取收藏夹信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseText = response.body().string();
                final List<Item> items = Utility.parseJSONForItem(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        collections.clear();
                        for(int i = 0; i < items.size(); i++){
                            collections.add(items.get(i));
                        }
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                onBackPressed();
                break;
        }
    }

    public static void actionStart(Context context){
        Intent intent = new Intent(context, CollectionActivity.class);
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.activity_right_fade_out, R.anim.activity_no);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.activity_left_fade_in);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }
}

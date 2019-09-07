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

import com.viktor.e_commerce.Adapter.BrowseHistoryAdapter;
import com.viktor.e_commerce.Util.HttpUtil;
import com.viktor.e_commerce.Util.Utility;
import com.viktor.e_commerce.gson.BrowseRecord;
import com.viktor.e_commerce.gson.Item;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class BrowseHistoryActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "BrowseHistoryActivity";

    private Button back;

    private SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView recyclerView;

    private BrowseHistoryAdapter adapter;

    private List<Item> items = new ArrayList<>();

    public static List<BrowseRecord> records = new ArrayList<>();

    public static Boolean is;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_history);
        //取控件
        back = (Button)findViewById(R.id.back);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        recyclerView = (RecyclerView)findViewById(R.id.recycle_view);
        //监听
        back.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestItem();
                requestRecord();
            }
        });
        //数据
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new BrowseHistoryAdapter(items);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(true);
        requestItem();
        requestRecord();
    }

    private void requestItem(){
        String url = ROOT + "get_history_item/" + userId;
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BrowseHistoryActivity.this, "加载浏览记录失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseText = response.body().string();
                final List<Item> itemTemp = Utility.parseJSONForItem(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        items.clear();
                        for(int i = 0; i < itemTemp.size(); i++){
                            items.add(itemTemp.get(i));
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private void requestRecord(){
        is = false;
        String url = ROOT + "browseRecord/?format=json&ordering=-record_create_time&record_user=" + userId;
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BrowseHistoryActivity.this, "加载浏览记录失败", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                        is = true;
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseText = response.body().string();
                records = Utility.parseJSONForBrowseRecord(responseText);
                is = true;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
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
        Intent intent = new Intent(context, BrowseHistoryActivity.class);
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

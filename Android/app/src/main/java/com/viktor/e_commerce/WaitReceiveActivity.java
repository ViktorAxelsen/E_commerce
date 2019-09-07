package com.viktor.e_commerce;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.Toast;

import com.viktor.e_commerce.Adapter.WaitReceiveOrderAdapter;
import com.viktor.e_commerce.Util.HttpUtil;
import com.viktor.e_commerce.Util.Utility;
import com.viktor.e_commerce.gson.Item;
import com.viktor.e_commerce.gson.Trade;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WaitReceiveActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "WaitReceiveActivity";

    private Button back;

    private SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView recyclerView;

    private WaitReceiveOrderAdapter adapter;

    private List<Item> waits = new ArrayList<>();

    public List<Trade> trades = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_receive);
        //取控件
        back = (Button)findViewById(R.id.back);
        recyclerView = (RecyclerView)findViewById(R.id.recycle_view);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        //监听
        back.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWait();
                requestTrade();
            }
        });
        //数据
        swipeRefreshLayout.setRefreshing(true);
        requestWait();
        requestTrade();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new WaitReceiveOrderAdapter(waits, new ConfirmReceiveListener() {
            @Override
            public void confirm(final int position) {
                swipeRefreshLayout.setRefreshing(true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while(trades.size() == 0);
                        requestConfirm(position);
                    }
                }).start();
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void requestConfirm(int position){
        String url = ROOT + "confirm_receive/" + trades.get(position).getTradeId();
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .build();
        Request request = new Request.Builder().url(url).post(body).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WaitReceiveActivity.this, "确认收货失败", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseText = response.body().string();
                if(responseText.equals("\"success!\"")){
                    requestWait();
                    requestTrade();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(WaitReceiveActivity.this, "收货成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void requestTrade(){
        String url = ROOT + "trade/?format=json&trade_user=" + userId + "&max_trade_info=2";
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WaitReceiveActivity.this, "获取交易信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseText = response.body().string();
                trades = Utility.parseJSONForTrade(responseText);
            }
        });
    }

    private void requestWait(){
        String url = ROOT + "get_wait_receive/?user_id=" + userId + "&return_type=info";
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WaitReceiveActivity.this, "加载待收货列表失败", Toast.LENGTH_SHORT).show();
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
                        waits.clear();
                        for(int i = 0; i < items.size(); i++){
                            waits.add(items.get(i));
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
        Intent intent = new Intent(context, WaitReceiveActivity.class);
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

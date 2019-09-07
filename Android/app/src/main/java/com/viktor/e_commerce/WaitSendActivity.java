package com.viktor.e_commerce;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.viktor.e_commerce.Adapter.StoreItemAdapter;
import com.viktor.e_commerce.Adapter.WaitSendAdapter;
import com.viktor.e_commerce.Util.HttpUtil;
import com.viktor.e_commerce.Util.Utility;
import com.viktor.e_commerce.gson.Address;
import com.viktor.e_commerce.gson.Item;
import com.viktor.e_commerce.gson.Trade;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WaitSendActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "WaitSendActivity";

    private SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView recyclerView;

    private WaitSendAdapter adapter;

    private Button back;

    private List<Trade> trades = new ArrayList<>();

    public static List<Address> addresses = new ArrayList<>();

    public static Boolean is;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_send);
        //取控件
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        recyclerView = (RecyclerView)findViewById(R.id.recycle_view);
        back = (Button)findViewById(R.id.back);
        //监听
        back.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWaitSend();
            }
        });
        //数据
        swipeRefreshLayout.setRefreshing(true);
        requestWaitSend();
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        adapter = new WaitSendAdapter(trades);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void requestWaitSend(){
        String url = ROOT + "trade/?format=json&trade_info=0&trade_store=" + storeId;
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WaitSendActivity.this, "获取待发货信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseText = response.body().string();
                final List<Trade> waitTrade = Utility.parseJSONForTrade(responseText);
                is = false;
                addresses.clear();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for(int i = 0; i < waitTrade.size(); i++){
                            requestAddress(waitTrade.get(i).getTradeUser(), i, waitTrade.size());
                        }
                    }
                }).start();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        trades.clear();
                        for(int i = 0; i < waitTrade.size(); i++){
                            trades.add(waitTrade.get(i));
                        }
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }

    private void requestAddress(int user, final int num, final int total){
        String url = ROOT + "address/?format=json&address_user=" + user;
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WaitSendActivity.this, "获取收件人地址失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseText = "";
                Address address = new Address();
                try{
                    responseText = response.body().string();
                    address = Utility.parseJSONForAddress(responseText).get(0);
                }catch (Exception e){
                    address = new Address(0, "none", "none", "none", "none", false, 0);
                }
                while(addresses.size() != num);//保证address按顺序入列表
                addresses.add(address);
                if(num == total - 1){
                    is = true;
                }
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
        Intent intent = new Intent(context, WaitSendActivity.class);
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

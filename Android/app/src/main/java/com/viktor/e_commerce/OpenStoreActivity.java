package com.viktor.e_commerce;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.viktor.e_commerce.Util.Util;
import com.viktor.e_commerce.Util.Utility;
import com.viktor.e_commerce.gson.Store;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OpenStoreActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "OpenStoreActivity";

    private SwipeRefreshLayout swipeRefreshLayout;

    private Button back;

    private EditText storeName;

    private EditText storeInfo;

    private Button openStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_store);
        //取控件
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        back = (Button)findViewById(R.id.back);
        storeName = (EditText)findViewById(R.id.store_name);
        storeInfo = (EditText)findViewById(R.id.store_info);
        openStore = (Button)findViewById(R.id.open_store);
        //监听
        back.setOnClickListener(this);
        openStore.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        //数据
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                onBackPressed();
                break;
            case R.id.open_store:
                swipeRefreshLayout.setRefreshing(true);
                requestOpenStore();
                break;
        }
    }

    private void requestOpenStore(){
        String storeN = storeName.getText().toString();
        String storeI = storeInfo.getText().toString();
        if(TextUtils.isEmpty(storeN)){
            Toast.makeText(this, "店名不能为空哦", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
            return;
        }

        String url = ROOT + "store/";
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("store_name", storeN)
                .add("store_info", storeI)
                .add("store_user", userId + "")
                .build();
        Request request = new Request.Builder().url(url).post(body).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(OpenStoreActivity.this, "连接服务器失败，请重试", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseText = response.body().string();
                try{
                    Store store = new Gson().fromJson(responseText, Store.class);
                    storeId = store.getStoreId();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                            Toast.makeText(OpenStoreActivity.this, "开店成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public static void actionStart(Context context){
        Intent intent = new Intent(context, OpenStoreActivity.class);
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

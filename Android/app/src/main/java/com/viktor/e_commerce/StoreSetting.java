package com.viktor.e_commerce;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.viktor.e_commerce.Util.HttpUtil;
import com.viktor.e_commerce.Util.Utility;
import com.viktor.e_commerce.gson.Store;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.HttpCookie;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class StoreSetting extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "StoreSetting";

    private Button back;

    private TextView storeIdd;

    private TextView storeName;

    private TextView storeInfo;

    private TextView storeCreateTime;

    private Button up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_setting);
        //取控件
        back = (Button)findViewById(R.id.back);
        storeIdd = (TextView) findViewById(R.id.store_id);
        storeName = (TextView)findViewById(R.id.store_name);
        storeInfo = (TextView)findViewById(R.id.store_info);
        storeCreateTime = (TextView)findViewById(R.id.store_create_time);
        up = (Button)findViewById(R.id.up);
        //监听
        back.setOnClickListener(this);
        up.setOnClickListener(this);
        //数据
        requestStore();
    }

    private void requestStore(){
        String url = ROOT + "store/" + storeId + "/?format=json";
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(StoreSetting.this, "获取商店信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseText = response.body().string();
                final Store store = new Gson().fromJson(responseText, Store.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        storeIdd.setText("商店编号：" + store.getStoreId());
                        storeName.setText("商店名：" + store.getStoreName());
                        storeInfo.setText("商店简介：" + store.getStoreInfo());
                        storeCreateTime.setText("商店创建时间：" + store.getStoreCreateTime());
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
            case R.id.up:
                UpItemActivity.actionStart(this);
                break;
        }
    }

    public static void actionStart(Context context){
        Intent intent = new Intent(context, StoreSetting.class);
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

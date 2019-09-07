package com.viktor.e_commerce;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UpItemActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "UpItemActivity";

    private Button back;

    private EditText itemName;

    private EditText itemPrice;

    private EditText itemStoke;

    private EditText itemSubCategory;

    private Button up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_item);
        //控件
        back = (Button)findViewById(R.id.back);
        itemName = (EditText)findViewById(R.id.item_name);
        itemPrice = (EditText)findViewById(R.id.item_price);
        itemStoke = (EditText)findViewById(R.id.item_stoke);
        itemSubCategory = (EditText)findViewById(R.id.item_category);
        up = (Button)findViewById(R.id.up);
        //监听
        back.setOnClickListener(this);
        up.setOnClickListener(this);
        //数据
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                onBackPressed();
                break;
            case R.id.up:
                requestPostItem();
                break;
        }
    }

    private void requestPostItem(){
        String item_name = itemName.getText().toString();
        String item_price = itemPrice.getText().toString();
        String item_stoke = itemStoke.getText().toString();
        String item_category = itemSubCategory.getText().toString();

        if(TextUtils.isEmpty(item_name) || TextUtils.isEmpty(item_price) || TextUtils.isEmpty(item_stoke) || TextUtils.isEmpty(item_category)){
            Toast.makeText(this, "请完善信息", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = ROOT + "item/";
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("item_name", item_name)
                .add("item_price", item_price)
                .add("item_stoke", item_stoke)
                .add("item_sub_category", item_category)
                .add("item_store", storeId + "")
                .build();
        Request request = new Request.Builder().url(url).post(body).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UpItemActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseText = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UpItemActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });
    }

    public static void actionStart(Context context){
        Intent intent = new Intent(context, UpItemActivity.class);
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

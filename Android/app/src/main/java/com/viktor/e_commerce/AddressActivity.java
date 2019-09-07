package com.viktor.e_commerce;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.viktor.e_commerce.Util.HttpUtil;
import com.viktor.e_commerce.Util.Utility;
import com.viktor.e_commerce.gson.Address;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddressActivity extends BaseActivity implements View.OnClickListener {

    private Button back;

    private TextView detail;

    private TextView name;

    private TextView tel;

    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        //控件
        back = (Button)findViewById(R.id.back);
        detail = (TextView)findViewById(R.id.detail);
        name = (TextView)findViewById(R.id.name);
        tel = (TextView)findViewById(R.id.tel);
        save = (Button)findViewById(R.id.save);
        //监听
        back.setOnClickListener(this);
        save.setOnClickListener(this);
        //数据
        requestAddress();
    }

    private void requestAddress(){
        String url = ROOT + "address/?format=json&address_user=" + userId;
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AddressActivity.this, "获取用户地址信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseText = response.body().string();
                try{
                    final Address address = Utility.parseJSONForAddress(responseText).get(0);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            detail.setText(address.getAddressDetail());
                            name.setText(address.getAddressUsername());
                            tel.setText(address.getAddressTel());
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
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
            case R.id.save:
                requestSave();
                break;
        }
    }

    private void requestSave(){
        String de = detail.getText().toString();
        String na = name.getText().toString();
        String te = tel.getText().toString();

        if(TextUtils.isEmpty(de) || TextUtils.isEmpty(na) || TextUtils.isEmpty(te)){
            Toast.makeText(this, "请完善信息", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = ROOT + "update_user_address/?user_id=" + userId + "&detail=" + de + "&name=" + na + "&tel=" + te;
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("user_id", userId + "")
                .add("detail", de)
                .add("name", na)
                .add("tel", te)
                .build();
        Request request = new Request.Builder().url(url).post(body).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AddressActivity.this, "保存失败，请重试", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseText = response.body().string();
                if(responseText.equals("\"success!\"")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AddressActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    public static void actionStart(Context context){
        Intent intent = new Intent(context, AddressActivity.class);
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

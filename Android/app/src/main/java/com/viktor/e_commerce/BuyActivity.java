package com.viktor.e_commerce;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class BuyActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "BuyActivity";

    private List<Boolean> finish;

    private List<String> storeId;

    private List<String> itemId;

    private List<String> amount;

    private String where;

    private Button back;

    private Button pay;

    private TextView price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        //取控件
        back = (Button)findViewById(R.id.back);
        pay = (Button)findViewById(R.id.pay);
        price = (TextView)findViewById(R.id.price);
        //监听
        back.setOnClickListener(this);
        pay.setOnClickListener(this);
        //设置数据
        Bundle bundle = getIntent().getBundleExtra("calc");
        storeId = bundle.getStringArrayList("store_id");
        itemId = bundle.getStringArrayList("item_id");
        amount = bundle.getStringArrayList("amount");
        where = bundle.getString("where", "none");
        price.setText("¥" + bundle.getString("price"));
        finish = new ArrayList<>();
        for(int i = 0; i < itemId.size(); i++){
            finish.add(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                onBackPressed();
                break;
            case R.id.pay:
                if(itemId.size() == 0){
                    Toast.makeText(this, "请往购物车添加商品", Toast.LENGTH_SHORT).show();
                    return;
                }
                for(int i = 0; i < itemId.size(); i++){
                    requestPay(i);
                }
                Boolean is = false;
                while(!is){
                    for(int i = 0; i < finish.size(); i++){
                        if(!finish.get(i)){
                            is = false;
                            break;
                        }
                        is = true;
                    }
                }
                finish();
                break;
        }
    }

    private void requestPay(final int i){
        String url = "";
        if(where.equals("cart")){
            url = ROOT + "buy_in_cart/?user_id=" + userId + "&item_id=" + itemId.get(i) + "&store_id=" + storeId.get(i) + "&amount=" + amount.get(i);
        }else{
            url = ROOT + "buy_now/?user_id=" + userId + "&item_id=" + itemId.get(i) + "&store_id=" + storeId.get(i) + "&amount=" + amount.get(i);
        }
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("user_id", userId + "")
                .add("item_id", itemId.get(i))
                .add("store_id", storeId.get(i))
                .add("amount", amount.get(i))
                .build();
        Request request = new Request.Builder().url(url).post(body).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BuyActivity.this, "付款失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseText = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(responseText.equals("\"stoke is not enough!\"")){
                            Toast.makeText(BuyActivity.this, "库存不足", Toast.LENGTH_SHORT).show();
                        }else if(responseText.equals("\"success!\"")){
                            Toast.makeText(BuyActivity.this, "付款成功！", Toast.LENGTH_SHORT).show();
                        }else if(responseText.equals("\"Argument Error!\"")){
                            Toast.makeText(BuyActivity.this, "请求参数错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                finish.set(i, true);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    public static void actionStart(Context context, Bundle bundle){
        Intent intent = new Intent(context, BuyActivity.class);
        intent.putExtra("calc", bundle);
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.activity_right_fade_out, R.anim.activity_no);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.activity_left_fade_in);
    }
}

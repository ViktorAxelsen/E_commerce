package com.viktor.e_commerce;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.viktor.e_commerce.Adapter.ItemAdapter;
import com.viktor.e_commerce.Util.HttpUtil;
import com.viktor.e_commerce.Util.Utility;
import com.viktor.e_commerce.gson.Item;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ItemListActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "ItemListActivity";

    private SwipeRefreshLayout swipeRefreshLayout;

    private String keyword;

    private String type;

    private String categoryId;

    private Button back;

    private SearchView searchView;

    private Button search;

    private RadioGroup radioGroup;

    private RadioButton comprehensive;

    private RadioButton sale;

    private RadioButton price;

    private RadioButton screen;

    private RecyclerView recyclerView;

    private ItemAdapter adapter;

    private List<Item> itemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        //取控件
        back = (Button)findViewById(R.id.back);
        searchView = (SearchView)findViewById(R.id.search_view);
        search = (Button)findViewById(R.id.search);
        radioGroup = (RadioGroup)findViewById(R.id.top_toolbar_radio_group);
        comprehensive = (RadioButton)findViewById(R.id.comprehensive);
        sale = (RadioButton)findViewById(R.id.sale);
        price = (RadioButton)findViewById(R.id.price);
        screen = (RadioButton)findViewById(R.id.screen);
        recyclerView = (RecyclerView)findViewById(R.id.recycle_view);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        //数据
        comprehensive.setChecked(true);
        comprehensive.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new ItemAdapter(itemList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        keyword = getIntent().getStringExtra("keyword");
        categoryId = getIntent().getStringExtra("categoryId");
        type = "综合";
        requestItem(keyword, type, categoryId);
        searchView.setQuery(keyword, false);
        swipeRefreshLayout.setRefreshing(true);
        if(userId != 0 && keyword != null){
            updateSearchRecord(keyword);
        }
        //监听
        back.setOnClickListener(this);
        search.setOnClickListener(this);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                search();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.comprehensive:
                        comprehensive.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                        sale.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        price.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        screen.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        type = "综合";
                        requestItem(keyword, type, categoryId);
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case R.id.sale:
                        comprehensive.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        sale.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                        price.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        screen.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        type = "销量";
                        requestItem(keyword, type, categoryId);
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case R.id.price:
                        comprehensive.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        sale.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        price.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                        screen.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        type = "价格";
                        requestItem(keyword, type, categoryId);
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case R.id.screen:
                        comprehensive.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        sale.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        price.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        screen.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                        break;
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestItem(keyword, type, categoryId);
            }
        });
    }

    private void requestItem(String keyword, String type, String categoryId){
        String url = ROOT + "item/?format=json&item_name__icontains=";
        if(keyword != null){
            url = url + keyword;
        }
        if(categoryId != null){
            url = url + "&item_sub_category=" + categoryId;
        }

        if(type.equals("销量")){
            url = url + "&ordering=item_sale";
        }else if(type.equals("价格")){
            url = url + "&ordering=item_price";
        }
        Log.e(TAG, url);

        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ItemListActivity.this, "获取商品信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseText = response.body().string();
                itemList = Utility.parseJSONForItem(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new ItemAdapter(itemList);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }

    private void updateSearchRecord(String content){
        String url = ROOT + "search_content/" + userId + "/?content=" + content;
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("content", content)
                .build();
        Request request = new Request.Builder().url(url).post(body).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.search:
                search();
                break;
        }
    }

    public static void actionStart(Context context, String keyword, String categoryId){
        Intent intent = new Intent(context, ItemListActivity.class);
        intent.putExtra("keyword", keyword);
        intent.putExtra("categoryId", categoryId);
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.activity_fade_out, R.anim.activity_no);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.activity_fade_in);
    }

    private void search(){
        keyword = searchView.getQuery().toString();
        requestItem(keyword, type, categoryId);
        swipeRefreshLayout.setRefreshing(true);
        if(userId != 0){
            updateSearchRecord(keyword);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }
}

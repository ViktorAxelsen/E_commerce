package com.viktor.e_commerce;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.media.Image;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.viktor.e_commerce.Adapter.ItemAdapter;
import com.viktor.e_commerce.Util.HttpUtil;
import com.viktor.e_commerce.Util.Utility;
import com.viktor.e_commerce.gson.Item;
import com.viktor.e_commerce.gson.ItemDetailImage;
import com.youth.banner.Banner;

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

public class ItemDetailActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "ItemDetailActivity";

    private Item itemInstance;

    private int itemId;

    private int itemAmount;

    private Banner banner;

    private List<String> bannerImage;

    private RelativeLayout relativeLayout;

    private TextView itemPrice;

    private TextView itemPrePrice;

    private TextView itemOff;

    private TextView itemFollow;

    private TextView itemName;

    private TextView itemInfo;

    private ImageView store;

    private ImageView cart;

    private CheckBox itemFollowCheckbox;

    private ImageView itemPreviewImage;

    private Button addIntoCart;

    private Button buyNow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        //取控件
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        banner = (Banner)findViewById(R.id.banner);
        relativeLayout = (RelativeLayout)findViewById(R.id.select_detail);
        itemPrice = (TextView)findViewById(R.id.item_price);
        itemPrePrice = (TextView)findViewById(R.id.item_pre_price);
        itemOff = (TextView)findViewById(R.id.item_off);
        itemFollow = (TextView)findViewById(R.id.item_follow);
        itemName = (TextView)findViewById(R.id.item_name);
        itemInfo = (TextView)findViewById(R.id.select_info);
        store = (ImageView)findViewById(R.id.store);
        cart = (ImageView)findViewById(R.id.cart);
        itemFollowCheckbox = (CheckBox) findViewById(R.id.follow_checkbox);
        addIntoCart = (Button)findViewById(R.id.item_add_to_car);
        buyNow = (Button)findViewById(R.id.item_buy_now);
        itemPreviewImage = (ImageView)findViewById(R.id.item_preview);
        //设置数据
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        Intent intent = getIntent();
        itemId = intent.getIntExtra("itemId", 0);
        itemAmount = intent.getIntExtra("itemAmount", 0);
        requestItemDetailImage(itemId);
        requestItem(itemId);
        requestItemFollow();
        if(userId != 0){
            requestWhetherUserCollectItem();
        }else{
            itemFollowCheckbox.setChecked(false);
        }
        //监听
        relativeLayout.setOnClickListener(this);
        store.setOnClickListener(this);
        cart.setOnClickListener(this);
        addIntoCart.setOnClickListener(this);
        buyNow.setOnClickListener(this);
        itemFollowCheckbox.setOnClickListener(this);
    }

    private void requestWhetherUserCollectItem(){
        String url = ROOT + "whether_user_collect_item/?item_id=" + itemId + "&user_id=" + userId;
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ItemDetailActivity.this, "获取商品关注信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseText = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(responseText.equals("1")){
                            itemFollowCheckbox.setChecked(true);
                        }else{
                            itemFollowCheckbox.setChecked(false);
                        }
                    }
                });
            }
        });
    }

    private void requestItemFollow(){
        String url = ROOT + "get_item_collection_amount/" + itemId;
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ItemDetailActivity.this, "获取商品关注信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseText = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        itemFollow.setText(responseText);
                    }
                });
            }
        });
    }

    private void requestItemDetailImage(int itemId){
        String url = ROOT + "itemDetailImage/?format=json&image_belong=" + itemId;
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ItemDetailActivity.this, "获取商品详情图失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseText = response.body().string();
                final List<ItemDetailImage> images = Utility.parseJSONForItemDetailImage(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bannerImage = new ArrayList<>();
                        for(int i = 0; i < images.size(); i++){
                            bannerImage.add(images.get(i).getImage_url());
                        }
                        banner.setImageLoader(new GlideImageLoader());
                        banner.setImages(bannerImage);
                        banner.start();
                    }
                });
            }
        });
    }

    private void requestItem(int itemId){
        String url = "";
        if(userId != 0){
            url = ROOT + "browse_item/?user_id=" + userId + "&item_id=" + itemId;
        }else{
            url = ROOT + "item/?format=json&item_id=" + itemId;
        }
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ItemDetailActivity.this, "获取商品信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseText = response.body().string();
                Item itemTemp;
                if(userId != 0){
                    itemTemp = new Gson().fromJson(responseText, Item.class);
                }else{
                    itemTemp = Utility.parseJSONForItem(responseText).get(0);
                }
                final Item item = itemTemp;
                itemInstance = item;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        itemPrice.setText("¥" + item.getItemPrice());
                        itemPrePrice.setText("¥" + item.getItemPrePrice());
                        itemPrePrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);//删除线
                        itemOff.setText(item.getItemOff());
                        itemName.setText(item.getItemName());
                        itemInfo.setText(item.getItemName() + "，" + itemAmount + "件，" + "可选服务");
                        try{
                            if(userId != 0){
                                Glide.with(ItemDetailActivity.this).load("http://www.mallproject.cn:8000" + item.getItemPreviewImage()).into(itemPreviewImage);
                            }else{
                                Glide.with(ItemDetailActivity.this).load(item.getItemPreviewImage()).into(itemPreviewImage);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.select_detail:
                initBottomSheetDialog();
                break;
            case R.id.store:
                break;
            case R.id.cart:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("cartFragment", 1);
                startActivity(intent);
                break;
            case R.id.item_add_to_car:
                initBottomSheetDialog();
                break;
            case R.id.item_buy_now:
                initBottomSheetDialog();
                break;
            case R.id.follow_checkbox:
                if(userId == 0){
                    Toast.makeText(ItemDetailActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                    LoginActivity.actionStart(ItemDetailActivity.this);
                }else{
                    if(itemFollowCheckbox.isChecked()){
                        requestPostCollection();
                    }else{
                        requestDeleteCollection();
                    }
                }
                break;
        }
    }

    private void requestPostCollection(){
        String url = ROOT + "collection/";
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("collection_user", userId + "")
                .add("collection_item", itemId + "")
                .build();
        Request request = new Request.Builder().url(url).post(body).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ItemDetailActivity.this, "更改关注信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseText = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ItemDetailActivity.this, "已收藏", Toast.LENGTH_SHORT).show();
                        itemFollow.setText((Integer.valueOf(itemFollow.getText().toString()) + 1) + "");
                    }
                });
            }
        });
    }

    private void requestDeleteCollection(){
        String url = ROOT + "delete_user_collection/?item_id=" + itemId + "&user_id=" + userId;
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("item_id", itemId + "")
                .add("user_id", userId + "")
                .build();
        Request request = new Request.Builder().url(url).delete(body).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ItemDetailActivity.this, "更改关注信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseText = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(responseText.equals("\"success!\"")){
                            Toast.makeText(ItemDetailActivity.this, "已取消收藏", Toast.LENGTH_SHORT).show();
                            itemFollow.setText((Integer.valueOf(itemFollow.getText().toString()) - 1) + "");
                        }
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    public static void actionStart(Context context, int itemId, int amount){
        Intent intent = new Intent(context, ItemDetailActivity.class);
        intent.putExtra("itemId", itemId);
        intent.putExtra("itemAmount", amount);
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.activity_right_fade_out, R.anim.activity_no);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.activity_left_fade_in);
    }

    private void initBottomSheetDialog(){
        MyBottomSheetDialogFragment shoppingSelectPagerFragment = MyBottomSheetDialogFragment.newInstance();
        Bundle arg = new Bundle();
        try{
            if(userId != 0){
                while(itemInstance == null);
                arg.putString("item_image", "http://www.mallproject.cn:8000" + itemInstance.getItemPreviewImage());
            }else{
                while(itemInstance == null);
                arg.putString("item_image", itemInstance.getItemPreviewImage());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        arg.putString("item_price", "¥" + itemInstance.getItemPrice());
        arg.putString("item_weight", "重量：1kg");
        arg.putString("item_stoke", "库存：" + itemInstance.getItemStoke() + "件");
        arg.putString("item_id", "编号：" + itemInstance.getItemId());
        arg.putString("item_amount", "" + itemAmount);
        arg.putInt("user", userId);
        arg.putInt("store", itemInstance.getItemStore());
        shoppingSelectPagerFragment.setArguments(arg);
        shoppingSelectPagerFragment.show(getSupportFragmentManager(), "open_shop_frag");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }
}

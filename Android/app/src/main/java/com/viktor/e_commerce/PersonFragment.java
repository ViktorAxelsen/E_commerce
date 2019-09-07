package com.viktor.e_commerce;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.viktor.e_commerce.Util.HttpUtil;
import com.viktor.e_commerce.Util.Util;
import com.viktor.e_commerce.Util.Utility;
import com.viktor.e_commerce.gson.Store;
import com.viktor.e_commerce.gson.User;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PersonFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "PersonFragment";

    private static final String ROOT = "http://www.mallproject.cn:8000/api/";

    private int user;

    private SwipeRefreshLayout swipeRefreshLayout;

    private Button setting;

    private Button message;

    private Button group;

    private CircleImageView userHead;

    private TextView userNickname;

    private TextView itemFollow;

    private TextView storeFollow;

    private TextView collection;

    private TextView browseHistory;

    private LinearLayout waitReceive;

    private LinearLayout waitEvaluate;

    private LinearLayout order;

    private TextView TJUCarat;

    private TextView coupon;

    private LinearLayout wallet;

    private LinearLayout browse;

    private LinearLayout itemFollowClick;

    private LinearLayout collectionClick;

    private Button storeInfo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.person_fragment, container, false);
        //取控件
        setting = (Button)view.findViewById(R.id.settings);
        message = (Button)view.findViewById(R.id.message);
        group = (Button)view.findViewById(R.id.group);
        userHead = view.findViewById(R.id.user_head);
        itemFollow = view.findViewById(R.id.item_follow);
        storeFollow = view.findViewById(R.id.store_follow);
        collection = view.findViewById(R.id.item_collect);
        browseHistory = view.findViewById(R.id.browse_history);
        waitReceive = view.findViewById(R.id.wait_receive);
        waitEvaluate = view.findViewById(R.id.wait_evaluate);
        order = view.findViewById(R.id.order);
        TJUCarat = (TextView) view.findViewById(R.id.tju_carat);
        coupon = view.findViewById(R.id.coupon);
        wallet = view.findViewById(R.id.wallet);
        userNickname = view.findViewById(R.id.user_nickname);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh);
        browse = (LinearLayout)view.findViewById(R.id.browse);
        itemFollowClick = (LinearLayout)view.findViewById(R.id.item_follow_click);
        collectionClick = (LinearLayout)view.findViewById(R.id.collection);
        storeInfo = (Button)view.findViewById(R.id.storeInfo);
        //设置数据

        //监听
        setting.setOnClickListener(this);
        message.setOnClickListener(this);
        group.setOnClickListener(this);
        userHead.setOnClickListener(this);
        storeFollow.setOnClickListener(this);
        browse.setOnClickListener(this);
        waitReceive.setOnClickListener(this);
        waitEvaluate.setOnClickListener(this);
        order.setOnClickListener(this);
        TJUCarat.setOnClickListener(this);
        coupon.setOnClickListener(this);
        wallet.setOnClickListener(this);
        itemFollowClick.setOnClickListener(this);
        collectionClick.setOnClickListener(this);
        storeInfo.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(user != 0){
                    requestUser();
                    requestOthers();
                }else{
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getContext(), "请登录", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        user = ((MainActivity)getActivity()).userId;
        Log.e(TAG, user + "");
        if(user != 0){
            requestUser();
            requestOthers();
            requestStoreInfo();
        }else{
            Glide.with(getContext()).load(R.drawable.head_default).into(userHead);
            userNickname.setText("");
            itemFollow.setText("--");
            storeFollow.setText("--");
            collection.setText("--");
            browseHistory.setText("--");
            TJUCarat.setText("--");
            coupon.setText("--");
            storeInfo.setText("我要开店");
        }
    }

    private void requestStoreInfo(){
        String url = ROOT + "get_user_store_info/" + user;
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "加载商店信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseText = response.body().string();
                try{
                    ((MainActivity)getActivity()).storeId = new Gson().fromJson(responseText, Store.class).getStoreId();
                    while(getActivity() == null);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            storeInfo.setText("进入店铺");
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void requestUser(){
        String url = ROOT + "user/?format=json&user_id=" + user;
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "加载用户信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseText = response.body().string();
                final User user = Utility.parseJSONForUser(responseText).get(0);
                while(getActivity() == null);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Glide.with(getContext()).load(user.getUserHeadImage()).into(userHead);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        userNickname.setText(user.getUserNickname());
                        TJUCarat.setText(user.getUserTJUCarat() + "");
                        coupon.setText(user.getUserCoupon().size() + "");
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }

    private void requestOthers(){
        requestStoreFollow();
        requestCollection();
        requestBrowseHistory();
    }

    private void requestStoreFollow(){
        String url = ROOT + "get_user_store_follow/" + user;
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "加载商店关注信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseText = response.body().string();
                final int store = Utility.parseJSONForStore(responseText).size();
                while(getActivity() == null);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        storeFollow.setText(store + "");
                    }
                });
            }
        });
    }

    private void requestCollection(){
        String url = ROOT + "get_user_collection/" + user;
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "加载收藏夹信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseText = response.body().string();
                final int collect = Utility.parseJSONForItem(responseText).size();
                while(getActivity() == null);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        collection.setText(collect + "");
                        itemFollow.setText(collect + "");
                    }
                });
            }
        });
    }

    private void requestBrowseHistory(){
        String url = ROOT + "browseRecord/?format=json&record_user=" + user;
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "加载用户浏览记录失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseText = response.body().string();
                final int history = Utility.parseJSONForBrowseRecord(responseText).size();
                while(getActivity() == null);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        browseHistory.setText(history + "");
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.settings:
                SettingActivity.actionStart(getContext());
                break;
            case R.id.message:
                break;
            case R.id.group:
                break;
            case R.id.user_head:
                if(user != 0){
                    PersonalInformationActivity.actionStart(getContext());
                }else{
                    LoginActivity.actionStart(getContext());
                }
                break;
            case R.id.item_follow_click:
            case R.id.collection:
                if(user != 0){
                    CollectionActivity.actionStart(getContext());
                }else{
                    LoginActivity.actionStart(getContext());
                }
                break;
            case R.id.store_follow:
                break;
            case R.id.browse:
                if(user != 0){
                    BrowseHistoryActivity.actionStart(getContext());
                }else{
                    Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
                    LoginActivity.actionStart(getContext());
                }
                break;
            case R.id.wait_receive:
                if(user != 0){
                    WaitReceiveActivity.actionStart(getContext());
                }else{
                    Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
                    LoginActivity.actionStart(getContext());
                }
                break;
            case R.id.wait_evaluate:
                break;
            case R.id.order:
                if(user != 0){
                    OrderActivity.actionStart(getContext());
                }else{
                    Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
                    LoginActivity.actionStart(getContext());
                }
                break;
            case R.id.tju_carat:
                break;
            case R.id.coupon:
                break;
            case R.id.wallet:
                break;
            case R.id.storeInfo:
                if(user == 0){
                    Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
                    LoginActivity.actionStart(getContext());
                }else{
                    if(((MainActivity)getActivity()).storeId == 0){
                        OpenStoreActivity.actionStart(getContext());
                    }else{
                        StoreActivity.actionStart(getContext());
                    }
                }
                break;
        }
    }

}

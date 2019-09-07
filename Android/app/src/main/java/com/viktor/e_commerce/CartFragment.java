package com.viktor.e_commerce;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.viktor.e_commerce.Adapter.CartAdapter;
import com.viktor.e_commerce.Util.HttpUtil;
import com.viktor.e_commerce.Util.Utility;
import com.viktor.e_commerce.gson.CartItem;
import com.viktor.e_commerce.gson.Item;
import com.viktor.e_commerce.gson.ShoppingCart;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CartFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "CartFragment";

    private static final String ROOT = "http://www.mallproject.cn:8000/api/";

    private int user;

    private int amount;

    private Boolean is;

    private Boolean syn;

    private int cartId;

    private SwipeRefreshLayout swipeRefreshLayout;

    private CheckBox subSetting;

    private PopupMenu popupMenu;

    private CheckBox allSelect;

    private TextView totalPrice;

    private Button calc;

    private RecyclerView recyclerView;

    private CartAdapter adapter;

    private List<CartItem> cartCartItemList = new ArrayList<>();

    public static Map<Integer,Boolean> map = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cart_fragment, container, false);
        //取控件
        subSetting = (CheckBox)view.findViewById(R.id.sub_setting);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycle_view);
        allSelect = (CheckBox)view.findViewById(R.id.all_select);
        totalPrice = (TextView)view.findViewById(R.id.total_price);
        calc = (Button)view.findViewById(R.id.goto_calc);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh);
        //监听
        calc.setOnClickListener(this);
        //设置数据
        user = ((MainActivity)getActivity()).userId;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new CartAdapter(cartCartItemList, new CartChangeListener() {
            @Override
            public void selected(double total) {
                totalPrice.setText("合计：¥" + total);
            }

            @Override
            public void isAllSelected(boolean is) {
                if(is){
                    allSelect.setChecked(true);
                }else{
                    allSelect.setChecked(false);
                }
            }

            @Override
            public void delete() {
                if(user != 0)
                    requestCartItem();
                swipeRefreshLayout.setRefreshing(true);
            }
        });
        recyclerView.setAdapter(adapter);

        return view;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //adapter.totalPrice = getArguments().getDouble("totalPrice");
        subSetting.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switch (buttonView.getId()){
                    case R.id.sub_setting:
                        if(isChecked){
                            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_forever);
                            animation.setFillAfter(true);
                            subSetting.startAnimation(animation);
                            showPopupMenu(subSetting);
                        }else{
                            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_forever_reverse);
                            animation.setFillAfter(true);
                            subSetting.startAnimation(animation);
                        }
                        break;
                }
            }
        });
        allSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allSelect.isChecked()){
                    adapter.selectAll();
                }else{
                    adapter.cancelAll();
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(user != 0){
                    requestCartItem();
                }else{
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        user = ((MainActivity)getActivity()).userId;
        if(user != 0){
            swipeRefreshLayout.setRefreshing(true);
            requestCartItem();
        }else{
            cartCartItemList.clear();
            adapter.notifyDataSetChanged();
        }
    }

    private void requestCartItem(){
        String url = ROOT + "get_user_cart_item/" + user;
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "获取购物车信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseText = response.body().string();
                final List<Item> items = Utility.parseJSONForItem(responseText);
                while(getActivity() == null);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cartCartItemList.clear();
                        for(int i = 0; i < items.size(); i++){
                            is = false;
                            Item item = items.get(i);
                            requestAmount(user, item.getItemId());
                            while(!is);
                            cartCartItemList.add(new CartItem(cartId, user, item.getItemId(), item.getItemStore(), "http://www.mallproject.cn:8000" + item.getItemPreviewImage(), item.getItemName(), "¥" + item.getItemPrice(), amount));
                        }
                        syn = true;
                        map.clear();
                        allSelect.setChecked(false);
                        adapter.notifyDataSetChanged();
                        totalPrice.setText("合计：¥0.0");
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }

    private void requestAmount(int userId, int itemId){
        String url = ROOT + "shoppingCart/?format=json&cart_user=" + userId + "&cart_item=" + itemId;
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "获取数量信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseText = response.body().string();
                final ShoppingCart cart = Utility.parseJSONForShoppingCart(responseText).get(0);
                //这里千万不能在ui线程里执行，否则无法更新值，主线程已经被while（!is）卡住了，只能从其他线程介入
                amount = cart.getCartItemAmount();
                cartId = cart.getCartId();
                is = true;
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.goto_calc:
                if(user != 0){
                    final ArrayList<Integer> check = new ArrayList<>();
                    final ArrayList<String> store = new ArrayList<>();
                    final ArrayList<String> item = new ArrayList<>();
                    final ArrayList<String> amount = new ArrayList<>();
                    for(int i = 0; i < cartCartItemList.size(); i++){
                        if(map.containsKey(i)){
                            if(map.get(i)){
                                check.add(i);
                            }
                        }
                    }
                    swipeRefreshLayout.setRefreshing(true);
                    syn = false;
                    requestCartItem();
                    //thread
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            int price = 0;
                            while(!syn);
                            for(int i = 0; i < check.size(); i++){
                                store.add(cartCartItemList.get(check.get(i)).getStoreId() + "");
                                item.add(cartCartItemList.get(check.get(i)).getItemId() + "");
                                amount.add(cartCartItemList.get(check.get(i)).getItemAmount() + "");
                                price += Double.valueOf(cartCartItemList.get(check.get(i)).getItemPrice().substring(1)) * cartCartItemList.get(check.get(i)).getItemAmount();
                            }
                            final int price_temp = price;
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Bundle bundle = new Bundle();
                                    bundle.putStringArrayList("store_id", store);
                                    bundle.putStringArrayList("item_id", item);
                                    bundle.putStringArrayList("amount", amount);
                                    bundle.putString("price", price_temp + "");
                                    bundle.putString("where", "cart");
                                    BuyActivity.actionStart(getContext(), bundle);
                                }
                            });
                        }
                    }).start();
                }else{
                    Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
                    LoginActivity.actionStart(getContext());
                }
                break;
        }
    }

    private void showPopupMenu(View view){
        if(popupMenu == null){
            popupMenu = new PopupMenu(getContext(), view);
            popupMenu.getMenuInflater().inflate(R.menu.main_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.settings:
                            SettingActivity.actionStart(getContext());
                            break;
                    }
                    return true;
                }
            });
            popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
                @Override
                public void onDismiss(PopupMenu popupMenu) {
                    subSetting.setChecked(false);
                }
            });
        }
        popupMenu.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getArguments().putDouble("totalPrice", Double.valueOf(totalPrice.getText().toString().substring(4)));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(popupMenu != null){
            popupMenu.dismiss();
            popupMenu = null;
        }
    }


}

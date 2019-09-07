package com.viktor.e_commerce;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.viktor.e_commerce.gson.User;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyBottomSheetDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener{

    private static final String TAG = "MyBottomSheetDialogFrag";

    private static final String ROOT = "http://www.mallproject.cn:8000/api/";

    private int user;

    private int store;

    private ImageView itemImage;

    private TextView itemPrice;

    private TextView itemWeight;

    private TextView itemStoke;

    private TextView itemId;

    private RadioGroup radioGroup;

    private Button itemDec;

    private Button itemInc;

    private EditText itemAmount;

    private Button itemToCar;

    private Button itemBuyNow;

    private Button close;

    public static MyBottomSheetDialogFragment newInstance(){
        return new MyBottomSheetDialogFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shopping_select_pager_layout, container, false);
        itemImage = view.findViewById(R.id.item_image);
        itemPrice = view.findViewById(R.id.item_price);
        itemWeight = view.findViewById(R.id.item_weight);
        itemStoke = view.findViewById(R.id.item_stoke);
        itemId = view.findViewById(R.id.item_id);
        radioGroup = view.findViewById(R.id.radio_group);
        itemDec = view.findViewById(R.id.item_amount_dec);
        itemInc = view.findViewById(R.id.item_amount_inc);
        itemAmount = view.findViewById(R.id.item_amount_edit);
        itemToCar = view.findViewById(R.id.item_add_to_car);
        itemBuyNow = view.findViewById(R.id.item_buy_now);
        close = view.findViewById(R.id.item_close);
        //服务器返回数据初始化
        Bundle data = getArguments();
        user = data.getInt("user");//可能为0或1
        store = data.getInt("store");
        Glide.with(getContext()).load(data.getString("item_image")).into(itemImage);
        itemPrice.setText(data.getString("item_price"));
        itemWeight.setText(data.getString("item_weight"));
        itemStoke.setText(data.getString("item_stoke"));
        itemId.setText(data.getString("item_id"));
        itemAmount.setText(data.getString("item_amount"));
        itemAmount.setSelection(itemAmount.getText().length());
        radioGroup.check(R.id.item_name1);
        //监听初始化
        radioGroup.setOnCheckedChangeListener(this);
        itemDec.setOnClickListener(this);
        itemInc.setOnClickListener(this);
        itemToCar.setOnClickListener(this);
        itemBuyNow.setOnClickListener(this);
        close.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.item_amount_dec:
                if(TextUtils.isEmpty(itemAmount.getText().toString())){
                    itemAmount.setText("1");
                }
                else if(Integer.valueOf(itemAmount.getText().toString()) == 1){
                    Toast.makeText(getContext(), "最低购买1件！", Toast.LENGTH_SHORT).show();
                    break;
                }else{
                    itemAmount.setText(String.valueOf(Integer.valueOf(itemAmount.getText().toString()) - 1));
                }
                break;
            case R.id.item_amount_inc:
                if(TextUtils.isEmpty(itemAmount.getText().toString())){
                    itemAmount.setText("1");
                }else{
                    itemAmount.setText(String.valueOf(Integer.valueOf(itemAmount.getText().toString()) + 1));
                }
                break;
            case R.id.item_add_to_car:
                if(user == 0){
                    Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
                    dismiss();
                    LoginActivity.actionStart(getContext());
                }else{
                    Toast.makeText(getContext(), "正在处理请求......", Toast.LENGTH_SHORT).show();
                    requestAddToCart();
                }
                break;
            case R.id.item_buy_now:
                if(user == 0){
                    Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
                    dismiss();
                    LoginActivity.actionStart(getContext());
                }else{
                    Bundle bundle = new Bundle();
                    ArrayList<String> data = new ArrayList<>();
                    data.add(store + "");
                    bundle.putStringArrayList("store_id", data);
                    data = new ArrayList<>();
                    data.add(itemId.getText().toString().substring(3));
                    bundle.putStringArrayList("item_id", data);
                    data = new ArrayList<>();
                    data.add(itemAmount.getText().toString());
                    bundle.putStringArrayList("amount", data);
                    bundle.putString("price", Double.valueOf(itemPrice.getText().toString().substring(1)) * Integer.valueOf(itemAmount.getText().toString()) + "");
                    BuyActivity.actionStart(getContext(), bundle);
                }
                break;
            case R.id.item_close:
                dismiss();
                break;
        }
    }

    private void requestAddToCart(){
        String item_id = itemId.getText().toString().substring(3);
        String item_amount = itemAmount.getText().toString();
        String url = ROOT + "add_into_cart/?user_id=" + user + "&item_id=" + item_id + "&amount=" + item_amount;
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("user_id", user + "")
                .add("item_id", item_id)
                .add("amount", item_amount)
                .build();
        final Request request = new Request.Builder().url(url).post(body).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "加入失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseText = response.body().string();
                Log.e(TAG, responseText);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(responseText.equals("\"stoke is not enough!\"")){
                            Toast.makeText(getContext(), "库存不足", Toast.LENGTH_SHORT).show();
                        }else if(responseText.equals("\"success!\"")){
                            Toast.makeText(getContext(), "已加入购物车", Toast.LENGTH_SHORT).show();
                        }else if(responseText.equals("\"Argument Error!\"")){
                            Toast.makeText(getContext(), "请求参数错误", Toast.LENGTH_SHORT).show();
                        }
                        dismiss();
                    }
                });
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.item_name1:
                break;
            case R.id.item_name2:
                break;
        }
    }
}

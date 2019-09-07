package com.viktor.e_commerce.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.viktor.e_commerce.CartChangeListener;
import com.viktor.e_commerce.CartFragment;
import com.viktor.e_commerce.ItemDetailActivity;
import com.viktor.e_commerce.MainActivity;
import com.viktor.e_commerce.R;
import com.viktor.e_commerce.gson.CartItem;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder>{

    private static final String TAG = "CartAdapter";

    private static final String ROOT = "http://www.mallproject.cn:8000/api/";

    private Context mContext;

    private List<CartItem> cartItemList;

    public Double totalPrice = 0.0;

    private boolean isAllSelected = false;

    private CartChangeListener listener;


    static class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;

        CheckBox checkBox;

        ImageView itemImage;

        TextView itemName;

        TextView itemPrice;

        Button itemInc;

        Button itemDec;

        EditText itemAmount;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            checkBox = (CheckBox)view.findViewById(R.id.checkbox);
            itemImage = (ImageView)view.findViewById(R.id.item_image);
            itemName = (TextView)view.findViewById(R.id.item_name);
            itemPrice = (TextView)view.findViewById(R.id.item_price);
            itemInc = (Button)view.findViewById(R.id.item_amount_inc);
            itemDec = (Button)view.findViewById(R.id.item_amount_dec);
            itemAmount = (EditText)view.findViewById(R.id.item_amount_edit);
        }
    }

    public CartAdapter(List<CartItem> cartItemList, CartChangeListener listener){
        this.cartItemList = cartItemList;
        this.listener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(mContext == null){
            mContext = viewGroup.getContext();
        }

        View view = LayoutInflater.from(mContext).inflate(R.layout.cart_layout, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }



    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        final CartItem item = cartItemList.get(i);
        viewHolder.itemAmount.setText(item.getItemAmount() + "");
        viewHolder.itemPrice.setText(item.getItemPrice());
        viewHolder.itemName.setText(item.getItemName());
        Glide.with(mContext).load(item.getItemImage()).into(viewHolder.itemImage);
        viewHolder.checkBox.setTag(i);

        if(CartFragment.map.containsKey(i)){
            viewHolder.checkBox.setChecked(CartFragment.map.get(i));
        }else{
            viewHolder.checkBox.setChecked(false);
        }
        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //viewHolder对应的是每个项目的viewHolder，在这进行针对每个Item的操作就比较合理，可以避免Recycleview的抽风加载机制导致的各种问题。
                if(viewHolder.checkBox.isChecked()){
                    CartFragment.map.put(i, true);
                    totalPrice += Double.valueOf(viewHolder.itemPrice.getText().toString().substring(1)) * Integer.valueOf(viewHolder.itemAmount.getText().toString());
                    listener.selected(Math.round(totalPrice * 100) / 100.0);
                }else{
                    CartFragment.map.put(i, false);
                    totalPrice -= Double.valueOf(viewHolder.itemPrice.getText().toString().substring(1)) * Integer.valueOf(viewHolder.itemAmount.getText().toString());
                    listener.selected(Math.round(totalPrice * 100) / 100.0);
                }

                if(CartFragment.map.size() == cartItemList.size()){
                    for(int i = 0; i < cartItemList.size(); i++){
                        if(!CartFragment.map.get(i)){
                            isAllSelected = false;
                            break;
                        }
                        isAllSelected = true;
                    }
                    if(isAllSelected){
                        listener.isAllSelected(true);
                    }else{
                        listener.isAllSelected(false);
                    }
                }else{
                    isAllSelected = false;
                    listener.isAllSelected(false);
                }
            }
        });


        viewHolder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setTitle("移除商品");
                dialog.setMessage("确定从购物车中移除该商品？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestDelete(item.getCartId() + "");
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                return true;
            }
        });
        viewHolder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemDetailActivity.actionStart(mContext, item.getItemId(), item.getItemAmount());//后续附带上数据
            }
        });
        viewHolder.itemInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(viewHolder.itemAmount.getText().toString())){
                    viewHolder.itemAmount.setText("1");
                }else{
                    int amount = Integer.valueOf(viewHolder.itemAmount.getText().toString()) + 1;
                    cartItemList.get(i).setItemAmount(amount);
                    viewHolder.itemAmount.setText(String.valueOf(amount));
                    if(viewHolder.checkBox.isChecked()){
                        totalPrice += Double.valueOf(cartItemList.get(i).getItemPrice().substring(1));
                        listener.selected(totalPrice);
                    }
                }
                requestPut(item.getCartId() + "", Integer.valueOf(viewHolder.itemAmount.getText().toString()), item.getUserId(), item.getItemId());
            }
        });
        viewHolder.itemDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(viewHolder.itemAmount.getText().toString())){
                    viewHolder.itemAmount.setText("1");
                }
                else if(Integer.valueOf(viewHolder.itemAmount.getText().toString()) == 1){
                    Toast.makeText(mContext, "最低购买一件哦~", Toast.LENGTH_SHORT).show();
                }else{
                    int amount = Integer.valueOf(viewHolder.itemAmount.getText().toString()) - 1;
                    cartItemList.get(i).setItemAmount(amount);
                    viewHolder.itemAmount.setText(String.valueOf(amount));
                    if(viewHolder.checkBox.isChecked()){
                        totalPrice -= Double.valueOf(cartItemList.get(i).getItemPrice().substring(1));
                        listener.selected(totalPrice);
                    }
                }
                requestPut(item.getCartId() + "", Integer.valueOf(viewHolder.itemAmount.getText().toString()), item.getUserId(), item.getItemId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    private void requestDelete(String cartId){
        String url = ROOT + "shoppingCart/" + cartId + "/";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).delete().build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                ((MainActivity)mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "删除失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                ((MainActivity)mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                        listener.delete();
                    }
                });
            }
        });
    }

    private void requestPut(String cartId, int amount, int userId, int itemId){
        String url = ROOT + "shoppingCart/" + cartId + "/";
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("cart_item_amount", amount + "")
                .add("cart_user", userId + "")
                .add("cart_item", itemId + "")
                .build();
        Request request = new Request.Builder().url(url).put(body).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

            }
        });
    }

    public void selectAll(){
        isAllSelected = true;
        totalPrice = 0.0;
        for(int i = 0; i < cartItemList.size(); i++){
            CartFragment.map.put(i, true);
            totalPrice += Double.valueOf(cartItemList.get(i).getItemPrice().substring(1)) * Integer.valueOf(cartItemList.get(i).getItemAmount());
        }
        listener.selected(totalPrice);
        notifyDataSetChanged();
    }

    public void cancelAll(){
        isAllSelected = false;
        totalPrice = 0.0;
        for(int i = 0; i < cartItemList.size(); i++){
            CartFragment.map.put(i, false);
        }
        listener.selected(totalPrice);
        notifyDataSetChanged();
    }
}

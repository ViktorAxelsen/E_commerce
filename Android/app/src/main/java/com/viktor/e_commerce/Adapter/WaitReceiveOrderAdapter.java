package com.viktor.e_commerce.Adapter;

import android.app.usage.NetworkStats;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.viktor.e_commerce.ConfirmReceiveListener;
import com.viktor.e_commerce.ItemDetailActivity;
import com.viktor.e_commerce.R;
import com.viktor.e_commerce.gson.Item;
import com.viktor.e_commerce.gson.SubCategory;

import java.util.List;

public class WaitReceiveOrderAdapter extends RecyclerView.Adapter<WaitReceiveOrderAdapter.ViewHolder> {

    private Context mContext;

    private List<Item> waits;

    private ConfirmReceiveListener listener;

    static class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;

        ImageView itemImage;

        TextView itemName;

        TextView itemPrice;

        TextView itemAmount;

        Button confirm;


        public ViewHolder(View view){
            super(view);
            cardView = (CardView)view;
            itemImage = (ImageView)view.findViewById(R.id.item_image);
            itemName = (TextView)view.findViewById(R.id.item_name);
            itemPrice = (TextView)view.findViewById(R.id.item_price);
            confirm = (Button)view.findViewById(R.id.confirm);
            itemAmount = (TextView)view.findViewById(R.id.item_amount);
        }
    }

    public WaitReceiveOrderAdapter(List<Item> waits, ConfirmReceiveListener listener){
        this.waits = waits;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(mContext == null){
            mContext = viewGroup.getContext();
        }

        View view = LayoutInflater.from(mContext).inflate(R.layout.wait_receive_order_layout, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        final Item item = waits.get(i);
        Glide.with(mContext).load("http://www.mallproject.cn:8000" + item.getItemPreviewImage()).into(viewHolder.itemImage);
        viewHolder.itemName.setText(item.getItemName());
        viewHolder.itemPrice.setText("¥" + item.getItemPrice());
        viewHolder.itemAmount.setText("数量：" + item.getItemTradeAmount());
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemDetailActivity.actionStart(mContext, item.getItemId(), 1);
            }
        });
        viewHolder.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.confirm(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return waits.size();
    }
}

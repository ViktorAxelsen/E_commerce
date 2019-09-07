package com.viktor.e_commerce.Adapter;

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
import com.viktor.e_commerce.ItemDetailActivity;
import com.viktor.e_commerce.OrderActivity;
import com.viktor.e_commerce.R;
import com.viktor.e_commerce.gson.Item;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder>{

    private Context mContext;

    private List<Item> orders;

    static class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;

        ImageView itemImage;

        TextView itemName;

        TextView itemPrice;

        TextView orderFinishTime;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView)view;
            itemImage = (ImageView)view.findViewById(R.id.item_image);
            itemName = (TextView)view.findViewById(R.id.item_name);
            itemPrice = (TextView)view.findViewById(R.id.item_price);
            orderFinishTime = (TextView)view.findViewById(R.id.order_finish_time);
        }
    }

    public OrderAdapter(List<Item> orders){
        this.orders = orders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(mContext == null){
            mContext = viewGroup.getContext();
        }

        View view = LayoutInflater.from(mContext).inflate(R.layout.order_layout, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        final Item item = orders.get(i);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!OrderActivity.is);
                ((OrderActivity)mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        viewHolder.orderFinishTime.setText(OrderActivity.trades.get(i).getTradeFinishTime());
                    }
                });
            }
        }).start();
        Glide.with(mContext).load("http://www.mallproject.cn:8000" + item.getItemPreviewImage()).into(viewHolder.itemImage);
        viewHolder.itemName.setText(item.getItemName());
        viewHolder.itemPrice.setText("¥" + item.getItemPrice());
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemDetailActivity.actionStart(mContext, item.getItemId(), 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }
}

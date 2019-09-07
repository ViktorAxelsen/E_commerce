package com.viktor.e_commerce.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.viktor.e_commerce.BrowseHistoryActivity;
import com.viktor.e_commerce.ItemDetailActivity;
import com.viktor.e_commerce.OrderActivity;
import com.viktor.e_commerce.R;
import com.viktor.e_commerce.gson.BrowseRecord;
import com.viktor.e_commerce.gson.Item;

import java.util.List;

public class BrowseHistoryAdapter extends RecyclerView.Adapter<BrowseHistoryAdapter.ViewHolder>{

    private Context mContext;

    private List<Item> items;

    static class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;

        ImageView itemImage;

        TextView date;

        TextView itemName;

        TextView itemPrice;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView)view;
            date = (TextView)view.findViewById(R.id.date);
            itemImage = (ImageView)view.findViewById(R.id.item_image);
            itemName = (TextView)view.findViewById(R.id.item_name);
            itemPrice = (TextView)view.findViewById(R.id.item_price);
        }
    }

    public BrowseHistoryAdapter(List<Item> items){
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(mContext == null){
            mContext = viewGroup.getContext();
        }

        View view = LayoutInflater.from(mContext).inflate(R.layout.browse_history_layout, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        final Item item = items.get(i);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!BrowseHistoryActivity.is);
                ((BrowseHistoryActivity)mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        viewHolder.date.setText(BrowseHistoryActivity.records.get(i).getRecordCreateTime().substring(0, 10));
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
        return items.size();
    }
}

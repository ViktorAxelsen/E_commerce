package com.viktor.e_commerce.Adapter;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.viktor.e_commerce.ItemDetailActivity;
import com.viktor.e_commerce.OrderActivity;
import com.viktor.e_commerce.R;
import com.viktor.e_commerce.gson.Item;

import java.util.List;
import java.util.concurrent.TimeoutException;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.ViewHolder>{

    private Context mContext;

    private List<Item> collections;

    static class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;

        ImageView itemImage;

        TextView itemName;

        TextView itemBrowse;

        TextView itemSale;

        TextView itemPrice;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView)view;
            itemImage = (ImageView)view.findViewById(R.id.item_image);
            itemName = (TextView)view.findViewById(R.id.item_name);
            itemBrowse = (TextView)view.findViewById(R.id.item_browse);
            itemSale = (TextView)view.findViewById(R.id.item_sale);
            itemPrice = (TextView)view.findViewById(R.id.item_price);
        }
    }

    public CollectionAdapter(List<Item> collections){
        this.collections = collections;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(mContext == null){
            mContext = viewGroup.getContext();
        }

        View view = LayoutInflater.from(mContext).inflate(R.layout.collection_layout, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        final Item item = collections.get(i);
        Glide.with(mContext).load("http://www.mallproject.cn:8000" + item.getItemPreviewImage()).into(viewHolder.itemImage);
        viewHolder.itemName.setText(item.getItemName());
        viewHolder.itemPrice.setText("¥" + item.getItemPrice());
        viewHolder.itemBrowse.setText("浏览量：" + item.getItemBrowse());
        viewHolder.itemSale.setText("销量：" + item.getItemSale());
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemDetailActivity.actionStart(mContext, item.getItemId(), 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return collections.size();
    }
}

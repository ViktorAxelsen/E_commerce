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
import com.viktor.e_commerce.StoreItemChangeImageListener;
import com.viktor.e_commerce.gson.Item;

import java.util.List;

public class StoreItemAdapter extends RecyclerView.Adapter<StoreItemAdapter.ViewHolder>{

    private Context mContext;

    private List<Item> items;

    private StoreItemChangeImageListener listener;

    static class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;

        ImageView itemImage;

        TextView itemName;

        TextView itemPrice;

        TextView itemBrowse;

        TextView itemSale;

        TextView itemId;


        public ViewHolder(View view){
            super(view);
            cardView = (CardView)view;
            itemImage = (ImageView)view.findViewById(R.id.item_image);
            itemName = (TextView)view.findViewById(R.id.item_name);
            itemPrice = (TextView)view.findViewById(R.id.item_price);
            itemBrowse = (TextView)view.findViewById(R.id.item_browse);
            itemSale = (TextView)view.findViewById(R.id.item_sale);
            itemId = (TextView)view.findViewById(R.id.item_id);
        }
    }

    public StoreItemAdapter(List<Item> items, StoreItemChangeImageListener listener){
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(mContext == null){
            mContext = viewGroup.getContext();
        }

        View view = LayoutInflater.from(mContext).inflate(R.layout.store_item_layout, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        final Item item = items.get(i);
        Glide.with(mContext).load(item.getItemPreviewImage()).into(viewHolder.itemImage);
        viewHolder.itemName.setText(item.getItemName());
        viewHolder.itemPrice.setText("¥" + item.getItemPrice());
        viewHolder.itemSale.setText("销量：" + item.getItemSale());
        viewHolder.itemBrowse.setText("浏览量：" + item.getItemBrowse());
        viewHolder.itemId.setText("商品编号：" + item.getItemId());

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemDetailActivity.actionStart(mContext, item.getItemId(), 1);
            }
        });
        viewHolder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.change(item.getItemId());
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

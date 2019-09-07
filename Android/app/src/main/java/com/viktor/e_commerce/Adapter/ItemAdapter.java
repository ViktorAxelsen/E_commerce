package com.viktor.e_commerce.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.viktor.e_commerce.ItemDetailActivity;
import com.viktor.e_commerce.R;
import com.viktor.e_commerce.gson.Item;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>{

    private static final String TAG = "ItemAdapter";

    private Context mContext;

    private List<Item> itemList;

    static class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout linearLayout;

        ImageView itemPreviewImage;

        TextView itemName;

        TextView itemPrice;

        TextView itemSale;

        TextView itemBrowse;

        public ViewHolder(View view){
            super(view);
            linearLayout = (LinearLayout)view;
            itemPreviewImage = (ImageView)view.findViewById(R.id.item_image);
            itemName = (TextView)view.findViewById(R.id.item_name);
            itemPrice = (TextView)view.findViewById(R.id.item_price);
            itemSale = (TextView)view.findViewById(R.id.item_sale);
            itemBrowse = (TextView)view.findViewById(R.id.item_browse);
        }
    }

    public ItemAdapter(List<Item> itemList){
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(mContext == null){
            mContext = viewGroup.getContext();
        }

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_layout, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Item item = itemList.get(i);
        Glide.with(mContext).load(item.getItemPreviewImage()).into(viewHolder.itemPreviewImage);
        viewHolder.itemName.setText(item.getItemName());
        viewHolder.itemPrice.setText("¥" + item.getItemPrice());
        viewHolder.itemSale.setText("销量：" + item.getItemSale() + " 件");
        viewHolder.itemBrowse.setText("访问量：" + item.getItemBrowse());

        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemDetailActivity.actionStart(mContext, item.getItemId(), 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}

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
import com.viktor.e_commerce.ItemListActivity;
import com.viktor.e_commerce.R;
import com.viktor.e_commerce.gson.SubCategory;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{

    private Context mContext;

    private List<SubCategory> categoryList;


    static class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;

        ImageView categoryImage;

        TextView categoryName;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            categoryImage = (ImageView)view.findViewById(R.id.category_image);
            categoryName = (TextView)view.findViewById(R.id.category_text);
        }
    }

    public CategoryAdapter(List<SubCategory> categoryList){
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(mContext == null){
            mContext = viewGroup.getContext();
        }

        View view = LayoutInflater.from(mContext).inflate(R.layout.classification_layout, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final SubCategory category = categoryList.get(i);
        Glide.with(mContext).load(category.getCategoryImage()).into(viewHolder.categoryImage);
        viewHolder.categoryName.setText(category.getCategoryName());
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemListActivity.actionStart(mContext, null, category.getCategoryId() + "");
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}

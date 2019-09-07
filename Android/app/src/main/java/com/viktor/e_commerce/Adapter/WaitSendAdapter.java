package com.viktor.e_commerce.Adapter;

import android.app.Activity;
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
import com.viktor.e_commerce.ItemDetailActivity;
import com.viktor.e_commerce.R;
import com.viktor.e_commerce.WaitSendActivity;
import com.viktor.e_commerce.gson.Trade;

import java.util.List;
import java.util.concurrent.TimeoutException;

public class WaitSendAdapter extends RecyclerView.Adapter<WaitSendAdapter.ViewHolder>{

    private Context mContext;

    private List<Trade> trades;

    static class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;

        TextView tradeId;

        TextView tradeItemId;

        TextView tradeAmount;

        TextView tradeCreateTime;

        TextView address;

        TextView addressUser;

        TextView addressTel;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView)view;
            tradeId = (TextView)view.findViewById(R.id.trade_id);
            tradeItemId = (TextView)view.findViewById(R.id.trade_item_id);
            tradeAmount = (TextView)view.findViewById(R.id.trade_amount);
            tradeCreateTime = (TextView)view.findViewById(R.id.trade_create_time);
            address = (TextView)view.findViewById(R.id.address);
            addressUser = (TextView)view.findViewById(R.id.address_user);
            addressTel = (TextView)view.findViewById(R.id.address_tel);
        }
    }

    public WaitSendAdapter(List<Trade> trades){
        this.trades = trades;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(mContext == null){
            mContext = viewGroup.getContext();
        }

        View view = LayoutInflater.from(mContext).inflate(R.layout.wait_send_layout, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        final Trade trade = trades.get(i);
        viewHolder.tradeId.setText("订单号：" + trade.getTradeId());
        viewHolder.tradeItemId.setText("商品编号：" + trade.getTradeItem());
        viewHolder.tradeAmount.setText("数量：" + trade.getTradeAmount());
        viewHolder.tradeCreateTime.setText("交易创建时间：" + trade.getTradeCreateTime());
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!((WaitSendActivity)mContext).is);
                ((Activity)mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        viewHolder.address.setText("收件人地址：\n" + ((WaitSendActivity)mContext).addresses.get(i).getAddressDetail());
                        viewHolder.addressUser.setText("收件人姓名：\n" + ((WaitSendActivity)mContext).addresses.get(i).getAddressUsername());
                        viewHolder.addressTel.setText("收件人联系方式：\n" + ((WaitSendActivity)mContext).addresses.get(i).getAddressTel());
                    }
                });
            }
        }).start();
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemDetailActivity.actionStart(mContext, trade.getTradeItem(), 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trades.size();
    }
}

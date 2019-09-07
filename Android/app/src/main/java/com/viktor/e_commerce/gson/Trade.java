package com.viktor.e_commerce.gson;

import com.google.gson.annotations.SerializedName;

public class Trade {

    @SerializedName("trade_id")
    private int tradeId;

    @SerializedName("trade_amount")
    private int tradeAmount;

    @SerializedName("trade_isEvaluate")
    private Boolean tradeIsEvaluate;

    @SerializedName("trade_create_time")
    private String tradeCreateTime;

    @SerializedName("trade_finish_time")
    private String tradeFinishTime;

    @SerializedName("trade_info")
    private int tradeInfo;

    @SerializedName("trade_TJUCarat")
    private int tradeTJUCarat;

    @SerializedName("trade_user")
    private int tradeUser;

    @SerializedName("trade_item")
    private int tradeItem;

    @SerializedName("trade_store")
    private int tradeStore;

    public Trade(int tradeId, int tradeAmount, Boolean tradeIsEvaluate, String tradeCreateTime, String tradeFinishTime, int tradeInfo, int tradeTJUCarat, int tradeUser, int tradeItem, int tradeStore) {
        this.tradeId = tradeId;
        this.tradeAmount = tradeAmount;
        this.tradeIsEvaluate = tradeIsEvaluate;
        this.tradeCreateTime = tradeCreateTime;
        this.tradeFinishTime = tradeFinishTime;
        this.tradeInfo = tradeInfo;
        this.tradeTJUCarat = tradeTJUCarat;
        this.tradeUser = tradeUser;
        this.tradeItem = tradeItem;
        this.tradeStore = tradeStore;
    }

    public int getTradeId() {
        return tradeId;
    }

    public void setTradeId(int tradeId) {
        this.tradeId = tradeId;
    }

    public int getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(int tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public Boolean getTradeIsEvaluate() {
        return tradeIsEvaluate;
    }

    public void setTradeIsEvaluate(Boolean tradeIsEvaluate) {
        this.tradeIsEvaluate = tradeIsEvaluate;
    }

    public String getTradeCreateTime() {
        return tradeCreateTime;
    }

    public void setTradeCreateTime(String tradeCreateTime) {
        this.tradeCreateTime = tradeCreateTime;
    }

    public String getTradeFinishTime() {
        return tradeFinishTime;
    }

    public void setTradeFinishTime(String tradeFinishTime) {
        this.tradeFinishTime = tradeFinishTime;
    }

    public int getTradeInfo() {
        return tradeInfo;
    }

    public void setTradeInfo(int tradeInfo) {
        this.tradeInfo = tradeInfo;
    }

    public int getTradeTJUCarat() {
        return tradeTJUCarat;
    }

    public void setTradeTJUCarat(int tradeTJUCarat) {
        this.tradeTJUCarat = tradeTJUCarat;
    }

    public int getTradeUser() {
        return tradeUser;
    }

    public void setTradeUser(int tradeUser) {
        this.tradeUser = tradeUser;
    }

    public int getTradeItem() {
        return tradeItem;
    }

    public void setTradeItem(int tradeItem) {
        this.tradeItem = tradeItem;
    }

    public int getTradeStore() {
        return tradeStore;
    }

    public void setTradeStore(int tradeStore) {
        this.tradeStore = tradeStore;
    }
}

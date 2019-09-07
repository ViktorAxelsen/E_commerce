package com.viktor.e_commerce.gson;

import com.google.gson.annotations.SerializedName;

public class Store {

    @SerializedName("store_id")
    private int storeId;

    @SerializedName("store_name")
    private String storeName;

    @SerializedName("store_contact_info")
    private String storeContactInfo;

    @SerializedName("store_info")
    private String storeInfo;

    @SerializedName("store_evaluate")
    private String storeEvaluate;

    @SerializedName("store_create_time")
    private String storeCreateTime;

    @SerializedName("store_user")
    private int storeUser;

    public Store(int storeId, String storeName, String storeContactInfo, String storeInfo, String storeEvaluate, String storeCreateTime, int storeUser) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.storeContactInfo = storeContactInfo;
        this.storeInfo = storeInfo;
        this.storeEvaluate = storeEvaluate;
        this.storeCreateTime = storeCreateTime;
        this.storeUser = storeUser;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreContactInfo() {
        return storeContactInfo;
    }

    public void setStoreContactInfo(String storeContactInfo) {
        this.storeContactInfo = storeContactInfo;
    }

    public String getStoreInfo() {
        return storeInfo;
    }

    public void setStoreInfo(String storeInfo) {
        this.storeInfo = storeInfo;
    }

    public String getStoreEvaluate() {
        return storeEvaluate;
    }

    public void setStoreEvaluate(String storeEvaluate) {
        this.storeEvaluate = storeEvaluate;
    }

    public String getStoreCreateTime() {
        return storeCreateTime;
    }

    public void setStoreCreateTime(String storeCreateTime) {
        this.storeCreateTime = storeCreateTime;
    }

    public int getStoreUser() {
        return storeUser;
    }

    public void setStoreUser(int storeUser) {
        this.storeUser = storeUser;
    }
}

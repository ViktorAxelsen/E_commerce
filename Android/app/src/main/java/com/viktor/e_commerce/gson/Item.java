package com.viktor.e_commerce.gson;

import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("item_id")
    private int itemId;

    @SerializedName("item_name")
    private String itemName;

    @SerializedName("item_price")
    private String itemPrice;

    @SerializedName("item_stoke")
    private int itemStoke;

    @SerializedName("item_preview_image")
    private String itemPreviewImage;

    @SerializedName("item_browse")
    private int itemBrowse;

    @SerializedName("item_off")
    private String itemOff;

    @SerializedName("item_prePrice")
    private String itemPrePrice;

    @SerializedName("item_sale")
    private int itemSale;

    @SerializedName("item_create_time")
    private String itemCreateTime;

    @SerializedName("item_VIPOff")
    private int itemVIPOff;

    @SerializedName("item_category")
    private int itemCategory;

    @SerializedName("item_sub_category")
    private int itemSubCategory;

    @SerializedName("item_store")
    private int itemStore;

    @SerializedName("item_brand")
    private int itemBrand;

    @SerializedName("item_trade_amount")
    private int itemTradeAmount;

    public Item(int itemId, String itemName, String itemPrice, int itemStoke, String itemPreviewImage, int itemBrowse, String itemOff, String itemPrePrice, int itemSale, String itemCreateTime, int itemVIPOff, int itemCategory, int itemSubCategory, int itemStore, int itemBrand) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemStoke = itemStoke;
        this.itemPreviewImage = itemPreviewImage;
        this.itemBrowse = itemBrowse;
        this.itemOff = itemOff;
        this.itemPrePrice = itemPrePrice;
        this.itemSale = itemSale;
        this.itemCreateTime = itemCreateTime;
        this.itemVIPOff = itemVIPOff;
        this.itemCategory = itemCategory;
        this.itemSubCategory = itemSubCategory;
        this.itemStore = itemStore;
        this.itemBrand = itemBrand;
    }

    public Item(int itemId, String itemName, String itemPrice, int itemStoke, String itemPreviewImage, int itemBrowse, String itemOff, String itemPrePrice, int itemSale, String itemCreateTime, int itemVIPOff, int itemCategory, int itemSubCategory, int itemStore, int itemBrand, int itemTradeAmount) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemStoke = itemStoke;
        this.itemPreviewImage = itemPreviewImage;
        this.itemBrowse = itemBrowse;
        this.itemOff = itemOff;
        this.itemPrePrice = itemPrePrice;
        this.itemSale = itemSale;
        this.itemCreateTime = itemCreateTime;
        this.itemVIPOff = itemVIPOff;
        this.itemCategory = itemCategory;
        this.itemSubCategory = itemSubCategory;
        this.itemStore = itemStore;
        this.itemBrand = itemBrand;
        this.itemTradeAmount = itemTradeAmount;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getItemStoke() {
        return itemStoke;
    }

    public void setItemStoke(int itemStoke) {
        this.itemStoke = itemStoke;
    }

    public String getItemPreviewImage() {
        return itemPreviewImage;
    }

    public void setItemPreviewImage(String itemPreviewImage) {
        this.itemPreviewImage = itemPreviewImage;
    }

    public int getItemBrowse() {
        return itemBrowse;
    }

    public void setItemBrowse(int itemBrowse) {
        this.itemBrowse = itemBrowse;
    }

    public String getItemOff() {
        return itemOff;
    }

    public void setItemOff(String itemOff) {
        this.itemOff = itemOff;
    }

    public String getItemPrePrice() {
        return itemPrePrice;
    }

    public void setItemPrePrice(String itemPrePrice) {
        this.itemPrePrice = itemPrePrice;
    }

    public int getItemSale() {
        return itemSale;
    }

    public void setItemSale(int itemSale) {
        this.itemSale = itemSale;
    }

    public String getItemCreateTime() {
        return itemCreateTime;
    }

    public void setItemCreateTime(String itemCreateTime) {
        this.itemCreateTime = itemCreateTime;
    }

    public int getItemVIPOff() {
        return itemVIPOff;
    }

    public void setItemVIPOff(int itemVIPOff) {
        this.itemVIPOff = itemVIPOff;
    }

    public int getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(int itemCategory) {
        this.itemCategory = itemCategory;
    }

    public int getItemSubCategory() {
        return itemSubCategory;
    }

    public void setItemSubCategory(int itemSubCategory) {
        this.itemSubCategory = itemSubCategory;
    }

    public int getItemStore() {
        return itemStore;
    }

    public void setItemStore(int itemStore) {
        this.itemStore = itemStore;
    }

    public int getItemBrand() {
        return itemBrand;
    }

    public void setItemBrand(int itemBrand) {
        this.itemBrand = itemBrand;
    }

    public int getItemTradeAmount() {
        return itemTradeAmount;
    }

    public void setItemTradeAmount(int itemTradeAmount) {
        this.itemTradeAmount = itemTradeAmount;
    }
}

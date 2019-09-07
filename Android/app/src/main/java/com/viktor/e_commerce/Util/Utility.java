package com.viktor.e_commerce.Util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.viktor.e_commerce.gson.Address;
import com.viktor.e_commerce.gson.BrowseRecord;
import com.viktor.e_commerce.gson.Item;
import com.viktor.e_commerce.gson.ItemDetailImage;
import com.viktor.e_commerce.gson.ShoppingCart;
import com.viktor.e_commerce.gson.Store;
import com.viktor.e_commerce.gson.SubCategory;
import com.viktor.e_commerce.gson.Trade;
import com.viktor.e_commerce.gson.User;

import java.util.List;

public class Utility {

    public static List<Item> parseJSONForItem(String jsonData){
        Gson gson = new Gson();
        return gson.fromJson(jsonData, new TypeToken<List<Item>>(){}.getType());
    }

    public static List<ItemDetailImage> parseJSONForItemDetailImage(String jsonData){
        Gson gson = new Gson();
        return gson.fromJson(jsonData, new TypeToken<List<ItemDetailImage>>(){}.getType());
    }

    public static List<SubCategory> parseJSONForSubCategory(String jsonData){
        Gson gson = new Gson();
        return gson.fromJson(jsonData, new TypeToken<List<SubCategory>>(){}.getType());
    }

    public static List<User> parseJSONForUser(String jsonData){
        Gson gson = new Gson();
        return gson.fromJson(jsonData, new TypeToken<List<User>>(){}.getType());
    }

    public static List<ShoppingCart> parseJSONForShoppingCart(String jsonData){
        Gson gson = new Gson();
        return gson.fromJson(jsonData, new TypeToken<List<ShoppingCart>>(){}.getType());
    }

    public static List<Store> parseJSONForStore(String jsonData){
        Gson gson = new Gson();
        return gson.fromJson(jsonData, new TypeToken<List<Store>>(){}.getType());
    }

    public static List<BrowseRecord> parseJSONForBrowseRecord(String jsonData){
        Gson gson = new Gson();
        return gson.fromJson(jsonData, new TypeToken<List<BrowseRecord>>(){}.getType());
    }

    public static List<Trade> parseJSONForTrade(String jsonData){
        Gson gson = new Gson();
        return gson.fromJson(jsonData, new TypeToken<List<Trade>>(){}.getType());
    }

    public static List<Address> parseJSONForAddress(String jsonData){
        Gson gson = new Gson();
        return gson.fromJson(jsonData, new TypeToken<List<Address>>(){}.getType());
    }
}

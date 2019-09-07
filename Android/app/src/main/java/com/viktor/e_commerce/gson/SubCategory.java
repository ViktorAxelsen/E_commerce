package com.viktor.e_commerce.gson;

import com.google.gson.annotations.SerializedName;

public class SubCategory {

    @SerializedName("item_subCategory_id")
    private int categoryId;

    @SerializedName("item_subCategory")
    private String categoryName;

    @SerializedName("item_subCategory_image")
    private String categoryImage;

    @SerializedName("item_subCategory_belong")
    private int categoryBelong;

    public SubCategory(int categoryId, String categoryName, String categoryImage, int categoryBelong) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryImage = categoryImage;
        this.categoryBelong = categoryBelong;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public int getCategoryBelong() {
        return categoryBelong;
    }

    public void setCategoryBelong(int categoryBelong) {
        this.categoryBelong = categoryBelong;
    }
}

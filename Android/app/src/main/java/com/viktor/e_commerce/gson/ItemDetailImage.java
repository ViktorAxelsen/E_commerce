package com.viktor.e_commerce.gson;

public class ItemDetailImage {

    private int image_id;

    private String image_url;

    private int image_belong;

    public ItemDetailImage(int image_id, String image_url, int image_belong) {
        this.image_id = image_id;
        this.image_url = image_url;
        this.image_belong = image_belong;
    }

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public int getImage_belong() {
        return image_belong;
    }

    public void setImage_belong(int image_belong) {
        this.image_belong = image_belong;
    }
}

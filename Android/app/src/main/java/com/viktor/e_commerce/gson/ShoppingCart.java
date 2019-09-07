package com.viktor.e_commerce.gson;

import com.google.gson.annotations.SerializedName;

public class ShoppingCart {

    @SerializedName("cart_id")
    private int cartId;

    @SerializedName("cart_item_amount")
    private int cartItemAmount;

    @SerializedName("cart_user")
    private int cartUser;

    @SerializedName("cart_item")
    private int cartItem;

    public ShoppingCart(int cartId, int cartItemAmount, int cartUser, int cartItem) {
        this.cartId = cartId;
        this.cartItemAmount = cartItemAmount;
        this.cartUser = cartUser;
        this.cartItem = cartItem;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getCartItemAmount() {
        return cartItemAmount;
    }

    public void setCartItemAmount(int cartItemAmount) {
        this.cartItemAmount = cartItemAmount;
    }

    public int getCartUser() {
        return cartUser;
    }

    public void setCartUser(int cartUser) {
        this.cartUser = cartUser;
    }

    public int getCartItem() {
        return cartItem;
    }

    public void setCartItem(int cartItem) {
        this.cartItem = cartItem;
    }
}

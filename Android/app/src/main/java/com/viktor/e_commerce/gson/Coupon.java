package com.viktor.e_commerce.gson;

import com.google.gson.annotations.SerializedName;

public class Coupon {

    @SerializedName("coupon_id")
    private int couponId;

    @SerializedName("coupon_off")
    private int couponOff;

    @SerializedName("coupon_require")
    private int couponRequire;

    @SerializedName("coupon_last")
    private String couponLast;

    @SerializedName("coupon_category")
    private int couponCategory;

    public Coupon(int couponId, int couponOff, int couponRequire, String couponLast, int couponCategory) {
        this.couponId = couponId;
        this.couponOff = couponOff;
        this.couponRequire = couponRequire;
        this.couponLast = couponLast;
        this.couponCategory = couponCategory;
    }

    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }

    public int getCouponOff() {
        return couponOff;
    }

    public void setCouponOff(int couponOff) {
        this.couponOff = couponOff;
    }

    public int getCouponRequire() {
        return couponRequire;
    }

    public void setCouponRequire(int couponRequire) {
        this.couponRequire = couponRequire;
    }

    public String getCouponLast() {
        return couponLast;
    }

    public void setCouponLast(String couponLast) {
        this.couponLast = couponLast;
    }

    public int getCouponCategory() {
        return couponCategory;
    }

    public void setCouponCategory(int couponCategory) {
        this.couponCategory = couponCategory;
    }
}

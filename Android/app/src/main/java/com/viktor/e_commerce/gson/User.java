package com.viktor.e_commerce.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User {

    @SerializedName("user_id")
    private int userId;

    @SerializedName("user_nickname")
    private String userNickname;

    @SerializedName("user_password")
    private String userPassword;

    @SerializedName("user_sex")
    private String userSex;

    @SerializedName("user_tel")
    private String userTel;

    @SerializedName("user_headImage")
    private String userHeadImage;

    @SerializedName("user_isAdmin")
    private Boolean isAdmin;

    @SerializedName("user_isBindQQ")
    private Boolean isBindQQ;

    @SerializedName("user_location")
    private String userLocation;

    @SerializedName("user_location_x")
    private String userLocationX;

    @SerializedName("user_location_y")
    private String userLocationY;

    @SerializedName("user_TJUCarat")
    private int userTJUCarat;

    @SerializedName("user_isVIP")
    private Boolean isVIP;

    @SerializedName("user_coupon")
    private List<Coupon> userCoupon;

    public User(int userId) {
        this.userId = userId;
    }

    public User(int userId, String userNickname, String userPassword, String userSex, String userTel, String userHeadImage, Boolean isAdmin, Boolean isBindQQ, String userLocation, String userLocationX, String userLocationY, int userTJUCarat, Boolean isVIP, List<Coupon> userCoupon) {
        this.userId = userId;
        this.userNickname = userNickname;
        this.userPassword = userPassword;
        this.userSex = userSex;
        this.userTel = userTel;
        this.userHeadImage = userHeadImage;
        this.isAdmin = isAdmin;
        this.isBindQQ = isBindQQ;
        this.userLocation = userLocation;
        this.userLocationX = userLocationX;
        this.userLocationY = userLocationY;
        this.userTJUCarat = userTJUCarat;
        this.isVIP = isVIP;
        this.userCoupon = userCoupon;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getUserTel() {
        return userTel;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }

    public String getUserHeadImage() {
        return userHeadImage;
    }

    public void setUserHeadImage(String userHeadImage) {
        this.userHeadImage = userHeadImage;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public Boolean getBindQQ() {
        return isBindQQ;
    }

    public void setBindQQ(Boolean bindQQ) {
        isBindQQ = bindQQ;
    }

    public String getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
    }

    public String getUserLocationX() {
        return userLocationX;
    }

    public void setUserLocationX(String userLocationX) {
        this.userLocationX = userLocationX;
    }

    public String getUserLocationY() {
        return userLocationY;
    }

    public void setUserLocationY(String userLocationY) {
        this.userLocationY = userLocationY;
    }

    public int getUserTJUCarat() {
        return userTJUCarat;
    }

    public void setUserTJUCarat(int userTJUCarat) {
        this.userTJUCarat = userTJUCarat;
    }

    public Boolean getVIP() {
        return isVIP;
    }

    public void setVIP(Boolean VIP) {
        isVIP = VIP;
    }

    public List<Coupon> getUserCoupon() {
        return userCoupon;
    }

    public void setUserCoupon(List<Coupon> userCoupon) {
        this.userCoupon = userCoupon;
    }
}

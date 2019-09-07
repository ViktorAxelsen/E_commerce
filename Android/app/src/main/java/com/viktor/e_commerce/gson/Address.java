package com.viktor.e_commerce.gson;

import com.google.gson.annotations.SerializedName;

public class Address {

    @SerializedName("address_id")
    private int addressId;

    @SerializedName("address_username")
    private String addressUsername;

    @SerializedName("address_tel")
    private String addressTel;

    @SerializedName("address_detail")
    private String addressDetail;

    @SerializedName("address_type")
    private String addressType;

    @SerializedName("address_isDefault")
    private Boolean addressIsDefault;

    @SerializedName("address_user")
    private int addressUser;

    public Address() {
    }

    public Address(int addressId, String addressUsername, String addressTel, String addressDetail, String addressType, Boolean addressIsDefault, int addressUser) {
        this.addressId = addressId;
        this.addressUsername = addressUsername;
        this.addressTel = addressTel;
        this.addressDetail = addressDetail;
        this.addressType = addressType;
        this.addressIsDefault = addressIsDefault;
        this.addressUser = addressUser;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getAddressUsername() {
        return addressUsername;
    }

    public void setAddressUsername(String addressUsername) {
        this.addressUsername = addressUsername;
    }

    public String getAddressTel() {
        return addressTel;
    }

    public void setAddressTel(String addressTel) {
        this.addressTel = addressTel;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public Boolean getAddressIsDefault() {
        return addressIsDefault;
    }

    public void setAddressIsDefault(Boolean addressIsDefault) {
        this.addressIsDefault = addressIsDefault;
    }

    public int getAddressUser() {
        return addressUser;
    }

    public void setAddressUser(int addressUser) {
        this.addressUser = addressUser;
    }
}

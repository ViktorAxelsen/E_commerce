package com.viktor.e_commerce.gson;

import com.google.gson.annotations.SerializedName;

public class BrowseRecord {

    @SerializedName("record_id")
    private int recordId;

    @SerializedName("record_flag")
    private int recordFlag;

    @SerializedName("record_create_time")
    private String recordCreateTime;

    @SerializedName("record_user")
    private int recordUser;

    @SerializedName("record_item")
    private int recordItem;

    public BrowseRecord(int recordId, int recordFlag, String recordCreateTime, int recordUser, int recordItem) {
        this.recordId = recordId;
        this.recordFlag = recordFlag;
        this.recordCreateTime = recordCreateTime;
        this.recordUser = recordUser;
        this.recordItem = recordItem;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public int getRecordFlag() {
        return recordFlag;
    }

    public void setRecordFlag(int recordFlag) {
        this.recordFlag = recordFlag;
    }

    public String getRecordCreateTime() {
        return recordCreateTime;
    }

    public void setRecordCreateTime(String recordCreateTime) {
        this.recordCreateTime = recordCreateTime;
    }

    public int getRecordUser() {
        return recordUser;
    }

    public void setRecordUser(int recordUser) {
        this.recordUser = recordUser;
    }

    public int getRecordItem() {
        return recordItem;
    }

    public void setRecordItem(int recordItem) {
        this.recordItem = recordItem;
    }
}

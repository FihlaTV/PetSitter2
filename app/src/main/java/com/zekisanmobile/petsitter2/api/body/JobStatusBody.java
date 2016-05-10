package com.zekisanmobile.petsitter2.api.body;

public class JobStatusBody {

    private int status;

    private String app_id;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }
}

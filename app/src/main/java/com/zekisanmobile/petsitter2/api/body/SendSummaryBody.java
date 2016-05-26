package com.zekisanmobile.petsitter2.api.body;

public class SendSummaryBody {

    String app_id;

    String summary_app_id;

    String text;

    String created_at;

    String photo_app_id;

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getSummary_app_id() {
        return summary_app_id;
    }

    public void setSummary_app_id(String summary_app_id) {
        this.summary_app_id = summary_app_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getPhoto_app_id() {
        return photo_app_id;
    }

    public void setPhoto_app_id(String photo_app_id) {
        this.photo_app_id = photo_app_id;
    }
}

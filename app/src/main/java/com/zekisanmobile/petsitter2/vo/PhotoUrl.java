package com.zekisanmobile.petsitter2.vo;

import io.realm.RealmObject;

public class PhotoUrl extends RealmObject {

    private String thumb;

    private String medium;

    private String large;

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }
}

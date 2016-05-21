package com.zekisanmobile.petsitter2.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PetCare extends RealmObject {

    @PrimaryKey
    @JsonProperty("app_id")
    private String id;

    private String care;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCare() {
        return care;
    }

    public void setCare(String care) {
        this.care = care;
    }
}

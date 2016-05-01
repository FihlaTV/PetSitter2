package com.zekisanmobile.petsitter2.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Owner extends RealmObject {

    @PrimaryKey
    long id;

    String name;

    String address;

    String district;

    float latitude;

    float longitude;

    @JsonIgnore
    public long photo_id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
}

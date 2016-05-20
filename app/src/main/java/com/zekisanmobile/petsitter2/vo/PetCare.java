package com.zekisanmobile.petsitter2.vo;

import io.realm.RealmObject;

public class PetCare extends RealmObject {

    private String care;

    public String getCare() {
        return care;
    }

    public void setCare(String care) {
        this.care = care;
    }
}

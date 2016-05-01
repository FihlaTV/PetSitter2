package com.zekisanmobile.petsitter2.vo;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Animal extends RealmObject {

    @PrimaryKey
    long id;

    String name;

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
}

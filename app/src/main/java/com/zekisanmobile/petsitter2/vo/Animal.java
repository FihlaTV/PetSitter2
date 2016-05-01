package com.zekisanmobile.petsitter2.vo;

import io.realm.RealmObject;

public class Animal extends RealmObject {

    String name;

    String icon;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}

package com.zekisanmobile.petsitter2.api.body;

import java.util.List;

public class SearchSittersBody {

    // owner app_id
    String app_id;

    List<String> animals;

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public List<String> getAnimals() {
        return animals;
    }

    public void setAnimals(List<String> animals) {
        this.animals = animals;
    }

}

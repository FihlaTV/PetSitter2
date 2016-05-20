package com.zekisanmobile.petsitter2.model;

import com.zekisanmobile.petsitter2.vo.Pet;

import io.realm.Realm;

public class PetModel {

    private Realm realm;

    public PetModel(Realm realm) {
        this.realm = realm;
    }

    public Pet create(Pet p) {
        return null;
    }
}

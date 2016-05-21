package com.zekisanmobile.petsitter2.model;

import com.zekisanmobile.petsitter2.vo.Pet;

import io.realm.Realm;

public class PetModel {

    private Realm realm;

    public PetModel(Realm realm) {
        this.realm = realm;
    }

    public Pet save(Pet pet) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(pet);
        realm.commitTransaction();

        return pet;
    }

    public Pet find(String id) {
        return realm.where(Pet.class).equalTo("id", id).findFirst();
    }
}

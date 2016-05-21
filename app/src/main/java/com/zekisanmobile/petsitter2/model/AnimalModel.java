package com.zekisanmobile.petsitter2.model;

import com.zekisanmobile.petsitter2.vo.Animal;

import io.realm.Realm;

public class AnimalModel {

    Realm realm;

    public AnimalModel(Realm realm) {
        this.realm = realm;
    }

    public Animal save(Animal animal) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(animal);
        realm.commitTransaction();

        return animal;
    }

    public Animal find(long id) {
        return realm.where(Animal.class).equalTo("id", id).findFirst();
    }

    public Animal findByName(String name) {
        return realm.where(Animal.class).equalTo("name", name).findFirst();
    }
}

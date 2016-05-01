package com.zekisanmobile.petsitter2.model;

import com.zekisanmobile.petsitter2.vo.Animal;

import io.realm.Realm;

public class AnimalModel {

    Realm realm;

    public AnimalModel(Realm realm) {
        this.realm = realm;
    }

    public Animal save(Animal animal) {
        Animal animalToSave = createOrFind(animal);

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(animalToSave);
        realm.commitTransaction();

        return animalToSave;
    }

    private Animal createOrFind(Animal animalToFind) {
        Animal animal = find(animalToFind.getId());
        if (animal != null) {
            return animal;
        } else {
            return create(animalToFind);
        }
    }

    private Animal create(Animal animalToFind) {
        realm.beginTransaction();
        Animal animal = realm.createObject(Animal.class);

        animal.setId(animalToFind.getId());
        animal.setName(animalToFind.getName());
        realm.commitTransaction();

        return animal;
    }

    public Animal find(long id) {
        return realm.where(Animal.class).equalTo("id", id).findFirst();
    }
}

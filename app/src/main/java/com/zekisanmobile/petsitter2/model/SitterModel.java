package com.zekisanmobile.petsitter2.model;

import com.zekisanmobile.petsitter2.vo.Animal;
import com.zekisanmobile.petsitter2.vo.PhotoUrl;
import com.zekisanmobile.petsitter2.vo.Sitter;
import com.zekisanmobile.petsitter2.vo.User;

import io.realm.Realm;
import io.realm.RealmList;

public class SitterModel {

    Realm realm;
    private PhotoUrlModel photoUrlModel;
    private AnimalModel animalModel;

    public SitterModel(Realm realm) {
        this.realm = realm;
        photoUrlModel = new PhotoUrlModel(realm);
        animalModel = new AnimalModel(realm);
    }

    public Sitter save(Sitter sitter) {
        Sitter sitterToSave = createOrFind(sitter);

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(sitterToSave);
        realm.commitTransaction();

        return sitterToSave;
    }

    private Sitter createOrFind(Sitter sitterToFind) {
        Sitter sitter = find(sitterToFind.getId());
        if (sitter != null) {
            return sitter;
        } else {
            return create(sitterToFind);
        }
    }
    private Sitter create(Sitter sitterToFind) {
        PhotoUrl photoUrl = photoUrlModel.create(sitterToFind.getPhotoUrl());
        RealmList<Animal> animals = saveSitterAnimals(sitterToFind.getAnimals());

        realm.beginTransaction();
        Sitter sitter = realm.createObject(Sitter.class);
        sitter.setId(sitterToFind.getId());
        sitter.setName(sitterToFind.getName());
        sitter.setAddress(sitterToFind.getAddress());
        sitter.setDistrict(sitterToFind.getDistrict());
        sitter.setLatitude(sitterToFind.getLatitude());
        sitter.setLongitude(sitterToFind.getLongitude());
        sitter.setValueHour(sitterToFind.getValueHour());
        sitter.setAboutMe(sitterToFind.getAboutMe());
        sitter.setAnimals(animals);
        sitter.setPhotoUrl(photoUrl);
        realm.commitTransaction();

        return sitter;
    }

    private RealmList<Animal> saveSitterAnimals(RealmList<Animal> animals) {
        RealmList<Animal> animalsToReturn = new RealmList<>();
        for (Animal a: animals) {
            Animal savedAnimal = animalModel.save(a);
            animalsToReturn.add(savedAnimal);
        }
        return animalsToReturn;
    }

    public Sitter find(long id) {
        return realm.where(Sitter.class).equalTo("id", id).findFirst();
    }
}

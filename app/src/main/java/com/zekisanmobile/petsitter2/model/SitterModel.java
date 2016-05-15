package com.zekisanmobile.petsitter2.model;

import com.zekisanmobile.petsitter2.vo.Animal;
import com.zekisanmobile.petsitter2.vo.Job;
import com.zekisanmobile.petsitter2.vo.PhotoUrl;
import com.zekisanmobile.petsitter2.vo.Sitter;

import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.Sort;

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
            realm.beginTransaction();
            sitter.setRateAvg(sitterToFind.getRateAvg());
            realm.commitTransaction();
            return sitter;
        } else {
            return create(sitterToFind);
        }
    }
    private Sitter create(Sitter sitterToFind) {
        PhotoUrl photoUrl = photoUrlModel.create(sitterToFind.getPhotoUrl());
        RealmList<PhotoUrl> profilePhotos = new RealmList<>();
        for (PhotoUrl p : sitterToFind.getProfilePhotos()) {
            PhotoUrl photoToCreate = photoUrlModel.create(p);
            profilePhotos.add(photoToCreate);
        }
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
        sitter.setPhone(sitterToFind.getPhone());
        sitter.setAnimals(animals);
        sitter.setPhotoUrl(photoUrl);
        sitter.setProfilePhotos(profilePhotos);
        sitter.setRateAvg(sitterToFind.getRateAvg());
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

    public List<Job> getNextJobs(long id) {
        return realm.where(Job.class)
                .equalTo("sitter.id", id)
                .equalTo("status", 30)
                .greaterThan("dateStart", new Date())
                .findAllSorted("dateStart", Sort.DESCENDING);
    }

    public List<Job> getFinishedJobs(long id) {
        return realm.where(Job.class)
                .equalTo("sitter.id", id)
                .equalTo("status", 40)
                .findAll();
    }

    public List<Job> getNewJobs(long id){
        return realm.where(Job.class)
                .equalTo("sitter.id", id)
                .equalTo("status", 10)
                .greaterThanOrEqualTo("dateStart", new Date())
                .findAll();
    }

    public List<Job> getCurrentJobs(long id){
        return realm.where(Job.class)
                .equalTo("sitter.id", id)
                .equalTo("status", 30)
                .lessThanOrEqualTo("dateStart", new Date())
                .greaterThanOrEqualTo("dateFinal", new Date())
                .findAllSorted("dateStart", Sort.DESCENDING);
    }
}

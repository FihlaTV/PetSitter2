package com.zekisanmobile.petsitter2.model;

import com.zekisanmobile.petsitter2.vo.Job;
import com.zekisanmobile.petsitter2.vo.Owner;
import com.zekisanmobile.petsitter2.vo.PhotoUrl;

import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.Sort;

public class OwnerModel {

    Realm realm;
    private PhotoUrlModel photoUrlModel;

    public OwnerModel(Realm realm) {
        this.realm = realm;
        photoUrlModel = new PhotoUrlModel(realm);
    }

    public Owner save(Owner owner) {
        Owner ownerToSave = createOrFind(owner);

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(ownerToSave);
        realm.commitTransaction();

        return ownerToSave;
    }

    private Owner createOrFind(Owner ownerToFind) {
        Owner owner = find(ownerToFind.getId());
        if (owner != null) {
            return owner;
        } else {
            return create(ownerToFind);
        }
    }

    private Owner create(Owner ownerToFind) {
        PhotoUrl photoUrl = photoUrlModel.create(ownerToFind.getPhotoUrl());
        RealmList<PhotoUrl> profilePhotos = new RealmList<>();
        for (PhotoUrl p : ownerToFind.getProfilePhotos()) {
            PhotoUrl photoToCreate = photoUrlModel.create(p);
            profilePhotos.add(photoToCreate);
        }
        realm.beginTransaction();
        Owner owner = realm.createObject(Owner.class);

        owner.setId(ownerToFind.getId());
        owner.setName(ownerToFind.getName());
        owner.setAddress(ownerToFind.getAddress());
        owner.setDistrict(ownerToFind.getDistrict());
        owner.setLatitude(ownerToFind.getLatitude());
        owner.setLongitude(ownerToFind.getLongitude());
        owner.setPhotoUrl(photoUrl);
        owner.setProfilePhotos(profilePhotos);
        realm.commitTransaction();

        return owner;
    }

    public Owner find(long id) {
        return realm.where(Owner.class).equalTo("id", id).findFirst();
    }

    public List<Job> getNextJobs(long id) {
        return realm.where(Job.class)
                .equalTo("owner.id", id)
                .equalTo("status", 30)
                .greaterThan("dateStart", new Date())
                .findAllSorted("dateStart", Sort.DESCENDING);
    }

    public List<Job> getFinishedJobs(long id) {
        return realm.where(Job.class)
                .equalTo("owner.id", id)
                .equalTo("status", 40)
                .findAll();
    }

    public List<Job> getNewJobs(long id){
        return realm.where(Job.class)
                .equalTo("owner.id", id)
                .equalTo("status", 10)
                .greaterThanOrEqualTo("dateStart", new Date())
                .findAll();
    }

    public List<Job> getCurrentJobs(long id){
        return realm.where(Job.class)
                .equalTo("owner.id", id)
                .equalTo("status", 30)
                .lessThanOrEqualTo("dateStart", new Date())
                .greaterThanOrEqualTo("dateFinal", new Date())
                .findAllSorted("dateStart", Sort.DESCENDING);
    }
}

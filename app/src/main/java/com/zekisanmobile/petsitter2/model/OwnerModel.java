package com.zekisanmobile.petsitter2.model;

import com.zekisanmobile.petsitter2.vo.Job;
import com.zekisanmobile.petsitter2.vo.Owner;

import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.Sort;

public class OwnerModel {

    Realm realm;

    public OwnerModel(Realm realm) {
        this.realm = realm;
    }

    public Owner save(Owner owner) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(owner);
        realm.commitTransaction();

        return owner;
    }

    public void updateLocationData(Owner owner) {
        Owner ownerToUpdate = find(owner.getId());
        realm.beginTransaction();
        ownerToUpdate.setStreet(owner.getStreet());
        ownerToUpdate.setAddress_number(owner.getAddress_number());
        ownerToUpdate.setComplement(owner.getComplement());
        ownerToUpdate.setCep(owner.getCep());
        ownerToUpdate.setDistrict(owner.getDistrict());
        ownerToUpdate.setCity(owner.getCity());
        ownerToUpdate.setState(owner.getState());
        ownerToUpdate.setLatitude(owner.getLatitude());
        ownerToUpdate.setLongitude(owner.getLongitude());

        realm.commitTransaction();
    }

    public Owner find(String id) {
        return realm.where(Owner.class).equalTo("id", id).findFirst();
    }

    public List<Job> getNextJobs(String id) {
        return realm.where(Job.class)
                .equalTo("owner.id", id)
                .equalTo("status", 30)
                .greaterThan("dateStart", new Date())
                .findAllSorted("dateStart", Sort.DESCENDING);
    }

    public List<Job> getFinishedJobs(String id) {
        return realm.where(Job.class)
                .equalTo("owner.id", id)
                .equalTo("status", 40)
                .findAll();
    }

    public List<Job> getNewJobs(String id){
        return realm.where(Job.class)
                .equalTo("owner.id", id)
                .equalTo("status", 10)
                .findAll();
    }

    public List<Job> getCurrentJobs(String id){
        return realm.where(Job.class)
                .equalTo("owner.id", id)
                .equalTo("status", 30)
                .findAllSorted("dateStart", Sort.DESCENDING);
    }
}

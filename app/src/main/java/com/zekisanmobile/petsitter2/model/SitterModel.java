package com.zekisanmobile.petsitter2.model;

import com.zekisanmobile.petsitter2.vo.Job;
import com.zekisanmobile.petsitter2.vo.Sitter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.Sort;

public class SitterModel {

    Realm realm;

    public SitterModel(Realm realm) {
        this.realm = realm;
    }

    public Sitter save(Sitter sitter) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(sitter);
        realm.commitTransaction();

        return sitter;
    }

    public void saveList(List<Sitter> sitters) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(sitters);
        realm.commitTransaction();
    }

    public void updateLocationData(Sitter sitter) {
        Sitter sitterToUpdate = find(sitter.getId());
        realm.beginTransaction();
        sitterToUpdate.setStreet(sitter.getStreet());
        sitterToUpdate.setAddress_number(sitter.getAddress_number());
        sitterToUpdate.setComplement(sitter.getComplement());
        sitterToUpdate.setCep(sitter.getCep());
        sitterToUpdate.setDistrict(sitter.getDistrict());
        sitterToUpdate.setCity(sitter.getCity());
        sitterToUpdate.setState(sitter.getState());
        sitterToUpdate.setLatitude(sitter.getLatitude());
        sitterToUpdate.setLongitude(sitter.getLongitude());

        realm.commitTransaction();
    }

    public Sitter find(String id) {
        return realm.where(Sitter.class).equalTo("id", id).findFirst();
    }

    public List<Job> getNextJobs(String id) {
        return realm.where(Job.class)
                .equalTo("sitter.id", id)
                .equalTo("status", 30)
                .greaterThan("dateStart", new Date())
                .findAllSorted("dateStart", Sort.DESCENDING);
    }

    public List<Job> getFinishedJobs(String id) {
        return realm.where(Job.class)
                .equalTo("sitter.id", id)
                .equalTo("status", 40)
                .findAll();
    }

    public List<Job> getNewJobs(String id) {
        return realm.where(Job.class)
                .equalTo("sitter.id", id)
                .equalTo("status", 10)
                .greaterThanOrEqualTo("dateStart", new Date())
                .findAll();
    }

    public List<Job> getCurrentJobs(String id) {
        return realm.where(Job.class)
                .equalTo("sitter.id", id)
                .equalTo("status", 30)
                .lessThan("dateStart", new Date())
                .findAllSorted("dateStart", Sort.DESCENDING);
    }

    public List<Job> getJobsWithRates(String sitter_id) {
        List<Job> jobs = new ArrayList<>();
        for (Job job : getFinishedJobs(sitter_id)) {
            if (job.getRate() != null) jobs.add(job);
        }
        return jobs;
    }
}

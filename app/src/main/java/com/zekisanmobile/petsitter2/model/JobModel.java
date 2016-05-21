package com.zekisanmobile.petsitter2.model;

import com.zekisanmobile.petsitter2.vo.Job;

import java.util.List;

import io.realm.Realm;

public class JobModel {

    private Realm realm;

    public JobModel(Realm realm) {
        this.realm = realm;
    }

    public Job save(Job job) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(job);
        realm.commitTransaction();

        return job;
    }

    public void saveList(List<Job> jobs) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(jobs);
        realm.commitTransaction();
    }

    public Job find(String id) {
        return realm.where(Job.class).equalTo("id", id).findFirst();
    }

    public void updateStatus(Realm realm, String id, int status) {
        Job job = realm.where(Job.class).equalTo("id", id).findFirst();
        realm.beginTransaction();
        job.setStatus(status);
        realm.commitTransaction();
    }
}
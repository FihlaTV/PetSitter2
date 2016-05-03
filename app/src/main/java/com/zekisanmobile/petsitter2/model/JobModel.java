package com.zekisanmobile.petsitter2.model;

import com.zekisanmobile.petsitter2.vo.Job;

import io.realm.Realm;

public class JobModel {

    private Realm realm;
    private PhotoUrlModel photoUrlModel;
    private OwnerModel ownerModel;
    private SitterModel sitterModel;

    public JobModel(Realm realm) {
        this.realm = realm;
        photoUrlModel = new PhotoUrlModel(realm);
        ownerModel = new OwnerModel(realm);
        sitterModel = new SitterModel(realm);
    }

    public Job save(Job job) {
        Job jobToSave = createOrFind(job);

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(jobToSave);
        realm.commitTransaction();

        return jobToSave;
    }

    private Job createOrFind(Job jobToFind) {
        Job job = find(jobToFind.getId());
        if (job != null) {
            return job;
        } else {
            return create(jobToFind);
        }
    }

    public Job create(Job jobToCreate) {
        realm.beginTransaction();
        Job job = realm.createObject(Job.class);

        job.setId(jobToCreate.getId());
        job.setApiId(jobToCreate.getApiId());
        job.setDateStart(jobToCreate.getDateStart());
        job.setDateFinal(jobToCreate.getDateFinal());
        job.setTimeStart(jobToCreate.getTimeStart());
        job.setTimeFinal(jobToCreate.getTimeFinal());
        job.setStatus(jobToCreate.getStatus());
        job.setTotalValue(jobToCreate.getTotalValue());
        realm.commitTransaction();

        return job;
    }

    public Job find(long id) {
        return realm.where(Job.class).equalTo("id", id).findFirst();
    }

}

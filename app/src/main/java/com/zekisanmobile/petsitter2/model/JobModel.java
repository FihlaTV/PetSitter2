package com.zekisanmobile.petsitter2.model;

import com.zekisanmobile.petsitter2.vo.Animal;
import com.zekisanmobile.petsitter2.vo.Job;
import com.zekisanmobile.petsitter2.vo.Owner;
import com.zekisanmobile.petsitter2.vo.Sitter;

import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmList;

public class JobModel {

    private Realm realm;
    private OwnerModel ownerModel;
    private SitterModel sitterModel;
    private AnimalModel animalModel;

    public JobModel(Realm realm) {
        this.realm = realm;
        ownerModel = new OwnerModel(realm);
        sitterModel = new SitterModel(realm);
        animalModel = new AnimalModel(realm);
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
            realm.beginTransaction();
            job.setStatus(jobToFind.getStatus());
            realm.commitTransaction();
            return job;
        } else {
            return create(jobToFind);
        }
    }

    public Job create(Job jobToCreate) {
        String newId = jobToCreate.getId() == null ? generateUniqueId() : jobToCreate.getId();
        Owner owner = ownerModel.save(jobToCreate.getOwner());
        Sitter sitter = sitterModel.save(jobToCreate.getSitter());
        RealmList<Animal> animals = new RealmList<>();
        for (Animal a: jobToCreate.getAnimals()) {
            Animal animal = animalModel.find(a.getId());
            animals.add(animal);
        }

        realm.beginTransaction();
        Job job = realm.createObject(Job.class);

        job.setId(newId);
        job.setDateStart(jobToCreate.getDateStart());
        job.setDateFinal(jobToCreate.getDateFinal());
        job.setTimeStart(jobToCreate.getTimeStart());
        job.setTimeFinal(jobToCreate.getTimeFinal());
        job.setStatus(jobToCreate.getStatus());
        job.setTotalValue(jobToCreate.getTotalValue());
        job.setSitter(sitter);
        job.setOwner(owner);
        job.setAnimals(animals);
        realm.commitTransaction();

        return job;
    }

    private String generateUniqueId() {
        return UUID.randomUUID().toString();
    }

    private long getNextId() {
        try {
            return realm.where(Job.class).max("id").longValue() + 1;
        } catch (NullPointerException e) {
            return 1;
        }
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

    public void delete(Realm realm, String id) {
        Job job = realm.where(Job.class).equalTo("id", id).findFirst();
        realm.beginTransaction();
        job.deleteFromRealm();
        realm.commitTransaction();
    }

}

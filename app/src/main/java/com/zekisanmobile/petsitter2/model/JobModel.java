package com.zekisanmobile.petsitter2.model;

import com.zekisanmobile.petsitter2.vo.Animal;
import com.zekisanmobile.petsitter2.vo.Job;
import com.zekisanmobile.petsitter2.vo.Owner;
import com.zekisanmobile.petsitter2.vo.Sitter;

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
            return job;
        } else {
            return create(jobToFind);
        }
    }

    public Job create(Job jobToCreate) {
        long newId = getNextId();
        Owner owner = ownerModel.find(jobToCreate.getOwner().getId());
        Sitter sitter = sitterModel.find(jobToCreate.getSitter().getId());
        RealmList<Animal> animals = new RealmList<>();
        for (Animal a: jobToCreate.getAnimals()) {
            Animal animal = animalModel.find(a.getId());
            animals.add(animal);
        }

        realm.beginTransaction();
        Job job = realm.createObject(Job.class);

        job.setId(newId);
        job.setApiId(jobToCreate.getApiId());
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

    private long getNextId() {
        try {
            return realm.where(Job.class).max("id").longValue() + 1;
        } catch (NullPointerException e) {
            return 1;
        }
    }

    public Job find(long id) {
        return realm.where(Job.class).equalTo("id", id).findFirst();
    }

}

package com.zekisanmobile.petsitter2.model;

import com.zekisanmobile.petsitter2.vo.Rate;

import java.util.UUID;

import io.realm.Realm;

public class RateModel {

    private Realm realm;

    public RateModel(Realm realm) {
        this.realm = realm;
    }

    public Rate save(Rate rate) {
        Rate rateToSave = createOrFind(rate);

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(rateToSave);
        realm.commitTransaction();

        return rateToSave;
    }

    private Rate createOrFind(Rate rateToFind) {
        Rate rate = find(rateToFind.getId());
        if (rate != null) {
            realm.beginTransaction();
            rate.setStarsQtd(rateToFind.getStarsQtd());
            rate.setSitterComment(rateToFind.getSitterComment());
            rate.setOwnerComment(rateToFind.getOwnerComment());
            realm.commitTransaction();
            return rate;
        } else {
            return create(rateToFind);
        }
    }

    private Rate create(Rate rateToFind) {
        String newId = rateToFind.getId() == null ? generateUniqueId() : rateToFind.getId();
        realm.beginTransaction();
        Rate rate = realm.createObject(Rate.class);

        rate.setId(newId);
        rate.setStarsQtd(rateToFind.getStarsQtd());
        rate.setOwnerComment(rateToFind.getOwnerComment());
        rate.setSitterComment(rateToFind.getSitterComment());
        realm.commitTransaction();

        return rate;
    }

    private String generateUniqueId() {
        return UUID.randomUUID().toString();
    }

    public Rate find(String id) {
        return realm.where(Rate.class).equalTo("id", id).findFirst();
    }
}

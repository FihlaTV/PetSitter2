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
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(rate);
        realm.commitTransaction();

        return rate;
    }

    public Rate find(String id) {
        return realm.where(Rate.class).equalTo("id", id).findFirst();
    }

    public void SetSitterComment(String rateId, String sitterComment) {
        Rate rateToSave = find(rateId);
        realm.beginTransaction();
        rateToSave.setSitterComment(sitterComment);
        realm.commitTransaction();
    }
}

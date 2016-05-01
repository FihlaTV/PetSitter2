package com.zekisanmobile.petsitter2.model;

import com.zekisanmobile.petsitter2.vo.Sitter;

import io.realm.Realm;

public class SitterModel {

    Realm realm;

    public SitterModel(Realm realm) {
        this.realm = realm;
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
            return sitter;
        } else {
            return create(sitterToFind);
        }
    }
    private Sitter create(Sitter sitterToFind) {
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
        realm.commitTransaction();

        return sitter;
    }

    public Sitter find(long id) {
        return realm.where(Sitter.class).equalTo("id", id).findFirst();
    }
}

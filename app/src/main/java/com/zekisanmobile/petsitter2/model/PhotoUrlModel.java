package com.zekisanmobile.petsitter2.model;

import com.zekisanmobile.petsitter2.vo.PhotoUrl;

import io.realm.Realm;

public class PhotoUrlModel {

    Realm realm;

    public PhotoUrlModel(Realm realm) {
        this.realm = realm;
    }

    public PhotoUrl create(PhotoUrl photoUrl) {

        realm.beginTransaction();
        PhotoUrl photoUrlToSave = realm.createObject(PhotoUrl.class);
        photoUrlToSave.setThumb(photoUrl.getThumb());
        photoUrlToSave.setMedium(photoUrl.getMedium());
        photoUrlToSave.setLarge(photoUrl.getLarge());
        realm.commitTransaction();

        return photoUrlToSave;
    }
}

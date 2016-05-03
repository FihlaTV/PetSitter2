package com.zekisanmobile.petsitter2.model;

import com.zekisanmobile.petsitter2.vo.Owner;
import com.zekisanmobile.petsitter2.vo.PhotoUrl;

import io.realm.Realm;

public class OwnerModel {

    Realm realm;
    private PhotoUrlModel photoUrlModel;

    public OwnerModel(Realm realm) {
        this.realm = realm;
        photoUrlModel = new PhotoUrlModel(realm);
    }

    public Owner save(Owner owner) {
        Owner ownerToSave = createOrFind(owner);

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(ownerToSave);
        realm.commitTransaction();

        return ownerToSave;
    }

    private Owner createOrFind(Owner ownerToFind) {
        Owner owner = find(ownerToFind.getId());
        if (owner != null) {
            return owner;
        } else {
            return create(ownerToFind);
        }
    }

    private Owner create(Owner ownerToFind) {
        PhotoUrl photoUrl = photoUrlModel.create(ownerToFind.getPhotoUrl());
        realm.beginTransaction();
        Owner owner = realm.createObject(Owner.class);

        owner.setId(ownerToFind.getId());
        owner.setName(ownerToFind.getName());
        owner.setAddress(ownerToFind.getAddress());
        owner.setDistrict(ownerToFind.getDistrict());
        owner.setLatitude(ownerToFind.getLatitude());
        owner.setLongitude(ownerToFind.getLongitude());
        owner.setPhotoUrl(photoUrl);
        realm.commitTransaction();

        return owner;
    }

    public Owner find(long id) {
        return realm.where(Owner.class).equalTo("id", id).findFirst();
    }
}

package com.zekisanmobile.petsitter2.model;

import com.zekisanmobile.petsitter2.vo.Owner;

import io.realm.Realm;

public class OwnerModel {

    Realm realm;

    public OwnerModel(Realm realm) {
        this.realm = realm;
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
        realm.beginTransaction();
        Owner owner = realm.createObject(Owner.class);

        owner.setId(ownerToFind.getId());
        owner.setName(ownerToFind.getName());
        owner.setAddress(ownerToFind.getAddress());
        owner.setDistrict(ownerToFind.getDistrict());
        owner.setLatitude(ownerToFind.getLatitude());
        owner.setLongitude(ownerToFind.getLongitude());
        realm.commitTransaction();

        return owner;
    }

    public Owner find(long id) {
        return realm.where(Owner.class).equalTo("id", id).findFirst();
    }
}

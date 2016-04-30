package com.zekisanmobile.petsitter2.model;

import com.zekisanmobile.petsitter2.vo.PhotoUrl;
import com.zekisanmobile.petsitter2.vo.User;

import io.realm.Realm;

public class UserModel {

    private Realm realm;

    private PhotoUrlModel photoUrlModel;

    public UserModel(Realm realm) {
        this.realm = realm;
        photoUrlModel = new PhotoUrlModel(realm);
    }

    public User save(User user) {
        User userToSave = createOrFind(user);

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(userToSave);
        realm.commitTransaction();

        return userToSave;
    }

    private User createOrFind(User userToFind) {
        User user = find(userToFind.getId());
        if (user != null) {
            return user;
        } else {
            return create(userToFind);
        }
    }

    private User create(User userToFind) {
        PhotoUrl photoUrl = photoUrlModel.create(userToFind.getPhotoUrl());
        realm.beginTransaction();
        User user = realm.createObject(User.class);

        user.setId(userToFind.getId());
        user.setEmail(userToFind.getEmail());
        user.setEntityId(userToFind.getEntityId());
        user.setEntityType(userToFind.getEntityType());
        user.setPhotoUrl(photoUrl);
        realm.commitTransaction();

        return user;
    }

    public User find(long id) {
        return realm.where(User.class).equalTo("id", id).findFirst();
    }
}

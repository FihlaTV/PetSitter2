package com.zekisanmobile.petsitter2.model;

import com.zekisanmobile.petsitter2.vo.User;

import io.realm.Realm;

public class UserModel {

    private Realm realm;

    public UserModel(Realm realm) {
        this.realm = realm;
    }

    public User save(User user) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(user);
        realm.commitTransaction();

        return user;
    }

    public User find(String id) {
        return realm.where(User.class).equalTo("id", id).findFirst();
    }
}

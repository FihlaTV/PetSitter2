package com.zekisanmobile.petsitter2;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class PetSitterApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        RealmConfiguration config = new RealmConfiguration.Builder(this)
                .name("petsitter.realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }
}

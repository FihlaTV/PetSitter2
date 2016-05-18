package com.zekisanmobile.petsitter2;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.zekisanmobile.petsitter2.di.component.AppComponent;
import com.zekisanmobile.petsitter2.di.component.DaggerAppComponent;
import com.zekisanmobile.petsitter2.di.module.ApplicationModule;
import com.zekisanmobile.petsitter2.di.module.NetModule;
import com.zekisanmobile.petsitter2.util.API;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class PetSitterApp extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        RealmConfiguration config = new RealmConfiguration.Builder(this)
                .name("petsitter.realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);

        //Realm.deleteRealm(config);

        appComponent = DaggerAppComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .netModule(new NetModule(API.BASE_URL))
                .build();

        Stetho.InitializerBuilder initializerBuilder = Stetho.newInitializerBuilder(this);
        initializerBuilder.enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this));
        initializerBuilder.enableDumpapp(Stetho.defaultDumperPluginsProvider(this));
        Stetho.Initializer initializer = initializerBuilder.build();
        Stetho.initialize(initializer);
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}

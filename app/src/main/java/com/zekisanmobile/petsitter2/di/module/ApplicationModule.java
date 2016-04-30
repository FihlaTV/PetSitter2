package com.zekisanmobile.petsitter2.di.module;

import android.app.Application;

import com.zekisanmobile.petsitter2.PetSitterApp;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    PetSitterApp application;

    public ApplicationModule(PetSitterApp application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return application;
    }
}

package com.zekisanmobile.petsitter2.di.component;

import com.zekisanmobile.petsitter2.asyncTask.LoginTask;
import com.zekisanmobile.petsitter2.asyncTask.LogoutTask;
import com.zekisanmobile.petsitter2.asyncTask.SearchSittersTask;
import com.zekisanmobile.petsitter2.di.module.ApplicationModule;
import com.zekisanmobile.petsitter2.di.module.NetModule;
import com.zekisanmobile.petsitter2.job.SendJobRequestJob;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, NetModule.class })
public interface AppComponent {

    void inject(LoginTask loginTask);
    void inject(SearchSittersTask searchSittersTask);
    void inject(LogoutTask logoutTask);

    void inject(SendJobRequestJob sendJobRequestJob);
}

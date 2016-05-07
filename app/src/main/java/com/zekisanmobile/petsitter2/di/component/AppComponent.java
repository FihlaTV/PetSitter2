package com.zekisanmobile.petsitter2.di.component;

import com.zekisanmobile.petsitter2.asyncTask.LoginTask;
import com.zekisanmobile.petsitter2.asyncTask.LogoutTask;
import com.zekisanmobile.petsitter2.asyncTask.SearchSittersTask;
import com.zekisanmobile.petsitter2.di.module.ApplicationModule;
import com.zekisanmobile.petsitter2.di.module.NetModule;
import com.zekisanmobile.petsitter2.job.job.FetchOwnerJobsJob;
import com.zekisanmobile.petsitter2.job.job.FetchSitterJobsJob;
import com.zekisanmobile.petsitter2.job.job.SendJobRequestJob;
import com.zekisanmobile.petsitter2.job.job.SendJobStatusJob;
import com.zekisanmobile.petsitter2.view.owner.NewJobRequestActivity;
import com.zekisanmobile.petsitter2.view.sitter.JobDetailsActivity;
import com.zekisanmobile.petsitter2.view.sitter.SitterHomeActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, NetModule.class })
public interface AppComponent {

    void inject(LoginTask loginTask);
    void inject(SearchSittersTask searchSittersTask);
    void inject(LogoutTask logoutTask);

    void inject(SendJobRequestJob sendJobRequestJob);
    void inject(FetchOwnerJobsJob fetchOwnerJobsJob);
    void inject(FetchSitterJobsJob fetchSitterJobsJob);
    void inject(SendJobStatusJob sendJobStatusJob);

    void inject(NewJobRequestActivity newJobRequestActivity);
    void inject(SitterHomeActivity sitterHomeActivity);
    void inject(JobDetailsActivity jobDetailsActivity);
}

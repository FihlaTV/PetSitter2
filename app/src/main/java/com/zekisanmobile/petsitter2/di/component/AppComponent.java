package com.zekisanmobile.petsitter2.di.component;

import com.zekisanmobile.petsitter2.asyncTask.AddNewPetTask;
import com.zekisanmobile.petsitter2.asyncTask.CreateOwnerTask;
import com.zekisanmobile.petsitter2.asyncTask.CreateSitterTask;
import com.zekisanmobile.petsitter2.asyncTask.LocationTask;
import com.zekisanmobile.petsitter2.asyncTask.LoginTask;
import com.zekisanmobile.petsitter2.asyncTask.LogoutTask;
import com.zekisanmobile.petsitter2.asyncTask.SearchSittersTask;
import com.zekisanmobile.petsitter2.di.module.ApplicationModule;
import com.zekisanmobile.petsitter2.di.module.NetModule;
import com.zekisanmobile.petsitter2.job.job.FetchOwnerJobsJob;
import com.zekisanmobile.petsitter2.job.job.FetchSitterJobsJob;
import com.zekisanmobile.petsitter2.job.job.ReplyRateJob;
import com.zekisanmobile.petsitter2.job.job.SendJobRateJob;
import com.zekisanmobile.petsitter2.job.job.SendJobRequestJob;
import com.zekisanmobile.petsitter2.job.job.SendJobStatusJob;
import com.zekisanmobile.petsitter2.job.job.SendSummaryJob;
import com.zekisanmobile.petsitter2.view.owner.NewJobRequestActivity;
import com.zekisanmobile.petsitter2.view.owner.OwnerHomeActivity;
import com.zekisanmobile.petsitter2.view.owner.RateJobActivity;
import com.zekisanmobile.petsitter2.view.sitter.ReplyRateActivity;
import com.zekisanmobile.petsitter2.view.sitter.SitterHomeActivity;
import com.zekisanmobile.petsitter2.view.sitter.SitterJobDetailsActivity;
import com.zekisanmobile.petsitter2.view.summary.NewSummaryActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, NetModule.class })
public interface AppComponent {

    void inject(LoginTask loginTask);
    void inject(SearchSittersTask searchSittersTask);
    void inject(LogoutTask logoutTask);
    void inject(LocationTask locationTask);
    void inject(CreateOwnerTask createOwnerTask);
    void inject(CreateSitterTask createSitterTask);
    void inject(AddNewPetTask addNewPetTask);

    void inject(SendJobRequestJob sendJobRequestJob);
    void inject(FetchOwnerJobsJob fetchOwnerJobsJob);
    void inject(FetchSitterJobsJob fetchSitterJobsJob);
    void inject(SendJobStatusJob sendJobStatusJob);
    void inject(SendJobRateJob sendJobRateJob);
    void inject(ReplyRateJob replyRateJob);
    void inject(SendSummaryJob sendSummaryJob);

    void inject(NewJobRequestActivity newJobRequestActivity);
    void inject(SitterHomeActivity sitterHomeActivity);
    void inject(SitterJobDetailsActivity sitterJobDetailsActivity);
    void inject(OwnerHomeActivity ownerHomeActivity);
    void inject(RateJobActivity rateJobActivity);
    void inject(ReplyRateActivity replyRateActivity);
    void inject(NewSummaryActivity newSummaryActivity);
}

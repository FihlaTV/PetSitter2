package com.zekisanmobile.petsitter2.job.job;

import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.zekisanmobile.petsitter2.PetSitterApp;
import com.zekisanmobile.petsitter2.api.ApiService;
import com.zekisanmobile.petsitter2.api.body.GetJobsBody;
import com.zekisanmobile.petsitter2.di.component.AppComponent;
import com.zekisanmobile.petsitter2.job.BaseJob;
import com.zekisanmobile.petsitter2.model.JobModel;
import com.zekisanmobile.petsitter2.vo.Job;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FetchOwnerJobsJob extends BaseJob {

    public static final int PRIORITY = 1;
    private static final String GROUP = "FetchOwnerJobsJob";
    private String owner_id;

    @Inject
    transient ApiService service;

    public FetchOwnerJobsJob(String owner_id) {
        super(new Params(PRIORITY).addTags(GROUP).requireNetwork().persist());
        this.owner_id = owner_id;
    }

    @Override
    public void inject(AppComponent appComponent) {
        super.inject(appComponent);
        appComponent.inject(this);
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        Realm realm = Realm.getDefaultInstance();
        JobModel jobModel = new JobModel(realm);

        GetJobsBody body = new GetJobsBody();
        body.setApp_id(owner_id);
        Response<List<Job>> response = service.ownerJobs(body).execute();
        List<Job> jobs = response.body();
        if (jobs != null && !jobs.isEmpty()) {
            jobModel.saveList(jobs);
        }
        realm.close();
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

    }

    @Override
    public RetryConstraint shouldReRunOnThrowable(Throwable throwable, int runCount,
                                                  int maxRunCount) {
        if (shouldRetry(throwable)) {
            return RetryConstraint.createExponentialBackoff(runCount, 1000);
        }
        return RetryConstraint.CANCEL;
    }

    @Override
    protected int getRetryLimit() {
        return 2;
    }
}
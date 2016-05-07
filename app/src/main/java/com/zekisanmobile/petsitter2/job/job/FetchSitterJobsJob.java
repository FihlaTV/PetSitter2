package com.zekisanmobile.petsitter2.job.job;

import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.zekisanmobile.petsitter2.api.ApiService;
import com.zekisanmobile.petsitter2.di.component.AppComponent;
import com.zekisanmobile.petsitter2.event.job.FetchedSitterJobsEvent;
import com.zekisanmobile.petsitter2.job.BaseJob;
import com.zekisanmobile.petsitter2.model.JobModel;
import com.zekisanmobile.petsitter2.vo.Job;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import retrofit2.Response;

public class FetchSitterJobsJob extends BaseJob {

    public static final int PRIORITY = 1;
    private static final String GROUP = "FetchSitterJobsJob";
    private long sitter_id;

    @Inject
    transient ApiService service;

    public FetchSitterJobsJob(long sitter_id) {
        super(new Params(PRIORITY).addTags(GROUP).requireNetwork().persist());
        this.sitter_id = sitter_id;
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

        Response<List<Job>> response = service.sitterJobs(sitter_id).execute();
        List<Job> jobs = response.body();
        for (Job job : jobs) {
            jobModel.save(job);
        }
        realm.close();

        if (jobs != null && !jobs.isEmpty()) {
            EventBus.getDefault().post(new FetchedSitterJobsEvent(true, sitter_id));
        }
    }

    @Override
    protected void onCancel(int cancelReason) {

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

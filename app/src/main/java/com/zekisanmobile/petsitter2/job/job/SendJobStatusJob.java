package com.zekisanmobile.petsitter2.job.job;

import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.zekisanmobile.petsitter2.api.ApiService;
import com.zekisanmobile.petsitter2.api.body.JobStatusBody;
import com.zekisanmobile.petsitter2.di.component.AppComponent;
import com.zekisanmobile.petsitter2.job.BaseJob;

import javax.inject.Inject;

public class SendJobStatusJob extends BaseJob {

    private static final String GROUP = "SendJobStatusJob";
    public static final int PRIORITY = 1;
    private String jobId;
    private int status;

    @Inject
    transient ApiService service;

    public SendJobStatusJob(int status, String jobId) {
        super(new Params(PRIORITY).addTags(GROUP).requireNetwork().persist());
        this.jobId = jobId;
        this.status = status;
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
        JobStatusBody body = new JobStatusBody();
        body.setStatus(status);
        body.setApp_id(jobId);
        service.sendJobStatusUpdate(body).execute();
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

package com.zekisanmobile.petsitter2.job.job;

import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.zekisanmobile.petsitter2.api.ApiService;
import com.zekisanmobile.petsitter2.api.body.ReplyRateBody;
import com.zekisanmobile.petsitter2.di.component.AppComponent;
import com.zekisanmobile.petsitter2.job.BaseJob;

import javax.inject.Inject;

public class ReplyRateJob extends BaseJob {

    public static final int PRIORITY = 1;
    private static final String GROUP = "ReplyRateJob";
    private String rateId;
    private String sitterComment;

    @Inject
    transient ApiService service;

    public ReplyRateJob(String rateId, String sitterComment) {
        super(new Params(PRIORITY).addTags(GROUP).requireNetwork().persist());
        this.rateId = rateId;
        this.sitterComment = sitterComment;
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
        ReplyRateBody body = new ReplyRateBody();
        body.setApp_id(rateId);
        body.setSitter_comment(sitterComment);

        service.replyRate(body).execute();
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

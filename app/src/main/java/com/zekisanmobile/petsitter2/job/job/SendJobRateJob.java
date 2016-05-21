package com.zekisanmobile.petsitter2.job.job;

import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.zekisanmobile.petsitter2.api.ApiService;
import com.zekisanmobile.petsitter2.api.body.RateJobBody;
import com.zekisanmobile.petsitter2.di.component.AppComponent;
import com.zekisanmobile.petsitter2.job.BaseJob;
import com.zekisanmobile.petsitter2.model.RateModel;
import com.zekisanmobile.petsitter2.vo.Rate;

import javax.inject.Inject;

import io.realm.Realm;

public class SendJobRateJob extends BaseJob {

    public static final int PRIORITY = 1;
    private static final String GROUP = "SendJobRateJob";
    private String job_id;
    private String owner_id;
    private String rate_app_id;

    @Inject
    transient ApiService service;

    public SendJobRateJob(String job_id, String owner_id, String rate_app_id) {
        super(new Params(PRIORITY).addTags(GROUP).requireNetwork().persist());
        this.job_id = job_id;
        this.owner_id = owner_id;
        this.rate_app_id = rate_app_id;
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
        RateModel rateModel = new RateModel(realm);
        Rate rate = rateModel.find(rate_app_id);

        RateJobBody body = new RateJobBody();
        body.setOwner_app_id(owner_id);
        body.setContact_app_id(job_id);
        body.setRate_app_id(rate_app_id);
        body.setOwner_comment(rate.getOwnerComment());
        body.setStars_qtd(rate.getStarsQtd());

        service.ratejob(body).execute();
        realm.close();
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

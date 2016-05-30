package com.zekisanmobile.petsitter2.job.job;

import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.zekisanmobile.petsitter2.api.ApiService;
import com.zekisanmobile.petsitter2.api.body.JobRequestBody;
import com.zekisanmobile.petsitter2.api.body.PetBody;
import com.zekisanmobile.petsitter2.di.component.AppComponent;
import com.zekisanmobile.petsitter2.job.BaseJob;
import com.zekisanmobile.petsitter2.model.JobModel;
import com.zekisanmobile.petsitter2.util.DateFormatter;
import com.zekisanmobile.petsitter2.vo.Job;
import com.zekisanmobile.petsitter2.vo.Pet;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;

public class SendJobRequestJob extends BaseJob {

    public static final int PRIORITY = 1;
    private String job_id;
    private static final String GROUP = "SendJobRequestJob";

    @Inject
    transient ApiService service;

    public SendJobRequestJob(String job_id) {
        super(new Params(PRIORITY).addTags(GROUP).requireNetwork().persist());
        this.job_id = job_id;
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
        Job job = jobModel.find(job_id);
        List<PetBody> pets = new ArrayList<>();
        for (Pet pet : job.getPets()) {
            PetBody petBody = new PetBody();
            petBody.setPet_app_id(pet.getId());
            pets.add(petBody);
        }

        JobRequestBody body = new JobRequestBody();
        body.setApp_id(job.getOwner().getId());
        body.setContact_app_id(job.getId());
        body.setDate_start(DateFormatter.formattedDateForAPI(job.getDateStart()));
        body.setDate_final(DateFormatter.formattedDateForAPI(job.getDateFinal()));
        body.setTime_start(job.getTimeStart());
        body.setTime_final(job.getTimeFinal());
        body.setTotal_value(job.getTotalValue());
        body.setSitter_id(job.getSitter().getId());
        body.setPet_contacts(pets);

        service.sendJobRequest(body).execute();
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

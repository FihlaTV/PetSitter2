package com.zekisanmobile.petsitter2.job;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.zekisanmobile.petsitter2.PetSitterApp;
import com.zekisanmobile.petsitter2.api.ApiService;
import com.zekisanmobile.petsitter2.api.body.AnimalBody;
import com.zekisanmobile.petsitter2.api.body.JobRequestBody;
import com.zekisanmobile.petsitter2.util.DateFormatter;
import com.zekisanmobile.petsitter2.vo.Animal;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Retrofit;

public class SendJobRequestJob extends Job {

    public static final int PRIORITY = 1;
    private com.zekisanmobile.petsitter2.vo.Job job;

    @Inject
    Retrofit retrofit;

    public SendJobRequestJob(com.zekisanmobile.petsitter2.vo.Job job, PetSitterApp application) {
        super(new Params(PRIORITY).requireNetwork().persist());
        application.getAppComponent().inject(this);
        this.job = job;
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        List<AnimalBody> animals = new ArrayList<>();
        for (Animal animal : job.getAnimals()) {
            AnimalBody animalBody = new AnimalBody();
            animalBody.setAnimal_id(animal.getId());
        }

        JobRequestBody body = new JobRequestBody();
        body.setApp_id(job.getId());
        body.setDate_start(DateFormatter.formattedDateForAPI(job.getDateStart()));
        body.setDate_final(DateFormatter.formattedDateForAPI(job.getDateFinal()));
        body.setTime_start(job.getTimeStart());
        body.setTime_final(job.getTimeFinal());
        body.setTotal_value(job.getTotalValue());
        body.setSitter_id(job.getSitter().getId());
        body.setAnimal_contacts(animals);

        ApiService service = retrofit.create(ApiService.class);
        service.sendJobRequest(job.getOwner().getId(), body).execute();
    }

    @Override
    protected void onCancel(int cancelReason) {

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(Throwable throwable, int runCount, int maxRunCount) {
        return RetryConstraint.createExponentialBackoff(runCount, 1000);
    }
}

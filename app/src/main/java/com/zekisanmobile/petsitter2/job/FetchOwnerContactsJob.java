package com.zekisanmobile.petsitter2.job;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.zekisanmobile.petsitter2.PetSitterApp;
import com.zekisanmobile.petsitter2.api.ApiService;
import com.zekisanmobile.petsitter2.model.JobModel;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FetchOwnerContactsJob extends Job {

    public static final int PRIORITY = 1;
    private long owner_id;
    private JobModel jobModel;
    private Realm realm;

    @Inject
    Retrofit retrofit;

    public FetchOwnerContactsJob(long owner_id, PetSitterApp application) {
        super(new Params(PRIORITY).requireNetwork().persist());
        application.getAppComponent().inject(this);
        this.owner_id = owner_id;
        realm = Realm.getDefaultInstance();
        jobModel = new JobModel(realm);
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        ApiService service = retrofit.create(ApiService.class);
        Response<List<com.zekisanmobile.petsitter2.vo.Job>> response = service.ownerJobs(owner_id).execute();
        List<com.zekisanmobile.petsitter2.vo.Job> jobs = response.body();
        for (com.zekisanmobile.petsitter2.vo.Job job : jobs) {
            jobModel.save(job);
        }
        realm.close();
    }

    @Override
    protected void onCancel(int cancelReason) {

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(Throwable throwable, int runCount, int maxRunCount) {
        return null;
    }
}
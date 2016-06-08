package com.zekisanmobile.petsitter2.job.job;

import android.net.Uri;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.zekisanmobile.petsitter2.api.ApiService;
import com.zekisanmobile.petsitter2.di.component.AppComponent;
import com.zekisanmobile.petsitter2.event.summary.UpdateSummariesUIEvent;
import com.zekisanmobile.petsitter2.job.BaseJob;
import com.zekisanmobile.petsitter2.model.JobModel;
import com.zekisanmobile.petsitter2.vo.Job;
import com.zekisanmobile.petsitter2.vo.Summary;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import javax.inject.Inject;

import io.realm.Realm;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class SendSummaryJob extends BaseJob {

    private static final String GROUP = "SendSummaryJob";
    public static final int PRIORITY = 1;

    private String jobId;
    private String summaryId;

    @Inject
    transient ApiService service;

    public SendSummaryJob(String jobId, String summaryId) {
        super(new Params(PRIORITY).addTags(GROUP).requireNetwork().persist());
        this.jobId = jobId;
        this.summaryId = summaryId;
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
        Summary summary = realm.where(Summary.class).equalTo("id", summaryId).findFirst();

        RequestBody app_id = RequestBody.create(MediaType.parse("multipart/form-data"), jobId);
        RequestBody summary_app_id = RequestBody.create(MediaType.parse("multipart/form-data"),
                summaryId);
        RequestBody text = RequestBody.create(MediaType.parse("multipart/form-data"),
                summary.getText());
        RequestBody created_at = RequestBody.create(MediaType.parse("multipart/form-data"),
                summary.getCreatedAt());
        RequestBody photo_app_id = RequestBody.create(MediaType.parse("multipart/form-data"),
                summary.getPhotoUrl().getId());

        Uri fileUri = Uri.parse(summary.getPhotoUrl().getImage());
        File file =  new File(fileUri.getPath());
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file);

        Response<Job> response = service.saveSummary(app_id, summary_app_id, text, created_at,
                photo_app_id, fileBody).execute();
        Job job = response.body();
        new JobModel(realm).save(job);

        EventBus.getDefault().post(new UpdateSummariesUIEvent());

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

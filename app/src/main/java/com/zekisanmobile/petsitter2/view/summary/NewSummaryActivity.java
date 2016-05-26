package com.zekisanmobile.petsitter2.view.summary;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.birbit.android.jobqueue.JobManager;
import com.zekisanmobile.petsitter2.PetSitterApp;
import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.job.job.SendSummaryJob;
import com.zekisanmobile.petsitter2.model.JobModel;
import com.zekisanmobile.petsitter2.util.Config;
import com.zekisanmobile.petsitter2.util.DateFormatter;
import com.zekisanmobile.petsitter2.util.EntityType;
import com.zekisanmobile.petsitter2.util.UniqueID;
import com.zekisanmobile.petsitter2.vo.Job;
import com.zekisanmobile.petsitter2.vo.PhotoUrl;
import com.zekisanmobile.petsitter2.vo.Summary;

import java.io.File;
import java.util.Date;
import java.util.UUID;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class NewSummaryActivity extends AppCompatActivity {

    private Uri fileUri;
    private static final int RC_TAKE_PICTURE = 101;
    private static final String TAG = "NewSummaryActivity";

    private Realm realm;
    private String jobId;
    private JobModel jobModel;
    private Job job;
    private PhotoUrl photoUrl;
    private Summary summary;
    private String entityType;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.iv_photo)
    ImageView ivPhoto;

    @BindView(R.id.et_comment)
    EditText etComment;

    @Inject
    JobManager jobManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_summary);

        ((PetSitterApp)getApplication()).getAppComponent().inject(this);

        ButterKnife.bind(this);

        this.jobId = getIntent().getStringExtra(Config.JOB_ID);
        this.entityType = getIntent().getStringExtra(EntityType.TYPE);

        configureToolbar();
        defineMembers();
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.iv_photo)
    public void openCamera() {
        launchCamera();
    }

    @OnClick(R.id.btn_send)
    public void send() {
        savePhoto();
        saveSummary();
        saveJob();
        enqueueJob();
        redirectToSummaryList();
    }

    private void configureToolbar() {
        toolbar.setTitle(getString(R.string.send_daily_summary));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void defineMembers() {
        realm = Realm.getDefaultInstance();
        jobModel = new JobModel(realm);
        job = jobModel.find(jobId);
    }

    private void launchCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File file = new File(Environment.getExternalStorageDirectory(), UUID.randomUUID().toString()
                + ".jpg");
        fileUri = Uri.fromFile(file);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        startActivityForResult(takePictureIntent, RC_TAKE_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);

        if (requestCode == RC_TAKE_PICTURE) {
            if (resultCode == RESULT_OK) {
                if (fileUri != null) {
                    uploadFromUri(fileUri);
                } else {
                    Log.w(TAG, "File URI is null");
                }
            } else {
                Toast.makeText(this, "Taking picture failed.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadFromUri(Uri fileUri) {
        Log.d(TAG, "uploadFromUri:src:" + fileUri.toString());

        ivPhoto.setImageURI(fileUri);
    }

    private void savePhoto() {
        realm.beginTransaction();
        PhotoUrl photoUrl = realm.createObject(PhotoUrl.class);
        photoUrl.setId(UniqueID.generateUniqueID());
        photoUrl.setLarge(fileUri.toString());
        realm.commitTransaction();

        this.photoUrl = photoUrl;
    }

    private void saveSummary() {
        realm.beginTransaction();
        Summary summary = realm.createObject(Summary.class);
        summary.setId(UniqueID.generateUniqueID());
        summary.setPhotoUrl(this.photoUrl);
        summary.setText(etComment.getText().toString().trim());
        summary.setCreatedAt(DateFormatter.formattedDateForAPI(new Date()));
        realm.commitTransaction();

        this.summary = summary;
    }

    private void saveJob() {
        realm.beginTransaction();
        job.getSummaries().add(this.summary);
        realm.commitTransaction();
    }

    private void enqueueJob() {
        jobManager.addJobInBackground(new SendSummaryJob(jobId, summary.getId()));
    }

    private void redirectToSummaryList() {
        Intent intent = new Intent(NewSummaryActivity.this, DailySummariesListActivity.class);
        intent.putExtra(Config.JOB_ID, jobId);
        intent.putExtra(EntityType.TYPE, entityType);
        startActivity(intent);
        finish();
    }
}

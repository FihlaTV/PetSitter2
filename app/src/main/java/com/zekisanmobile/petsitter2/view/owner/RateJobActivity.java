package com.zekisanmobile.petsitter2.view.owner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.model.JobModel;
import com.zekisanmobile.petsitter2.util.Config;
import com.zekisanmobile.petsitter2.vo.Job;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class RateJobActivity extends AppCompatActivity {

    private String jobStatus;
    private String jobId;
    private Job job;
    private JobModel jobModel;

    private Realm realm;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_job);

        ButterKnife.bind(this);

        this.jobId = getIntent().getStringExtra(Config.JOB_ID);

        defineMembers();
        configureToolbar();
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

    private void defineMembers() {
        realm = Realm.getDefaultInstance();

        jobModel = new JobModel(realm);
        job = jobModel.find(jobId);
    }

    private void configureToolbar() {
        toolbar.setTitle(getString(R.string.rate_job_title));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }
}

package com.zekisanmobile.petsitter2.view.sitter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.model.JobModel;
import com.zekisanmobile.petsitter2.util.Config;
import com.zekisanmobile.petsitter2.vo.Job;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class JobDetailsActivity extends AppCompatActivity {

    private Realm realm;

    private long jobId;
    private Job job;
    private JobModel jobModel;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);

        ButterKnife.bind(this);

        jobId = getIntent().getLongExtra(Config.JOB_ID, 0);

        defineMembers();
        configureToolbar();
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    private void defineMembers() {
        realm = Realm.getDefaultInstance();

        jobModel = new JobModel(realm);
        job = jobModel.find(jobId);
    }

    private void configureToolbar() {
        toolbar.setTitle(getString(R.string.search_result_title));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }
}

package com.zekisanmobile.petsitter2.view.sitter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.birbit.android.jobqueue.JobManager;
import com.zekisanmobile.petsitter2.PetSitterApp;
import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.job.job.ReplyRateJob;
import com.zekisanmobile.petsitter2.model.JobModel;
import com.zekisanmobile.petsitter2.model.RateModel;
import com.zekisanmobile.petsitter2.util.Config;
import com.zekisanmobile.petsitter2.util.DateFormatter;
import com.zekisanmobile.petsitter2.vo.Job;
import com.zekisanmobile.petsitter2.vo.Rate;

import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class ReplyRateActivity extends AppCompatActivity {

    private Realm realm;

    private String jobId;
    private Job job;
    private JobModel jobModel;
    private RateModel rateModel;

    @Inject
    JobManager jobManager;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tv_rate_date)
    TextView tvRateDate;

    @BindView(R.id.tv_owner_comment)
    TextView tvOwnerComment;

    @BindView(R.id.et_sitter_comment)
    EditText etSitterComment;

    @BindView(R.id.rating_bar)
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_rate);

        ((PetSitterApp) getApplication()).getAppComponent().inject(this);

        ButterKnife.bind(this);

        this.jobId = getIntent().getStringExtra(Config.JOB_ID);

        defineMembers();
        setupToolbar();
        setupViews();
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reply_job, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.m_save:
                rateModel.SetSitterComment(job.getRate().getId(),
                        etSitterComment.getText().toString().trim());
                jobModel.save(job);

                jobManager.addJobInBackground(new ReplyRateJob(job.getRate().getId(),
                        job.getRate().getSitterComment()));

                Intent intent = new Intent(ReplyRateActivity.this, SitterRatesActivity.class);
                intent.putExtra(Config.SITTER_ID, job.getSitter().getId());
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void defineMembers() {
        realm = Realm.getDefaultInstance();

        jobModel = new JobModel(realm);
        rateModel = new RateModel(realm);
        job = jobModel.find(jobId);
    }

    private void setupToolbar() {
        toolbar.setTitle(getString(R.string.my_rate));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void setupViews() {
        tvRateDate.setText(DateFormatter.formattedDateForView(new Date()));
        tvOwnerComment.setText(job.getRate().getOwnerComment());
        ratingBar.setRating(job.getRate().getStarsQtd());
    }
}

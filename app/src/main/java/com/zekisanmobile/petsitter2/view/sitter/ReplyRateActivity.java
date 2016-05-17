package com.zekisanmobile.petsitter2.view.sitter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.model.JobModel;
import com.zekisanmobile.petsitter2.util.Config;
import com.zekisanmobile.petsitter2.util.DateFormatter;
import com.zekisanmobile.petsitter2.vo.Job;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class ReplyRateActivity extends AppCompatActivity {

    private Realm realm;

    private String jobId;
    private Job job;
    private JobModel jobModel;

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

    private void defineMembers() {
        realm = Realm.getDefaultInstance();

        jobModel = new JobModel(realm);
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

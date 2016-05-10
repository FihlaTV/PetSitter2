package com.zekisanmobile.petsitter2.view.owner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.model.JobModel;
import com.zekisanmobile.petsitter2.util.CircleTransform;
import com.zekisanmobile.petsitter2.util.Config;
import com.zekisanmobile.petsitter2.vo.Job;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class RateJobActivity extends AppCompatActivity {

    private String jobId;
    private Job job;
    private JobModel jobModel;

    private Realm realm;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.iv_sitter)
    ImageView ivSitter;

    @BindView(R.id.tv_owner)
    TextView tvOwner;

    @BindView(R.id.rating_bar)
    RatingBar ratingBar;

    @BindView(R.id.tv_rate_value)
    TextView tvRateValue;

    @BindView(R.id.et_owner_comment)
    EditText etOwnerComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_job);

        ButterKnife.bind(this);

        this.jobId = getIntent().getStringExtra(Config.JOB_ID);

        defineMembers();
        configureToolbar();
        setupViews();
        addListenerOnRatingBar();
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ok, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                finish();
                break;
            case R.menu.menu_ok:
                // TODO: Enviar rate para API
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

    private void setupViews() {
        Picasso.with(this)
                .load(job.getSitter().getPhotoUrl().getMedium())
                .transform(new CircleTransform())
                .into(ivSitter);
        tvOwner.setText(job.getSitter().getName());

    }

    public void addListenerOnRatingBar() {
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,  boolean fromUser) {
                tvRateValue.setText(String.valueOf(Math.round(rating)));
            }
        });
    }

}

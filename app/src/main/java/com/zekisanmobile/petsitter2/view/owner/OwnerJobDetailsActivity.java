package com.zekisanmobile.petsitter2.view.owner;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.adapter.SearchAnimalListAdapter;
import com.zekisanmobile.petsitter2.model.JobModel;
import com.zekisanmobile.petsitter2.util.CircleTransform;
import com.zekisanmobile.petsitter2.util.Config;
import com.zekisanmobile.petsitter2.util.DateFormatter;
import com.zekisanmobile.petsitter2.util.EntityType;
import com.zekisanmobile.petsitter2.util.JobsStatusString;
import com.zekisanmobile.petsitter2.view.summary.DailySummariesListActivity;
import com.zekisanmobile.petsitter2.vo.Job;
import com.zekisanmobile.petsitter2.vo.SearchAnimalItem;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class OwnerJobDetailsActivity extends AppCompatActivity {

    private Realm realm;

    private String jobStatus;
    private String jobId;
    private Job job;
    private JobModel jobModel;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.iv_sitter)
    ImageView ivSitter;

    @BindView(R.id.tv_sitter)
    TextView tvSitter;

    @BindView(R.id.tv_period)
    TextView tvPeriod;

    @BindView(R.id.tv_time)
    TextView tvTime;

    @BindView(R.id.tv_total_value)
    TextView tvTotalValue;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_job_details);

        ButterKnife.bind(this);

        jobId = getIntent().getStringExtra(Config.JOB_ID);
        jobStatus = getIntent().getStringExtra(Config.JOB_STATUS);

        defineMembers();
        configureToolbar();
        setupViews();
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        switch (jobStatus) {
            case JobsStatusString.NEW:
                break;
            case JobsStatusString.NEXT:
                break;
            case JobsStatusString.CURRENT:
                getMenuInflater().inflate(R.menu.menu_summaries, menu);
                break;
            default:
                if (job.getRate() == null) {
                    getMenuInflater().inflate(R.menu.menu_job_rate, menu);
                }
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;

        switch (id) {
            case R.id.menu_rate:
                intent = new Intent(OwnerJobDetailsActivity.this, RateJobActivity.class);
                intent.putExtra(Config.JOB_ID, job.getId());
                startActivity(intent);
                break;
            case R.id.menu_summary:
                intent = new Intent(OwnerJobDetailsActivity.this,
                        DailySummariesListActivity.class);
                intent.putExtra(Config.JOB_ID, jobId);
                intent.putExtra(EntityType.TYPE, EntityType.OWNER);
                startActivity(intent);
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
        toolbar.setTitle(getTitleForToolbar());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @NonNull
    private String getTitleForToolbar() {
        switch (jobStatus) {
            case JobsStatusString.NEW:
                return getString(R.string.new_job);
            case JobsStatusString.NEXT:
                return getString(R.string.next_job);
            case JobsStatusString.CURRENT:
                return getString(R.string.current_job);
            default:
                return getString(R.string.finished_job);
        }
    }

    private void setupViews() {
        tvSitter.setText(job.getSitter().getName());
        Picasso.with(this)
                .load(job.getSitter().getPhotoUrl().getImage())
                .transform(new CircleTransform())
                .into(ivSitter);
        tvPeriod.setText(DateFormatter.formattedDatePeriodForView(job.getDateStart(),
                job.getDateFinal()));
        tvTime.setText(DateFormatter.formattedTimePeriodForView(job.getTimeStart(),
                job.getTimeFinal()));
        tvTotalValue.setText(NumberFormat.getCurrencyInstance().format(job.getTotalValue()));
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        List<SearchAnimalItem> animalItems = populateAnimalsRecyclerView();
        SearchAnimalListAdapter adapter = new SearchAnimalListAdapter(animalItems);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private List<SearchAnimalItem> populateAnimalsRecyclerView() {
        String[] animalIcons = this.getResources().getStringArray(R.array.animal_icons);
        String[] animalNames = this.getResources().getStringArray(R.array.animal_names);
        List<SearchAnimalItem> animalItems = new ArrayList<>();

        for (int i = 0; i < animalNames.length; i++) {
            for (int j = 0; j < job.getAnimals().size(); j++) {
                if(job.getAnimals().get(j).getName().equalsIgnoreCase(animalNames[i])) {
                    SearchAnimalItem item = new SearchAnimalItem();
                    item.setIcon(animalIcons[i]);
                    item.setName(animalNames[i]);
                    animalItems.add(item);
                }
            }
        }
        return animalItems;
    }
}

package com.zekisanmobile.petsitter2.view.summary;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.adapter.SummaryListAdapter;
import com.zekisanmobile.petsitter2.customListener.RecyclerViewOnClickListener;
import com.zekisanmobile.petsitter2.event.summary.UpdateSummariesUIEvent;
import com.zekisanmobile.petsitter2.fragment.SummarySlideShowDialogFragment;
import com.zekisanmobile.petsitter2.model.JobModel;
import com.zekisanmobile.petsitter2.util.Config;
import com.zekisanmobile.petsitter2.util.EntityType;
import com.zekisanmobile.petsitter2.util.GridSpacingItemDecoration;
import com.zekisanmobile.petsitter2.vo.Job;
import com.zekisanmobile.petsitter2.vo.Summary;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class DailySummariesListActivity extends AppCompatActivity {

    private Realm realm;
    private JobModel jobModel;
    private Job job;
    private String jobId;
    private List<Summary> summaryList;
    private SummaryListAdapter adapter;
    private String entityType;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_summaries_list);

        ButterKnife.bind(this);

        this.jobId = getIntent().getStringExtra(Config.JOB_ID);
        this.entityType = getIntent().getStringExtra(EntityType.TYPE);

        configureToolbar();
        defineMembers();
        setupRecyclerView();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        switch (entityType) {
            case EntityType.OWNER:

                break;
            case EntityType.SITTER:
                getMenuInflater().inflate(R.menu.menu_summary_sitter, menu);
                break;
        }
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
            case R.id.menu_add_summary:
                Intent intent = new Intent(DailySummariesListActivity.this, NewSummaryActivity.class);
                intent.putExtra(Config.JOB_ID, jobId);
                intent.putExtra(EntityType.TYPE, entityType);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void configureToolbar() {
        toolbar.setTitle(getString(R.string.daily_summaries));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void defineMembers() {
        realm = Realm.getDefaultInstance();
        jobModel = new JobModel(realm);
        job = jobModel.find(jobId);
        if ((summaryList = job.getSummaries()) == null){
            summaryList = new ArrayList<Summary>();
        }
    }

    private void setupRecyclerView() {
        adapter = new SummaryListAdapter(this, summaryList, new RecyclerViewOnClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle args = new Bundle();
                args.putString(Config.JOB_ID, jobId);
                args.putInt(Config.SELECTED_POSITION, position);

                FragmentTransaction ft = getSupportFragmentManager()
                        .beginTransaction();
                SummarySlideShowDialogFragment fragment =
                        SummarySlideShowDialogFragment.newInstance();
                fragment.setArguments(args);
                fragment.show(ft, "slideshow");
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                r.getDisplayMetrics()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(UpdateSummariesUIEvent event) {
        this.summaryList = jobModel.find(jobId).getSummaries();
        adapter.updateSummaryList(summaryList);
        adapter.notifyDataSetChanged();
    }
}

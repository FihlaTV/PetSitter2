package com.zekisanmobile.petsitter2.view.owner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.adapter.JobListAdapter;
import com.zekisanmobile.petsitter2.customListener.RecyclerViewOnClickListener;
import com.zekisanmobile.petsitter2.model.OwnerModel;
import com.zekisanmobile.petsitter2.model.UserModel;
import com.zekisanmobile.petsitter2.session.SessionManager;
import com.zekisanmobile.petsitter2.util.Config;
import com.zekisanmobile.petsitter2.util.DividerItemDecoration;
import com.zekisanmobile.petsitter2.util.EntityType;
import com.zekisanmobile.petsitter2.util.JobsStatusString;
import com.zekisanmobile.petsitter2.vo.Job;
import com.zekisanmobile.petsitter2.vo.Owner;
import com.zekisanmobile.petsitter2.vo.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class OwnerJobListActivity extends AppCompatActivity {

    private Realm realm;

    private SessionManager sessionManager;
    private Owner owner;
    private OwnerModel ownerModel;
    private User user;
    private UserModel userModel;
    private List<Job> jobList = new ArrayList<>();
    private String jobStatus;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_job_list);

        ButterKnife.bind(this);

        this.jobStatus = getIntent().getStringExtra(Config.JOB_STATUS);

        defineMembers();
        configureToolbar();
        setupRecyclerView();
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void defineMembers() {
        realm = Realm.getDefaultInstance();

        sessionManager = new SessionManager(this);
        userModel = new UserModel(realm);
        ownerModel = new OwnerModel(realm);

        long user_id = sessionManager.getUserId();
        user = userModel.find(user_id);
        owner = ownerModel.find(user.getEntityId());

        setJobList();
    }

    private void setJobList() {
        switch (jobStatus) {
            case JobsStatusString.NEW:
                jobList = ownerModel.getNewJobs(owner.getId());
                break;
            case JobsStatusString.NEXT:
                jobList = ownerModel.getNextJobs(owner.getId());
                break;
            case JobsStatusString.CURRENT:
                jobList = ownerModel.getCurrentJobs(owner.getId());
                break;
            case JobsStatusString.FINISHED:
                jobList = ownerModel.getFinishedJobs(owner.getId());
                break;
        }
    }

    private void configureToolbar() {
        toolbar.setTitle(getToolbarTitle());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private String getToolbarTitle() {
        switch (jobStatus) {
            case JobsStatusString.NEW:
                return getString(R.string.reservation_list_new);
            case JobsStatusString.NEXT:
                return getString(R.string.reservation_list_next);
            case JobsStatusString.CURRENT:
                return getString(R.string.reservation_list_current);
            default:
                return getString(R.string.reservation_list_finished);
        }
    }

    private void setupRecyclerView() {
        JobListAdapter adapter = new JobListAdapter(jobList, this,
                new RecyclerViewOnClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent intent = new Intent(OwnerJobListActivity.this,
                                OwnerJobDetailsActivity.class);
                        intent.putExtra(Config.JOB_ID, jobList.get(position).getId());
                        intent.putExtra(Config.JOB_STATUS, jobStatus);
                        startActivity(intent);
                    }
                }, EntityType.SITTER);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
    }
}

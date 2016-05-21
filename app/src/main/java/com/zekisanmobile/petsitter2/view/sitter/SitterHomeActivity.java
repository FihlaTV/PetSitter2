package com.zekisanmobile.petsitter2.view.sitter;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.birbit.android.jobqueue.JobManager;
import com.zekisanmobile.petsitter2.PetSitterApp;
import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.adapter.ViewPagerAdapter;
import com.zekisanmobile.petsitter2.asyncTask.LogoutTask;
import com.zekisanmobile.petsitter2.event.job.FetchedSitterJobsEvent;
import com.zekisanmobile.petsitter2.fragment.sitter.JobListFragment;
import com.zekisanmobile.petsitter2.job.job.FetchSitterJobsJob;
import com.zekisanmobile.petsitter2.model.SitterModel;
import com.zekisanmobile.petsitter2.model.UserModel;
import com.zekisanmobile.petsitter2.session.SessionManager;
import com.zekisanmobile.petsitter2.util.Config;
import com.zekisanmobile.petsitter2.util.JobsStatusString;
import com.zekisanmobile.petsitter2.view.HomeView;
import com.zekisanmobile.petsitter2.view.login.LoginActivity;
import com.zekisanmobile.petsitter2.vo.Job;
import com.zekisanmobile.petsitter2.vo.Sitter;
import com.zekisanmobile.petsitter2.vo.User;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class SitterHomeActivity extends AppCompatActivity implements HomeView {

    private Realm realm;
    private User user;
    private UserModel userModel;
    private Sitter sitter;
    private SitterModel sitterModel;
    private SessionManager sessionManager;

    private JobListFragment newJobsFragments;
    private JobListFragment nextJobsFragments;
    private JobListFragment currentJobsFragments;
    private JobListFragment finishedJobsFragments;

    @Inject
    JobManager jobManager;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitter_home);

        ((PetSitterApp) getApplication()).getAppComponent().inject(this);

        ButterKnife.bind(this);

        defineMembers();
        setupToolbar();
        setupViewPager();
        setupTabLayout();
        startJobs();
    }

    @Override
    protected void onStart(){
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
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
        getMenuInflater().inflate(R.menu.menu_main_sitter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.m_rates:
                Intent intent = new Intent(SitterHomeActivity.this, SitterRatesActivity.class);
                intent.putExtra(Config.SITTER_ID, sitter.getId());
                startActivity(intent);
                break;
            case R.id.m_logout:
                new LogoutTask(this, user.getId()).execute();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Application getPetSitterApp() {
        return getApplication();
    }

    @Override
    public Context getViewContext() {
        return this;
    }

    @Override
    public void redirectToLoginPage() {
        clearSession();
        Intent intent = new Intent(SitterHomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void clearSession() {
        sessionManager.cleanAllEntries();
    }

    private void defineMembers() {
        realm = Realm.getDefaultInstance();
        userModel = new UserModel(realm);
        sitterModel = new SitterModel(realm);
        sessionManager = new SessionManager(this);
        String user_id = sessionManager.getUserId();
        user = userModel.find(user_id);
        sitter = sitterModel.find(user.getEntityId());

        newJobsFragments = JobListFragment.newInstance(sitter.getId(), JobsStatusString.NEW);
        nextJobsFragments = JobListFragment.newInstance(sitter.getId(), JobsStatusString.NEXT);
        currentJobsFragments = JobListFragment.newInstance(sitter.getId(), JobsStatusString.CURRENT);
        finishedJobsFragments = JobListFragment.newInstance(sitter.getId(), JobsStatusString.FINISHED);
    }

    private void setupToolbar() {
        toolbar.setTitle(sitter.getName());
        setSupportActionBar(toolbar);
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(newJobsFragments, getString(R.string.tabs_new_jobs));
        adapter.addFragment(nextJobsFragments, getString(R.string.tabs_next_jobs));
        adapter.addFragment(currentJobsFragments, getString(R.string.tabs_current_jobs));
        adapter.addFragment(finishedJobsFragments, getString(R.string.tabs_finished_jobs));
        viewPager.setAdapter(adapter);
    }

    private void setupTabLayout() {
        tabLayout.setupWithViewPager(viewPager);
    }

    private void startJobs() {
        jobManager.addJobInBackground(new FetchSitterJobsJob(sitter.getId()));
    }

    private void updateAdapters(List<Job> newJobs,
                                List<Job> nextJobs,
                                List<Job> currentJobs,
                                List<Job> finishedJobs) {

        switch (tabLayout.getSelectedTabPosition()) {
            case 0:
                newJobsFragments.updateJobs(newJobs);
                break;
            case 1:
                nextJobsFragments.updateJobs(nextJobs);
                break;
            case 2:
                currentJobsFragments.updateJobs(currentJobs);
                break;
            case 3:
                finishedJobsFragments.updateJobs(finishedJobs);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(FetchedSitterJobsEvent event) {
        if (event.isSuccess()) {
            updateAdapters(sitterModel.getNewJobs(event.getSitterId()),
                    sitterModel.getNextJobs(event.getSitterId()),
                    sitterModel.getCurrentJobs(event.getSitterId()),
                    sitterModel.getFinishedJobs(event.getSitterId()));
        }
    }
}

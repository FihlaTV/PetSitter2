package com.zekisanmobile.petsitter2.view.owner;

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
import com.zekisanmobile.petsitter2.fragment.owner.AnimalFilterFragment;
import com.zekisanmobile.petsitter2.fragment.owner.ReservationsFragment;
import com.zekisanmobile.petsitter2.job.job.FetchOwnerJobsJob;
import com.zekisanmobile.petsitter2.model.OwnerModel;
import com.zekisanmobile.petsitter2.model.UserModel;
import com.zekisanmobile.petsitter2.session.SessionManager;
import com.zekisanmobile.petsitter2.util.Config;
import com.zekisanmobile.petsitter2.view.HomeView;
import com.zekisanmobile.petsitter2.view.login.LoginActivity;
import com.zekisanmobile.petsitter2.vo.Owner;
import com.zekisanmobile.petsitter2.vo.User;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class OwnerHomeActivity extends AppCompatActivity implements HomeView {

    private Realm realm;
    private User user;
    private UserModel userModel;
    private Owner owner;
    private OwnerModel ownerModel;
    private SessionManager sessionManager;

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
        setContentView(R.layout.activity_owner_home);

        ((PetSitterApp) getApplication()).getAppComponent().inject(this);

        ButterKnife.bind(this);

        defineMembers();
        getJobs();
        setupToolbar();
        setupViewPager();
        setupTabLayout();
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_owner, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.m_logout:
                new LogoutTask(this, user.getId()).execute();
                break;
            case R.id.m_my_pets:
                Intent intent = new Intent(OwnerHomeActivity.this, MyPetsActivity.class);
                intent.putExtra(Config.OWNER_ID, owner.getId());
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void defineMembers() {
        realm = Realm.getDefaultInstance();
        userModel = new UserModel(realm);
        ownerModel = new OwnerModel(realm);
        sessionManager = new SessionManager(this);

        String user_id = sessionManager.getUserId();
        user = userModel.find(user_id);
        owner = ownerModel.find(user.getEntityId());
    }

    private void getJobs() {
        jobManager.addJobInBackground(new FetchOwnerJobsJob(owner.getId()));
    }

    private void setupToolbar() {
        toolbar.setTitle(owner.getName() + " " + owner.getSurname());
        setSupportActionBar(toolbar);
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AnimalFilterFragment(), getString(R.string.tabs_pet_sitters));
        adapter.addFragment(new ReservationsFragment(), getString(R.string.tabs_reservations));
        viewPager.setAdapter(adapter);
    }

    private void setupTabLayout() {
        tabLayout.setupWithViewPager(viewPager);
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
        Intent intent = new Intent(OwnerHomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void clearSession() {
        sessionManager.cleanAllEntries();
    }
}

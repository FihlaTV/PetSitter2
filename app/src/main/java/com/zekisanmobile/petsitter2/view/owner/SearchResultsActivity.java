package com.zekisanmobile.petsitter2.view.owner;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.asyncTask.SearchSittersTask;
import com.zekisanmobile.petsitter2.event.UpdateSittersEvent;
import com.zekisanmobile.petsitter2.model.OwnerModel;
import com.zekisanmobile.petsitter2.model.UserModel;
import com.zekisanmobile.petsitter2.session.SessionManager;
import com.zekisanmobile.petsitter2.vo.Owner;
import com.zekisanmobile.petsitter2.vo.User;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class SearchResultsActivity extends AppCompatActivity implements SearchResultsView {

    private Realm realm;

    private SessionManager sessionManager;
    private Owner owner;
    private OwnerModel ownerModel;
    private User user;
    private UserModel userModel;
    private ArrayList<String> animals = new ArrayList<>();

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        ButterKnife.bind(this);

        animals = getIntent().getExtras().getStringArrayList("selected_animals");

        defineMembers();
        configureToolbar();
        startSearch();
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
    public Application getPetSitterApp() {
        return getApplication();
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(UpdateSittersEvent event) {

    }

    private void defineMembers() {
        realm = Realm.getDefaultInstance();

        sessionManager = new SessionManager(this);
        userModel = new UserModel(realm);
        ownerModel = new OwnerModel(realm);

        long user_id = sessionManager.getUserId();
        user = userModel.find(user_id);
        owner = ownerModel.find(user.getEntityId());
    }

    private void configureToolbar() {
        toolbar.setTitle(getString(R.string.search_result_title));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void startSearch() {
        new SearchSittersTask(this, owner.getId(), animals).execute();
    }
}

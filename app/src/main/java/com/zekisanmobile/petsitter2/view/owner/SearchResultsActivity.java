package com.zekisanmobile.petsitter2.view.owner;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.adapter.SitterListAdapter;
import com.zekisanmobile.petsitter2.asyncTask.SearchSittersTask;
import com.zekisanmobile.petsitter2.customListener.RecyclerViewOnClickListener;
import com.zekisanmobile.petsitter2.event.UpdateSittersEvent;
import com.zekisanmobile.petsitter2.model.OwnerModel;
import com.zekisanmobile.petsitter2.model.UserModel;
import com.zekisanmobile.petsitter2.session.SessionManager;
import com.zekisanmobile.petsitter2.util.DividerItemDecoration;
import com.zekisanmobile.petsitter2.vo.Owner;
import com.zekisanmobile.petsitter2.vo.Sitter;
import com.zekisanmobile.petsitter2.vo.User;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

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
    private SitterListAdapter adapter;
    private ArrayList<String> animals = new ArrayList<>();
    private List<Sitter> sitters = new ArrayList<>();

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        ButterKnife.bind(this);

        animals = getIntent().getExtras().getStringArrayList("selected_animals");

        defineMembers();
        configureToolbar();
        setupRecyclerView();
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
        if (event.getSitters() != null && !event.getSitters().isEmpty()) {
            this.sitters = event.getSitters();
            adapter.setSittersList(event.getSitters());
            adapter.notifyDataSetChanged();
        }
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

    private void setupRecyclerView() {
        adapter = new SitterListAdapter(new ArrayList<Sitter>(), getContext(),
                new RecyclerViewOnClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(SearchResultsActivity.this, SitterProfileActivity.class);
                intent.putExtra("sitter_id", sitters.get(position).getId());
                startActivity(intent);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
    }

    private void startSearch() {
        new SearchSittersTask(this, owner.getId(), animals).execute();
    }
}
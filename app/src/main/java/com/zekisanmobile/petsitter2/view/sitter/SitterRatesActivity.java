package com.zekisanmobile.petsitter2.view.sitter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.adapter.OwnerRateListAdapter;
import com.zekisanmobile.petsitter2.model.SitterModel;
import com.zekisanmobile.petsitter2.util.Config;
import com.zekisanmobile.petsitter2.util.DividerItemDecoration;
import com.zekisanmobile.petsitter2.vo.Job;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class SitterRatesActivity extends AppCompatActivity {

    private Realm realm;
    private long sitter_id;
    private SitterModel sitterModel;
    private List<Job> jobsWithRate;
    private OwnerRateListAdapter adapter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitter_rates);

        ButterKnife.bind(this);

        this.sitter_id = getIntent().getLongExtra(Config.SITTER_ID, 0);

        defineMembers();
        configureToolbar();
        setupRecyclerView();
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    private void defineMembers() {
        realm = Realm.getDefaultInstance();

        sitterModel = new SitterModel(realm);
        jobsWithRate = sitterModel.getJobsWithRates(sitter_id);
    }

    private void configureToolbar() {
        toolbar.setTitle(getString(R.string.my_rates));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void setupRecyclerView() {
        adapter = new OwnerRateListAdapter(jobsWithRate, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
    }
}

package com.zekisanmobile.petsitter2.view.owner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.adapter.PetListAdapter;
import com.zekisanmobile.petsitter2.model.OwnerModel;
import com.zekisanmobile.petsitter2.util.Config;
import com.zekisanmobile.petsitter2.util.DividerItemDecoration;
import com.zekisanmobile.petsitter2.vo.Owner;
import com.zekisanmobile.petsitter2.vo.Pet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class MyPetsActivity extends AppCompatActivity {

    private Realm realm;
    private Owner owner;
    private OwnerModel ownerModel;
    private String ownerId;
    private List<Pet> petList;
    private PetListAdapter adapter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pets);

        ButterKnife.bind(this);

        this.ownerId = getIntent().getStringExtra(Config.OWNER_ID);

        configureToolbar();
        defineMembers();
        setupRecyclerView();
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    @OnClick(R.id.fab)
    public void addPet() {
        Intent intent = new Intent(MyPetsActivity.this, AddNewPetActivity.class);
        intent.putExtra(Config.OWNER_ID, ownerId);
        startActivity(intent);
    }

    private void defineMembers() {
        realm = Realm.getDefaultInstance();

        ownerModel = new OwnerModel(realm);
        owner = ownerModel.find(ownerId);
        if ((petList = owner.getPets()) == null) {
            petList = new ArrayList<>();
        }
    }

    private void configureToolbar() {
        toolbar.setTitle(getString(R.string.action_my_pets));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void setupRecyclerView() {
        adapter = new PetListAdapter(this, petList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
    }
}

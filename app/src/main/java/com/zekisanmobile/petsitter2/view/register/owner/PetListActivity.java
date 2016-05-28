package com.zekisanmobile.petsitter2.view.register.owner;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.adapter.PetListAdapter;
import com.zekisanmobile.petsitter2.asyncTask.CreateOwnerTask;
import com.zekisanmobile.petsitter2.model.OwnerModel;
import com.zekisanmobile.petsitter2.util.Config;
import com.zekisanmobile.petsitter2.util.DividerItemDecoration;
import com.zekisanmobile.petsitter2.view.register.RegisterView;
import com.zekisanmobile.petsitter2.vo.Owner;
import com.zekisanmobile.petsitter2.vo.Pet;
import com.zekisanmobile.petsitter2.vo.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class PetListActivity extends AppCompatActivity implements RegisterView {

    private Realm realm;
    private PetListAdapter adapter;
    private List<Pet> petList;
    private OwnerModel ownerModel;
    private Owner owner;
    private User user;
    private String ownerId;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_list);

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

    @OnClick(R.id.btn_add_pet)
    public void addPet() {
        Intent intent = new Intent(PetListActivity.this, PetRegisterActivity.class);
        intent.putExtra(Config.OWNER_ID, ownerId);
        startActivity(intent);
    }

    @OnClick(R.id.btn_next)
    public void next() {
        new CreateOwnerTask(ownerId, user.getId(), this).execute();
    }

    private void defineMembers() {
        realm = Realm.getDefaultInstance();
        ownerModel = new OwnerModel(realm);
        owner = ownerModel.find(ownerId);
        user = realm.where(User.class).equalTo("entityId", ownerId).findFirst();
        if ((petList = owner.getPets()) == null){
            petList = new ArrayList<Pet>();
        }
    }

    private void configureToolbar() {
        toolbar.setTitle(getString(R.string.your_pets));
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

    @Override
    public Application getPetSitterApp() {
        return getApplication();
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }
}

package com.zekisanmobile.petsitter2.view.register.sitter;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.EditText;

import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.adapter.SearchAnimalAdapter;
import com.zekisanmobile.petsitter2.model.SitterModel;
import com.zekisanmobile.petsitter2.util.Config;
import com.zekisanmobile.petsitter2.util.DividerItemDecoration;
import com.zekisanmobile.petsitter2.vo.Animal;
import com.zekisanmobile.petsitter2.vo.SearchAnimalItem;
import com.zekisanmobile.petsitter2.vo.Sitter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class AnimalListActivity extends AppCompatActivity {

    private Realm realm;
    private SitterModel sitterModel;
    private String sitterId;

    private SearchAnimalAdapter adapter;
    private List<SearchAnimalItem> animalItems;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.et_value_hour)
    EditText etValueHour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_list);

        ButterKnife.bind(this);

        this.sitterId = getIntent().getStringExtra(Config.SITTER_ID);

        configureToolbar();
        defineMembers();
        prepareAnimalItemsList();
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
    }

    @OnClick(R.id.btn_next)
    public void saveAnimals() {
        ArrayList<String> selectedAnimals = adapter.getSelectedAnimals();
        if (selectedAnimals == null || selectedAnimals.isEmpty() ) {
            showNoAnimalsSelectedDialog(getString(R.string.registry_no_animals_selected));
        } else {
            saveSitter(selectedAnimals);
            redirectToAboutMeRegister();
        }
    }

    private void redirectToAboutMeRegister() {

    }

    private void saveSitter(ArrayList<String> selectedAnimals) {
        List<Animal> animalsToSave = new ArrayList<>();
        for (String a : selectedAnimals) {
            Animal animal = realm.where(Animal.class).equalTo("name", a).findFirst();
            animalsToSave.add(animal);
        }
        realm.beginTransaction();
        Sitter sitter = sitterModel.find(sitterId);
        sitter.getAnimals().addAll(animalsToSave);
        sitter.setValueHour(Float.parseFloat(etValueHour.getText().toString().trim().replace(",", ".")));
        realm.commitTransaction();
    }

    private void configureToolbar() {
        toolbar.setTitle(getString(R.string.which_pets_do_you_care));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void prepareAnimalItemsList() {
        String[] animalIcons = this.getResources().getStringArray(R.array.animal_icons);
        String[] animalNames = this.getResources().getStringArray(R.array.animal_names);
        animalItems = new ArrayList<>();

        for (int i = 0; i < animalNames.length; i++) {
            SearchAnimalItem item = new SearchAnimalItem();
            item.setIcon(animalIcons[i]);
            item.setName(animalNames[i]);
            animalItems.add(item);
        }
    }

    private void setupRecyclerView() {
        adapter = new SearchAnimalAdapter(animalItems);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
    }

    public void showNoAnimalsSelectedDialog(String message) {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        dialog.show();
        keepDialog(dialog);
    }

    private void keepDialog(Dialog dialog) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(layoutParams);
    }
}

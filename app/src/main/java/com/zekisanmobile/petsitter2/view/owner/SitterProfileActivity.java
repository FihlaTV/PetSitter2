package com.zekisanmobile.petsitter2.view.owner;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.adapter.AnimalListAdapter;
import com.zekisanmobile.petsitter2.adapter.PhotoListAdapter;
import com.zekisanmobile.petsitter2.customListener.RecyclerViewOnClickListener;
import com.zekisanmobile.petsitter2.fragment.SlideshowDialogFragment;
import com.zekisanmobile.petsitter2.model.SitterModel;
import com.zekisanmobile.petsitter2.util.Config;
import com.zekisanmobile.petsitter2.util.Extra;
import com.zekisanmobile.petsitter2.vo.PhotoUrl;
import com.zekisanmobile.petsitter2.vo.SearchAnimalItem;
import com.zekisanmobile.petsitter2.vo.Sitter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class SitterProfileActivity extends AppCompatActivity {

    private Realm realm;

    private Sitter sitter;
    private SitterModel sitterModel;

    private long sitter_id;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.iv_sitter)
    ImageView ivSitter;

    @BindView(R.id.tv_value_hour)
    TextView tvValueHour;

    @BindView(R.id.tv_district)
    TextView tvDistrict;

    @BindView(R.id.tv_about_me)
    TextView tvAboutMe;

    @BindView(R.id.recycler_view_animals)
    RecyclerView recyclerViewAnimals;

    @BindView(R.id.btn_call)
    Button btnCall;

    @BindView(R.id.recycler_view_photos)
    RecyclerView recyclerViewPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitter_profile);

        ButterKnife.bind(this);

        sitter_id = getIntent().getLongExtra("sitter_id", 0);

        defineMembers();
        configureToolbar();
        defineViewsValues();
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    @OnClick(R.id.fab)
    public void requestJob() {
        Intent intent = new Intent(SitterProfileActivity.this, NewJobRequestActivity.class);
        intent.putExtra(Extra.SITTER_ID, sitter.getId());
        startActivity(intent);
    }

    @OnClick(R.id.btn_call)
    public void call() {
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(sitter.getPhone()));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.d("DIAL FAILED: ", e.getMessage());
        }
    }

    private void defineViewsValues() {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        tvValueHour.setText(format.format(sitter.getValueHour()) + "/h");
        tvDistrict.setText(sitter.getDistrict());
        tvAboutMe.setText(sitter.getAboutMe());
        List<SearchAnimalItem> animalItems = populateAnimalsRecyclerView();
        setupAnimalsRecyclerView(animalItems);
        setupPhotosRecyclerView(sitter.getProfilePhotos());
        btnCall.setText(sitter.getPhone());
    }

    private List<SearchAnimalItem> populateAnimalsRecyclerView() {
        String[] animalIcons = this.getResources().getStringArray(R.array.animal_icons);
        String[] animalNames = this.getResources().getStringArray(R.array.animal_names);
        List<SearchAnimalItem> animalItems = new ArrayList<>();

        for (int i = 0; i < animalNames.length; i++) {
            for (int j = 0; j < sitter.getAnimals().size(); j++) {
                if (sitter.getAnimals().get(j).getName().equalsIgnoreCase(animalNames[i])) {
                    SearchAnimalItem item = new SearchAnimalItem();
                    item.setIcon(animalIcons[i]);
                    item.setName(animalNames[i]);
                    animalItems.add(item);
                }
            }
        }
        return animalItems;
    }

    private void defineMembers() {
        realm = Realm.getDefaultInstance();

        sitterModel = new SitterModel(realm);
        sitter = sitterModel.find(sitter_id);
    }

    private void configureToolbar() {
        toolbar.setTitle(sitter.getName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Picasso.with(this)
                .load(sitter.getPhotoUrl().getLarge())
                .into(ivSitter);
    }

    public void setupAnimalsRecyclerView(List<SearchAnimalItem> animalItems) {
        AnimalListAdapter adapter = new AnimalListAdapter(animalItems);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewAnimals.setLayoutManager(linearLayoutManager);
        recyclerViewAnimals.setAdapter(adapter);
    }

    private void setupPhotosRecyclerView(List<PhotoUrl> profilePhotos) {
        PhotoListAdapter adapter = new PhotoListAdapter(profilePhotos, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewPhotos.setLayoutManager(linearLayoutManager);
        recyclerViewPhotos.setAdapter(adapter);
        recyclerViewPhotos.addOnItemTouchListener(new PhotoListAdapter
                .RecyclerTouchListener(getApplicationContext(), recyclerViewPhotos,
                new PhotoListAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle args = new Bundle();
                args.putLong(Config.SITTER_ID, sitter.getId());
                args.putInt(Config.SELECTED_POSITION, position);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                newFragment.setArguments(args);
                newFragment.show(ft, "slideshow");
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }
}

package com.zekisanmobile.petsitter2.view.sitter;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.adapter.AnimalListAdapter;
import com.zekisanmobile.petsitter2.model.JobModel;
import com.zekisanmobile.petsitter2.util.CircleTransform;
import com.zekisanmobile.petsitter2.util.Config;
import com.zekisanmobile.petsitter2.util.DateFormatter;
import com.zekisanmobile.petsitter2.util.JobsStatusString;
import com.zekisanmobile.petsitter2.vo.Job;
import com.zekisanmobile.petsitter2.vo.SearchAnimalItem;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class JobDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Realm realm;

    private String jobStatus;
    private long jobId;
    private Job job;
    private JobModel jobModel;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.iv_owner)
    ImageView ivOwner;

    @BindView(R.id.tv_owner)
    TextView tvOwner;

    @BindView(R.id.tv_address)
    TextView tvAddress;

    @BindView(R.id.tv_period)
    TextView tvPeriod;

    @BindView(R.id.tv_time)
    TextView tvTime;

    @BindView(R.id.tv_total_value)
    TextView tvTotalValue;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);

        ButterKnife.bind(this);

        jobId = getIntent().getLongExtra(Config.JOB_ID, 0);
        jobStatus = getIntent().getStringExtra(Config.JOB_STATUS);

        defineMembers();
        configureToolbar();
        configureMap();
        setupViews();
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = new LatLng(job.getOwner().getLatitude(),
                job.getOwner().getLongitude());
        googleMap.addMarker(new MarkerOptions().position(latLng));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
        googleMap.animateCamera(cameraUpdate);
    }

    private void defineMembers() {
        realm = Realm.getDefaultInstance();

        jobModel = new JobModel(realm);
        job = jobModel.find(jobId);
    }

    private void configureToolbar() {
        toolbar.setTitle(getTitleForToolbar());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @NonNull
    private String getTitleForToolbar() {
        switch (jobStatus) {
            case JobsStatusString.NEW:
                return getString(R.string.new_job);
            case JobsStatusString.NEXT:
                return getString(R.string.next_job);
            case JobsStatusString.CURRENT:
                return getString(R.string.current_job);
            default:
                return getString(R.string.finished_job);
        }
    }

    private void configureMap() {
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void setupViews() {
        tvOwner.setText(job.getOwner().getName());
        Picasso.with(this)
                .load(job.getOwner().getPhotoUrl().getLarge())
                .transform(new CircleTransform())
                .into(ivOwner);
        tvAddress.setText(job.getOwner().getAddress());
        tvPeriod.setText(DateFormatter.formattedDatePeriodForView(job.getDateStart(),
                job.getDateFinal()));
        tvTime.setText(DateFormatter.formattedTimePeriodForView(job.getTimeStart(),
                job.getTimeFinal()));
        tvTotalValue.setText(NumberFormat.getCurrencyInstance().format(job.getTotalValue()));
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        List<SearchAnimalItem> animalItems = populateAnimalsRecyclerView();
        AnimalListAdapter adapter = new AnimalListAdapter(animalItems);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private List<SearchAnimalItem> populateAnimalsRecyclerView() {
        String[] animalIcons = this.getResources().getStringArray(R.array.animal_icons);
        String[] animalNames = this.getResources().getStringArray(R.array.animal_names);
        List<SearchAnimalItem> animalItems = new ArrayList<>();

        for (int i = 0; i < animalNames.length; i++) {
            for (int j = 0; j < job.getAnimals().size(); j++) {
                if(job.getAnimals().get(j).getName().equalsIgnoreCase(animalNames[i])) {
                    SearchAnimalItem item = new SearchAnimalItem();
                    item.setIcon(animalIcons[i]);
                    item.setName(animalNames[i]);
                    animalItems.add(item);
                }
            }
        }
        return animalItems;
    }
}

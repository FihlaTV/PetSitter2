package com.zekisanmobile.petsitter2.view.sitter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.birbit.android.jobqueue.JobManager;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.zekisanmobile.petsitter2.PetSitterApp;
import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.adapter.AnimalListAdapter;
import com.zekisanmobile.petsitter2.job.job.SendJobStatusJob;
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

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class SitterJobDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Realm realm;

    private String jobStatus;
    private String jobId;
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

    @Inject
    JobManager jobManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitter_job_details);

        ((PetSitterApp) getApplication()).getAppComponent().inject(this);

        ButterKnife.bind(this);

        jobId = getIntent().getStringExtra(Config.JOB_ID);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        switch (jobStatus) {
            case JobsStatusString.NEW:
                getMenuInflater().inflate(R.menu.menu_job_details, menu);
            case JobsStatusString.NEXT:
                break;
            case JobsStatusString.CURRENT:
                getMenuInflater().inflate(R.menu.menu_summaries, menu);
                break;
            default:
                getMenuInflater().inflate(R.menu.menu_job_rate, menu);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_reject:
                showRejectDialog();
                break;
            case R.id.menu_accept:
                showAcceptDialog();
                break;
            case R.id.menu_rate:
                break;
            case R.id.menu_summary:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAcceptDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(getString(R.string.job_accepted_message) + " "
                        + DateFormatter.formattedDateForView(job.getDateStart()) + ".")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateJobStatus(30);
                        redirectToSitterHome();
                    }
                }).create();
        dialog.show();
        keepDialog(dialog);
    }

    private void showRejectDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(R.string.job_rejected_message)
                .setPositiveButton(R.string.reject, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateJobStatus(20);
                        redirectToSitterHome();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
        dialog.show();
        keepDialog(dialog);
    }

    private void updateJobStatus(int status) {
        jobModel.updateStatus(realm, job.getId(), status);
        jobManager.addJobInBackground(new SendJobStatusJob(status, job.getId()));
    }

    private void keepDialog(Dialog dialog){
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(layoutParams);
    }

    private void redirectToSitterHome() {
        Intent intent = new Intent(SitterJobDetailsActivity.this, SitterHomeActivity.class);
        startActivity(intent);
        finish();
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
        tvAddress.setText(job.getOwner().getDistrict());
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
package com.zekisanmobile.petsitter2.fragment.owner;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.adapter.SearchAnimalListAdapter;
import com.zekisanmobile.petsitter2.adapter.PhotoListAdapter;
import com.zekisanmobile.petsitter2.fragment.SlideshowDialogFragment;
import com.zekisanmobile.petsitter2.model.SitterModel;
import com.zekisanmobile.petsitter2.util.Config;
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

public class SitterInfoFragment extends Fragment implements OnMapReadyCallback {

    private Realm realm;
    private Sitter sitter;
    private SitterModel sitterModel;
    private String sitter_id;

    @BindView(R.id.tv_value_hour)
    TextView tvValueHour;

    @BindView(R.id.tv_location)
    TextView tvLocation;

    @BindView(R.id.tv_about_me)
    TextView tvAboutMe;

    @BindView(R.id.recycler_view_animals)
    RecyclerView recyclerViewAnimals;

    @BindView(R.id.btn_call)
    Button btnCall;

    @BindView(R.id.recycler_view_photos)
    RecyclerView recyclerViewPhotos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sitter_info, container, false);
        ButterKnife.bind(this, view);

        this.sitter_id = getArguments().getString(Config.SITTER_ID);

        defineMembers();
        defineViewsValues();
        configureMap();

        return view;
    }

    @Override
    public void onDestroyView() {
        realm.close();
        super.onDestroyView();
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

    public static SitterInfoFragment newInstance(String sitter_id) {
        SitterInfoFragment fragment = new SitterInfoFragment();
        Bundle args = new Bundle();
        args.putString(Config.SITTER_ID, sitter_id);
        fragment.setArguments(args);
        return fragment;
    }

    private void defineMembers() {
        realm = Realm.getDefaultInstance();

        sitterModel = new SitterModel(realm);
        sitter = sitterModel.find(sitter_id);
    }

    private void defineViewsValues() {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        tvValueHour.setText(format.format(sitter.getValueHour()) + "/h");
        tvLocation.setText(sitter.getDistrict() + " - " + sitter.getCity() + " / " + sitter.getState());
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
    public void setupAnimalsRecyclerView(List<SearchAnimalItem> animalItems) {
        SearchAnimalListAdapter adapter = new SearchAnimalListAdapter(animalItems);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewAnimals.setLayoutManager(linearLayoutManager);
        recyclerViewAnimals.setAdapter(adapter);
    }

    private void setupPhotosRecyclerView(List<PhotoUrl> profilePhotos) {
        PhotoListAdapter adapter = new PhotoListAdapter(profilePhotos, getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewPhotos.setLayoutManager(linearLayoutManager);
        recyclerViewPhotos.setAdapter(adapter);
        recyclerViewPhotos.addOnItemTouchListener(new PhotoListAdapter
                .RecyclerTouchListener(getActivity().getApplicationContext(), recyclerViewPhotos,
                new PhotoListAdapter.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Bundle args = new Bundle();
                        args.putString(Config.SITTER_ID, sitter.getId());
                        args.putInt(Config.SELECTED_POSITION, position);

                        FragmentTransaction ft = getActivity().getSupportFragmentManager()
                                .beginTransaction();
                        SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                        newFragment.setArguments(args);
                        newFragment.show(ft, "slideshow");
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = new LatLng(sitter.getLatitude(), sitter.getLongitude());
        double radiusInMeters = 200.0;
        int strokeColor = 0xffff0000;
        int shadeColor = 0x44ff0000;
        CircleOptions circleOptions = new CircleOptions().center(latLng).radius(radiusInMeters)
                .fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(2);
        googleMap.addCircle(circleOptions);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
        googleMap.animateCamera(cameraUpdate);
    }

    private void configureMap() {
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
}

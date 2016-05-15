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

import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.adapter.AnimalListAdapter;
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

public class SitterInfoFragment extends Fragment {

    private Realm realm;
    private Sitter sitter;
    private SitterModel sitterModel;
    private long sitter_id;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sitter_info, container, false);
        ButterKnife.bind(this, view);

        this.sitter_id = getArguments().getLong(Config.SITTER_ID, 0);

        defineMembers();
        defineViewsValues();

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

    public static SitterInfoFragment newInstance(long sitter_id) {
        SitterInfoFragment fragment = new SitterInfoFragment();
        Bundle args = new Bundle();
        args.putLong(Config.SITTER_ID, sitter_id);
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
    public void setupAnimalsRecyclerView(List<SearchAnimalItem> animalItems) {
        AnimalListAdapter adapter = new AnimalListAdapter(animalItems);
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
                        args.putLong(Config.SITTER_ID, sitter.getId());
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
}

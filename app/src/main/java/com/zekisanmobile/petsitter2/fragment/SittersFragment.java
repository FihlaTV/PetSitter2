package com.zekisanmobile.petsitter2.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.adapter.AnimalAdapter;
import com.zekisanmobile.petsitter2.util.DividerItemDecoration;
import com.zekisanmobile.petsitter2.vo.SearchAnimalItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SittersFragment extends Fragment {

    private AnimalAdapter adapter;
    private List<SearchAnimalItem> animalItems;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    public SittersFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sitters, container, false);
        ButterKnife.bind(this, view);

        prepareAnimalItemsList();
        setupRecyclerView();

        return view;
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
        adapter = new AnimalAdapter(animalItems);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(),
                LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
    }
}

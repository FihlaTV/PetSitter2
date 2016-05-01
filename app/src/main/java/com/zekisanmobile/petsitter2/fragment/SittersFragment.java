package com.zekisanmobile.petsitter2.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.adapter.AnimalAdapter;
import com.zekisanmobile.petsitter2.util.DividerItemDecoration;
import com.zekisanmobile.petsitter2.vo.SearchAnimalItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @OnClick(R.id.btn_search)
    public void search() {
        String[] selectedAnimals = adapter.getSelectedAnimals();
        if (selectedAnimals.length == 0) {
            showNoAnimalsSelectedDialog(this.getContext()
                    .getString(R.string.search_no_animals_selected));
        } else {

        }
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

    public void showNoAnimalsSelectedDialog(String message) {
        final AlertDialog dialog = new AlertDialog.Builder(this.getContext())
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

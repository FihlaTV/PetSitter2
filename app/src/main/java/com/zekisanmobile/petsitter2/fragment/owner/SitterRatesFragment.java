package com.zekisanmobile.petsitter2.fragment.owner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.adapter.OwnerRateListAdapter;
import com.zekisanmobile.petsitter2.model.SitterModel;
import com.zekisanmobile.petsitter2.util.Config;
import com.zekisanmobile.petsitter2.util.DividerItemDecoration;
import com.zekisanmobile.petsitter2.vo.Job;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class SitterRatesFragment extends Fragment{

    private Realm realm;
    private long sitter_id;
    private SitterModel sitterModel;
    private List<Job> jobsWithRate;
    private OwnerRateListAdapter adapter;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sitter_rates, container, false);
        ButterKnife.bind(this, view);

        this.sitter_id = getArguments().getLong(Config.SITTER_ID, 0);

        defineMembers();
        setupRecyclerView();

        return view;
    }

    @Override
    public void onDestroyView() {
        realm.close();
        super.onDestroyView();
    }

    private void defineMembers() {
        realm = Realm.getDefaultInstance();

        sitterModel = new SitterModel(realm);
        jobsWithRate = sitterModel.getJobsWithRates(sitter_id);
    }

    public static SitterRatesFragment newInstance(long sitter_id) {
        SitterRatesFragment fragment = new SitterRatesFragment();
        Bundle args = new Bundle();
        args.putLong(Config.SITTER_ID, sitter_id);
        fragment.setArguments(args);
        return fragment;
    }

    private void setupRecyclerView() {
        adapter = new OwnerRateListAdapter(jobsWithRate, this.getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(),
                LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
    }
}

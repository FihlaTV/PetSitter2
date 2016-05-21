package com.zekisanmobile.petsitter2.fragment.sitter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.adapter.JobListAdapter;
import com.zekisanmobile.petsitter2.customListener.RecyclerViewOnClickListener;
import com.zekisanmobile.petsitter2.model.SitterModel;
import com.zekisanmobile.petsitter2.util.Config;
import com.zekisanmobile.petsitter2.util.DividerItemDecoration;
import com.zekisanmobile.petsitter2.util.EntityType;
import com.zekisanmobile.petsitter2.util.JobsStatusString;
import com.zekisanmobile.petsitter2.view.sitter.SitterJobDetailsActivity;
import com.zekisanmobile.petsitter2.vo.Job;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class JobListFragment extends Fragment {

    private JobListAdapter adapter;
    private List<Job> jobList = new ArrayList<>();
    private String sitter_id;
    private String jobStatus;
    private Realm realm;
    private SitterModel sitterModel;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jobs, container, false);
        ButterKnife.bind(this, view);

        this.sitter_id = getArguments().getString(Config.SITTER_ID);
        this.jobStatus = getArguments().getString(Config.JOB_STATUS);

        realm = Realm.getDefaultInstance();
        sitterModel = new SitterModel(realm);

        getJobListFromDB();
        setupRecyclerView();

        return view;
    }

    @Override
    public void onDestroyView() {
        realm.close();
        super.onDestroyView();
    }

    public static JobListFragment newInstance(String sitter_id, String jobStatus) {
        JobListFragment fragment = new JobListFragment();
        Bundle args = new Bundle();
        args.putString(Config.SITTER_ID, sitter_id);
        args.putString(Config.JOB_STATUS, jobStatus);
        fragment.setArguments(args);
        return fragment;
    }

    private void setupRecyclerView() {
        adapter = new JobListAdapter(jobList, this.getActivity(),
                new RecyclerViewOnClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Job job = jobList.get(position);
                        Intent intent = new Intent(getActivity(), SitterJobDetailsActivity.class);
                        intent.putExtra(Config.JOB_ID, job.getId());
                        intent.putExtra(Config.JOB_STATUS, jobStatus);
                        startActivity(intent);
                    }
                }, EntityType.OWNER);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(),
                LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
    }

    public void updateJobs(List<Job> jobList) {
        this.jobList = jobList;
        adapter.setJobList(jobList);
    }

    public void getJobListFromDB() {
        switch (jobStatus) {
            case JobsStatusString.NEW:
                jobList = sitterModel.getNewJobs(sitter_id);
                break;
            case JobsStatusString.NEXT:
                jobList = sitterModel.getNextJobs(sitter_id);
                break;
            case JobsStatusString.CURRENT:
                jobList = sitterModel.getCurrentJobs(sitter_id);
                break;
            default:
                jobList = sitterModel.getFinishedJobs(sitter_id);
                break;
        }
    }
}

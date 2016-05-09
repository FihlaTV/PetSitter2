package com.zekisanmobile.petsitter2.fragment.owner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.util.Config;
import com.zekisanmobile.petsitter2.util.JobsStatusString;
import com.zekisanmobile.petsitter2.view.owner.OwnerJobListActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReservationsFragment extends Fragment {

    public ReservationsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reservations, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick(R.id.rl_new_jobs)
    public void onClickNewJobs() {
        redirectToOwnerJobList(JobsStatusString.NEW);
    }

    @OnClick(R.id.rl_next_jobs)
    public void onClickNextJobs() {
        redirectToOwnerJobList(JobsStatusString.NEXT);
    }

    @OnClick(R.id.rl_current_jobs)
    public void onClickCurrentJobs() {
        redirectToOwnerJobList(JobsStatusString.CURRENT);
    }

    @OnClick(R.id.rl_finished_jobs)
    public void onClickFinishedJobs() {
        redirectToOwnerJobList(JobsStatusString.FINISHED);
    }

    private void redirectToOwnerJobList(String status) {
        Intent intent = new Intent(this.getActivity(), OwnerJobListActivity.class);
        intent.putExtra(Config.JOB_STATUS, status);
        startActivity(intent);
    }
}

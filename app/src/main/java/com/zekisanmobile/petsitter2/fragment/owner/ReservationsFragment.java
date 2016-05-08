package com.zekisanmobile.petsitter2.fragment.owner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zekisanmobile.petsitter2.R;

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

    }

    @OnClick(R.id.rl_next_jobs)
    public void onClickNextJobs() {

    }

    @OnClick(R.id.rl_current_jobs)
    public void onClickCurrentJobs() {

    }

    @OnClick(R.id.rl_finished_jobs)
    public void onClickFinishedJobs() {

    }
}

package com.zekisanmobile.petsitter2.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zekisanmobile.petsitter2.vo.Job;

import java.util.List;

import butterknife.ButterKnife;

public class JobListAdapter extends RecyclerView.Adapter<JobListAdapter.ViewHolder>{

    private List<Job> jobList;

    public JobListAdapter(List<Job> jobList) {
        this.jobList = jobList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void setJobList(List<Job> jobList) {
        this.jobList = jobList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}

package com.zekisanmobile.petsitter2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.customListener.RecyclerViewOnClickListener;
import com.zekisanmobile.petsitter2.util.CircleTransform;
import com.zekisanmobile.petsitter2.vo.Job;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class JobListAdapter extends RecyclerView.Adapter<JobListAdapter.ViewHolder>{

    private List<Job> jobList;
    private RecyclerViewOnClickListener listener;
    private Context context;

    public JobListAdapter(List<Job> jobList, Context context,
                          RecyclerViewOnClickListener listener) {
        this.jobList = jobList;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.job_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Job job = jobList.get(position);

        Picasso.with(context)
                .load(job.getOwner().getPhotoUrl().getMedium())
                .transform(new CircleTransform())
                .into(holder.ivOwner);

        holder.tvName.setText(job.getOwner().getName());
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public void setJobList(List<Job> jobList) {
        this.jobList = jobList;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_owner)
        public ImageView ivOwner;

        @BindView(R.id.tv_name)
        public TextView tvName;

        @BindView(R.id.tv_status)
        public TextView tvStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onClick(v, getAdapterPosition());
            }
        }
    }

}

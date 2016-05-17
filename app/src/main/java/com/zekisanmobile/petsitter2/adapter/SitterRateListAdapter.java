package com.zekisanmobile.petsitter2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.customListener.RecyclerViewOnClickListener;
import com.zekisanmobile.petsitter2.util.CircleTransform;
import com.zekisanmobile.petsitter2.util.DateFormatter;
import com.zekisanmobile.petsitter2.vo.Job;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SitterRateListAdapter extends RecyclerView.Adapter<SitterRateListAdapter.ViewHolder>{

    private List<Job> jobsWithRate;
    private Context context;
    private RecyclerViewOnClickListener listener;

    public SitterRateListAdapter(List<Job> jobsWithRate, Context context,
                                 RecyclerViewOnClickListener listener) {
        this.jobsWithRate = jobsWithRate;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rate_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Job job = jobsWithRate.get(position);

        Picasso.with(context)
                .load(job.getOwner().getPhotoUrl().getMedium())
                .transform(new CircleTransform())
                .into(holder.ivPhoto);

        holder.tvOwner.setText(job.getOwner().getName());
        holder.tvCreatedAt.setText(DateFormatter.formattedDateForView(new Date()));
        holder.tvStarsQtd.setText(Integer.toString(job.getRate().getStarsQtd()));
        holder.ratingBar.setRating(job.getRate().getStarsQtd());
        holder.tvOwnerComment.setText(job.getRate().getOwnerComment());
    }

    @Override
    public int getItemCount() {
        return jobsWithRate.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_photo)
        public ImageView ivPhoto;

        @BindView(R.id.tv_owner)
        public TextView tvOwner;

        @BindView(R.id.tv_created_at)
        public TextView tvCreatedAt;

        @BindView(R.id.rating_bar)
        public RatingBar ratingBar;

        @BindView(R.id.tv_stars_qtd)
        public TextView tvStarsQtd;

        @BindView(R.id.tv_owner_comment)
        public TextView tvOwnerComment;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onClick(v, getAdapterPosition());
            }
        }
    }
}

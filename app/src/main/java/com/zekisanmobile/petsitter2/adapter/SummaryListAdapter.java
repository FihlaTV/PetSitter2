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
import com.zekisanmobile.petsitter2.util.CircleTransform;
import com.zekisanmobile.petsitter2.util.DateFormatter;
import com.zekisanmobile.petsitter2.vo.Summary;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SummaryListAdapter extends RecyclerView.Adapter<SummaryListAdapter.MyViewHolder> {

    Context context;
    private List<Summary> summaryList;

    public SummaryListAdapter(Context context, List<Summary> summaryList) {
        this.context = context;
        this.summaryList = summaryList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.summary_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Summary summary = summaryList.get(position);

        holder.textSummary.setText(summary.getText());
        holder.dateSummary.setText(DateFormatter.formattedDateForView(new Date()));

        Picasso.with(context)
                .load(summary.getPhotoUrl().getMedium())
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return summaryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.thumbnail)
        public ImageView thumbnail;

        @BindView(R.id.text_summary)
        public TextView textSummary;

        @BindView(R.id.date_summary)
        public TextView dateSummary;

        public MyViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}

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
import com.zekisanmobile.petsitter2.vo.Sitter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SitterListAdapter extends RecyclerView.Adapter<SitterListAdapter.ViewHolder>{

    private List<Sitter> sitters;
    private Context context;
    private RecyclerViewOnClickListener listener;

    public SitterListAdapter(List<Sitter> sitters, Context context,
                             RecyclerViewOnClickListener listener) {
        this.sitters = sitters;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sitter_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Sitter sitter = sitters.get(position);

        Picasso.with(context)
                .load(sitter.getPhotoUrl().getImage())
                .transform(new CircleTransform())
                .into(holder.ivSitter);

        holder.tvName.setText(sitter.getName());
        holder.tvValueHour.setText(String.valueOf(sitter.getValueHour()));
        holder.tvSitterRateAvg.setText(String.valueOf(sitter.getRateAvg()));
    }

    @Override
    public int getItemCount() {
        return sitters.size();
    }

    public void setSittersList(List<Sitter> sitters) {
        this.sitters = sitters;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_sitter)
        ImageView ivSitter;

        @BindView(R.id.tv_name)
        TextView tvName;

        @BindView(R.id.tv_value_hour)
        TextView tvValueHour;

        @BindView(R.id.tv_sitter_rate_avg)
        TextView tvSitterRateAvg;

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

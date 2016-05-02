package com.zekisanmobile.petsitter2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.vo.SearchAnimalItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileAnimalAdapter extends RecyclerView.Adapter<ProfileAnimalAdapter.ViewHolder> {

    private List<SearchAnimalItem> animals;

    public ProfileAnimalAdapter(List<SearchAnimalItem> animals) {
        this.animals = animals;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.animal_for_search_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final SearchAnimalItem animal = animals.get(position);

        Context context = holder.ivAnimalIcon.getContext();
        int imageId = context.getResources()
                .getIdentifier(animal.getIcon(), "drawable", context.getPackageName());

        holder.ivAnimalIcon.setImageResource(imageId);
        holder.tvAnimalName.setText(animal.getName());
        holder.tvAnimalName.setTextSize(15);
        holder.cbAnimal.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return animals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_animal_icon)
        public ImageView ivAnimalIcon;

        @BindView(R.id.tv_animal_name)
        public TextView tvAnimalName;

        @BindView(R.id.cb_animal)
        public CheckBox cbAnimal;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}

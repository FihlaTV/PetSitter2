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
import com.zekisanmobile.petsitter2.vo.Pet;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectedPetListForJobAdapter extends RecyclerView.Adapter<SelectedPetListForJobAdapter.ViewHolder>{

    private Context context;
    private List<Pet> selectedPetsList;

    public SelectedPetListForJobAdapter(Context context, List<Pet> selectedPetsList) {
        this.context = context;
        this.selectedPetsList = selectedPetsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.selected_pet_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Pet pet = selectedPetsList.get(position);

        Picasso.with(context)
                .load(pet.getPhotoUrl().getImage())
                .transform(new CircleTransform())
                .into(holder.ivPhoto);
        holder.tvPetName.setText(pet.getName());
    }

    @Override
    public int getItemCount() {
        return selectedPetsList.size();
    }

    public void setSelectedPetsList(List<Pet> selectedPetsList) {
        this.selectedPetsList = selectedPetsList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_photo)
        public ImageView ivPhoto;

        @BindView(R.id.tv_pet_name)
        public TextView tvPetName;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}

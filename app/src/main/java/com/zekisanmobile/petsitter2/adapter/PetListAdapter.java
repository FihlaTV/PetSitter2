package com.zekisanmobile.petsitter2.adapter;

import android.content.Context;
import android.net.Uri;
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

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PetListAdapter extends RecyclerView.Adapter<PetListAdapter.ViewHolder>{

    private Context context;
    private List<Pet> petList;

    public PetListAdapter(Context context, List<Pet> petList) {
        this.context = context;
        this.petList = petList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pet_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Pet pet = petList.get(position);

        if (pet.getPhotoUrl().getImage().contains("http")) {
            Picasso.with(context)
                    .load(pet.getPhotoUrl().getImage())
                    .transform(new CircleTransform())
                    .into(holder.ivPhoto);
        } else {
            File file = new File(Uri.parse(pet.getPhotoUrl().getImage()).getPath());
            Picasso.with(context)
                    .load(file)
                    .transform(new CircleTransform())
                    .into(holder.ivPhoto);
        }

        holder.tvName.setText(pet.getName());
        holder.tvAge.setText(String.valueOf(pet.getAge()) + " " + pet.getAgeText());
        holder.tvSize.setText(pet.getSize());
        holder.tvWeight.setText(String.valueOf(pet.getWeight()) + " Kg");
        holder.tvBreed.setText(pet.getBreed());
        holder.tvCare.setText(pet.getPetCare());
    }

    @Override
    public int getItemCount() {
        return petList.size();
    }

    public void setPetList(List<Pet> petList) {
        this.petList = petList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_photo)
        public ImageView ivPhoto;

        @BindView(R.id.tv_name)
        public TextView tvName;

        @BindView(R.id.tv_age)
        public TextView tvAge;

        @BindView(R.id.tv_size)
        public TextView tvSize;

        @BindView(R.id.tv_weight)
        public TextView tvWeight;

        @BindView(R.id.tv_breed)
        public TextView tvBreed;

        @BindView(R.id.tv_care)
        public TextView tvCare;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}

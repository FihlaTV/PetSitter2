package com.zekisanmobile.petsitter2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.util.CircleTransform;
import com.zekisanmobile.petsitter2.vo.Pet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PetListForJobAdapter extends RecyclerView.Adapter<PetListForJobAdapter.ViewHolder>{

    private Context context;
    private List<Pet> petList;
    private List<String> selectedIds = new ArrayList<>();

    public PetListForJobAdapter(Context context, List<Pet> petList) {
        this.context = context;
        this.petList = petList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pet_for_job_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Pet pet = petList.get(position);

        Picasso.with(context)
                .load(pet.getPhotoUrl().getImage())
                .transform(new CircleTransform())
                .into(holder.ivPhoto);
        holder.tvPetName.setText(pet.getName());

        holder.cbPet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectedIds.add(petList.get(position).getId());
                } else {
                    selectedIds.remove(petList.get(position).getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return petList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_photo)
        public ImageView ivPhoto;

        @BindView(R.id.tv_pet_name)
        public TextView tvPetName;

        @BindView(R.id.cb_pet)
        public CheckBox cbPet;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    public ArrayList<Pet> getSelectedPets() {
        ArrayList<Pet> selectedPets = new ArrayList<>();
        for (String id : selectedIds) {
            for (Pet pet : petList) {
                if (pet.getId().equalsIgnoreCase(id)) {
                    selectedPets.add(pet);
                }
            }
        }

        return selectedPets;
    }
}

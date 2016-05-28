package com.zekisanmobile.petsitter2.asyncTask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.zekisanmobile.petsitter2.PetSitterApp;
import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.api.ApiService;
import com.zekisanmobile.petsitter2.api.body.CreateOwnerBody;
import com.zekisanmobile.petsitter2.api.body.CreatePetBody;
import com.zekisanmobile.petsitter2.view.register.RegisterView;
import com.zekisanmobile.petsitter2.view.register.owner.PetListActivity;
import com.zekisanmobile.petsitter2.vo.Owner;
import com.zekisanmobile.petsitter2.vo.Pet;
import com.zekisanmobile.petsitter2.vo.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;

public class CreateOwnerTask extends AsyncTask<Void, Void, Void> {

    private String ownerId;
    private String userId;
    private ProgressDialog progressDialog;
    private RegisterView view;

    @Inject
    ApiService service;

    public CreateOwnerTask(String ownerId, String userId, RegisterView view) {
        this.ownerId = ownerId;
        this.userId = userId;
        this.view = view;
        ((PetSitterApp) view.getPetSitterApp()).getAppComponent().inject(this);
        progressDialog = new ProgressDialog((PetListActivity) view);
        progressDialog.setMessage(view.getContext().getString(R.string.saving_registry));
        progressDialog.setCancelable(false);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (progressDialog != null) progressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        CreateOwnerBody ownerBody = getCreateOwnerBody();
        List<CreatePetBody> petBodyList = getCreatePetBodyList();

        try {
            service.createOwner(ownerBody).execute();
            for(CreatePetBody body : petBodyList) {
                service.createPet(body).execute();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<CreatePetBody> getCreatePetBodyList() {
        Realm realm = Realm.getDefaultInstance();
        List<CreatePetBody> petBodyList = new ArrayList<>();
        Owner owner = realm.where(Owner.class).equalTo("id", ownerId).findFirst();
        for (Pet pet : owner.getPets()) {
            CreatePetBody body = new CreatePetBody();
            body.setOwner_app_id(ownerId);
            body.setAnimal_id(pet.getAnimal().getId());
            body.setName(pet.getName());
            body.setAge(pet.getAge());
            body.setAge_text(pet.getAgeText());
            body.setSize(pet.getSize());
            body.setWeight(pet.getWeight());
            body.setBreed(pet.getBreed());
            body.setPet_care(pet.getPetCare());
            petBodyList.add(body);
        }
        realm.close();

        return petBodyList;
    }

    @NonNull
    private CreateOwnerBody getCreateOwnerBody() {
        Realm realm = Realm.getDefaultInstance();
        Owner owner = realm.where(Owner.class).equalTo("id", ownerId).findFirst();
        CreateOwnerBody body = new CreateOwnerBody();
        body.setOwner_app_id(owner.getId());
        body.setName(owner.getName());
        body.setSurname(owner.getSurname());
        body.setPhone(owner.getPhone());
        body.setStreet(owner.getStreet());
        body.setAddress_number(owner.getAddress_number());
        body.setComplement(owner.getComplement());
        body.setCep(owner.getCep());
        body.setDistrict(owner.getDistrict());
        body.setCity(owner.getCity());
        body.setState(owner.getState());
        body.setLatitude(owner.getLatitude());
        body.setLongitude(owner.getLongitude());

        User user = realm.where(User.class).equalTo("id", userId).findFirst();
        body.setUser_app_id(user.getId());
        body.setEmail(user.getEmail());
        body.setPassword(user.getPassword());
        body.setEntity_id(user.getEntityId());
        body.setEntity_type(user.getEntityType());
        body.setDevice_token(user.getDeviceToken());
        realm.close();
        return body;
    }

    @Override
    protected void onPostExecute(Void aVoid) {;
        progressDialog.dismiss();
    }
}

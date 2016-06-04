package com.zekisanmobile.petsitter2.asyncTask;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;

import com.zekisanmobile.petsitter2.PetSitterApp;
import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.api.ApiService;
import com.zekisanmobile.petsitter2.api.body.CreatePetBody;
import com.zekisanmobile.petsitter2.view.login.LoginView;
import com.zekisanmobile.petsitter2.view.owner.AddNewPetActivity;
import com.zekisanmobile.petsitter2.vo.Pet;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import io.realm.Realm;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class AddNewPetTask extends AsyncTask<Void, Void, Boolean> {

    private LoginView view;
    private ProgressDialog progressDialog;
    private String ownerId;
    private String petId;

    @Inject
    ApiService service;

    public AddNewPetTask(LoginView view, String ownerId, String petId) {
        this.view = view;
        this.ownerId = ownerId;
        this.petId = petId;
        ((PetSitterApp) view.getPetSitterApp()).getAppComponent().inject(this);
        progressDialog = new ProgressDialog((AddNewPetActivity) view);
        progressDialog.setMessage(view.getContext().getString(R.string.saving_pet_dialog));
        progressDialog.setCancelable(false);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (progressDialog != null) progressDialog.show();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Realm realm = Realm.getDefaultInstance();
        Pet pet = realm.where(Pet.class).equalTo("id", petId).findFirst();

        CreatePetBody body = new CreatePetBody();
        body.setOwner_app_id(ownerId);
        body.setAnimal_id(pet.getAnimal().getId());
        body.setApp_id(pet.getId());
        body.setName(pet.getName());
        body.setAge(pet.getAge());
        body.setAge_text(pet.getAgeText());
        body.setSize(pet.getSize());
        body.setWeight(pet.getWeight());
        body.setBreed(pet.getBreed());
        body.setPet_care(pet.getPetCare());
        try {
            service.createPet(body).execute();


            RequestBody app_id = RequestBody.create(MediaType.parse("multipart/form-data"),
                    body.getApp_id());
            RequestBody photo_app_id = RequestBody.create(MediaType.parse("multipart/form-data"),
                    getPetPhotoAppId(body.getApp_id()));
            Uri fileUri = Uri.parse(getPetPhotoFile(body.getApp_id()));
            File file = new File(fileUri.getPath());
            RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file);
            service.insertPetPhoto(app_id, photo_app_id, fileBody).execute();
            realm.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getPetPhotoAppId(String app_id) {
        Realm realm = Realm.getDefaultInstance();
        String photoAppId = realm.where(Pet.class).equalTo("id", app_id).findFirst().getPhotoUrl()
                .getId();
        realm.close();

        return photoAppId;
    }

    private String getPetPhotoFile(String app_id) {
        Realm realm = Realm.getDefaultInstance();
        String file = realm.where(Pet.class).equalTo("id", app_id).findFirst().getPhotoUrl()
                .getImage();
        realm.close();

        return file;
    }

    @Override
    protected void onPostExecute(Boolean isSuccessful) {
        progressDialog.dismiss();
        if (view != null && isSuccessful) {
            ((AddNewPetActivity) view).redirectToPetList();
        }
    }
}

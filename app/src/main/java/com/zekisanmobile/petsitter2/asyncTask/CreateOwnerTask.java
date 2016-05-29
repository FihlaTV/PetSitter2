package com.zekisanmobile.petsitter2.asyncTask;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.zekisanmobile.petsitter2.PetSitterApp;
import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.api.ApiService;
import com.zekisanmobile.petsitter2.api.body.CreateOwnerBody;
import com.zekisanmobile.petsitter2.api.body.CreatePetBody;
import com.zekisanmobile.petsitter2.api.body.GetOwnerBody;
import com.zekisanmobile.petsitter2.api.body.GetSitterBody;
import com.zekisanmobile.petsitter2.api.body.LoginBody;
import com.zekisanmobile.petsitter2.model.OwnerModel;
import com.zekisanmobile.petsitter2.model.SitterModel;
import com.zekisanmobile.petsitter2.model.UserModel;
import com.zekisanmobile.petsitter2.session.SessionManager;
import com.zekisanmobile.petsitter2.util.EntityType;
import com.zekisanmobile.petsitter2.util.GCMToken;
import com.zekisanmobile.petsitter2.view.register.RegisterView;
import com.zekisanmobile.petsitter2.view.register.owner.PetListActivity;
import com.zekisanmobile.petsitter2.vo.Owner;
import com.zekisanmobile.petsitter2.vo.Pet;
import com.zekisanmobile.petsitter2.vo.Sitter;
import com.zekisanmobile.petsitter2.vo.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

public class CreateOwnerTask extends AsyncTask<Void, Void, Void> {

    private String ownerId;
    private String userId;
    private ProgressDialog progressDialog;
    private RegisterView view;
    private User user;

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
        try {
            createOwner();
            createPets();
            login();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void login() {
        LoginBody body = new LoginBody();
        body.setEmail(getUserEmail());
        body.setPassword(getUserPassword());

        try {
            Response<User> response = service.login(body).execute();
            if (response.isSuccessful()) {
                User user = response.body();
                Realm realm = Realm.getDefaultInstance();
                UserModel userModel = new UserModel(realm);
                userModel.save(user);
                realm.close();
                this.user = user;

                saveSharedPreferences(user.getId());
                getEntity(user.getEntityId(), user.getEntityType());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getEntity(String entityId, String entityType) {
        switch (entityType) {
            case EntityType.OWNER:
                getOwner(entityId);
                break;
            default:
                getSitter(entityId);
                break;
        }
    }

    private void getSitter(String entityId) {
        GetSitterBody body = new GetSitterBody();
        body.setApp_id(entityId);
        Call<Sitter> call = service.getSitter(body);
        try {
            Sitter sitter = call.execute().body();
            Realm realm = Realm.getDefaultInstance();
            SitterModel sitterModel = new SitterModel(realm);
            sitterModel.save(sitter);
            realm.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getOwner(String entityId) {
        GetOwnerBody body = new GetOwnerBody();
        body.setApp_id(entityId);
        Call<Owner> call = service.getOwner(body);
        try {
            Owner owner = call.execute().body();
            Realm realm = Realm.getDefaultInstance();
            OwnerModel ownerModel = new OwnerModel(realm);
            ownerModel.save(owner);
            realm.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveSharedPreferences(String id) {
        SessionManager sessionManager = new SessionManager(view.getContext());
        sessionManager.setUserId(id);
        sessionManager.setTokenSentToServer(true);
    }

    private String getUserPassword() {
        Realm realm = Realm.getDefaultInstance();
        String password = realm.where(User.class).equalTo("id", userId).findFirst().getPassword();
        realm.close();

        return password;
    }

    private String getUserEmail() {
        Realm realm = Realm.getDefaultInstance();
        String email = realm.where(User.class).equalTo("id", userId).findFirst().getEmail();
        realm.close();

        return email;
    }

    private void createPets() throws IOException {
        List<CreatePetBody> petBodyList = getCreatePetBodyList();
        for(CreatePetBody body : petBodyList) {
            service.createPet(body).execute();

            RequestBody app_id = RequestBody.create(MediaType.parse("multipart/form-data"),
                    body.getApp_id());
            RequestBody photo_app_id = RequestBody.create(MediaType.parse("multipart/form-data"),
                    getPetPhotoAppId(body.getApp_id()));
            Uri fileUri = Uri.parse(getPetPhotoFile(body.getApp_id()));
            File file =  new File(fileUri.getPath());
            RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file);
            service.insertPetPhoto(app_id, photo_app_id, fileBody).execute();
        }
    }

    private void createOwner() throws IOException {
        CreateOwnerBody ownerBody = getCreateOwnerBody();
        service.createOwner(ownerBody).execute();

        RequestBody owner_app_id = RequestBody.create(MediaType.parse("multipart/form-data"),
                ownerId);
        RequestBody owner_photo_app_id = RequestBody.create(MediaType.parse("multipart/form-data"),
                getOwnerPhotoAppId(ownerId));
        Uri ownerFileUri = Uri.parse(getOwnerPhotoFile(ownerId));
        File ownerFile =  new File(ownerFileUri.getPath());
        RequestBody ownerFileBody = RequestBody.create(MediaType.parse("image/*"), ownerFile);
        service.insertOwnerPhoto(owner_app_id, owner_photo_app_id, ownerFileBody).execute();
    }

    private String getOwnerPhotoFile(String ownerId) {
        Realm realm = Realm.getDefaultInstance();
        String file = realm.where(Owner.class).equalTo("id", ownerId).findFirst().getPhotoUrl()
                .getImage();
        realm.close();

        return file;
    }

    private String getOwnerPhotoAppId(String ownerId) {
        Realm realm = Realm.getDefaultInstance();
        String photo_app_id = realm.where(Owner.class).equalTo("id", ownerId).findFirst()
                .getPhotoUrl().getId();
        realm.close();
        return photo_app_id;
    }

    private String getPetPhotoFile(String app_id) {
        Realm realm = Realm.getDefaultInstance();
        String file = realm.where(Pet.class).equalTo("id", app_id).findFirst().getPhotoUrl()
                .getImage();
        realm.close();

        return file;
    }

    private String getPetPhotoAppId(String app_id) {
        Realm realm = Realm.getDefaultInstance();
        String photoAppId = realm.where(Pet.class).equalTo("id", app_id).findFirst().getPhotoUrl()
                .getId();
        realm.close();

        return photoAppId;
    }

    private List<CreatePetBody> getCreatePetBodyList() {
        Realm realm = Realm.getDefaultInstance();
        List<CreatePetBody> petBodyList = new ArrayList<>();
        Owner owner = realm.where(Owner.class).equalTo("id", ownerId).findFirst();
        for (Pet pet : owner.getPets()) {
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

        String token = GCMToken.getTokenFromCGM(view.getContext(), view.getSenderId());
        body.setDevice_token(token);
        realm.close();
        return body;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        progressDialog.dismiss();
        if (view != null) {
            ((PetListActivity) view).redirectUser(user);
        }
    }
}

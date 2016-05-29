package com.zekisanmobile.petsitter2.asyncTask;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;

import com.zekisanmobile.petsitter2.PetSitterApp;
import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.api.ApiService;
import com.zekisanmobile.petsitter2.api.body.AnimalBody;
import com.zekisanmobile.petsitter2.api.body.CreateSitterBody;
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
import com.zekisanmobile.petsitter2.view.register.sitter.AboutMeRegisterActivity;
import com.zekisanmobile.petsitter2.vo.Animal;
import com.zekisanmobile.petsitter2.vo.Owner;
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

public class CreateSitterTask extends AsyncTask<Void, Void, Void> {

    private String sitterId;
    private String userId;
    private ProgressDialog progressDialog;
    private RegisterView view;
    private User user;

    @Inject
    ApiService service;

    public CreateSitterTask(String sitterId, String userId, RegisterView view) {
        this.sitterId = sitterId;
        this.userId = userId;
        this.view = view;
        ((PetSitterApp) view.getPetSitterApp()).getAppComponent().inject(this);
        progressDialog = new ProgressDialog((AboutMeRegisterActivity) view);
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
            createSitter();
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

    private void createSitter() throws IOException {
        CreateSitterBody sitterBody = getCreateSitterBody();
        service.createSitter(sitterBody).execute();

        RequestBody sitter_app_id = RequestBody.create(MediaType.parse("multipart/form-data"),
                sitterId);
        RequestBody sitter_photo_app_id = RequestBody.create(MediaType.parse("multipart/form-data"),
                getOwnerPhotoAppId(sitterId));
        Uri sitterFileUri = Uri.parse(getOwnerPhotoFile(sitterId));
        File sitterFile = new File(sitterFileUri.getPath());
        RequestBody sitterFileBody = RequestBody.create(MediaType.parse("image/*"), sitterFile);
        service.insertSitterPhoto(sitter_app_id, sitter_photo_app_id, sitterFileBody).execute();
    }

    private String getOwnerPhotoFile(String sitterId) {
        Realm realm = Realm.getDefaultInstance();
        String file = realm.where(Sitter.class).equalTo("id", sitterId).findFirst().getPhotoUrl()
                .getImage();
        realm.close();

        return file;
    }

    private String getOwnerPhotoAppId(String sitterId) {
        Realm realm = Realm.getDefaultInstance();
        String photo_app_id = realm.where(Sitter.class).equalTo("id", sitterId).findFirst()
                .getPhotoUrl().getId();
        realm.close();
        return photo_app_id;
    }

    private CreateSitterBody getCreateSitterBody() {
        Realm realm = Realm.getDefaultInstance();
        Sitter sitter = realm.where(Sitter.class).equalTo("id", sitterId).findFirst();

        CreateSitterBody body = new CreateSitterBody();
        List<AnimalBody> animalBodyList = new ArrayList<>();
        for (Animal animal : sitter.getAnimals()) {
            AnimalBody animalBody = new AnimalBody();
            animalBody.setAnimal_id(animal.getId());
            animalBodyList.add(animalBody);
        }

        body.setSitter_app_id(sitter.getId());
        body.setName(sitter.getName());
        body.setSurname(sitter.getSurname());
        body.setPhone(sitter.getPhone());
        body.setStreet(sitter.getStreet());
        body.setAddress_number(sitter.getAddress_number());
        body.setComplement(sitter.getComplement());
        body.setCep(sitter.getCep());
        body.setDistrict(sitter.getDistrict());
        body.setCity(sitter.getCity());
        body.setState(sitter.getState());
        body.setLatitude(sitter.getLatitude());
        body.setLongitude(sitter.getLongitude());
        body.setAbout_me(sitter.getAboutMe());
        body.setValue_hour(sitter.getValueHour());
        body.setAnimals(animalBodyList);

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
            ((AboutMeRegisterActivity) view).redirectUser(user);
        }
    }
}

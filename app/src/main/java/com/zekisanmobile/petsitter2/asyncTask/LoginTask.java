package com.zekisanmobile.petsitter2.asyncTask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.zekisanmobile.petsitter2.PetSitterApp;
import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.api.ApiService;
import com.zekisanmobile.petsitter2.api.body.DeviceTokenBody;
import com.zekisanmobile.petsitter2.api.body.LoginBody;
import com.zekisanmobile.petsitter2.model.OwnerModel;
import com.zekisanmobile.petsitter2.model.SitterModel;
import com.zekisanmobile.petsitter2.model.UserModel;
import com.zekisanmobile.petsitter2.session.SessionManager;
import com.zekisanmobile.petsitter2.util.EntityType;
import com.zekisanmobile.petsitter2.view.login.LoginActivity;
import com.zekisanmobile.petsitter2.view.login.LoginView;
import com.zekisanmobile.petsitter2.vo.Owner;
import com.zekisanmobile.petsitter2.vo.Sitter;
import com.zekisanmobile.petsitter2.vo.User;

import java.io.IOException;

import javax.inject.Inject;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginTask extends AsyncTask<String, Void, Boolean>{

    private LoginView view;
    private String token;
    private ProgressDialog progressDialog;
    private User user;

    @Inject
    Retrofit retrofit;

    public LoginTask(LoginView view) {
        ((PetSitterApp) view.getPetSitterApp()).getAppComponent().inject(this);
        this.view = view;
        progressDialog = new ProgressDialog((LoginActivity) view);
        progressDialog.setMessage(view.getContext().getString(R.string.login_dialog_message));
        progressDialog.setCancelable(false);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (progressDialog != null) progressDialog.show();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        LoginBody body = new LoginBody();
        body.setEmail(params[0]);
        body.setPassword(params[1]);

        ApiService service = retrofit.create(ApiService.class);
        try {
            Response<User> response = service.login(body).execute();
            if (response.isSuccessful()) {
                User user = response.body();
                Realm realm = Realm.getDefaultInstance();
                UserModel userModel = new UserModel(realm);
                userModel.save(user);
                realm.close();
                this.user = user;

                getTokenFromCGM();
                sendTokenToServer(user.getId());
                saveSharedPreferences(user.getId());
                getEntity(user.getEntityId(), user.getEntityType());
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void getEntity(long entityId, String entityType) {
        switch (entityType) {
            case EntityType.OWNER:
                getOwner(entityId);
                break;
            default:
                getSitter(entityId);
                break;
        }
    }

    private void getSitter(long entityId) {
        ApiService service = retrofit.create(ApiService.class);
        Call<Sitter> call = service.getSitter(entityId);
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

    private void getOwner(long entityId) {
        ApiService service = retrofit.create(ApiService.class);
        Call<Owner> call = service.getOwner(entityId);
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

    private void saveSharedPreferences(long id) {
        SessionManager sessionManager = new SessionManager(view.getContext());
        sessionManager.setUserId(id);
        sessionManager.setTokenSentToServer(true);
    }

    @Override
    protected void onPostExecute(Boolean isSuccessful) {
        progressDialog.dismiss();
        if (view != null && !isSuccessful) {
            ((LoginActivity) view)
                    .showLoginDialog(view.getContext().getString(R.string.login_invalid_login));
        } else if (user != null && isSuccessful) {
            ((LoginActivity) view).redirectUser(user);
        }
    }

    private void sendTokenToServer(long id) {
        DeviceTokenBody body = new DeviceTokenBody();
        body.setToken(token);

        ApiService service = retrofit.create(ApiService.class);
        try {
            service.updateDeviceToken(id, body).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getTokenFromCGM() {
        InstanceID instanceID = InstanceID.getInstance(view.getContext());
        try {
            token = instanceID.getToken(view.getSenderId(),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.i("DeviceToken", "GCM Registration Token: " + token);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

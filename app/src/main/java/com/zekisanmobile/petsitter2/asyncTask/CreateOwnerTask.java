package com.zekisanmobile.petsitter2.asyncTask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.zekisanmobile.petsitter2.PetSitterApp;
import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.api.ApiService;
import com.zekisanmobile.petsitter2.api.body.CreateOwnerBody;
import com.zekisanmobile.petsitter2.view.register.RegisterView;
import com.zekisanmobile.petsitter2.view.register.owner.PetListActivity;
import com.zekisanmobile.petsitter2.vo.Owner;
import com.zekisanmobile.petsitter2.vo.User;

import java.io.IOException;

import javax.inject.Inject;

public class CreateOwnerTask extends AsyncTask<Void, Void, Void> {

    private Owner owner;
    private User user;
    private ProgressDialog progressDialog;
    private RegisterView view;

    @Inject
    ApiService service;

    public CreateOwnerTask(Owner owner, User user, RegisterView view) {
        this.owner = owner;
        this.user = user;
        this.view = view;
        ((PetSitterApp) view.getPetSitterApp()).getAppComponent().inject(this);
        progressDialog = new ProgressDialog((PetListActivity) view);
        progressDialog.setMessage(view.getContext().getString(R.string.login_dialog_message));
        progressDialog.setCancelable(false);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (progressDialog != null) progressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        CreateOwnerBody body = getCreateOwnerBody();

        try {
            service.createOwner(body).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @NonNull
    private CreateOwnerBody getCreateOwnerBody() {
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

        body.setUser_app_id(user.getId());
        body.setEmail(user.getEmail());
        body.setPassword(user.getPassword());
        body.setEntity_id(user.getEntityId());
        body.setEntity_type(user.getEntityType());
        body.setDevice_token(user.getDeviceToken());
        return body;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        progressDialog.dismiss();
    }
}

package com.zekisanmobile.petsitter2.asyncTask;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.zekisanmobile.petsitter2.PetSitterApp;
import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.api.ApiService;
import com.zekisanmobile.petsitter2.view.HomeView;

import java.io.IOException;

import javax.inject.Inject;

import retrofit2.Retrofit;

public class LogoutTask extends AsyncTask<Void, Void, Void>{

    private HomeView view;
    private ProgressDialog progressDialog;
    private long user_id;

    @Inject
    ApiService service;

    public LogoutTask(HomeView view, long user_id) {
        ((PetSitterApp) view.getPetSitterApp()).getAppComponent().inject(this);
        this.view = view;
        this.user_id = user_id;
        progressDialog = new ProgressDialog(view.getViewContext());
        progressDialog.setMessage(view.getViewContext().getString(R.string.logout_dialog_message));
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
            service.logout(user_id).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        progressDialog.dismiss();
        view.redirectToLoginPage();
    }
}

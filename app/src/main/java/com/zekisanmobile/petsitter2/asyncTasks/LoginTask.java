package com.zekisanmobile.petsitter2.asyncTasks;

import android.os.AsyncTask;

import com.zekisanmobile.petsitter2.PetSitterApp;
import com.zekisanmobile.petsitter2.api.ApiService;
import com.zekisanmobile.petsitter2.api.body.LoginBody;
import com.zekisanmobile.petsitter2.view.login.LoginView;

import java.io.IOException;

import javax.inject.Inject;

import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginTask extends AsyncTask<String, Void, Void>{

    private LoginView view;

    @Inject
    Retrofit retrofit;

    public LoginTask(LoginView view) {
        ((PetSitterApp) view.getPetSitterApp()).getAppComponent().inject(this);
        this.view = view;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Void doInBackground(String... params) {
        LoginBody body = new LoginBody();
        body.setEmail(params[0]);
        body.setPassword(params[1]);

        ApiService service = retrofit.create(ApiService.class);
        try {
            Response response = service.login(body).execute();
            if (response.isSuccessful()) {
                // TODO: salvar user no realm
                // TODO: enviar token para GCM
            } else {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void v){

    }
}

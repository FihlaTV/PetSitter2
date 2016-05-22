package com.zekisanmobile.petsitter2.asyncTask;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.zekisanmobile.petsitter2.PetSitterApp;
import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.api.CepService;
import com.zekisanmobile.petsitter2.view.register.AddressRegisterActivity;
import com.zekisanmobile.petsitter2.view.register.LocationView;
import com.zekisanmobile.petsitter2.vo.ViaCep;

import java.io.IOException;

import javax.inject.Inject;

import retrofit2.Response;

public class LocationTask extends AsyncTask<Void, Void, ViaCep> {

    private LocationView view;
    private String cep;
    private ProgressDialog progressDialog;

    @Inject
    CepService service;

    public LocationTask(LocationView view, String cep) {
        ((PetSitterApp) view.getPetSitterApp()).getAppComponent().inject(this);

        this.view = view;
        this.cep = cep;
        progressDialog = new ProgressDialog((AddressRegisterActivity) view);
        progressDialog.setMessage(view.getContext().getString(R.string.search_for_cep));
        progressDialog.setCancelable(false);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (progressDialog != null) progressDialog.show();
    }

    @Override
    protected ViaCep doInBackground(Void... params) {
        try {
            Response<ViaCep> response = service.getLocationFromCep(cep).execute();
            if (response.isSuccessful()) {
                ViaCep viaCep = response.body();
                return viaCep;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ViaCep viaCep) {
        progressDialog.dismiss();
        if (viaCep != null) {
            ((AddressRegisterActivity) view).setAddressFromCep(viaCep);
        }
    }
}

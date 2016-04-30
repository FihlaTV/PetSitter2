package com.zekisanmobile.petsitter2.asyncTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.zekisanmobile.petsitter2.util.Config;

public class RegistrationAsyncTask extends AsyncTask<String, Void, Void> {

    Activity activity;
    ProgressDialog progressDialog;
    String token;

    public RegistrationAsyncTask(Activity activity) {
        this.activity = activity;
        if (activity != null) {
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Registrando");
            progressDialog.setCancelable(false);
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (progressDialog != null) progressDialog.show();
    }

    @Override
    protected Void doInBackground(String... params) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        String senderId = params[0];
        Log.d("test", "sender id : " + senderId);

        try {
            InstanceID instanceID = InstanceID.getInstance(activity);
            token = instanceID.getToken(senderId,
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            Log.i("test", "GCM Registration Token: " + token);

            sendTokenToServer();

            sharedPreferences.edit().putBoolean(Config.TOKEN_SENT_TO_SERVER, true).apply();

        } catch (Exception e) {
            Log.d("test", "Failed to complete token refresh", e);
            sharedPreferences.edit().putBoolean(Config.TOKEN_SENT_TO_SERVER, false).apply();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (progressDialog != null) progressDialog.dismiss();

    }

    private void sendTokenToServer(){}
}

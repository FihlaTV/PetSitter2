package com.zekisanmobile.petsitter2.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.gcm.GcmListenerService;
import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.model.JobModel;
import com.zekisanmobile.petsitter2.util.Config;
import com.zekisanmobile.petsitter2.util.JobsStatusString;
import com.zekisanmobile.petsitter2.view.sitter.SitterJobDetailsActivity;
import com.zekisanmobile.petsitter2.vo.Job;

import java.io.IOException;

import io.realm.Realm;

public class PetSitterGcmListenerService extends GcmListenerService {

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
        Log.d("GCM Received on device.", data.toString());
        String type = data.getString("type");
        switch (type) {
            case "job":
                getJobFromMessage(data);
                break;

        }
    }

    private void getJobFromMessage(Bundle data) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Job job = mapper.readValue(data.getString("body"), Job.class);
            Realm realm = Realm.getDefaultInstance();
            JobModel jobModel = new JobModel(realm);
            jobModel.save(job);
            realm.close();
            sendNotification(job);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendNotification(Job job) {
        Intent intent = new Intent(this, SitterJobDetailsActivity.class);
        intent.putExtra(Config.JOB_ID, job.getId());
        intent.putExtra(Config.JOB_STATUS, JobsStatusString.NEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.paw)
                .setContentTitle("Nova solicitação de trabalho")
                .setContentText("Toque para visualizar")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

}

package com.zekisanmobile.petsitter2.util;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

public class GCMToken {

    public static String getTokenFromCGM(Context context, String senderId) {
        InstanceID instanceID = InstanceID.getInstance(context);
        try {
            String token = instanceID.getToken(senderId,
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.i("DeviceToken", "GCM Registration Token: " + token);
            return token;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}

package com.zekisanmobile.petsitter2.service;

import com.google.android.gms.iid.InstanceIDListenerService;
import com.zekisanmobile.petsitter2.asyncTasks.RegistrationAsyncTask;

public class PetSitterInstanceIDListenerService extends InstanceIDListenerService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        new RegistrationAsyncTask(null).execute();
    }

}

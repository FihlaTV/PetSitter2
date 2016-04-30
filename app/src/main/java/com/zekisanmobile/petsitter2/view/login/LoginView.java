package com.zekisanmobile.petsitter2.view.login;

import android.app.Application;
import android.content.Context;

public interface LoginView {

    Application getPetSitterApp();

    String getSenderId();

    Context getContext();
}

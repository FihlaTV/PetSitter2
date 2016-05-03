package com.zekisanmobile.petsitter2.view;

import android.app.Application;
import android.content.Context;

public interface HomeView {

    Application getPetSitterApp();

    Context getViewContext();

    void redirectToLoginPage();

}

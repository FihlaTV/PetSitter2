package com.zekisanmobile.petsitter2;

import android.app.Application;
import android.content.Context;

import io.appflate.restmock.android.RESTMockTestRunner;

public class CustomTestRunner extends RESTMockTestRunner {
    @Override
    public Application newApplication(ClassLoader cl,
                                      String className,
                                      Context context) throws InstantiationException, IllegalAccessException, ClassNotFoundException {

        String testApplicationClassName = PetSitterApp.class.getCanonicalName();
        return super.newApplication(cl, testApplicationClassName, context);
    }
}

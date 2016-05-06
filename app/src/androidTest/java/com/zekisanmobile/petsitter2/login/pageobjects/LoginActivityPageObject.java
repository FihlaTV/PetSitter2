package com.zekisanmobile.petsitter2.login.pageobjects;

import com.zekisanmobile.petsitter2.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class LoginActivityPageObject {

    public void typeEmail(String email) {
        onView(withId(R.id.et_email)).perform(replaceText(email));
    }

    public void typePassword(String password) {
        onView(withId(R.id.et_password)).perform(replaceText(password));
    }

    public void pressLogin() {
        onView(withId(R.id.btn_login)).perform(click());
    }

}

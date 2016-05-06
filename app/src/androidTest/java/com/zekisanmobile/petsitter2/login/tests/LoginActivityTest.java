package com.zekisanmobile.petsitter2.login.tests;

import android.support.test.rule.ActivityTestRule;

import com.zekisanmobile.petsitter2.login.pageobjects.LoginActivityPageObject;
import com.zekisanmobile.petsitter2.view.login.LoginActivity;
import com.zekisanmobile.petsitter2.view.owner.OwnerHomeActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.appflate.restmock.RESTMockServer;
import io.appflate.restmock.utils.RequestMatchers;

import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;

public class LoginActivityTest {

    public static final String LOGIN_PATH = "login";
    public static final String EMAIL = "zekisan88@gmail.com";
    public static final String PASSWORD = "123";
    public static final String USER_JSON = "mocks/users/zekisan.json";

    private LoginActivityPageObject pageObject;

    @Rule
    public ActivityTestRule<LoginActivity> rule = new ActivityTestRule<LoginActivity>(
            LoginActivity.class,
            true,
            false
    );

    @Before
    public void setup() throws Exception {
        pageObject = new LoginActivityPageObject();
        RESTMockServer.reset();
    }

    @Test
    public void shouldOpenOwnerHomeActivity() {
        RESTMockServer.whenPOST(RequestMatchers.pathEndsWith(LOGIN_PATH))
                .thenReturnFile(USER_JSON);
        rule.launchActivity(null);
        pageObject.typeEmail(EMAIL);
        pageObject.typePassword(PASSWORD);
        pageObject.pressLogin();
        intended(hasComponent(OwnerHomeActivity.class.getName()));
    }
}

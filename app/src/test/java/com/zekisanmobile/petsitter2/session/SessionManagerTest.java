package com.zekisanmobile.petsitter2.session;

import android.os.Build;

import com.zekisanmobile.petsitter2.BuildConfig;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class SessionManagerTest {

    private SessionManager sessionManager;

    @Before
    public void setup() {
        sessionManager = new SessionManager(RuntimeEnvironment.application);
    }

    @Test
    public void shouldSetUserId() {
        sessionManager.setUserId("asdasd");
        assertEquals("asdasd", sessionManager.getUserId());
    }

    @Test
    public void shouldSetTokenSentToServer() {
        sessionManager.setTokenSentToServer(true);
        assertEquals(true, sessionManager.getTokenSentToServer());
    }

    @Test
    public void shouldCleanAllEntries() {
        sessionManager.setUserId("asdasd");
        sessionManager.setTokenSentToServer(true);
        sessionManager.cleanAllEntries();
        assertEquals("asdasd", sessionManager.getUserId());
        assertEquals(false, sessionManager.getTokenSentToServer());
    }

    @After
    public void cleanSharedPreferences() {
        sessionManager.cleanAllEntries();
    }

}

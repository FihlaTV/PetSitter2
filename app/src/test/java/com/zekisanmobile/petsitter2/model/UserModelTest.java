package com.zekisanmobile.petsitter2.model;

import android.os.Build;

import com.zekisanmobile.petsitter2.BuildConfig;
import com.zekisanmobile.petsitter2.vo.User;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import io.realm.Realm;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
@PrepareForTest({Realm.class, UserModel.class})
public class UserModelTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    Realm mockRealm;

    UserModel mockUserModel;

    @Before
    public void setup() {
        mockStatic(Realm.class);
        mockStatic(UserModel.class);

        Realm mockRealm = PowerMockito.mock(Realm.class);
        UserModel mockUserModel = PowerMockito.mock(UserModel.class);

        when(Realm.getDefaultInstance()).thenReturn(mockRealm);

        this.mockRealm = mockRealm;
        this.mockUserModel = mockUserModel;
    }

    @Test
    public void shouldBeAbleToGetDefaultInstance() {
        assertThat(Realm.getDefaultInstance(), is(mockRealm));
    }

    @Test
    public void shouldBeAbleToCreateARealmObject() {
        User user = new User();
        when(mockRealm.createObject(User.class)).thenReturn(user);

        User output = mockRealm.createObject(User.class);

        assertThat(output, is(user));
    }

    @Test
    public void shouldBeAbleToCreateUser() {
        User user = mockRealm.createObject(User.class);

        User output = mockUserModel.save(new User());

        assertThat(output, is(user));
    }
}

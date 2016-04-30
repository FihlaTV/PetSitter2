package com.zekisanmobile.petsitter2.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.model.UserModel;
import com.zekisanmobile.petsitter2.session.SessionManager;

import butterknife.ButterKnife;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    private Realm realm;

    private UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        sessionManager = new SessionManager(this);

        realm = Realm.getDefaultInstance();

        userModel = new UserModel(realm);

        getLoggedUser();
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    private void getLoggedUser() {
        if (sessionManager.getUserId() != 0) {

        } else {

        }
    }
}

package com.zekisanmobile.petsitter2.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.model.UserModel;
import com.zekisanmobile.petsitter2.session.SessionManager;
import com.zekisanmobile.petsitter2.util.EntityType;
import com.zekisanmobile.petsitter2.view.login.LoginActivity;
import com.zekisanmobile.petsitter2.view.owner.OwnerHomeActivity;
import com.zekisanmobile.petsitter2.view.sitter.SitterHomeActivity;
import com.zekisanmobile.petsitter2.vo.User;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    private Realm realm;

    private UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            User user = userModel.find(sessionManager.getUserId());
            redirectUser(user);
        } else {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void redirectUser(User user) {
        Intent intent;
        switch (user.getEntityType()) {
            case EntityType.OWNER:
                intent = new Intent(MainActivity.this, OwnerHomeActivity.class);
                break;
            default: // EntityType.SITTER
                intent = new Intent(MainActivity.this, SitterHomeActivity.class);
                break;
        }
        intent.putExtra("user_id", user.getId());
        startActivity(intent);
        finish();
    }
}

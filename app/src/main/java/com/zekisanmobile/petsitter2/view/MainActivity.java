package com.zekisanmobile.petsitter2.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.model.UserModel;
import com.zekisanmobile.petsitter2.session.SessionManager;
import com.zekisanmobile.petsitter2.util.EntityType;
import com.zekisanmobile.petsitter2.view.login.LoginActivity;
import com.zekisanmobile.petsitter2.view.owner.OwnerHomeActivity;
import com.zekisanmobile.petsitter2.view.sitter.SitterHomeActivity;
import com.zekisanmobile.petsitter2.vo.Animal;
import com.zekisanmobile.petsitter2.vo.User;

import java.util.List;

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

        saveAnimalsToDB();
        getLoggedUser();
    }

    private void saveAnimalsToDB() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        if (sharedPreferences.getInt("flag", 0) == 0) {
            Log.i("LOG", "init()");
            sharedPreferences.edit().putInt("flag", 1).apply();

            String[] animalNames = this.getResources().getStringArray(R.array.animal_names);

            for (int i = 0; i < animalNames.length; i++) {
                realm.beginTransaction();
                Animal animal = realm.createObject(Animal.class);
                animal.setId(i + 1);
                animal.setName(animalNames[i]);
                realm.commitTransaction();
            }
        } else {
            List<Animal> animals = realm.where(Animal.class).findAll();
            for (Animal a : animals) {
                Log.i("LOG", "Animal: { id: " + a.getId() + ", name: " + a.getName() + " }");
            }
        }
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    private void getLoggedUser() {
        if (!sessionManager.getUserId().isEmpty()) {
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
        startActivity(intent);
        finish();
    }
}

package com.zekisanmobile.petsitter2.view.sitter;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.asyncTask.LogoutTask;
import com.zekisanmobile.petsitter2.model.SitterModel;
import com.zekisanmobile.petsitter2.model.UserModel;
import com.zekisanmobile.petsitter2.session.SessionManager;
import com.zekisanmobile.petsitter2.view.HomeView;
import com.zekisanmobile.petsitter2.view.login.LoginActivity;
import com.zekisanmobile.petsitter2.vo.Sitter;
import com.zekisanmobile.petsitter2.vo.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class SitterHomeActivity extends AppCompatActivity implements HomeView {

    private Realm realm;
    private User user;
    private UserModel userModel;
    private Sitter sitter;
    private SitterModel sitterModel;
    private SessionManager sessionManager;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitter_home);

        ButterKnife.bind(this);

        defineMembers();
        setupToolbar();
    }

    private void defineMembers() {
        realm = Realm.getDefaultInstance();
        userModel = new UserModel(realm);
        sitterModel = new SitterModel(realm);
        sessionManager = new SessionManager(this);
        long user_id = sessionManager.getUserId();
        user = userModel.find(user_id);
        sitter = sitterModel.find(user.getEntityId());
    }

    private void setupToolbar() {
        toolbar.setTitle(sitter.getName());
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.m_logout) {
            new LogoutTask(this, user.getId()).execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Application getPetSitterApp() {
        return getApplication();
    }

    @Override
    public Context getViewContext() {
        return this;
    }

    @Override
    public void redirectToLoginPage() {
        clearSession();
        Intent intent = new Intent(SitterHomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void clearSession() {
        sessionManager.cleanAllEntries();
    }
}

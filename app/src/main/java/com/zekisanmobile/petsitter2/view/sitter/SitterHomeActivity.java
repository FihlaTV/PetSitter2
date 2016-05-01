package com.zekisanmobile.petsitter2.view.sitter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.model.SitterModel;
import com.zekisanmobile.petsitter2.model.UserModel;
import com.zekisanmobile.petsitter2.session.SessionManager;
import com.zekisanmobile.petsitter2.vo.Sitter;
import com.zekisanmobile.petsitter2.vo.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class SitterHomeActivity extends AppCompatActivity {

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
        configureToolbar();
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

    private void configureToolbar() {
        toolbar.setTitle(sitter.getName());
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }
}

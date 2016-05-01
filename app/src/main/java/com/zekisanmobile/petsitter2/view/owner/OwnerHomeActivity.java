package com.zekisanmobile.petsitter2.view.owner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.model.OwnerModel;
import com.zekisanmobile.petsitter2.model.UserModel;
import com.zekisanmobile.petsitter2.session.SessionManager;
import com.zekisanmobile.petsitter2.vo.Owner;
import com.zekisanmobile.petsitter2.vo.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class OwnerHomeActivity extends AppCompatActivity {

    private Realm realm;
    private User user;
    private UserModel userModel;
    private Owner owner;
    private OwnerModel ownerModel;
    private SessionManager sessionManager;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_home);

        ButterKnife.bind(this);

        defineMembers();
        configureToolbar();

    }

    private void configureToolbar() {
        toolbar.setTitle(owner.getName());
    }

    private void defineMembers() {
        realm = Realm.getDefaultInstance();
        userModel = new UserModel(realm);
        ownerModel = new OwnerModel(realm);
        long user_id = sessionManager.getUserId();
        user = userModel.find(user_id);
        owner = ownerModel.find(user.getEntityId());
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }
}

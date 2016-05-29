package com.zekisanmobile.petsitter2.view.register.sitter;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.asyncTask.CreateSitterTask;
import com.zekisanmobile.petsitter2.model.SitterModel;
import com.zekisanmobile.petsitter2.util.Config;
import com.zekisanmobile.petsitter2.util.EntityType;
import com.zekisanmobile.petsitter2.view.owner.OwnerHomeActivity;
import com.zekisanmobile.petsitter2.view.register.RegisterView;
import com.zekisanmobile.petsitter2.view.sitter.SitterHomeActivity;
import com.zekisanmobile.petsitter2.vo.Sitter;
import com.zekisanmobile.petsitter2.vo.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class AboutMeRegisterActivity extends AppCompatActivity implements RegisterView {

    private Realm realm;
    private SitterModel sitterModel;
    private String sitterId;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.et_about_me)
    EditText etAboutMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me_register);

        ButterKnife.bind(this);

        this.sitterId = getIntent().getStringExtra(Config.SITTER_ID);

        configureToolbar();
        defineMembers();
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    @OnClick(R.id.btn_finish)
    public void finishRegistry() {
        saveSitter();
    }

    private void saveSitter() {
        realm.beginTransaction();
        Sitter sitter = sitterModel.find(sitterId);
        sitter.setAboutMe(etAboutMe.getText().toString().trim());
        realm.commitTransaction();
        String userId = realm.where(User.class).equalTo("entityId", sitterId).findFirst().getId();
        new CreateSitterTask(sitterId, userId, this).execute();
    }

    private void configureToolbar() {
        toolbar.setTitle(getString(R.string.write_about_you));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void defineMembers() {
        realm = Realm.getDefaultInstance();
        sitterModel = new SitterModel(realm);
    }

    @Override
    public Application getPetSitterApp() {
        return getApplication();
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public String getSenderId() {
        return getString(R.string.gcm_sender_id);
    }

    public void redirectUser(User user) {
        Intent intent;
        switch (user.getEntityType()) {
            case EntityType.OWNER:
                intent = new Intent(AboutMeRegisterActivity.this, OwnerHomeActivity.class);
                break;
            default: // EntityType.SITTER
                intent = new Intent(AboutMeRegisterActivity.this, SitterHomeActivity.class);
                break;
        }
        startActivity(intent);
        finish();
    }
}

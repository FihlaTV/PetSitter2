package com.zekisanmobile.petsitter2.view.register;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;

import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.util.Config;
import com.zekisanmobile.petsitter2.util.EntityType;
import com.zekisanmobile.petsitter2.util.UniqueID;
import com.zekisanmobile.petsitter2.vo.Owner;
import com.zekisanmobile.petsitter2.vo.Sitter;
import com.zekisanmobile.petsitter2.vo.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class BasicRegisterActivity extends AppCompatActivity {

    private String entityType;
    private String entityId;
    private String userId;
    private Realm realm;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.et_email)
    EditText etEmail;

    @BindView(R.id.et_name)
    EditText etName;

    @BindView(R.id.et_surname)
    EditText etSurname;

    @BindView(R.id.et_phone)
    EditText etPhone;

    @BindView(R.id.et_password)
    EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_register);

        ButterKnife.bind(this);

        this.entityType = getIntent().getStringExtra(EntityType.TYPE);

        defineMembers();
        configureToolbar();
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btn_next)
    public void registerBasic() {
        if (validateRegisterFields()) {
            saveUser();
        } else {
            showRegisterDialog(getString(R.string.fill_all_fields));
        }
    }

    private void defineMembers() {
        realm = Realm.getDefaultInstance();
    }

    private void saveUser() {
        switch (entityType) {
            case EntityType.OWNER:
                createOwner();
                break;
            case EntityType.SITTER:
                createSitter();
                break;
        }
        createUser();
        redirectToAddressRegister();
    }

    private void redirectToAddressRegister() {
        Intent intent = new Intent(BasicRegisterActivity.this, AddressRegisterActivity.class);
        intent.putExtra(Config.USER_ID, userId);
        startActivity(intent);
    }

    private void createUser() {
        realm.beginTransaction();
        User user = realm.createObject(User.class);
        user.setId(UniqueID.generateUniqueID());
        user.setEmail(etEmail.getText().toString().trim());
        user.setPassword(etPassword.getText().toString().trim());
        user.setEntityType(entityType);
        user.setEntityId(entityId);
        realm.commitTransaction();

        this.userId = user.getId();
    }

    private void createSitter() {
        realm.beginTransaction();
        Sitter sitter = realm.createObject(Sitter.class);
        sitter.setId(UniqueID.generateUniqueID());
        sitter.setName(etName.getText().toString().trim());
        sitter.setSurname(etSurname.getText().toString().trim());
        sitter.setPhone(etPhone.getText().toString().trim());
        realm.commitTransaction();

        entityId = sitter.getId();
    }

    private void createOwner() {
        realm.beginTransaction();
        Owner owner = realm.createObject(Owner.class);
        owner.setId(UniqueID.generateUniqueID());
        owner.setName(etName.getText().toString().trim());
        owner.setSurname(etSurname.getText().toString().trim());
        owner.setPhone(etPhone.getText().toString().trim());
        realm.commitTransaction();

        entityId = owner.getId();
    }

    private boolean validateRegisterFields() {
        if (TextUtils.isEmpty(etEmail.getText().toString().trim()) ||
                TextUtils.isEmpty(etName.getText().toString().trim()) ||
                TextUtils.isEmpty(etSurname.getText().toString().trim()) ||
                TextUtils.isEmpty(etPhone.getText().toString().trim()) ||
                TextUtils.isEmpty(etPassword.getText().toString().trim())
                ) {
            return false;
        }
        return true;
    }

    private void configureToolbar() {
        toolbar.setTitle(getString(R.string.register));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    public void showRegisterDialog(String message) {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        dialog.show();
        keepDialog(dialog);
    }

    private void keepDialog(Dialog dialog) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(layoutParams);
    }
}

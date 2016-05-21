package com.zekisanmobile.petsitter2.view.register;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;

import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.util.EntityType;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BasicRegisterActivity extends AppCompatActivity {

    private String entityType;

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

        configureToolbar();
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

    @OnClick(R.id.btn_register)
    public void register() {
        if (validateRegisterFields()) {
            switch (entityType) {
                case EntityType.OWNER:
                    break;
                case EntityType.SITTER:
                    break;
            }
        } else {
            showRegisterDialog(getString(R.string.fill_all_fields));
        }
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

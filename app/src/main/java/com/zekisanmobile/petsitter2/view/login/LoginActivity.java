package com.zekisanmobile.petsitter2.view.login;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.asyncTask.LoginTask;
import com.zekisanmobile.petsitter2.util.EntityType;
import com.zekisanmobile.petsitter2.view.owner.OwnerHomeActivity;
import com.zekisanmobile.petsitter2.view.sitter.SitterHomeActivity;
import com.zekisanmobile.petsitter2.vo.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginView {

    @BindView(R.id.et_email)
    EditText etEmail;

    @BindView(R.id.et_password)
    EditText etPassword;

    @BindView(R.id.tv_brand)
    TextView tvBrand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        tvBrand.setText(Html.fromHtml("Pet<b>Care</b>"));
    }

    @OnClick(R.id.btn_login)
    public void login() {
        if (validateLoginFields()) {
            String[] params = { etEmail.getText().toString().trim(),
                    etPassword.getText().toString().trim() };
            new LoginTask(this).execute(params);
        }
    }

    private boolean validateLoginFields() {
        if (TextUtils.isEmpty(etEmail.getText().toString().trim())) {
            showLoginDialog(getString(R.string.login_invalid_email));
            return false;
        }

        if (!validateEmail()) {
            showLoginDialog(getString(R.string.login_invalid_email));
            return false;
        }

        if (TextUtils.isEmpty(etPassword.getText().toString().trim())) {
            showLoginDialog(getString(R.string.login_invalid_password));
            return false;
        }

        return true;
    }

    public void showLoginDialog(String message) {
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

    public void redirectUser(User user) {
        Intent intent;
        switch (user.getEntityType()) {
            case EntityType.OWNER:
                intent = new Intent(LoginActivity.this, OwnerHomeActivity.class);
                break;
            default: // EntityType.SITTER
                intent = new Intent(LoginActivity.this, SitterHomeActivity.class);
                break;
        }
        startActivity(intent);
        finish();
    }

    private boolean validateEmail() {
       return Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches();
    }

    @Override
    public Application getPetSitterApp() {
        return getApplication();
    }

    @Override
    public String getSenderId() {
        return getString(R.string.gcm_sender_id);
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }
}

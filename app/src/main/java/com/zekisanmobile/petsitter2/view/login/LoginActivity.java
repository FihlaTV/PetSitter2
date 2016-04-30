package com.zekisanmobile.petsitter2.view.login;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.WindowManager;
import android.widget.EditText;

import com.zekisanmobile.petsitter2.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.et_email)
    EditText etEmail;

    @BindView(R.id.et_password)
    EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_login)
    public void login() {
        if (validateLoginFields()) {

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

    private void showLoginDialog(String message) {
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

    private boolean validateEmail() {
       return Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches();
    }
}

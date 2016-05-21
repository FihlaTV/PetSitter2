package com.zekisanmobile.petsitter2.view.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.util.EntityType;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterHomeActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_home);

        ButterKnife.bind(this);

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

    @OnClick(R.id.btn_owner)
    public void ownerRegister() {
        redirectToBasicRegister(EntityType.OWNER);
    }

    @OnClick(R.id.btn_pet_sitter)
    public void sitterRegister() {
        redirectToBasicRegister(EntityType.SITTER);
    }

    private void configureToolbar() {
        toolbar.setTitle(getString(R.string.welcome));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void redirectToBasicRegister(String entityType) {
        Intent intent = new Intent(RegisterHomeActivity.this, BasicRegisterActivity.class);
        intent.putExtra(EntityType.TYPE, entityType);
        startActivity(intent);
    }
}

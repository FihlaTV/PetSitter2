package com.zekisanmobile.petsitter2.view.register.owner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.zekisanmobile.petsitter2.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PetRegisterActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_register);

        ButterKnife.bind(this);

        configureToolbar();
    }

    private void configureToolbar() {
        toolbar.setTitle(getString(R.string.your_pets));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }
}

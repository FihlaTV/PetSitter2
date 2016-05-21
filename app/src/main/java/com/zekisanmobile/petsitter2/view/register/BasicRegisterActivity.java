package com.zekisanmobile.petsitter2.view.register;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.util.EntityType;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BasicRegisterActivity extends AppCompatActivity {

    private String entityType;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

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

    private void configureToolbar() {
        toolbar.setTitle(getString(R.string.register));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }
}

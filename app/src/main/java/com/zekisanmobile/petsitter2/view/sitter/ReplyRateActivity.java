package com.zekisanmobile.petsitter2.view.sitter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.zekisanmobile.petsitter2.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReplyRateActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_rate);

        ButterKnife.bind(this);

        setupToolbar();
    }

    private void setupToolbar() {
        toolbar.setTitle(getString(R.string.my_rate));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }
}

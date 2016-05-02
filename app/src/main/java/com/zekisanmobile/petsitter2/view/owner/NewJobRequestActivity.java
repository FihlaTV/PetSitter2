package com.zekisanmobile.petsitter2.view.owner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.model.SitterModel;
import com.zekisanmobile.petsitter2.util.Extra;
import com.zekisanmobile.petsitter2.vo.Sitter;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class NewJobRequestActivity extends AppCompatActivity {

    private long sitter_id;
    private Sitter sitter;
    private SitterModel sitterModel;

    private Realm realm;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_job_request);

        ButterKnife.bind(this);

        sitter_id = getIntent().getLongExtra(Extra.SITTER_ID, 0);

        defineMembers();
        configureToolbar();
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_job_request, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.m_save:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void defineMembers() {
        realm = Realm.getDefaultInstance();

        sitterModel = new SitterModel(realm);
        sitter = sitterModel.find(sitter_id);
    }

    private void configureToolbar() {
        toolbar.setTitle(getString(R.string.job_request_title));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
    }
}
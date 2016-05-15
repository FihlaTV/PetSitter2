package com.zekisanmobile.petsitter2.view.owner;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.adapter.ViewPagerAdapter;
import com.zekisanmobile.petsitter2.fragment.owner.SitterInfoFragment;
import com.zekisanmobile.petsitter2.fragment.owner.SitterRatesFragment;
import com.zekisanmobile.petsitter2.model.SitterModel;
import com.zekisanmobile.petsitter2.util.Extra;
import com.zekisanmobile.petsitter2.vo.Sitter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class SitterProfileActivity extends AppCompatActivity {

    private Realm realm;

    private Sitter sitter;
    private SitterModel sitterModel;

    private long sitter_id;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.iv_sitter)
    ImageView ivSitter;

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitter_profile);

        ButterKnife.bind(this);

        sitter_id = getIntent().getLongExtra("sitter_id", 0);

        defineMembers();
        configureToolbar();
        setupViewPager();
        setupTabLayout();
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    @OnClick(R.id.fab)
    public void requestJob() {
        Intent intent = new Intent(SitterProfileActivity.this, NewJobRequestActivity.class);
        intent.putExtra(Extra.SITTER_ID, sitter.getId());
        startActivity(intent);
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(SitterInfoFragment.newInstance(sitter_id), getString(R.string.tab_information));
        adapter.addFragment(SitterRatesFragment.newInstance(sitter_id), getString(R.string.tab_rate));
        viewPager.setAdapter(adapter);
    }

    private void setupTabLayout() {
        tabLayout.setupWithViewPager(viewPager);
    }

    private void defineMembers() {
        realm = Realm.getDefaultInstance();

        sitterModel = new SitterModel(realm);
        sitter = sitterModel.find(sitter_id);
    }

    private void configureToolbar() {
        toolbar.setTitle(sitter.getName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Picasso.with(this)
                .load(sitter.getPhotoUrl().getLarge())
                .into(ivSitter);
    }


}

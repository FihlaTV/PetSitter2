package com.zekisanmobile.petsitter2.view.owner;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.adapter.ViewPagerAdapter;
import com.zekisanmobile.petsitter2.fragment.SittersFragment;
import com.zekisanmobile.petsitter2.model.OwnerModel;
import com.zekisanmobile.petsitter2.model.UserModel;
import com.zekisanmobile.petsitter2.session.SessionManager;
import com.zekisanmobile.petsitter2.vo.Owner;
import com.zekisanmobile.petsitter2.vo.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class OwnerHomeActivity extends AppCompatActivity {

    private Realm realm;
    private User user;
    private UserModel userModel;
    private Owner owner;
    private OwnerModel ownerModel;
    private SessionManager sessionManager;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_home);

        ButterKnife.bind(this);

        defineMembers();
        setupToolbar();
        setupViewPager();
        setupTabLayout();
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    private void defineMembers() {
        realm = Realm.getDefaultInstance();
        userModel = new UserModel(realm);
        ownerModel = new OwnerModel(realm);
        sessionManager = new SessionManager(this);

        long user_id = sessionManager.getUserId();
        user = userModel.find(user_id);
        owner = ownerModel.find(user.getEntityId());
    }

    private void setupToolbar() {
        toolbar.setTitle(owner.getName());
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SittersFragment(), getString(R.string.tabs_pet_sitters));
        viewPager.setAdapter(adapter);
    }

    private void setupTabLayout() {
        tabLayout.setupWithViewPager(viewPager);
    }
}

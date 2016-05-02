package com.zekisanmobile.petsitter2.view.owner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.model.SitterModel;
import com.zekisanmobile.petsitter2.model.UserModel;
import com.zekisanmobile.petsitter2.session.SessionManager;
import com.zekisanmobile.petsitter2.vo.Sitter;
import com.zekisanmobile.petsitter2.vo.User;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
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

    @BindView(R.id.tv_value_hour)
    TextView tvValueHour;

    @BindView(R.id.tv_district)
    TextView tvDistrict;

    @BindView(R.id.tv_about_me)
    TextView tvAboutMe;

    @BindView(R.id.lv_pets)
    ListView lvPets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitter_profile);

        ButterKnife.bind(this);

        sitter_id = getIntent().getLongExtra("sitter_id", 0);

        defineMembers();
        configureToolbar();
        defineViewsValues();
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    private void defineViewsValues() {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        tvValueHour.setText(format.format(sitter.getValueHour()) + "/h");
        tvDistrict.setText(sitter.getDistrict());
        tvAboutMe.setText(sitter.getAboutMe());

        List<String> animalsForList = new ArrayList<>();
        for (int i = 0; i < sitter.getAnimals().size(); i++) {
            animalsForList.add(sitter.getAnimals().get(i).getName());
        }
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,
                animalsForList);
        lvPets.setAdapter(adapter);
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

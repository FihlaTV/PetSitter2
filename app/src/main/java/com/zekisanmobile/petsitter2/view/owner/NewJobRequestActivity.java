package com.zekisanmobile.petsitter2.view.owner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.model.SitterModel;
import com.zekisanmobile.petsitter2.util.CircleTransform;
import com.zekisanmobile.petsitter2.util.Extra;
import com.zekisanmobile.petsitter2.vo.Sitter;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class NewJobRequestActivity extends AppCompatActivity {

    private long sitter_id;
    private int selectedAnimalsCount;
    private Sitter sitter;
    private SitterModel sitterModel;

    private int year, month, day, hour, minute;
    private Calendar tDefault;
    private final int FLAG_START = 0;
    private final int FLAG_END = 1;
    private int flag;

    private Realm realm;

    private List<String> animals = new ArrayList<>();

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tv_sitter)
    TextView tvSitter;

    @BindView(R.id.iv_sitter)
    ImageView ivSitter;

    @BindView(R.id.et_date_start)
    EditText etDateStart;

    @BindView(R.id.et_date_final)
    EditText etDateFinal;

    @BindView(R.id.et_time_start)
    EditText etTimeStart;

    @BindView(R.id.et_time_final)
    EditText etTimeFinal;

    @BindView(R.id.tv_total_value)
    TextView tvTotalValue;

    @BindView(R.id.sp_animal)
    Spinner spAnimal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_job_request);

        ButterKnife.bind(this);

        sitter_id = getIntent().getLongExtra(Extra.SITTER_ID, 0);

        defineMembers();
        configureToolbar();
        setupViews();
        configureSpinner();
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

    private void setupViews() {
        tvSitter.setText(sitter.getName());
        Picasso.with(this)
                .load(sitter.getPhotoUrl().getLarge())
                .transform(new CircleTransform())
                .into(ivSitter);
    }

    private void configureSpinner() {
        String[] animalNames = this.getResources().getStringArray(R.array.animal_names);

        for (String a : animalNames) {
            animals.add(a);
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, animals);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAnimal.setAdapter(dataAdapter);

        View btRemoveAnimal = findViewById(R.id.bt_remove_animal);
        btRemoveAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callRemoveAnimal(v);
            }
        });
    }

    public void callAddAnimal(View view) {
        createPetForView(view, animals);
    }

    private void callRemoveAnimal(View view) {
        LinearLayout linearLayoutParent = (LinearLayout) view.getParent().getParent();

        if (linearLayoutParent.getChildCount() > 2) {
            linearLayoutParent.removeView((LinearLayout) view.getParent());
            calculateTotalValue();
        }
    }

    private void createPetForView(View view, List<String> animals) {
        LayoutInflater inflater = this.getLayoutInflater();
        LinearLayout linearLayoutChild = (LinearLayout) inflater.inflate(R.layout.box_animal, null);

        Spinner spAnimal = (Spinner) linearLayoutChild.findViewById(R.id.sp_animal);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, animals);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAnimal.setAdapter(dataAdapter);

        View btRemoveAnimal = linearLayoutChild.findViewById(R.id.bt_remove_animal);
        btRemoveAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callRemoveAnimal(view);
            }
        });

        float scale = getResources().getDisplayMetrics().density;
        int margin = (int) (5 * scale + 0.5f);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(margin, margin, margin, margin);
        linearLayoutChild.setLayoutParams(layoutParams);

        LinearLayout linearLayoutParent = (LinearLayout) view.getParent();
        linearLayoutParent.addView(linearLayoutChild, linearLayoutParent.getChildCount() - 2);

        calculateTotalValue();
    }

    private double calculateTotalValue() {
        updateSelectedAnimalsCount();
        double totalValue;
        if (canCalculateTotalValue() && selectedAnimalsCount > 0) {
            SimpleDateFormat formatterDate = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat formatterTime = new SimpleDateFormat("hh:mm");
            try {
                Date dateStart = formatterDate.parse(etDateStart.getText().toString());
                Date dateFinal = formatterDate.parse(etDateFinal.getText().toString());
                long days = ((dateFinal.getTime() - dateStart.getTime()) / (24 * 60 * 60 * 1000)) + 1;

                Date convertedTimeStart = formatterTime.parse(etTimeStart.getText().toString().replace("h", ":"));
                Date convertedTimeFinal = formatterTime.parse(etTimeFinal.getText().toString().replace("h", ":"));
                long minutes = (convertedTimeFinal.getTime() - (convertedTimeStart.getTime())) / 60000;

                double minuteValue = sitter.getValueHour() / 60;
                double totalMinutesValue = minutes * minuteValue;
                totalValue = totalMinutesValue * days * selectedAnimalsCount;
                tvTotalValue.setText(NumberFormat.getCurrencyInstance().format(totalValue));
                return totalValue;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    private void updateSelectedAnimalsCount() {
        /*View btAddAnimal = findViewById(R.id.bt_add_animal);
        LinearLayout linearLayoutParent = (LinearLayout) btAddAnimal.getParent();
        LinearLayout linearLayoutChild = (LinearLayout) linearLayoutParent.getChildAt(9);*/
        LinearLayout llAnimals = (LinearLayout) findViewById(R.id.ll_animals);
        selectedAnimalsCount = llAnimals.getChildCount() - 1;
    }

    private boolean canCalculateTotalValue() {
        if (etDateStart.getText() != null && etDateFinal.getText() != null
                && etTimeStart.getText() != null && etTimeFinal.getText() != null) return true;
        return false;
    }

    private String setDateOnTextView() {
        return (day < 10 ? "0" + day : day) + "/" +
                ((month + 1) < 10 ? "0" + (month + 1) : (month + 1)) + "/" + year;
    }

    private String setTimeOnTextView() {
        return (hour < 10 ? "0" + hour : hour) + "h" +
                (this.minute < 10 ? "0" + this.minute : this.minute);
    }

}
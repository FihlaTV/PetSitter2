package com.zekisanmobile.petsitter2.view.owner;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
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
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class NewJobRequestActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener, DialogInterface.OnCancelListener{

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
                if (isJobValid()) {

                } else {
                    showJobRequestDialog(getString(R.string.job_request_validation));
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        year = month = day = hour = minute = 0;
        etTimeStart.setText("");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        tDefault = Calendar.getInstance();
        tDefault.set(this.year, month, day, hour, minute);

        this.year = year;
        month = monthOfYear;
        day = dayOfMonth;

        switch (flag) {
            case FLAG_START:
                etDateStart.setText(setDateOnTextView());
                break;
            case FLAG_END:
                etDateFinal.setText(setDateOnTextView());
                break;
        }
        calculateTotalValue();
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        this.hour = hourOfDay;
        this.minute = minute;

        switch (flag) {
            case FLAG_START:
                etTimeStart.setText(setTimeOnTextView());
                break;
            case FLAG_END:
                etTimeFinal.setText(setTimeOnTextView());
                break;
        }
        calculateTotalValue();
    }

    @OnClick(R.id.et_date_start)
    public void doScheduleDateStart() {
        flag = FLAG_START;
        scheduleJobDate();
    }

    @OnClick(R.id.et_date_final)
    public void doScheduleDateFinal() {
        flag = FLAG_END;
        scheduleJobDate();
    }

    @OnClick(R.id.et_time_start)
    public void doScheduleTimeStart() {
        flag = FLAG_START;
        scheduleJobTime();
    }

    @OnClick(R.id.et_time_final)
    public void doScheduleTimeFinal() {
        flag = FLAG_END;
        scheduleJobTime();
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

        for (int i = 0; i < animalNames.length; i++) {
            for (int j =0; j < sitter.getAnimals().size(); j++) {
                if (sitter.getAnimals().get(j).getName().equalsIgnoreCase(animalNames[i])) {
                    animals.add(animalNames[i]);
                }
            }
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
        if (isJobValid() && selectedAnimalsCount > 0) {
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
        LinearLayout llAnimals = (LinearLayout) findViewById(R.id.ll_animals);
        selectedAnimalsCount = llAnimals.getChildCount() - 1;
    }

    private boolean isJobValid() {
        if (!TextUtils.isEmpty(etDateStart.getText().toString().trim())
                && !TextUtils.isEmpty(etDateFinal.getText().toString().trim())
                && !TextUtils.isEmpty(etTimeStart.getText().toString().trim())
                && !TextUtils.isEmpty(etTimeFinal.getText().toString().trim())) {
            return true;
        } else {
            return false;
        }
    }

    private void scheduleJobDate() {
        initDateData();
        Calendar calendarDefault = Calendar.getInstance();
        calendarDefault.set(year, month, day);
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                this,
                calendarDefault.get(Calendar.YEAR),
                calendarDefault.get(Calendar.MONTH),
                calendarDefault.get(Calendar.DAY_OF_MONTH)
        );

        Calendar cMin = Calendar.getInstance();
        Calendar cMax = Calendar.getInstance();

        cMax.set(cMax.get(Calendar.YEAR), 11, 31);
        datePickerDialog.setMinDate(cMin);
        datePickerDialog.setMaxDate(cMax);

        List<Calendar> daysList = new LinkedList<>();
        Calendar[] daysArray;
        Calendar cAux = Calendar.getInstance();
        while (cAux.getTimeInMillis() <= cMax.getTimeInMillis()) {
            if (cAux.get(Calendar.DAY_OF_WEEK) != 1 && cAux.get(Calendar.DAY_OF_WEEK) != 7) {
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(cAux.getTimeInMillis());

                daysList.add(c);
            }
            cAux.setTimeInMillis(cAux.getTimeInMillis() + (24 * 60 * 60 * 1000));
        }

        daysArray = new Calendar[daysList.size()];
        for (int i = 0; i < daysArray.length; i++) {
            daysArray[i] = daysList.get(i);
        }

        datePickerDialog.setSelectableDays(daysArray);
        datePickerDialog.setOnCancelListener(this);
        datePickerDialog.show(getFragmentManager(), "DatePickerDialog");
    }

    private void scheduleJobTime() {
        tDefault = Calendar.getInstance();
        initTimeData();
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                this,
                tDefault.get(Calendar.HOUR_OF_DAY),
                tDefault.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.setOnCancelListener(this);
        timePickerDialog.show(getFragmentManager(), "TimePickerDialog");
    }

    private void initDateData() {
        if (year == 0) {
            Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        }
    }

    private void initTimeData() {
        if (hour == 0) {
            Calendar c = Calendar.getInstance();
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
        }
    }

    private String setDateOnTextView() {
        return (day < 10 ? "0" + day : day) + "/" +
                ((month + 1) < 10 ? "0" + (month + 1) : (month + 1)) + "/" + year;
    }

    private String setTimeOnTextView() {
        return (hour < 10 ? "0" + hour : hour) + "h" +
                (this.minute < 10 ? "0" + this.minute : this.minute);
    }

    public void showJobRequestDialog(String message) {
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
}
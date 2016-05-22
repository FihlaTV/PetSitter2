package com.zekisanmobile.petsitter2.view.register;

import android.app.Application;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.asyncTask.LocationTask;
import com.zekisanmobile.petsitter2.model.UserModel;
import com.zekisanmobile.petsitter2.util.Config;
import com.zekisanmobile.petsitter2.vo.User;
import com.zekisanmobile.petsitter2.vo.ViaCep;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.realm.Realm;

public class AddressRegisterActivity extends AppCompatActivity
        implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationView {

    private String userId;
    private UserModel userModel;
    private User user;
    private Realm realm;

    private float latitude;
    private float longitude;

    private GoogleApiClient googleApiClient;
    private Location lastLocation;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.et_cep)
    EditText etCep;

    @BindView(R.id.btn_refresh_location)
    Button btnRefreshLocation;

    @BindView(R.id.et_street)
    EditText etStreet;

    @BindView(R.id.et_address_number)
    EditText etAddressNumber;

    @BindView(R.id.tv_district)
    TextView tvDistrict;

    @BindView(R.id.tv_city)
    TextView tvCity;

    @BindView(R.id.tv_state)
    TextView tvState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_register);

        ButterKnife.bind(this);

        this.userId = getIntent().getStringExtra(Config.USER_ID);

        defineMembers();
        configureToolbar();
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    @OnCheckedChanged(R.id.sw_my_location)
    public void getMyLocation(boolean isChecked) {
        if (isChecked) {
            btnRefreshLocation.setVisibility(View.INVISIBLE);
            buildGoogleApiClient();
            if(googleApiClient!= null){
                googleApiClient.connect();
            }
        } else {
            btnRefreshLocation.setVisibility(View.VISIBLE);
            cleanViews();
        }
    }

    @OnTextChanged(R.id.et_cep)
    public void onTextChanged(CharSequence text) {
        if (text.length() == 8) {
            btnRefreshLocation.setVisibility(View.VISIBLE);
        } else {
            btnRefreshLocation.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick(R.id.btn_refresh_location)
    public void refreshLocation() {
        if (!TextUtils.isEmpty(etCep.getText().toString().trim())) {
            new LocationTask(this, etCep.getText().toString().trim()).execute();
        }
    }

    private void defineMembers() {
        realm = Realm.getDefaultInstance();
        userModel = new UserModel(realm);
        user = userModel.find(userId);
    }

    private void configureToolbar() {
        toolbar.setTitle(getString(R.string.where_are_you));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(
                googleApiClient);

        if (lastLocation != null) {
            latitude = (float) lastLocation.getLatitude();
            longitude = (float) lastLocation.getLongitude();

            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                Address address = addresses.get(0);
                setupAddressViews(address.getPostalCode(), address.getThoroughfare(),
                        address.getFeatureName(), address.getSubLocality(), address.getLocality(),
                        address.getAdminArea());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        } catch (SecurityException e) {
            Toast.makeText(this, getString(R.string.failed_get_location), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, getString(R.string.failed_get_location), Toast.LENGTH_SHORT).show();
    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void cleanViews() {
        etCep.setText("");
        etStreet.setText("");
        etAddressNumber.setText("");
        tvDistrict.setText("Bairro");
        tvCity.setText("Cidade");
        tvState.setText("Estado");
    }

    public void setAddressFromCep(ViaCep viaCep) {
        setupAddressViews(viaCep.getCep(), viaCep.getLogradouro(), "", viaCep.getBairro(),
                viaCep.getLocalidade(), viaCep.getUf());
    }

    private void setupAddressViews(String cep, String street, String addressNumber, String district,
                                   String city, String state) {
        etCep.setText(cep.replace("-", ""));
        etStreet.setText(street);
        etAddressNumber.setText(addressNumber);
        tvDistrict.setText(district);
        tvCity.setText(city);
        tvState.setText(state);
    }

    @Override
    public Application getPetSitterApp() {
        return getApplication();
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }
}

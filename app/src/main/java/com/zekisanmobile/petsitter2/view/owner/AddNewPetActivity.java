package com.zekisanmobile.petsitter2.view.owner;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.squareup.picasso.Picasso;
import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.asyncTask.AddNewPetTask;
import com.zekisanmobile.petsitter2.model.OwnerModel;
import com.zekisanmobile.petsitter2.util.Config;
import com.zekisanmobile.petsitter2.util.UniqueID;
import com.zekisanmobile.petsitter2.view.login.LoginView;
import com.zekisanmobile.petsitter2.view.register.owner.PetListActivity;
import com.zekisanmobile.petsitter2.vo.Animal;
import com.zekisanmobile.petsitter2.vo.Owner;
import com.zekisanmobile.petsitter2.vo.Pet;
import com.zekisanmobile.petsitter2.vo.PhotoUrl;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.tajchert.nammu.Nammu;

public class AddNewPetActivity extends AppCompatActivity implements LoginView {

    private Realm realm;
    private OwnerModel ownerModel;
    private Owner owner;
    private String ownerId;
    private Pet pet;

    private Uri fileUri;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.sp_animals)
    Spinner spAnimals;

    @BindView(R.id.et_name)
    EditText etName;

    @BindView(R.id.et_age)
    EditText etAge;

    @BindView(R.id.sp_age_text)
    Spinner spAgeText;

    @BindView(R.id.sp_sizes)
    Spinner spSizes;

    @BindView(R.id.et_weight)
    EditText etWeight;

    @BindView(R.id.et_breed)
    EditText etBreed;

    @BindView(R.id.et_care)
    EditText etCare;

    @BindView(R.id.iv_photo)
    ImageView ivPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_register);

        ButterKnife.bind(this);

        this.ownerId = getIntent().getStringExtra(Config.OWNER_ID);

        EasyImage.configuration(this)
                .setImagesFolderName("PetCare")
                .saveInAppExternalFilesDir()
                .setCopyExistingPicturesToPublicLocation(true);

        configureToolbar();
        defineMembers();
        configureAnimalsSpinner();
        configureAgeTextSpinner();
        configureSizesSpinner();
    }

    @Override
    protected void onDestroy() {
        realm.close();
        EasyImage.clearConfiguration(this);
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @OnClick(R.id.iv_photo)
    public void choosePetPhoto() {
        selectImage();
    }

    @OnClick(R.id.btn_save)
    public void savePet() {
        if (fileUri != null) {
            if (validateFields()) {
                PhotoUrl photoUrl = savePhoto();
                realm.beginTransaction();
                Pet pet = realm.createObject(Pet.class);
                pet.setId(UniqueID.generateUniqueID());
                pet.setName(etName.getText().toString().trim());
                pet.setAge(Integer.parseInt(etAge.getText().toString().trim()));
                pet.setAgeText(spAgeText.getSelectedItem().toString());
                pet.setSize(spSizes.getSelectedItem().toString());
                pet.setWeight(Double.parseDouble(etWeight.getText().toString().trim()));
                pet.setBreed(etBreed.getText().toString().trim());
                pet.setPetCare(etCare.getText().toString().trim());
                pet.setAnimal(getAnimal());
                pet.setPhotoUrl(photoUrl);
                realm.commitTransaction();

                this.pet = pet;
                saveOwner();
                sendPetToApi(pet.getId());
            } else {
                showRegisterPhotoDialog("Por favor, preencha todos os campos.");
            }
        } else {
            showRegisterPhotoDialog("Por favor, escolha uma foto para o seu pet.");
        }
    }

    private boolean validateFields() {
        if (TextUtils.isEmpty(etName.getText().toString().trim()) ||
                TextUtils.isEmpty(etAge.getText().toString().trim()) ||
                TextUtils.isEmpty(etWeight.getText().toString().trim()) ||
                TextUtils.isEmpty(etBreed.getText().toString().trim())) {
            return false;
        }
        return true;
    }

    private void sendPetToApi(String petId) {
        new AddNewPetTask(this, ownerId, petId).execute();
    }

    public void redirectToPetList() {
        Intent intent = new Intent(AddNewPetActivity.this, MyPetsActivity.class);
        intent.putExtra(Config.OWNER_ID, ownerId);
        startActivity(intent);
        finish();
    }

    private void saveOwner() {
        realm.beginTransaction();
        owner.getPets().add(pet);
        realm.commitTransaction();
    }

    private PhotoUrl savePhoto() {
        realm.beginTransaction();
        PhotoUrl photoUrl = realm.createObject(PhotoUrl.class);
        photoUrl.setId(UniqueID.generateUniqueID());
        photoUrl.setImage(fileUri.toString());
        realm.commitTransaction();

        return photoUrl;
    }

    private Animal getAnimal() {
        return realm.where(Animal.class).equalTo("name",
                spAnimals.getSelectedItem().toString()).findFirst();
    }

    private void configureToolbar() {
        toolbar.setTitle(getString(R.string.register_pet));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void configureAnimalsSpinner() {
        String[] animals = getResources().getStringArray(R.array.animal_names);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, animals);
        spAnimals.setAdapter(adapter);
    }

    private void configureAgeTextSpinner() {
        String[] ageTexts = {"Meses", "Anos"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, ageTexts);
        spAgeText.setAdapter(adapter);
    }

    private void configureSizesSpinner() {
        String[] sizeTexts = {"Pequeno", "Médio", "Grande"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, sizeTexts);
        spSizes.setAdapter(adapter);
    }

    private void defineMembers() {
        realm = Realm.getDefaultInstance();
        ownerModel = new OwnerModel(realm);
        owner = ownerModel.find(ownerId);
    }

    private void selectImage() {
        EasyImage.openChooserWithGallery(this, "Escolha a foto do seu pet", 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                //Handle the image
                fileUri = Uri.fromFile(imageFile);
                onPhotoReturned(imageFile);
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                //Cancel handling, you might wanna remove taken photo if it was canceled
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(AddNewPetActivity.this);
                    if (photoFile != null) photoFile.delete();
                }
            }
        });
    }

    private void onPhotoReturned(File photoFile) {
        Picasso.with(this)
                .load(photoFile)
                .fit()
                .centerCrop()
                .into(ivPhoto);
    }

    public void showRegisterPhotoDialog(String message) {
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

    @Override
    public Application getPetSitterApp() {
        return getApplication();
    }

    @Override
    public String getSenderId() {
        return getString(R.string.gcm_sender_id);
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }
}

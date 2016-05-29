package com.zekisanmobile.petsitter2.view.register;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.model.UserModel;
import com.zekisanmobile.petsitter2.util.Config;
import com.zekisanmobile.petsitter2.util.EntityType;
import com.zekisanmobile.petsitter2.util.UniqueID;
import com.zekisanmobile.petsitter2.view.register.owner.PetListActivity;
import com.zekisanmobile.petsitter2.view.register.sitter.AnimalListActivity;
import com.zekisanmobile.petsitter2.vo.Owner;
import com.zekisanmobile.petsitter2.vo.PhotoUrl;
import com.zekisanmobile.petsitter2.vo.Sitter;
import com.zekisanmobile.petsitter2.vo.User;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.tajchert.nammu.Nammu;

public class ChoosePhotoRegisterActivity extends AppCompatActivity {

    private Realm realm;

    private Uri fileUri;

    private String userId;
    private UserModel userModel;
    private User user;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.iv_photo)
    ImageView ivPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_photo_register);

        ButterKnife.bind(this);

        EasyImage.configuration(this)
                .setImagesFolderName("PetCare")
                .saveInAppExternalFilesDir()
                .setCopyExistingPicturesToPublicLocation(true);

        this.userId = getIntent().getStringExtra(Config.USER_ID);

        defineMembers();
        configureToolbar();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        realm.close();
        EasyImage.clearConfiguration(this);
        super.onDestroy();
    }

    @OnClick(R.id.iv_photo)
    public void choosePhoto() {
        selectImage();
    }

    @OnClick(R.id.btn_next)
    public void registerPhoto() {
        realm.beginTransaction();
        PhotoUrl photoUrl = realm.createObject(PhotoUrl.class);
        photoUrl.setId(UniqueID.generateUniqueID());
        photoUrl.setImage(fileUri.toString());
        realm.commitTransaction();

        saveEntity(photoUrl);
    }

    private void redirectToRegisterPets() {
        Intent intent = new Intent(ChoosePhotoRegisterActivity.this, PetListActivity.class);
        intent.putExtra(Config.OWNER_ID, user.getEntityId());
        startActivity(intent);
    }

    private void redirectToRegisterAnimals() {
        Intent intent = new Intent(ChoosePhotoRegisterActivity.this, AnimalListActivity.class);
        intent.putExtra(Config.SITTER_ID, user.getEntityId());
        startActivity(intent);
    }

    private void saveEntity(PhotoUrl photoUrl) {
        switch (user.getEntityType()) {
            case EntityType.OWNER:
                saveOwner(photoUrl);
                redirectToRegisterPets();
                break;
            case EntityType.SITTER:
                saveSitter(photoUrl);
                redirectToRegisterAnimals();
                break;
        }
    }

    private void saveOwner(PhotoUrl photoUrl) {
        realm.beginTransaction();
        Owner owner = realm.where(Owner.class).equalTo("id", user.getEntityId()).findFirst();
        owner.setPhotoUrl(photoUrl);
        realm.commitTransaction();
    }

    private void saveSitter(PhotoUrl photoUrl) {
        realm.beginTransaction();
        Sitter sitter = realm.where(Sitter.class).equalTo("id", user.getEntityId()).findFirst();
        sitter.setPhotoUrl(photoUrl);
        realm.commitTransaction();
    }

    private void defineMembers() {
        realm = Realm.getDefaultInstance();
        userModel = new UserModel(realm);
        user = userModel.find(userId);
    }

    private void configureToolbar() {
        toolbar.setTitle(getString(R.string.choose_your_photo));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void selectImage() {
        EasyImage.openChooserWithGallery(this, "Escolha sua foto", 0);
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
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(ChoosePhotoRegisterActivity.this);
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
}

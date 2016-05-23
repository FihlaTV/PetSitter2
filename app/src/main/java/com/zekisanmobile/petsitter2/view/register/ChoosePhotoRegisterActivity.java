package com.zekisanmobile.petsitter2.view.register;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.util.Config;
import com.zekisanmobile.petsitter2.util.ImageUtility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class ChoosePhotoRegisterActivity extends AppCompatActivity {

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Realm realm;
    private String userChoosenTask;

    private Uri fileUri;

    private String userId;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.iv_photo)
    ImageView ivPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_photo_register);

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

    @OnClick(R.id.iv_photo)
    public void choosePhoto() {
        selectImage();
    }

    private void defineMembers() {
        realm = Realm.getDefaultInstance();
    }

    private void configureToolbar() {
        toolbar.setTitle(getString(R.string.choose_your_photo));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void selectImage() {
        final CharSequence[] items = {"Câmera", "Galeria", "Cancelar"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ChoosePhotoRegisterActivity.this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = ImageUtility.checkPermission(ChoosePhotoRegisterActivity.this);

                if (items[item].equals("Câmera")) {
                    userChoosenTask = "Câmera";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Galeria")) {
                    userChoosenTask = "Galeria";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancelar")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Selecione um arquivo"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case ImageUtility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Câmera"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Galeria"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ivPhoto.setImageBitmap(thumbnail);
        resizeImageView();
        fileUri = Uri.fromFile(getOutputMediaFile());
    }

    private File getOutputMediaFile() {
        File file = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Config.IMAGE_DIRECTORY
        );

        if (!file.exists()) {
            if (!file.mkdirs()) {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File picture = new File(file.getPath() + File.separator
        + "IMG_" + timeStamp + ".jpg");

        return picture;
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ivPhoto.setImageBitmap(bm);
        resizeImageView();
    }

    private void resizeImageView() {
        ivPhoto.getLayoutParams().height = 400;
    }
}

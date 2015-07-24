package com.tmobtech.tmobbeaconproject;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;
import com.tmobtech.tmobbeaconproject.entity.BeaconMap;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Deniz
 */

public class CameraActivity extends Activity implements View.OnClickListener {
    private static final String LOG_TAG = CameraActivity.class.getSimpleName();

    // Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int SELECT_PHOTO = 200;
    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "BeaconCamera";

    private Uri fileUri; // file url to store image

    private ImageView mImgPreview;
    private EditText mEditText;
    private BeaconMap mBeaconMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        initViews();

        String action = getIntent().getStringExtra(Intent.EXTRA_TEXT);
        mBeaconMap = new BeaconMap();

        // if action is camera
        if (action.equals(MainActivity.ACTION_CAMERA)) {
            captureImage();
        } else if (action.equals(MainActivity.ACTION_GALLERY)) { // if action is gallery
            pickFromGallery();
        }

        // Checking camera availability
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            finish();
        }
    }

    private void initViews() {
        mImgPreview = (ImageView) findViewById(R.id.imgPreview);
        mEditText = (EditText) findViewById(R.id.editText);
        Button mNextButton = (Button) findViewById(R.id.button_next);
        mNextButton.setOnClickListener(this);
    }

    /**
     * Checking device has camera hardware or not
     */
    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /**
     * Capturing Camera Image will lauch camera app requrest image capture
     */
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    private void pickFromGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }


    /**
     * Receiving activity result method will be called after closing the camera
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PHOTO)
            if (resultCode == RESULT_OK) {
                try {
                    Uri imageUri = data.getData();
                    String imagePath = getRealPathFromURI(imageUri);
                    previewCapturedImage(imagePath);
                    mBeaconMap.setImagePath(imagePath);
                    Log.d(LOG_TAG, "ImagePath from Gallery: " + imagePath);

                } catch (Exception e) {
                    Log.e("Gallery ge select error", e.getMessage());
                }
            } else if (resultCode == RESULT_CANCELED) {
                finish();
            }
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                previewCapturedImage(fileUri.toString());
                mBeaconMap.setImagePath(fileUri.toString());
                Log.d(LOG_TAG, "ImagePath from Camera: " + fileUri.toString());

            } else if (resultCode == RESULT_CANCELED) {
                finish();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return "file://" + cursor.getString(idx);
    }

    /**
     * Display image from a path to ImageView
     */
    private void previewCapturedImage(String imagePath) {
//        try {
            mImgPreview.setVisibility(View.VISIBLE);
            Picasso.with(this).load(imagePath).fit().centerInside().into(mImgPreview);
    }


    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_next:
                handleEvent();
                break;
        }
    }

    private void handleEvent() {
        if (mEditText.getText().toString().equals("")) {
            Toast.makeText(this, "Please give a name for your map", Toast.LENGTH_LONG).show();
        } else {
            mBeaconMap.setName(mEditText.getText().toString());
            save();
            Intent intent = new Intent(this, PlaceBeaconActivity.class);
            intent.putExtra("mapId", mBeaconMap.getObjectId());

            startActivity(intent);
            finish();
        }
    }

    private void save()  {
        try {
            mBeaconMap.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.e("getObjectId", mBeaconMap.getObjectId() + "");

    }
}
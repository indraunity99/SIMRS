package com.mobile.rsupsanglah.simrsmobile.activity.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.mobile.rsupsanglah.simrsmobile.R;
import com.mobile.rsupsanglah.simrsmobile.activity.control.CameraUtils;
import com.mobile.rsupsanglah.simrsmobile.activity.control.HttpRequest;
import com.mobile.rsupsanglah.simrsmobile.activity.control.SharedPreference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;

import com.mobile.rsupsanglah.simrsmobile.activity.control.SimrsmConstant;

public class ImagePreviewActivity extends AppCompatActivity {

    // Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;

    // key to store image path in savedInstance state
    public static final String KEY_IMAGE_STORAGE_PATH = "image_path";

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    // Bitmap sampling size
    public static final int BITMAP_SAMPLE_SIZE = 8;

    // Gallery directory name to store the images or videos
    public static final String GALLERY_DIRECTORY_NAME = "Hello Camera";

    // Image and Video file extensions
    public static final String IMAGE_EXTENSION = "jpg";

    private static String imageStoragePath;

    private TextView txtDescription;
    private ImageView imgPreview;
    private VideoView videoPreview;
    private Button btnCapturePicture, btnRecordVideo;
    Bitmap bitmap;
    String encodedString, fileName;
    private static Context context;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImagePreviewActivity.context = getApplicationContext();
        setContentView(R.layout.activity_preview_camera);


        txtDescription = findViewById(R.id.txt_desc);
        imgPreview = findViewById(R.id.imgPreview);
        videoPreview = findViewById(R.id.videoPreview);
        btnCapturePicture = findViewById(R.id.btnCapturePicture);
        btnRecordVideo = findViewById(R.id.btnRecordVideo);

        txtDescription.setText("No Picture Available");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Image Preview");

        restoreFromBundle(savedInstanceState);

        imageStoragePath = getIntent().getStringExtra("ImagePath");

        btnCapturePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toastMessage("Uploading...");
                saveImage();
            }
        });

        btnRecordVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), ViewResponKeluhanActivity.class);
                myIntent.putExtra("item_click", SharedPreference.getInstance(getApplication()).getActtoFrag());
                startActivity(myIntent);
                finish();
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ImagePreviewActivity.this, ViewResponKeluhanActivity.class);
                intent.putExtra("item_click", SharedPreference.getInstance(getApplication()).getActtoFrag());
                startActivity(intent);
                finish();
            }
        });

    }

    /**
     * Restoring store image path from saved instance state
     */
    private void restoreFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(KEY_IMAGE_STORAGE_PATH)) {
                imageStoragePath = savedInstanceState.getString(KEY_IMAGE_STORAGE_PATH);
                if (!TextUtils.isEmpty(imageStoragePath)) {
                    if (imageStoragePath.substring(imageStoragePath.lastIndexOf(".")).equals("." + IMAGE_EXTENSION)) {
                        previewCapturedImage();
                    }
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putString(KEY_IMAGE_STORAGE_PATH, imageStoragePath);
    }

    /**
     * Restoring image path from saved instance state
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        imageStoragePath = savedInstanceState.getString(KEY_IMAGE_STORAGE_PATH);
    }

    private void previewCapturedImage() {
        try {
            // hide video preview
            txtDescription.setVisibility(View.GONE);
            videoPreview.setVisibility(View.GONE);

            imgPreview.setVisibility(View.VISIBLE);

            Bitmap bitmap = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath);

            ExifInterface ei = new ExifInterface(imageStoragePath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    bitmap = rotateImage(bitmap, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    bitmap = rotateImage(bitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    bitmap = rotateImage(bitmap, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    bitmap = bitmap;
            }
            imgPreview.setImageBitmap(bitmap);
            String fileNameSegments[] = imageStoragePath.split("/");
            fileName = fileNameSegments[fileNameSegments.length - 1];
            Log.e("FFF", fileName);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            toastMessage(e.getMessage());
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }


    @Override
    protected void onResume() {
        super.onResume();
        previewCapturedImage();
    }

    public static Context getAppContext() {
        return ImagePreviewActivity.context;
    }

    private void saveImage() {
        final String ipAddress = SharedPreference.getInstance(getApplicationContext()).getIpAddress();
        @SuppressLint("StaticFieldLeak")
        class saveImage extends AsyncTask<Void, Void, String> {
            private final WeakReference<ImagePreviewActivity> mActivityRef;

            private saveImage(ImagePreviewActivity activity) {
                mActivityRef = new WeakReference<>(activity);
            }

            protected String doInBackground(Void[] params) {
                String response = "";
                BitmapFactory.Options options = null;
                options = new BitmapFactory.Options();
                options.inSampleSize = 3;
                bitmap = BitmapFactory.decodeFile(imageStoragePath, options);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Must compress the Image to reduce image size to make upload easy
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                byte[] byte_arr = stream.toByteArray();
                // Encode Image to String
                encodedString = Base64.encodeToString(byte_arr, 0);

                HashMap<String, String> map = new HashMap<>();
                map.put("image", "data:image/jpg;base64," + encodedString);
                map.put("filename", (fileName));

                try {
                    HttpRequest req = new HttpRequest(ipAddress + SimrsmConstant.ServiceType.URLSAVEIMAGE);/////////
                    response = req.prepare(HttpRequest.Method.POST).withData(map).sendAndReadString();
                } catch (IOException e) {
                    response = e.getMessage();
                }
                return response;
            }

            protected void onPostExecute(String result) {
                onTaskCompletedFinish(result);
            }
        }
        new saveImage(ImagePreviewActivity.this).execute();
    }

    public void onTaskCompletedFinish(String response) {
        if (response.contains("sukses")) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 2s = 2000ms
                    Intent intent = new Intent(getApplicationContext(), ViewResponKeluhanActivity.class);
                    intent.putExtra("item_click", SharedPreference.getInstance(getApplication()).getActtoFrag());
                    toastMessage("Upload Success");
                    startActivity(intent);
                    finish();
                }
            }, 2000);
        } else {
            toastMessage("Sorry! Something Error!");
        }
    }

    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(getApplicationContext(), ViewResponKeluhanActivity.class);
        myIntent.putExtra("item_click", SharedPreference.getInstance(getApplication()).getActtoFrag());
        startActivity(myIntent);
        finish();
    }

}
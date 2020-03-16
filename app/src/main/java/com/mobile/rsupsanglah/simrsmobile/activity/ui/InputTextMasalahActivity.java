package com.mobile.rsupsanglah.simrsmobile.activity.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobile.rsupsanglah.simrsmobile.R;
import com.mobile.rsupsanglah.simrsmobile.activity.control.HttpRequest;
import com.mobile.rsupsanglah.simrsmobile.activity.control.SharedPreference;
import com.mobile.rsupsanglah.simrsmobile.activity.control.SimrsmConstant;
import com.mobile.rsupsanglah.simrsmobile.activity.model.listJenisPerbaikan;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;

public class InputTextMasalahActivity extends AppCompatActivity {
    EditText editText;
    Button button;
    private final int jsoncode = 1;
    Toolbar toolbar;
    String jenis, responseNull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_text_masalah);
        editText = findViewById(R.id.editText);
        button = findViewById(R.id.submit_button);
        final String appUser = SharedPreference.getInstance(getApplicationContext()).getAppUser();
        Log.d("INPUTTEXTMASALAH", appUser);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Uraian Masalah/Keluhan");

        Intent intent = getIntent();
        jenis = intent.getStringExtra("jenis_perbaikan");
        responseNull = intent.getStringExtra("response");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().equals("")) {
                    toastMessage("Please fill out this field");
                } else {
                    listJenisPerbaikan listJenisPerbaikan = new listJenisPerbaikan();
                    listJenisPerbaikan.getMenuId();
                    Intent intent = getIntent();
                    String idOrder = intent.getStringExtra("ID");
                    sendOrderPerbaikan(appUser, editText.getText().toString(), SharedPreference.getInstance(getApplicationContext()).getMenuId(), idOrder);
                }
            }
        });
    }

    private void sendOrderPerbaikan(final String appUser, final String inputText, final String ID, final String sub_order) {
        final String ipAddress = SharedPreference.getInstance(getApplicationContext()).getIpAddress();
        @SuppressLint("StaticFieldLeak")
        class sendOrderPerbaikan extends AsyncTask<Void, Void, String> {
            private final WeakReference<InputTextMasalahActivity> mActivityRef;

            private sendOrderPerbaikan(InputTextMasalahActivity activity) {
                mActivityRef = new WeakReference<>(activity);
            }

            protected String doInBackground(Void[] params) {
                String response = "";
                HashMap<String, String> map = new HashMap<>();
                try {
                    HttpRequest req = new HttpRequest(ipAddress + SimrsmConstant.ServiceType.URLINPUTTEXT + appUser + "&order=" + ID + "&sub_order=" + sub_order + "&notes=" + inputText.replace(" ", "%20"));
                    Log.e("INPUT",ipAddress + SimrsmConstant.ServiceType.URLINPUTTEXT + appUser + "&order=" + ID + "&sub_order=" + sub_order + "&notes=" + inputText.replace(" ", "%20"));
                    response = req.prepare(HttpRequest.Method.POST).withData(map).sendAndReadString();
                } catch (IOException e) {
                    response = e.getMessage();
                }
                return response;
            }

            protected void onPostExecute(String result) {
                onTaskCompleted(result, jsoncode);
            }
        }
        new sendOrderPerbaikan(InputTextMasalahActivity.this).execute();
    }

    public void onTaskCompleted(String response, int serviceCode) {
        switch (serviceCode) {
            case jsoncode:
                if (response.contains("sukses")) {
                    toastMessage("Successfully saving data");
                    Intent intent = new Intent(InputTextMasalahActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    toastMessage("Error on saving data");
                }
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), InputTextMasalahActivity.class);
        startActivityForResult(myIntent, 0);
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (getIntent().hasExtra("response")) {
            Intent intent1 = new Intent(getApplicationContext(), JenisPerbaikanActivity.class);
            startActivity(intent1);
            finish();
        } else {
            Intent myIntent = new Intent(getApplicationContext(), OrderPerbaikanActivity.class);
            myIntent.putExtra("menu", jenis);
            startActivityForResult(myIntent, 0);
            finish();
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
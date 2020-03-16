package com.mobile.rsupsanglah.simrsmobile.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRreaderActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    private String fromwhere="", jenis="", pin="", nip="", phone="", id_="", timestamp_="", timestampDevice="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
        Bundle bundle = getIntent().getExtras();
        fromwhere = bundle.getString("FROM");
        jenis = bundle.getString("JENIS");
        pin= bundle.getString("PIN");
        nip= bundle.getString("NIP");
        phone= bundle.getString("PHONE");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Scan QR code dari menu pojok atas SIMARS Desktop")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        Button positiveButton = alert.getButton(android.app.AlertDialog.BUTTON_POSITIVE);



        positiveButton.setTextColor(Color.parseColor("#303F9F"));



    }
    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(final Result rawResult) {
        String scan = rawResult.getText(); // Prints scan results
        splitValue(scan);
        Long tsLong = System.currentTimeMillis()/1000;
        //timestampDevice = tsLong.toString();
        Log.v("TAG", rawResult.getBarcodeFormat().toString());
        if(fromwhere.equalsIgnoreCase("REGISTER"))
        {
            if(id_.equalsIgnoreCase(pin)) {
                Log.i("LONG STAT :", tsLong+">="+Long.parseLong(timestamp_)+"&&"+tsLong+"<="+(Long.parseLong(timestamp_)+(5 * 60 * 1000)));

                if(tsLong>=((Long.parseLong(timestamp_))-+(5 * 60 * 1000))&&tsLong<=((Long.parseLong(timestamp_)+(5 * 60 * 1000))))
                 {

                    Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                    i.putExtra("JENIS", jenis);
                    i.putExtra("FROM", "QR");
                    i.putExtra("PIN", pin);
                     if(jenis.equalsIgnoreCase("PEGAWAI")) {
                         i.putExtra("NIP", nip);
                     }
                     i.putExtra("PHONE", phone);
                    i.putExtra("SCAN", id_);
                    Log.i("JENIS", jenis);
                    Log.i("FROM", "REGISTER");
                    Log.i("PIN", pin);
                    if(jenis.equalsIgnoreCase("PEGAWAI")) {
                        Log.i("NIP", nip);
                    }
                    Log.i("PHONE", phone);
                    Log.i("SCAN", id_);
                    startActivity(i);
                    finish();
                }else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Barcode anda sudah expired, silahkan generate ulang")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    mScannerView.resumeCameraPreview(QRreaderActivity.this);
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                    Button positiveButton = alert.getButton(android.app.AlertDialog.BUTTON_POSITIVE);


                    // Change the alert dialog buttons text and background color
                    positiveButton.setTextColor(Color.parseColor("#303F9F"));
                    // positiveButton.setBackgroundColor(Color.parseColor("#FFE1FCEA"));


                }
            }else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Data anda tidak sesuai, silahkan ulangi kembali")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Intent i=new Intent(getApplicationContext(), RegisterChooseActivity.class);
                                startActivity(i);
                                finish();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                Button positiveButton = alert.getButton(android.app.AlertDialog.BUTTON_POSITIVE);


                positiveButton.setTextColor(Color.parseColor("#303F9F"));
            }
        }
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Data anda tidak sesuai, silahkan coba kembali")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            Intent i=new Intent(getApplicationContext(), RegisterChooseActivity.class);
                            startActivity(i);
                            finish();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }


       // mScannerView.resumeCameraPreview(this);
    }

    public void splitValue(String data) {
        String s = data;

        String[] arrayString = s.split(";");

        id_ = arrayString[0];
        timestamp_ = arrayString[1];

        id_ = id_.substring(id_.indexOf("ID:") + 3, id_.length());
        timestamp_ = timestamp_.substring(timestamp_.indexOf("TIMESTAMP:") + 10, timestamp_.length());

        System.out.println("ID---> " + id_);
        System.out.println("TIMESTAMP---> " + timestamp_);
    }

}
package com.mobile.rsupsanglah.simrsmobile.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.mobile.rsupsanglah.simrsmobile.R;
import com.mobile.rsupsanglah.simrsmobile.activity.control.HttpRequest;
import com.mobile.rsupsanglah.simrsmobile.activity.control.SimrsmConstant;
import com.mobile.rsupsanglah.simrsmobile.activity.control.SharedPreference;
import com.mobile.rsupsanglah.simrsmobile.activity.control.Utils;
import com.mobile.rsupsanglah.simrsmobile.activity.ui.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText _pinAbsen, _npp, _phone;
    Button _registrasi;
    public static final String mypreference = "installpref";
    public static final String status = "statusKey";
    public static String androidid = "androidIDKey";
    public static String buildserial = "buildSerialKey";
    public static String tempatTugas = "tempatTugas";
    SharedPreferences sharedpreferences;
    String android_id = "", build_serial = "";
    private String KEY_CON = "";
    String ipAddress = "";
    private final int jsoncode = 1;
    private boolean flag = false, isConnected = false;
    String pinAbsen = "";
    String nip = "";
    String phonenumber = "";
    String model = "";
    String brand = "", getTempatTugas = "";
    String ip = "";
    ProgressDialog progressDialog;
    String ipFromServer = "", valuedel = "no", jenisreg = "";
    public static String ipfromserver = "ipKey";
    String QRresult = "", fromwhere = "", pin = "", phone = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        jenisreg = bundle.getString("JENIS");

        if (jenisreg.equalsIgnoreCase("PEGAWAI")) {
            setContentView(R.layout.activity_register);
            fromwhere = bundle.getString("FROM");
            pin = bundle.getString("PIN");
            nip = bundle.getString("NIP");
            phone = bundle.getString("PHONE");
            QRresult = bundle.getString("SCAN");

            _registrasi = findViewById(R.id.btn_signup);
            _pinAbsen = findViewById(R.id.input_name);
            _npp = findViewById(R.id.input_email);
            _phone = findViewById(R.id.input_phone);
            _pinAbsen.setText(pin);
            _npp.setText(nip);
            _phone.setText(phone);

        } else {
            setContentView(R.layout.activity_register_residen);
            fromwhere = bundle.getString("FROM");
            pin = bundle.getString("PIN");
            phone = bundle.getString("PHONE");
            QRresult = bundle.getString("SCAN");


            _registrasi = findViewById(R.id.btn_signup);
            _pinAbsen = findViewById(R.id.input_name);
            _npp = findViewById(R.id.input_email);
            _phone = findViewById(R.id.input_phone);
            _pinAbsen.setText(pin);
            _phone.setText(phone);
        }


        if (fromwhere == null) {
            fromwhere = "REGISTER";
        }
        if (fromwhere.equalsIgnoreCase("QR")) {

            Log.i("jenis : ", jenisreg);
            Log.i("fromwhere", fromwhere);
            Log.i("pin : ", pin);
            Log.i("nip : ", nip);
            Log.i("phone :", phone);
            Log.i("QRcode : ", QRresult);

            _registrasi = findViewById(R.id.btn_signup);
            _pinAbsen = findViewById(R.id.input_name);
            _npp = findViewById(R.id.input_email);
            _phone = findViewById(R.id.input_phone);
            _pinAbsen.setText(pin);
            if (jenisreg.equalsIgnoreCase("PEGAWAI")) {
                _npp.setText(nip);
            }
            _phone.setText(phone);
        }

        if (QRresult != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
            builder.setMessage("Anda tercatat pada sistem, silahkan lanjutkan registrasi");

            // Set the positive button
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialoginterface, int i) {
                    if (!edittextValidation()) {
                    } else {
                        try {
                            GetName();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            AlertDialog dialog = builder.create();
            dialog.setCancelable(false);
            // Finally, display the alert dialog
            dialog.show();

            // Get the alert dialog buttons reference
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            Button neutralButton = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);

            // Change the alert dialog buttons text and background color
            positiveButton.setTextColor(Color.parseColor("#303F9F"));
            // positiveButton.setBackgroundColor(Color.parseColor("#FFE1FCEA"));

            negativeButton.setTextColor(Color.parseColor("#303F9F"));
            //negativeButton.setBackgroundColor(Color.parseColor("#FFFCB9B7"));

            neutralButton.setTextColor(Color.parseColor("#303F9F"));
            //neutralButton.setBackgroundColor(Color.parseColor("#FFD9E9FF"));

        }


        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        ipFromServer = sharedpreferences.getString(ipfromserver, "");
        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        //android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
        //      Settings.Secure.ANDROID_ID);
        model = Build.MODEL;
        brand = Build.BRAND;
        if (model.equalsIgnoreCase("") || brand.equalsIgnoreCase("")) {
            model = "generatebysistem";
            brand = "Sanglahphone";
        }

        chkStatus();


        _registrasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (QRresult == null) {
                    Intent intentQr = new Intent(RegisterActivity.this, QRreaderActivity.class);
                    intentQr.putExtra("FROM", "REGISTER");
                    intentQr.putExtra("JENIS", jenisreg);
                    intentQr.putExtra("PIN", _pinAbsen.getText().toString());
                    if (jenisreg.equalsIgnoreCase("PEGAWAI")) {
                        intentQr.putExtra("NIP", _npp.getText().toString());
                    }
                    intentQr.putExtra("PHONE", _phone.getText().toString());
                    startActivity(intentQr);
                    finish();
                } else if (QRresult.equalsIgnoreCase(_pinAbsen.getText().toString())) {

                } else {

                }

            }
        });
    }

    private boolean edittextValidation() {
        String middleName = "";
        String firstName = _pinAbsen.getText().toString();

        if (jenisreg.equalsIgnoreCase("PEGAWAI")) {
            middleName = _npp.getText().toString();
        }
        String lastName = _phone.getText().toString();
        if (firstName.equals("")) {
            if (jenisreg.equalsIgnoreCase("PEGAWAI")) {
                Toast.makeText(getApplicationContext(), " Anda Belum Mengisi PIN ABSEN",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), " Anda Belum Mengisi ID LOGIN",
                        Toast.LENGTH_SHORT).show();
            }
            return false;
        } else if (jenisreg.equalsIgnoreCase("PEGAWAI") && middleName.equals("")) {
            Toast.makeText(getApplicationContext(), " Anda Belum Mengisi NIP / NPP",
                    Toast.LENGTH_SHORT).show();

            return false;
        } else if (lastName.equals("")) {
            Toast.makeText(getApplicationContext(), " ANDA BELUM MENGISI NO TELEPON",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }


    private void storeToken(String token) {
        SharedPreference.getInstance(getApplicationContext()).saveDeviceToken(token);
    }

    public void genToken() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(RegisterActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                //Log.e("newToken",newToken);
                storeToken(newToken);
            }
        });
    }

    /***
     * POST DATA PIN
     * @throws IOException
     * @throws JSONException
     */

    //login using nip and pin then redirect to main act
    private void postDataPin(final String pin_, final String nip) throws IOException, JSONException {
        if (!Utils.isNetworkAvailable(RegisterActivity.this)) {
            Toast.makeText(RegisterActivity.this, "Internet is required!", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage("Harap menunggu..."); // Setting Message
        progressDialog.setTitle("Cek Data Pegawai"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        // Utils.showSimpleProgressDialog(RegisterActivity.this);
        @SuppressLint("StaticFieldLeak")
        class dataPin extends AsyncTask<Void, Void, String> {
            private final WeakReference<RegisterActivity> mActivityRef;

            private dataPin(RegisterActivity activity) {
                mActivityRef = new WeakReference<>(activity);
            }

            protected String doInBackground(Void[] params) {
                String response = "";
                HashMap<String, String> map = new HashMap<>();
                try {
                    HttpRequest req = new HttpRequest(ipAddress + SimrsmConstant.ServiceType.URLPIN + pin_ + "&" + "nip=" + nip);
                    Log.i("REQUEST --->", ipAddress + SimrsmConstant.ServiceType.URLPIN + pin_ + "&" + "nip=" + nip);
                    response = req.prepare(HttpRequest.Method.POST).withData(map).sendAndReadString();
                } catch (Exception e) {
                    response = e.getMessage();
                }
                return response;
            }

            protected void onPostExecute(String result) {
                onTaskCompletedPin(result, jsoncode);
            }
        }
        new dataPin(RegisterActivity.this).execute();
    }

    public void onTaskCompletedPin(String response, int serviceCode) {
//            //Log.d("responsejson", response);
        switch (serviceCode) {
            case jsoncode:
                if (response.contains("Succed")) {
                    progressDialog.dismiss();
                    try {
                        postDataDevice();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                    progressDialog.dismiss();
                    ViewDialog alert = new ViewDialog();
                    alert.showDialog(RegisterActivity.this, "PIN atau NPP tidak sesuai, silahkan coba kembali ! \n \n Silahkan hubungi Bagian SDM RSUP Sanglah");
                }
        }
    }

    /***
     * POST DATA DEVICE
     * @throws IOException
     * @throws JSONException
     */
    private void postDataDevice() throws IOException, JSONException {
        if (!Utils.isNetworkAvailable(RegisterActivity.this)) {
            Toast.makeText(RegisterActivity.this, "Internet is required!", Toast.LENGTH_SHORT).show();
            return;
        }
        final String token = SharedPreference.getInstance(RegisterActivity.this).getDeviceToken();
        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage("Harap Menunggu..."); // Setting Message
        progressDialog.setTitle("Cek device"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        @SuppressLint("StaticFieldLeak")
        class dataDevice extends AsyncTask<Void, Void, String> {
            private final WeakReference<RegisterActivity> mActivityRef;

            private dataDevice(RegisterActivity activity) {
                mActivityRef = new WeakReference<>(activity);
            }

            protected String doInBackground(Void[] params) {
                String response = "";
                HashMap<String, String> map = new HashMap<>();
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                    String currentDateandTime = sdf.format(new Date());
                    android_id = pinAbsen + currentDateandTime;
                    build_serial = pinAbsen + phonenumber;
                    String urlParse = ipAddress + SimrsmConstant.ServiceType.URLDEVICE
                            + android_id
                            + "&ser=" + build_serial
                            + "&token=" + token
                            + "&pid=" + pinAbsen
                            + "&mrk=" + Build.BRAND
                            + "&mod=" + Build.MODEL
                            + "&hp=" + phonenumber
                            + "&del=" + valuedel;
                    urlParse = urlParse.replaceAll(" ", "");
                    Log.i("URL REGIS", urlParse);
                    HttpRequest req = new HttpRequest(urlParse);
                    response = req.prepare(HttpRequest.Method.POST).withData(map).sendAndReadString();
                    SharedPreference.getInstance(getApplicationContext()).saveAppUser(pinAbsen);/////////////////////////////////////////////////////////////////////////////////////////////////
                } catch (Exception e) {
                    response = e.getMessage();
                }
                return response;
            }

            protected void onPostExecute(String result) {
                onTaskCompletedDevice(result, jsoncode);
            }
        }
        new dataDevice(RegisterActivity.this).execute();
    }

    public void onTaskCompletedDevice(String response, int serviceCode) {
        switch (serviceCode) {
            case jsoncode:
                if (response.contains("sukses")) {
                    progressDialog.dismiss();
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean(status, true);
                    editor.putString(androidid, android_id);
                    editor.putString(buildserial, build_serial);
                    editor.putString(tempatTugas, getTempatTugas);
                    editor.apply();
                    ViewDialog2 alert2 = new ViewDialog2();
                    alert2.showDialog2(RegisterActivity.this, "Selamat, device anda telah terdaftar !");
                    SharedPreference.getInstance(getApplicationContext()).saveTempatTugas(getTempatTugas);
//                    Log.e("TMPT TGS", SharedPreference.getInstance(getApplicationContext()).getTempatTugas());
                } else if (response.contains("registered")) {
                    String nameDevice = "";
                    String createDate = "";
                    String model = "";
                    progressDialog.dismiss();
                    try {
                        JSONObject jObj = new JSONObject(response);
                        nameDevice = recurseKeys(jObj, "brand");
                        createDate = recurseKeys(jObj, "create");
                        model = recurseKeys(jObj, "model");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ViewDialog alert = new ViewDialog();
                    alert.showDialog(RegisterActivity.this, "Saat ini anda teregistrasi menggunakan smartphone \n " +
                            "Jenis : " + nameDevice + "\n" +
                            "Model : " + model + "\n" +
                            "Tanggal : " + parseDateToddMMyyyy(createDate) + "\n\n" +

                            "Apakah anda ingin menghapus dan menggunakan smartphone ini sebagai data penggantinya ?");
                } else {
                    progressDialog.dismiss();
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean(status, false);
                    editor.putString(androidid, "");
                    editor.putString(buildserial, "");
                    editor.putString(tempatTugas, getTempatTugas);
                    editor.apply();
                }
        }
    }

    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd-MMM-yyyy h:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    void chkStatus() {
        final ConnectivityManager connMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifi.isConnectedOrConnecting()) {
            //genToken();
            KEY_CON = "Wifi";
            Toast.makeText(this, KEY_CON, Toast.LENGTH_LONG).show();
        } else if (mobile.isConnectedOrConnecting()) {
            //genToken();
            KEY_CON = "Mobile";
            Toast.makeText(this, KEY_CON, Toast.LENGTH_LONG).show();
        } else {
            KEY_CON = "No Network";
            Toast.makeText(this, KEY_CON, Toast.LENGTH_LONG).show();
        }
        if (KEY_CON.equalsIgnoreCase("Wifi")) {
            if (ip.contains("10.20.")) {/////////////////////////////////////////////////////////////////////////////////
                ipAddress = SimrsmConstant.ServiceType.LOCALIP;
            } else {
                ipAddress = "http://" + ipFromServer + ":9024";
            }
        } else {

            ipAddress = "http://" + ipFromServer + ":9024";
        }
    }

    private class ViewDialog {
        private void showDialog(RegisterActivity activity, String msg) {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog);

            TextView text = dialog.findViewById(R.id.text_dialog);
            text.setText(msg);

            Button dialogButton = dialog.findViewById(R.id.btn_dialog);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    valuedel = "yes";
                    try {
                        postDataDevice();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

            Button dialogCancel = dialog.findViewById(R.id.btn_dialogcancel);
            dialogCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            });

            dialog.show();

        }
    }

    private class ViewDialog2 {
        private void showDialog2(RegisterActivity activity, String msg) {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog2);

            TextView text = dialog.findViewById(R.id.text_dialog);
            text.setText(msg);

            Button dialogButton = dialog.findViewById(R.id.btn_dialog);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();

                }
            });
            dialog.show();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void GetName() throws IOException, JSONException {
        if (!Utils.isNetworkAvailable(RegisterActivity.this)) {
            Toast.makeText(RegisterActivity.this, "Internet is required!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            pinAbsen = _pinAbsen.getText().toString();
            if (jenisreg.equalsIgnoreCase("PEGAWAI")) {
                nip = _npp.getText().toString();
            }
            final String token = SharedPreference.getInstance(RegisterActivity.this).getDeviceToken();
            progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.setMessage("Harap menunggu..."); // Setting Message
            progressDialog.setTitle("Proses Registrasi"); // Setting Title
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setCancelable(false);
            @SuppressLint("StaticFieldLeak")
            class dataDevice extends AsyncTask<Void, Void, String> {
                private final WeakReference<RegisterActivity> mActivityRef;

                private dataDevice(RegisterActivity activity) {
                    mActivityRef = new WeakReference<>(activity);
                }

                protected String doInBackground(Void[] params) {
                    String response = "";
                    HashMap<String, String> map = new HashMap<>();
                    try {

                        String urlParse = ipAddress + SimrsmConstant.ServiceType.URLGETNAME
                                + pinAbsen + "&nid=" + nip;
                        Log.i("GET NAME (REGISTRASI):", urlParse);
                        HttpRequest req = new HttpRequest(urlParse);
                        response = req.prepare(HttpRequest.Method.POST).withData(map).sendAndReadString();
                    } catch (Exception e) {
                        response = e.getMessage();
                    }
                    return response;
                }

                protected void onPostExecute(String result) {
                    onTaskCompletedGetName(result, jsoncode);
                }
            }
            new dataDevice(RegisterActivity.this).execute();
        }
    }

    public void onTaskCompletedGetName(String response, int serviceCode) {
//            //Log.d("responsejson", response);
        progressDialog.dismiss();
        String extract = "";
        String tmp = "";
        switch (serviceCode) {
            case jsoncode:
                if (response.contains("Succed")) {
                    try {
                        JSONObject jObj = new JSONObject(response);
                        extract = recurseKeys(jObj, "nama");
                        tmp = recurseKeys(jObj, "tugas");
                        getTempatTugas = tmp;
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("namaUser", extract);
                        editor.putString("tempatTugas", tmp);
                        editor.apply();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //String result = json.getString("nama");    //result is key for which you need to retrieve data
                    //getnameresult = result;
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);

                    // Set a title for alert dialog
                    builder.setTitle("Verifikasi");

                    // Show a message on alert dialog
                    builder.setMessage("Anda adalah : " + extract + " \n" + "Tempat Tugas : " + tmp);


                    // Set the positive button
                    builder.setPositiveButton("Benar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            pinAbsen = _pinAbsen.getText().toString();
                            if (jenisreg.equalsIgnoreCase("PEGAWAI")) {
                                nip = _npp.getText().toString();
                            } else {
                                nip = "";
                            }

                            phonenumber = _phone.getText().toString();
//                            FirebaseMessaging.getInstance().subscribeToTopic("global");
                            try {
                                postDataPin(pinAbsen, nip);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            progressDialog.dismiss();
                        }
                    });
                    // Set the negative button
                    builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            dialoginterface.cancel();
                            progressDialog.dismiss();
                            Intent b = new Intent(getApplicationContext(), RegisterChooseActivity.class);
                            startActivity(b);
                            finish();

                        }
                    });
                    // Set the neutral button
                    builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            dialoginterface.cancel();
                            progressDialog.dismiss();
                            Intent b = new Intent(getApplicationContext(), RegisterChooseActivity.class);
                            startActivity(b);
                            finish();
                        }
                    });
                    // Create the alert dialog
                    AlertDialog dialog = builder.create();
                    dialog.setCancelable(false);
                    // Finally, display the alert dialog
                    dialog.show();

                    // Get the alert dialog buttons reference
                    Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                    Button neutralButton = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);

                    // Change the alert dialog buttons text and background color
                    positiveButton.setTextColor(Color.parseColor("#303F9F"));
                    // positiveButton.setBackgroundColor(Color.parseColor("#FFE1FCEA"));

                    negativeButton.setTextColor(Color.parseColor("#303F9F"));
                    //negativeButton.setBackgroundColor(Color.parseColor("#FFFCB9B7"));

                    neutralButton.setTextColor(Color.parseColor("#303F9F"));
                    //neutralButton.setBackgroundColor(Color.parseColor("#FFD9E9FF"));

                } else if (response.contains("failed")) {
                    progressDialog.dismiss();
                    ViewDialog alert = new ViewDialog();
                    alert.showDialog(RegisterActivity.this, "Maaf pin / nip tidak ditemukan");
                }


        }
    }

    public static String recurseKeys(JSONObject jObj, String findKey) throws JSONException {
        String finalValue = "";
        if (jObj == null) {
            return "";
        }

        Iterator<String> keyItr = jObj.keys();
        Map<String, String> map = new HashMap<>();

        while (keyItr.hasNext()) {
            String key = keyItr.next();
            map.put(key, jObj.getString(key));
        }

        for (Map.Entry<String, String> e : (map).entrySet()) {
            String key = e.getKey();
            if (key.equalsIgnoreCase(findKey)) {
                return jObj.getString(key);
            }

            // read value
            Object value = jObj.get(key);

            if (value instanceof JSONObject) {
                finalValue = recurseKeys((JSONObject) value, findKey);
            }
        }

        // key is not found
        return finalValue;
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {
        String uRl;

        AsyncTaskRunner(String url_) {
            this.uRl = url_;
        }


        @Override
        protected String doInBackground(String... params) {
            ConnectivityManager cm = (ConnectivityManager) getApplication().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL(uRl);   // Change to "http://google.com" for www  test.
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(5 * 1000);          // 5 s.
                    urlc.connect();
                    if (urlc.getResponseCode() == 200) {        // 200 = "OK" code (http connection is fine).
                        Log.wtf("Connection", "Success !");
                        return "true";
                    } else {
                        return "false";
                    }
                } catch (MalformedURLException e1) {
                    return "false";
                } catch (IOException e) {
                    return "false";
                }
            }
            return "false";
        }


        @Override
        protected void onPostExecute(String result) {
            isConnected = Boolean.valueOf(result);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

}

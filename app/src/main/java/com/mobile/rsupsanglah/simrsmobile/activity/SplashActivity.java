package com.mobile.rsupsanglah.simrsmobile.activity;


import com.mobile.rsupsanglah.simrsmobile.R;
import com.mobile.rsupsanglah.simrsmobile.activity.control.HttpRequest;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.mobile.rsupsanglah.simrsmobile.activity.control.SharedPreference;
import com.mobile.rsupsanglah.simrsmobile.activity.control.SimrsmConstant;
import com.mobile.rsupsanglah.simrsmobile.activity.ui.MainActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


public class SplashActivity extends AppCompatActivity {
    TextView tvSplash;
    boolean _status = false;
    public static final String mypreference = "installpref";
    String ipFromServer = "";
    public static String ipfromserver = "ipKey";
    public static String status = "statusKey";
    public static String androidid = "androidIDKey";
    public static String buildserial = "buildSerialKey";
    public static String ipnie = "";

    SharedPreferences sharedpreferences;
    private String KEY_CON = "";
    String ipAddress = "";
    String android_id = "", build_serial = "";
    private String URL = "";
    String version = "";
    boolean flagUpdate = false, isConnected = false;
    private GoogleApiClient googleApiClient;


    //----------------permission
    // lists for permissions
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private static final int ALL_PERMISSIONS_RESULT = 1011;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        version = getResources().getString(R.string.version);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        android_id = sharedpreferences.getString(androidid, "");
        build_serial = sharedpreferences.getString(buildserial, "");

        //menghilangkan ActionBar
        setContentView(R.layout.activity_splash);
        tvSplash = (TextView) findViewById(R.id.tvSplash);


        // we add permissions
        permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        permissions.add(android.Manifest.permission.INTERNET);
        permissions.add(android.Manifest.permission.ACCESS_NETWORK_STATE);
        permissions.add(android.Manifest.permission.ACCESS_WIFI_STATE);
        permissions.add(Manifest.permission.READ_PHONE_STATE);
        permissions.add(Manifest.permission.CAMERA);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.RECORD_AUDIO);
        permissionsToRequest = permissionsToRequest(permissions);

        chkStatus();
        DownloadWebPageTask task = new DownloadWebPageTask();
        task.execute(SimrsmConstant.ServiceType.URLGETIP);

        // Log.d("DEVICE KIRIM : ", details);

    }

    private boolean executeCommand(String url) {
        System.out.println("executeCommand");
        Runtime runtime = Runtime.getRuntime();
        try {
            Process mIpAddrProcess = Runtime.getRuntime().exec("/system/bin/ping -c 1 " + url);
            int mExitValue = mIpAddrProcess.waitFor();
            System.out.println(" mExitValue " + mExitValue);
            if (mExitValue == 0) {
                return true;
            } else {
                return false;
            }
        } catch (InterruptedException ignore) {
            ignore.printStackTrace();
            System.out.println(" Exception:" + ignore);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(" Exception:" + e);
        }
        return false;
    }

    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }

        return true;
    }

    private ArrayList<String> permissionsToRequest(ArrayList<String> wantedPermissions) {
        ArrayList<String> result = new ArrayList<>();

        for (String perm : wantedPermissions) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ALL_PERMISSIONS_RESULT:
                for (String perm : permissionsToRequest) {
                    if (!hasPermission(perm)) {
                        permissionsRejected.add(perm);
                    }
                }
                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            new AlertDialog.Builder(SplashActivity.this).
                                    setMessage("These permissions are mandatory to get your location. You need to allow them.").
                                    setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.
                                                        toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                                if (permissionsRejected.size() == 1) {

                                                }
                                            }
                                        }
                                    }).setNegativeButton("Cancel", null).create().show();

                            return;
                        }
                    }
                } else {
                    if (googleApiClient != null) {
                        googleApiClient.connect();
                    }
                    new AppVersion().execute();
                }
                break;
        }
    }


    public class CheckDevice extends AsyncTask<String, Void, String> {
        private static final String REQUEST_METHOD = "GET";
        private static final int READ_TIMEOUT = 15000;
        private static final int CONNECTION_TIMEOUT = 15000;
        int count = 0;

        @Override
        protected String doInBackground(String... params) {
            String result;
            String inputLine;
            try {
                //Create a URL object holding our url
                URL myUrl = new URL(ipAddress + SimrsmConstant.ServiceType.URLCHECKDEVICE + build_serial + "&device=" + android_id);
                Log.i("TAG VERSION : ", ipAddress + SimrsmConstant.ServiceType.URLCHECKDEVICE + build_serial + "&device=" + android_id);
                //Create a connection
                HttpURLConnection connection = (HttpURLConnection)
                        myUrl.openConnection();
                //Set methods and timeouts
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);

                //Connect to our url
                connection.connect();
                //Create a new InputStreamReader
                InputStreamReader streamReader = new
                        InputStreamReader(connection.getInputStream());
                //Create a new buffered reader and String Builder
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                //Check if the line we are reading is not null
                while ((inputLine = reader.readLine()) != null) {
                    stringBuilder.append(inputLine);
                }
                //Close our InputStream and Buffered reader
                reader.close();
                streamReader.close();
                //Set our result equal to our stringBuilder
                result = stringBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
                result = null;
            }
            return result;

        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result == null) {
                result = "Request Time Out";
            }

            String conv = result;

            if (conv.contains("Succed")) {
//                getUserIPPGS();
                //////////////////////////////////////////////////////////////////////////////////
                storeSharedPref(true);
                _status = true;
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            } else if (conv.contains("Failed")) {
                storeSharedPref(false);
                _status = false;
                startActivity(new Intent(getApplicationContext(), RegisterChooseActivity.class));
                //startActivity(new Intent(getApplicationContext(), MainActivity.class));

                finish();
            }

        }

    }

    public class AppVersion extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;
        int count = 0;

        @Override
        protected String doInBackground(String... params) {
            String result;
            String inputLine;
            try {
                //Create a URL object holding our url
                URL myUrl = new URL(ipAddress + SimrsmConstant.ServiceType.URLCHECKVERSION + version);
                Log.i("TAG VERSION : ", ipAddress + SimrsmConstant.ServiceType.URLCHECKVERSION + version);
                //Create a connection
                HttpURLConnection connection = (HttpURLConnection)
                        myUrl.openConnection();
                //Set methods and timeouts
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);

                //Connect to our url
                connection.connect();
                //Create a new InputStreamReader
                InputStreamReader streamReader = new
                        InputStreamReader(connection.getInputStream());
                //Create a new buffered reader and String Builder
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                //Check if the line we are reading is not null
                while ((inputLine = reader.readLine()) != null) {
                    stringBuilder.append(inputLine);
                }
                //Close our InputStream and Buffered reader
                reader.close();
                streamReader.close();
                //Set our result equal to our stringBuilder
                result = stringBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
                result = null;
            }
            Log.i("TAG VERSION : ", ipAddress + SimrsmConstant.ServiceType.URLCHECKVERSION + version);
            return result;

        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                if (result.equalsIgnoreCase("match")) {
                    flagUpdate = false;
                    new CheckDevice().execute();
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(SplashActivity.this, R.style.MyDialogTheme).create();
                    alertDialog.setTitle("Update Personal");
                    alertDialog.setMessage("Silahkan Perbaharui Aplikasi Personal Anda");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                    try {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(SimrsmConstant.ServiceType.URLMARKET + appPackageName)));
                                    } catch (android.content.ActivityNotFoundException anfe) {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(SimrsmConstant.ServiceType.URLPLAYSTORE + appPackageName)));
                                    }
                                }
                            });
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                    flagUpdate = true;
                }

            } catch (NullPointerException e) {

                if (count == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this, R.style.MyDialogTheme);

                    builder.setTitle("Notifikasi");
                    builder.setMessage("Ulangi proses...");

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing but close the dialog
                            DownloadWebPageTask task = new DownloadWebPageTask();
                            task.execute(SimrsmConstant.ServiceType.URLGETIP);
                            ipAddress = "http://" + ipFromServer + ":9024";
                            new AppVersion().execute();
                        }
                    });
                    count = +1;
                    AlertDialog alert = builder.create();
                    alert.show();
                } else if (count == 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this, R.style.MyDialogTheme);

                    builder.setTitle("Notifikasi");
                    builder.setMessage("Maaf, kami sedang ada gangguan teknis");

                    builder.setPositiveButton("Keluar", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing but close the dialog
                            finish();
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        }
    }

    void chkStatus() {
        final ConnectivityManager connMgr = (ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifi.isConnectedOrConnecting()) {
            KEY_CON = "Wifi";
        } else if (mobile.isConnectedOrConnecting()) {
            KEY_CON = "Mobile";
        } else {
            KEY_CON = "No Network";
        }
    }

    void storeSharedPref(boolean x) {
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(status, x);
        editor.commit();
    }

//    static public boolean isURLReachable(Context context, String url_) {
//
//    }


    public boolean serverCheck(String url) {

        boolean reachable = false;
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here
            try {
                reachable = InetAddress.getByName(url).isReachable(2000);
            } catch (IOException e) {
                e.printStackTrace();
                reachable = false;
            }

        }


        return reachable;
    }


    private class DownloadWebPageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            for (String url : urls) {
                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
                try {
                    HttpResponse execute = client.execute(httpGet);
                    InputStream content = execute.getEntity().getContent();

                    BufferedReader buffer = new BufferedReader(
                            new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("GET IP : ", result);

            WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
            String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
            try {
                ipFromServer = result;
            } catch (NullPointerException e) {
                AlertDialog alertDialog = new AlertDialog.Builder(SplashActivity.this).create();
                alertDialog.setTitle("Koneksi Internet");
                alertDialog.setMessage("Pastikan Internet device anda hidup dan memiliki paket data !");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                int pid = android.os.Process.myPid();
                                android.os.Process.killProcess(pid);
                                System.exit(0);
                            }
                        });
                alertDialog.show();
            }
            if (KEY_CON.equalsIgnoreCase("Wifi")) {
                if (ip.contains("10.20.")) {
                    ipAddress = SimrsmConstant.ServiceType.LOCALIP;
                } else {
                    ipAddress = "http://" + result + ":9024";
                }
            } else {
                ipAddress = "http://" + result + ":9024";
            }

            SharedPreference.getInstance(getApplicationContext()).saveipAddress(ipAddress);
            getUserIPPGS();

            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(ipfromserver, ipFromServer);
            editor.apply();

            if (flagUpdate == false) {

                //android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                //       Settings.Secure.ANDROID_ID);

                String details = "VERSION.RELEASE : " + Build.VERSION.RELEASE
                        + "\nVERSION.INCREMENTAL : " + Build.VERSION.INCREMENTAL
                        + "\nVERSION.SDK.NUMBER : " + Build.VERSION.SDK_INT
                        + "\nBOARD : " + Build.BOARD
                        + "\nBOOTLOADER : " + Build.BOOTLOADER
                        + "\nBRAND : " + Build.BRAND
                        + "\nCPU_ABI : " + Build.CPU_ABI
                        + "\nCPU_ABI2 : " + Build.CPU_ABI2
                        + "\nDISPLAY : " + Build.DISPLAY
                        + "\nFINGERPRINT : " + Build.FINGERPRINT
                        + "\nHARDWARE : " + Build.HARDWARE
                        + "\nHOST : " + Build.HOST
                        + "\nID : " + Build.ID
                        + "\nMANUFACTURER : " + Build.MANUFACTURER
                        + "\nMODEL : " + Build.MODEL
                        + "\nPRODUCT : " + Build.PRODUCT
                        + "\nSERIAL : " + build_serial
                        + "\nTAGS : " + Build.TAGS
                        + "\nTIME : " + Build.TIME
                        + "\nTYPE : " + Build.TYPE
                        + "\nUNKNOWN : " + Build.UNKNOWN
                        + "\nDevice ID : " + android_id
                        + "\nUSER : " + Build.USER;
                Log.d("STATUS SPLASH SCREEN :", details);
            } else {
                AlertDialog alertDialog = new AlertDialog.Builder(SplashActivity.this).create();
                alertDialog.setTitle("Update Personal");
                alertDialog.setMessage("Silahkan Perbaharui Aplikasi Personal Anda");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                            }
                        });
                alertDialog.show();
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (permissionsToRequest.size() > 0) {
                    requestPermissions(permissionsToRequest.toArray(
                            new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
                    Log.i("Masuk : ", String.valueOf(permissionsToRequest.size()));
                } else {
                    new CheckDevice().execute();
                }
            } else {
                Log.i("Masuk : ", "2");
                new CheckDevice().execute();
            }

        }
    }

    private void getUserIPPGS() {
        final String ipAddress = SharedPreference.getInstance(getApplicationContext()).getIpAddress();
        @SuppressLint("StaticFieldLeak")
        class getUserIPPGS extends AsyncTask<Void, Void, String> {
            private final WeakReference<SplashActivity> mActivityRef;

            private getUserIPPGS(SplashActivity activity) {
                mActivityRef = new WeakReference<>(activity);
            }
            protected String doInBackground(Void[] params) {
                String response = "";
                HashMap<String, String> map = new HashMap<>();
                try {
                    HttpRequest req = new HttpRequest(ipAddress + SimrsmConstant.ServiceType.URLAKSES);/////////
                    Log.e("getSPECIFICUSER", ipAddress + SimrsmConstant.ServiceType.URLAKSES);
                    response = req.prepare(HttpRequest.Method.POST).withData(map).sendAndReadString();
                } catch (IOException e) {
                    response = e.getMessage();
                }
                return response;
            }
            protected void onPostExecute(String result) {
                onTaskCompletedIPPGS(result);
            }
        }
        new getUserIPPGS(SplashActivity.this).execute();
    }

    public void onTaskCompletedIPPGS(String response) {
        Log.d("SPECIFIC USER", response);
        ArrayList<String> arrayList = new ArrayList<String>();
        if (!response.equals("")) {
            try {
                JSONObject obj = new JSONObject(response);
                JSONArray data = obj.getJSONArray("hak_akses");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject userObj = (JSONObject) data.get(i);
                    arrayList.add(userObj.getString("idpegawai"));
                    SharedPreference.getInstance(getApplicationContext()).saveArrayList(arrayList, "idpegawai");
                    Log.i("idpegawai ArrayList", SharedPreference.getInstance(getApplicationContext()).getArrayList("idpegawai").toString());
                }
            } catch (JSONException e) {
                response = e.getMessage();
                Log.e("JSON parsing error", response);
            }
        } else {
            Log.i("user logged is ", "users who have access");
        }
    }

}

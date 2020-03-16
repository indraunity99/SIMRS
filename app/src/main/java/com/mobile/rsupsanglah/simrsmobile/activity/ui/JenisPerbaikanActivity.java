package com.mobile.rsupsanglah.simrsmobile.activity.ui;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.rsupsanglah.simrsmobile.R;
import com.mobile.rsupsanglah.simrsmobile.activity.SplashActivity;
import com.mobile.rsupsanglah.simrsmobile.activity.adapter.listJenisPerbaikanAdapter;
import com.mobile.rsupsanglah.simrsmobile.activity.control.HttpRequest;
import com.mobile.rsupsanglah.simrsmobile.activity.control.SharedPreference;
import com.mobile.rsupsanglah.simrsmobile.activity.control.SimrsmConstant;
import com.mobile.rsupsanglah.simrsmobile.activity.helper.SimpleDividerItemDecoration;
import com.mobile.rsupsanglah.simrsmobile.activity.model.listJenisPerbaikan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

public class JenisPerbaikanActivity extends AppCompatActivity {
//    ListView listView;
    RecyclerView recyclerView;
    private Context context;
    TextView textView;
    Toolbar toolbar;
    String[] listItem;
    private ArrayList<listJenisPerbaikan> listJenisPerbaikanArrayList;
    private listJenisPerbaikanAdapter listJenisPerbaikanAdapter;


    // lists for permissions
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private static final int ALL_PERMISSIONS_RESULT = 1011;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jenisperbaikan);

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

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Jenis Order");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JenisPerbaikanActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });



        recyclerView = findViewById(R.id.recycler_view_jenis_perbaikan);
        listJenisPerbaikanArrayList = new ArrayList<>();

        listJenisPerbaikanAdapter = new listJenisPerbaikanAdapter(context, listJenisPerbaikanArrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                getApplicationContext()
        ));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(listJenisPerbaikanAdapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0) {
                requestPermissions(permissionsToRequest.toArray(
                        new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            } else {
                Log.i("TAG", "Masih kurang !");
                getListJenisPerbaikan();

            }
        } else {
            Log.i("Masuk : ", "2");
            getListJenisPerbaikan();

        }

        recyclerView.addOnItemTouchListener(new listJenisPerbaikanAdapter.RecyclerTouchListener(context, recyclerView, new listJenisPerbaikanAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(JenisPerbaikanActivity.this, OrderPerbaikanActivity.class);
                listJenisPerbaikan list = listJenisPerbaikanArrayList.get(position);
                intent.putExtra("menu", list.getMenu());
                SharedPreference.getInstance(getApplicationContext()).saveMenuId(list.getMenuId());
                startActivity(intent);
                finish();
            }
            @Override
            public void onLongClick(View view, int position) {
            }
        }));
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
                            new AlertDialog.Builder(JenisPerbaikanActivity.this).
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

                }
                break;
        }
    }



    private void getListJenisPerbaikan() {
        final String ipAddress = SharedPreference.getInstance(getApplicationContext()).getIpAddress();
        @SuppressLint("StaticFieldLeak")
        class getListJenisPerbaikan extends AsyncTask<Void, Void, String> {
            private final WeakReference<JenisPerbaikanActivity> mActivityRef;

            private getListJenisPerbaikan(JenisPerbaikanActivity activity) {
                mActivityRef = new WeakReference<>(activity);
            }
            protected String doInBackground(Void[] params) {
                String response = "";
                HashMap<String, String> map = new HashMap<>();
                try {
                    HttpRequest req = new HttpRequest(ipAddress + SimrsmConstant.ServiceType.URLLISTJENISPERBAIKAN);
                    Log.e("CDJNNJCD", ipAddress + SimrsmConstant.ServiceType.URLLISTJENISPERBAIKAN);
                    response = req.prepare(HttpRequest.Method.POST).withData(map).sendAndReadString();
                } catch (IOException e) {
                    response = e.getMessage();
                }
                return response;
            }
            protected void onPostExecute(String result) {
                onTaskCompleted(result);
            }
        }
        new getListJenisPerbaikan(JenisPerbaikanActivity.this).execute();
    }

    public void onTaskCompleted(String response) {
        if (response.equals("")) {
            toastMessage("Something Error");
            finish();
        } else {
            try {
                JSONObject obj = new JSONObject(response);
                JSONArray userArray = obj.getJSONArray("menu_order");
                for (int i = 0; i < userArray.length(); i++) {
                    JSONObject userObj = (JSONObject) userArray.get(i);
                    listJenisPerbaikan listJenisPerbaikan = new listJenisPerbaikan();
                    listJenisPerbaikan.setMenu(userObj.getString("menu"));
                    listJenisPerbaikan.setMenuId(userObj.getString("menu_id"));
                    listJenisPerbaikanArrayList.add(listJenisPerbaikan);
                }
            } catch (JSONException e) {
                response = e.getMessage();
                Log.e("JSON parsing error", response);
            }
        }
        listJenisPerbaikanAdapter.notifyDataSetChanged();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(),JenisPerbaikanActivity.class);
        startActivityForResult(myIntent, 0);
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(getApplicationContext(),MainActivity.class);
        startActivityForResult(myIntent, 0);
        finish();
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
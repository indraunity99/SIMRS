package com.mobile.rsupsanglah.simrsmobile.activity.ui;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.mobile.rsupsanglah.simrsmobile.R;
import com.mobile.rsupsanglah.simrsmobile.activity.adapter.listOrderPerbaikanAdapter;
import com.mobile.rsupsanglah.simrsmobile.activity.control.HttpRequest;
import com.mobile.rsupsanglah.simrsmobile.activity.control.SharedPreference;
import com.mobile.rsupsanglah.simrsmobile.activity.control.SimrsmConstant;
import com.mobile.rsupsanglah.simrsmobile.activity.helper.SimpleDividerItemDecoration;
import com.mobile.rsupsanglah.simrsmobile.activity.model.listOrderPerbaikan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

public class OrderPerbaikanActivity extends AppCompatActivity {
    private Context context;
    String jenis, menu;
    private RecyclerView recyclerView;
    private ArrayList<listOrderPerbaikan> listAdapterArrayList;
    private listOrderPerbaikanAdapter listOrderPerbaikanAdapter;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list_jenisperbaikan);
        recyclerView = findViewById(R.id.recycler_view);
        listAdapterArrayList = new ArrayList<>();
        Intent intent = getIntent();
//        jenis = intent.getStringExtra("jenis_perbaikan");
        menu = intent.getStringExtra("menu");

        listOrderPerbaikanAdapter = new listOrderPerbaikanAdapter(context, listAdapterArrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                getApplicationContext()
        ));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(listOrderPerbaikanAdapter);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Jenis Perbaikan");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderPerbaikanActivity.this, JenisPerbaikanActivity.class);
                startActivity(intent);
                finish();
            }
        });

        getListTypeOrder(menu);

        recyclerView.addOnItemTouchListener(new listOrderPerbaikanAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new listOrderPerbaikanAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(OrderPerbaikanActivity.this, InputTextMasalahActivity.class);
                listOrderPerbaikan list = listAdapterArrayList.get(position);
                intent.putExtra("nama_order", list.getNamaOrder());
                intent.putExtra("ID", list.getId());
                intent.putExtra("jenis_perbaikan", menu);
                startActivity(intent);
                finish();
            }
            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }

    private void getListTypeOrder(final String jenis_order) {
        final String ipAddress = SharedPreference.getInstance(getApplicationContext()).getIpAddress();
        @SuppressLint("StaticFieldLeak")
        class getListTypeOrder1 extends AsyncTask<Void, Void, String> {
            private final WeakReference<OrderPerbaikanActivity> mActivityRef;

            private getListTypeOrder1(OrderPerbaikanActivity activity) {
                mActivityRef = new WeakReference<>(activity);
            }

            protected String doInBackground(Void[] params) {
                String response = "";
                HashMap<String, String> map = new HashMap<>();
                try {
                    HttpRequest req = new HttpRequest(ipAddress + SimrsmConstant.ServiceType.URLJENISORDER + jenis_order.replace(" ", "%20"));
                    response = req.prepare(HttpRequest.Method.POST).withData(map).sendAndReadString();
                } catch (IOException e) {
                    response = e.getMessage();
                }
                return response;
            }
            protected void onPostExecute(String result) {
                onTaskCompletedPin(result);
            }
        }
        new getListTypeOrder1(OrderPerbaikanActivity.this).execute();
    }

    public void onTaskCompletedPin(String response) {
        Log.d("RESPONSE", response);
        if (response.equals("")) {
            Intent intentNull = new Intent(OrderPerbaikanActivity.this, InputTextMasalahActivity.class);
            intentNull.putExtra("response", jenis);
            startActivity(intentNull);
            finish();
        } else {
            try {
                JSONObject obj = new JSONObject(response);
                JSONArray userArray = obj.getJSONArray("list_jenis_order");
                for (int i = 0; i < userArray.length(); i++) {
                    JSONObject userObj = (JSONObject) userArray.get(i);
                    listOrderPerbaikan listOrderPerbaikan = new listOrderPerbaikan();
                    listOrderPerbaikan.setId(userObj.getString("ID"));
                    listOrderPerbaikan.setNamaOrder(userObj.getString("nama_order"));
                    listAdapterArrayList.add(listOrderPerbaikan);
                }
            } catch (JSONException e) {
                response = e.getMessage();
                Log.e("JSON parsing error", response);
            }
        }
        listOrderPerbaikanAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(getApplicationContext(), JenisPerbaikanActivity.class);
        startActivityForResult(myIntent, 0);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}

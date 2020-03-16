package com.mobile.rsupsanglah.simrsmobile.activity.ui;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.mobile.rsupsanglah.simrsmobile.R;
import com.mobile.rsupsanglah.simrsmobile.activity.adapter.listResponKeluhanAdapter;
import com.mobile.rsupsanglah.simrsmobile.activity.control.HttpRequest;
import com.mobile.rsupsanglah.simrsmobile.activity.control.SharedPreference;
import com.mobile.rsupsanglah.simrsmobile.activity.control.SimrsmConstant;
import com.mobile.rsupsanglah.simrsmobile.activity.helper.SimpleDividerItemDecoration;
import com.mobile.rsupsanglah.simrsmobile.activity.model.listResponKeluhan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

public class LaporanPendingActivity extends AppCompatActivity implements listResponKeluhanAdapter.listAdapterListener{

    private Context context;
    private RecyclerView recyclerView;
    private ArrayList<listResponKeluhan> listResponKeluhanArrayList;
    Toolbar toolbar;
    private  listResponKeluhanAdapter listResponKeluhanAdapter;
    private SearchView searchView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_dikerjakan);
        recyclerView = findViewById(R.id.recycler_view);
        listResponKeluhanArrayList = new ArrayList<>();

        listResponKeluhanAdapter = new listResponKeluhanAdapter(context, listResponKeluhanArrayList, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                getApplicationContext()
        ));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(listResponKeluhanAdapter);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Respon Keluhan");

        whiteNotificationBar(recyclerView);

        getListRespon(SharedPreference.getInstance(getApplicationContext()).getAppUser());

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LaporanPendingActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getListRespon(final String id_pegawai) {
        final String ipAddress = SharedPreference.getInstance(getApplicationContext()).getIpAddress();
        @SuppressLint("StaticFieldLeak")
        class getListRespon1 extends AsyncTask<Void, Void, String> {
            private final WeakReference<LaporanPendingActivity> mActivityRef;

            private getListRespon1(LaporanPendingActivity activity) {
                mActivityRef = new WeakReference<>(activity);
            }
            protected String doInBackground(Void[] params) {
                String response = "";
                HashMap<String, String> map = new HashMap<>();
                try {
                    HttpRequest req = new HttpRequest(ipAddress + SimrsmConstant.ServiceType.URLPENDING + "&user="+ id_pegawai);/////////////////////////+ pegawai id (show based on id)
                    response = req.prepare(HttpRequest.Method.POST).withData(map).sendAndReadString();
                    Log.e("list pending responses", ipAddress + SimrsmConstant.ServiceType.URLPENDING + "&user="+ id_pegawai);
                } catch (IOException e) {
                    response = e.getMessage();
                }
                return response;
            }
            protected void onPostExecute(String result) {
                onTaskCompleted(result);
            }
        }
        new getListRespon1(LaporanPendingActivity.this).execute();
    }

    public void onTaskCompleted(String response) {
        if (response.equals("")) {
            toastMessage("No Data Available");
//            finish();
        } else {
            try {
                JSONObject obj = new JSONObject(response);
                JSONArray userArray = obj.getJSONArray("list_respon");
                for (int i = 0; i < userArray.length(); i++) {
                    JSONObject userObj = (JSONObject) userArray.get(i);
                    listResponKeluhan listResponKeluhan = new listResponKeluhan();
                    listResponKeluhan.setId(userObj.getString("ID"));
                    listResponKeluhan.setKeluhan(userObj.getString("keluhan").replace("/\\s+/", " "));
                    listResponKeluhan.setJenisOrder(userObj.getString("jenis_order"));
                    listResponKeluhan.setPelapor(userObj.getString("pelapor"));
                    listResponKeluhan.setNoOrder(userObj.getString("no_order"));
                    listResponKeluhanArrayList.add(listResponKeluhan);
                }
            } catch (JSONException e) {
                response = e.getMessage();
                Log.e("JSON parsing error", response);
            }
        }
        listResponKeluhanAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_search, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                listResponKeluhanAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                listResponKeluhanAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public void onContactSelected(listResponKeluhan contact) {
        Intent intent = new Intent(LaporanPendingActivity.this, ViewResponKeluhanActivity.class);
        intent.putExtra("item_click",contact.getId());
        intent.putExtra("pending", "pending");
        Log.i("ITEM_CLICK", contact.getId());
        startActivity(intent);
        finish();
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        Intent myIntent = new Intent(getApplicationContext(), DashboardActivity.class);
        startActivityForResult(myIntent, 0);
        finish();
    }
}

package com.mobile.rsupsanglah.simrsmobile.activity.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.rsupsanglah.simrsmobile.R;
import com.mobile.rsupsanglah.simrsmobile.activity.adapter.InputBahanAdapter;
import com.mobile.rsupsanglah.simrsmobile.activity.adapter.ViewResponKeluhanAdapter;
import com.mobile.rsupsanglah.simrsmobile.activity.adapter.listImagesAdapter;
import com.mobile.rsupsanglah.simrsmobile.activity.control.HttpRequest;
import com.mobile.rsupsanglah.simrsmobile.activity.control.SharedPreference;
import com.mobile.rsupsanglah.simrsmobile.activity.control.SimrsmConstant;
import com.mobile.rsupsanglah.simrsmobile.activity.helper.BottomSheetDialogRespon;
import com.mobile.rsupsanglah.simrsmobile.activity.helper.BottomSheetDialogInput;
import com.mobile.rsupsanglah.simrsmobile.activity.helper.SimpleDividerItemDecoration;
import com.mobile.rsupsanglah.simrsmobile.activity.model.ViewResponKeluhan;
import com.mobile.rsupsanglah.simrsmobile.activity.model.listImages;
import com.mobile.rsupsanglah.simrsmobile.activity.model.listInputBahan;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

//Copyright 2013 Square, Inc.
//
//        Licensed under the Apache License, Version 2.0 (the "License");
//        you may not use this file except in compliance with the License.
//        You may obtain a copy of the License at
//
//        http://www.apache.org/licenses/LICENSE-2.0
//
//        Unless required by applicable law or agreed to in writing, software
//        distributed under the License is distributed on an "AS IS" BASIS,
//        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//        See the License for the specific language governing permissions and
//        limitations under the License.

public class ViewResponKeluhanActivity extends AppCompatActivity implements BottomSheetDialogRespon.BottomSheetListener, SwipeRefreshLayout.OnRefreshListener {
    Toolbar toolbar;
    TextView nama_keluhan, no_order, tgl_lapor, jenis_order;
    Button respon, input, fin;
    private Context context;
    ImageView image;
    private TextView emptyView, emptyView2, emptyView3;

    private RecyclerView recyclerViewPekerjaan;
    private ArrayList<ViewResponKeluhan> listHistoryPekerjaanArrayList;
    private ViewResponKeluhanAdapter ViewResponKeluhanAdapter;

    private RecyclerView recyclerViewInput;
    private ArrayList<listInputBahan> listInputBahanArrayList;
    private InputBahanAdapter InputBahanAdapter;

    private RecyclerView recyclerViewImages;
    private ArrayList<listImages> listImagesArrayList;
    private listImagesAdapter listImagesAdapter;

    ViewResponKeluhan ViewResponKeluhan = new ViewResponKeluhan();
    listInputBahan listInputBahan = new listInputBahan();

    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_respon_keluhan);

        nama_keluhan = findViewById(R.id.nama_keluhan_view);
        no_order = findViewById(R.id.nomor_order_view);
        tgl_lapor = findViewById(R.id.tgl_lapor_view);
        jenis_order = findViewById(R.id.jenis_order_view);

        respon = findViewById(R.id.button_respon_view);
        input = findViewById(R.id.button_input_bahan_view);
        fin = findViewById(R.id.button_finish_view);

        emptyView = findViewById(R.id.empty_view);
        emptyView2 = findViewById(R.id.empty_view_2);
        emptyView3 = findViewById(R.id.empty_view_3);
        image = findViewById(R.id.goProDialogImage);
/////////////////////////////////////////////////////////////////////////////////////////////////////
        recyclerViewPekerjaan = findViewById(R.id.recycler_view_pekerjaan);
        listHistoryPekerjaanArrayList = new ArrayList<>();

        ViewResponKeluhanAdapter = new ViewResponKeluhanAdapter(context, listHistoryPekerjaanArrayList);
        LinearLayoutManager layoutManagerPekerjaan = new LinearLayoutManager(context);
        recyclerViewPekerjaan.setHasFixedSize(true);
        recyclerViewPekerjaan.setLayoutManager(layoutManagerPekerjaan);
        recyclerViewPekerjaan.addItemDecoration(new SimpleDividerItemDecoration(
                getApplicationContext()
        ));
        recyclerViewPekerjaan.setItemAnimator(new DefaultItemAnimator());
        recyclerViewPekerjaan.setAdapter(ViewResponKeluhanAdapter);
/////////////////////////////////////////////////////////////////////////////////////////////////////
        recyclerViewInput = findViewById(R.id.recycler_view_input);
        listInputBahanArrayList = new ArrayList<>();

        InputBahanAdapter = new InputBahanAdapter(context, listInputBahanArrayList);
        LinearLayoutManager layoutManagerInput = new LinearLayoutManager(context);
        recyclerViewInput.setHasFixedSize(true);
        recyclerViewInput.setLayoutManager(layoutManagerInput);
        recyclerViewInput.addItemDecoration(new SimpleDividerItemDecoration(
                getApplicationContext()
        ));
        recyclerViewInput.setItemAnimator(new DefaultItemAnimator());
        recyclerViewInput.setAdapter(InputBahanAdapter);
/////////////////////////////////////////////////////////////////////////////////////////////////////
        recyclerViewImages = findViewById(R.id.recycler_view_images);
        listImagesArrayList = new ArrayList<>();

        listImagesAdapter = new listImagesAdapter(context, listImagesArrayList);
        LinearLayoutManager layoutManagerImages = new LinearLayoutManager(context);
        recyclerViewImages.setHasFixedSize(true);
        recyclerViewImages.setLayoutManager(layoutManagerImages);
        recyclerViewImages.addItemDecoration(new SimpleDividerItemDecoration(
                getApplicationContext()
        ));
        recyclerViewImages.setItemAnimator(new DefaultItemAnimator());
        recyclerViewImages.setAdapter(listImagesAdapter);
/////////////////////////////////////////////////////////////////////////////////////////////////////

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detail Respon Keluhan");

        Intent intent = getIntent();
        String intentRespon = intent.getStringExtra("item_click");
        SharedPreference.getInstance(getApplication()).saveActtoFrag(intentRespon);

        getFullListRespon(intentRespon);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(ViewResponKeluhanActivity.this, ListResponKeluhanActivity.class);
//                startActivity(intent);
//                finish();
                if (getIntent().hasExtra("dikerjakan")){
                    Intent dikerjakan = new Intent(getApplicationContext(), LaporanDikerjakanActivity.class);
                    startActivity(dikerjakan);
                    finish();
                } else if (getIntent().hasExtra("pending")){
                    Intent pending = new Intent(getApplicationContext(), LaporanPendingActivity.class);
                    startActivity(pending);
                    finish();
                } else if (getIntent().hasExtra("selesai")){
                    Intent selesai = new Intent(getApplicationContext(), LaporanSelesaiActivity.class);
                    startActivity(selesai);
                    finish();
                } else {
                    Intent myIntent = new Intent(getApplicationContext(), ListResponKeluhanActivity.class);
                    startActivityForResult(myIntent, 0);
                    finish();
                }
            }
        });

        respon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialogRespon bottomSheet = new BottomSheetDialogRespon();
                bottomSheet.show(getSupportFragmentManager(), "BottomSheetRespon");
            }
        });

        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialogInput bottomSheetDialogInput = new BottomSheetDialogInput();
                bottomSheetDialogInput.show(getSupportFragmentManager(), "BottomSheetInput");
            }
        });

        fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ViewResponKeluhanActivity.this, R.style.MyDialogThemeAlert);
                alertDialog.setTitle("Confirm Finish");
                alertDialog.setMessage("Are you sure you want to finish this response?");
                alertDialog.setIcon(R.drawable.ic_round_done_outline_black_24dp);
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finishRespon(SharedPreference.getInstance(getApplication()).getActtoFrag());
                        toastMessage("You finish this response");
                        Intent intent = new Intent(ViewResponKeluhanActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }
        });

        recyclerViewPekerjaan.addOnItemTouchListener(new ViewResponKeluhanAdapter.RecyclerTouchListener(getApplicationContext(), recyclerViewPekerjaan, new ViewResponKeluhanAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
            }

            @Override
            public void onLongClick(View view, final int position) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ViewResponKeluhanActivity.this, R.style.MyDialogThemeAlert);
                alertDialog.setTitle("Confirm Delete");
                alertDialog.setMessage("Are you sure you want delete this work history?");
                alertDialog.setIcon(R.drawable.ic_round_delete_outline_black_24dp);
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ViewResponKeluhan ViewResponKeluhan = listHistoryPekerjaanArrayList.get(position);
                        ViewResponKeluhan.getIdResponKeluhan();
                        deleteHistoryPekerjaan(ViewResponKeluhan.getIdResponKeluhan());//////////////////////////////////////////////////////////////////////////////////////
                        ViewResponKeluhanAdapter.removeItem(position);
//                        Log.i("JNCUELO", ViewResponKeluhan.getIdResponKeluhan());
                    }
                });
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }
        }));

        recyclerViewInput.addOnItemTouchListener(new InputBahanAdapter.RecyclerTouchListener(getApplicationContext(), recyclerViewInput, new InputBahanAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
            }

            @Override
            public void onLongClick(View view, final int position) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ViewResponKeluhanActivity.this, R.style.MyDialogThemeAlert);
                alertDialog.setTitle("Confirm Delete");
                alertDialog.setMessage("Are you sure you want delete this material history?");
                alertDialog.setIcon(R.drawable.ic_round_delete_outline_black_24dp);
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        listInputBahan listInputBahan = listInputBahanArrayList.get(position);
                        listInputBahan.getIdBahanatView();
                        deleteHistoryBahan(listInputBahan.getIdBahanatView());//////////////////////////////////////////////////////////////////////////////////////
                        InputBahanAdapter.removeItem(position);
//                        Log.i("JNCUELO", listInputBahan.getIdBahanatView());
                    }
                });
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }
        }));

        recyclerViewImages.addOnItemTouchListener(new listImagesAdapter.RecyclerTouchListener(getApplicationContext(), recyclerViewImages, new listImagesAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Dialog builder = new Dialog(ViewResponKeluhanActivity.this);
                builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                builder.getWindow().setBackgroundDrawable(
                        new ColorDrawable(android.graphics.Color.TRANSPARENT));
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        //nth
                    }
                });

                final ProgressDialog progressDialog = new ProgressDialog(ViewResponKeluhanActivity.this);
                listImages listImages = listImagesArrayList.get(position);
                final String img = SharedPreference.getInstance(getApplicationContext()).getIpAddress() + "/simserv/images/" + listImages.getNama_images();
                ImageView imageView = new ImageView(ViewResponKeluhanActivity.this);
                Picasso.get()
                        .load(img)
                        .into(imageView, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                progressDialog.setMessage("please wait");
                                progressDialog.setCancelable(false);
                                progressDialog.show();
                                // The image has loaded you can make the progress bar invisible
                                if (progressDialog.isShowing()){
                                    progressDialog.dismiss();
                                }
                            }

                            @Override
                            public void onError(Exception e) {
                                progressDialog.setMessage("please wait");
                                progressDialog.setCancelable(false);
                                progressDialog.show();
                                if (progressDialog.isShowing()){
                                    progressDialog.dismiss();
                                }
                            }
                        });


                builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                builder.show();
            }

            @Override
            public void onLongClick(View view, final int position) {
            }
        }));

        // SwipeRefreshLayout
        mSwipeRefreshLayout = findViewById(R.id.swiperefreshlayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                ViewResponKeluhanAdapter.notifyDataSetChanged();
                dataPekerjaaan();
                dataInput();
                dataImages();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_photo_activity, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add_photo_activity) {
            Intent intent = new Intent(ViewResponKeluhanActivity.this, CameraActivity.class);
            toastMessage("Add Photo");
            intent.putExtra("add_photo", "add_photo");
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void getFullListRespon(final String no_order) {
        final String ipAddress = SharedPreference.getInstance(getApplicationContext()).getIpAddress();
        @SuppressLint("StaticFieldLeak")
        class getFullListRespon1 extends AsyncTask<Void, Void, String> {
            private final WeakReference<ViewResponKeluhanActivity> mActivityRef;

            private getFullListRespon1(ViewResponKeluhanActivity activity) {
                mActivityRef = new WeakReference<>(activity);
            }

            protected String doInBackground(Void[] params) {
                String response = "";
                HashMap<String, String> map = new HashMap<>();
                try {
                    HttpRequest req = new HttpRequest(ipAddress + SimrsmConstant.ServiceType.URLRESPONKELUHANVIEW + no_order);
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
        new getFullListRespon1(ViewResponKeluhanActivity.this).execute();
    }

    public void onTaskCompleted(String response) {
        if (!response.equals("")) {
            try {
                JSONObject obj = new JSONObject(response);
                JSONObject data = obj.getJSONObject("detail_order");
                nama_keluhan.setText(data.getString("keluhan"));
                no_order.setText(data.getString("no_order"));
                tgl_lapor.setText(data.getString("tgl_lapor"));
                jenis_order.setText(data.getString("jenis_order"));
                ViewResponKeluhan.setNoOrderfromView(data.getString("id"));
//                Log.e("RESPON NO ORDER", ViewResponKeluhan.getNoOrderfromView());
            } catch (JSONException e) {
                response = e.getMessage();
                Log.e("JSON parsing error", response);
            }
        } else {
            toastMessage("Sorry! Something Wrong");
        }
    }

    private void getHistoryPekerjaan(final String no_order) {
        final String ipAddress = SharedPreference.getInstance(getApplicationContext()).getIpAddress();
        @SuppressLint("StaticFieldLeak")
        class getHistoryPekerjaan extends AsyncTask<Void, Void, String> {
            private final WeakReference<ViewResponKeluhanActivity> mActivityRef;

            private getHistoryPekerjaan(ViewResponKeluhanActivity activity) {
                mActivityRef = new WeakReference<>(activity);
            }

            protected String doInBackground(Void[] params) {
                String response = "";
                HashMap<String, String> map = new HashMap<>();
                try {
                    HttpRequest req = new HttpRequest(ipAddress + SimrsmConstant.ServiceType.URLGETHISTORYPEKERJAAN + no_order);
                    Log.e("getHistoryPekerjaa", ipAddress + SimrsmConstant.ServiceType.URLGETHISTORYPEKERJAAN + no_order);
                    response = req.prepare(HttpRequest.Method.POST).withData(map).sendAndReadString();
                } catch (IOException e) {
                    response = e.getMessage();
                }
                return response;
            }

            protected void onPostExecute(String result) {
                onTaskCompletedHistoryPekerjaan(result);
            }
        }
        new getHistoryPekerjaan(ViewResponKeluhanActivity.this).execute();
    }

    public void onTaskCompletedHistoryPekerjaan(String response) {
//        Log.d("CompletedHistoryKerja", response);
        if (!response.equals("")) {
            try {
                JSONObject obj = new JSONObject(response);
                JSONArray data = obj.getJSONArray("detail_order");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject userObj = (JSONObject) data.get(i);
                    ViewResponKeluhan ViewResponKeluhan = new ViewResponKeluhan();
                    ViewResponKeluhan.setIdResponKeluhan(userObj.getString("id"));
                    ViewResponKeluhan.setJenis_pekerjaan(userObj.getString("pekerjaan"));
                    ViewResponKeluhan.setWaktu_mulai(userObj.getString("tgl_mulai"));
                    ViewResponKeluhan.setWaktu_selesai(userObj.getString("tgl_selesai"));
                    listHistoryPekerjaanArrayList.add(ViewResponKeluhan);
                }
            } catch (JSONException e) {
                response = e.getMessage();
                Log.e("JSON parsing error", response);
            }
        } else {
            toastMessage("Swipe Down to Refresh");
        }
        ViewResponKeluhanAdapter.notifyDataSetChanged();
    }

    private void getHistoryBahan(final String no_order) {
        final String ipAddress = SharedPreference.getInstance(getApplicationContext()).getIpAddress();
        @SuppressLint("StaticFieldLeak")
        class getHistoryBahan extends AsyncTask<Void, Void, String> {
            private final WeakReference<ViewResponKeluhanActivity> mActivityRef;

            private getHistoryBahan(ViewResponKeluhanActivity activity) {
                mActivityRef = new WeakReference<>(activity);
            }

            protected String doInBackground(Void[] params) {
                String response = "";
                HashMap<String, String> map = new HashMap<>();
                try {
                    HttpRequest req = new HttpRequest(ipAddress + SimrsmConstant.ServiceType.URLGETHISTORYBAHAN + no_order);/////////
                    Log.e("getHistoryBahan", ipAddress + SimrsmConstant.ServiceType.URLGETHISTORYBAHAN + no_order);
                    response = req.prepare(HttpRequest.Method.POST).withData(map).sendAndReadString();
                } catch (IOException e) {
                    response = e.getMessage();
                }
                return response;
            }

            protected void onPostExecute(String result) {
                onTaskCompletedHistoryBahan(result);
            }
        }
        new getHistoryBahan(ViewResponKeluhanActivity.this).execute();
    }

    public void onTaskCompletedHistoryBahan(String response) {
//        Log.d("CompletedHistoryBahan", response);
        if (!response.equals("")) {
            try {
                JSONObject obj = new JSONObject(response);
                JSONArray data = obj.getJSONArray("bahan");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject userObj = (JSONObject) data.get(i);
                    listInputBahan listInputBahan = new listInputBahan();
                    listInputBahan.setIdBahanatView(userObj.getString("id"));
                    listInputBahan.setNama_bahan(userObj.getString("nama"));
                    listInputBahan.setJumlah_bahan(userObj.getString("terpakai"));
                    listInputBahan.setSatuanBahan(userObj.getString("satuan"));
                    listInputBahanArrayList.add(listInputBahan);
                }
            } catch (JSONException e) {
                response = e.getMessage();
                Log.e("JSON parsing error", response);
            }
        } else {
            toastMessage("Swipe Down to Refresh");
        }
        InputBahanAdapter.notifyDataSetChanged();
    }

    private void deleteHistoryPekerjaan(final String no_order) {
        final String ipAddress = SharedPreference.getInstance(getApplicationContext()).getIpAddress();
        @SuppressLint("StaticFieldLeak")
        class deleteHistoryPekerjaan extends AsyncTask<Void, Void, String> {
            private final WeakReference<ViewResponKeluhanActivity> mActivityRef;

            private deleteHistoryPekerjaan(ViewResponKeluhanActivity activity) {
                mActivityRef = new WeakReference<>(activity);
            }

            protected String doInBackground(Void[] params) {
                String response = "";
                HashMap<String, String> map = new HashMap<>();
                try {
                    HttpRequest req = new HttpRequest(ipAddress + SimrsmConstant.ServiceType.URLDELETEHISTORYPEKERJAAN + no_order);//////////////////////////////
                    Log.e("deleteHistoryPekerjaan", ipAddress + SimrsmConstant.ServiceType.URLDELETEHISTORYPEKERJAAN + no_order);
                    response = req.prepare(HttpRequest.Method.POST).withData(map).sendAndReadString();
                } catch (IOException e) {
                    response = e.getMessage();
                }
                return response;
            }

            protected void onPostExecute(String result) {
                onTaskCompletedDeletePekerjaan(result);
            }
        }
        new deleteHistoryPekerjaan(ViewResponKeluhanActivity.this).execute();
    }

    public void onTaskCompletedDeletePekerjaan(String response) {
//        Log.d("DeletePekerjaan", response);
        if (!response.contains("sukses")) {
            toastMessage("Error on delete data");
        } else {
            toastMessage("Sucessfully delete data");
        }
        ViewResponKeluhanAdapter.notifyDataSetChanged();
    }

    private void deleteHistoryBahan(final String no_order) {
        final String ipAddress = SharedPreference.getInstance(getApplicationContext()).getIpAddress();
        @SuppressLint("StaticFieldLeak")
        class deleteHistoryBahan extends AsyncTask<Void, Void, String> {
            private final WeakReference<ViewResponKeluhanActivity> mActivityRef;

            private deleteHistoryBahan(ViewResponKeluhanActivity activity) {
                mActivityRef = new WeakReference<>(activity);
            }

            protected String doInBackground(Void[] params) {
                String response = "";
                HashMap<String, String> map = new HashMap<>();
                try {
                    HttpRequest req = new HttpRequest(ipAddress + SimrsmConstant.ServiceType.URLDELETEHISTORYBAHAN + no_order);/////////
                    Log.e("deleteHistoryBahan", ipAddress + SimrsmConstant.ServiceType.URLDELETEHISTORYBAHAN + no_order);
                    response = req.prepare(HttpRequest.Method.POST).withData(map).sendAndReadString();
                } catch (IOException e) {
                    response = e.getMessage();
                }
                return response;
            }

            protected void onPostExecute(String result) {
                onTaskCompletedDeleteBahan(result);
            }
        }
        new deleteHistoryBahan(ViewResponKeluhanActivity.this).execute();
    }

    public void onTaskCompletedDeleteBahan(String response) {
//        Log.d("DeleteBahan", response);
        if (!response.contains("sukses")) {
            toastMessage("Error on delete data");
        } else {
            toastMessage("Sucessfully delete data");
        }
        InputBahanAdapter.notifyDataSetChanged();
    }

    private void finishRespon(final String no_order) {
        final String ipAddress = SharedPreference.getInstance(getApplicationContext()).getIpAddress();
        @SuppressLint("StaticFieldLeak")
        class finishRespon extends AsyncTask<Void, Void, String> {
            private final WeakReference<ViewResponKeluhanActivity> mActivityRef;

            private finishRespon(ViewResponKeluhanActivity activity) {
                mActivityRef = new WeakReference<>(activity);
            }

            protected String doInBackground(Void[] params) {
                String response = "";
                HashMap<String, String> map = new HashMap<>();
                try {
                    HttpRequest req = new HttpRequest(ipAddress + SimrsmConstant.ServiceType.URLFINISHRESPON + no_order);/////////
                    Log.e("deleteHistoryBahan", ipAddress + SimrsmConstant.ServiceType.URLFINISHRESPON + no_order);
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
        new finishRespon(ViewResponKeluhanActivity.this).execute();
    }

    public void onTaskCompletedFinish(String response) {
//        Log.d("DeleteBahan", response);
        if (response.contains("sukses")) {
            toastMessage("Sucessfully save data");
            Intent intent = new Intent(ViewResponKeluhanActivity.this, ListResponKeluhanActivity.class);
            startActivity(intent);
        } else {
            toastMessage("Error on saving data");
        }
    }

    private void listImages(final String no_order) {
        final String ipAddress = SharedPreference.getInstance(getApplicationContext()).getIpAddress();
        @SuppressLint("StaticFieldLeak")
        class listImages extends AsyncTask<Void, Void, String> {
            private final WeakReference<ViewResponKeluhanActivity> mActivityRef;

            private listImages(ViewResponKeluhanActivity activity) {
                mActivityRef = new WeakReference<>(activity);
            }

            protected String doInBackground(Void[] params) {
                String response = "";
                HashMap<String, String> map = new HashMap<>();
                try {
                    HttpRequest req = new HttpRequest(ipAddress + SimrsmConstant.ServiceType.URLGETIMAGES + no_order);/////////
                    Log.e("getimages", ipAddress + SimrsmConstant.ServiceType.URLGETIMAGES + no_order);
                    response = req.prepare(HttpRequest.Method.POST).withData(map).sendAndReadString();
                } catch (IOException e) {
                    response = e.getMessage();
                }
                return response;
            }

            protected void onPostExecute(String result) {
                onTaskCompletedImages(result);
            }
        }
        new listImages(ViewResponKeluhanActivity.this).execute();
    }

    public void onTaskCompletedImages(String response) {
//        Log.d("images", response);
        if (!response.equals("")) {
            try {
                JSONObject obj = new JSONObject(response);
                JSONArray data = obj.getJSONArray("img");//////////listimages
                for (int i = 0; i < data.length(); i++) {
                    JSONObject userObj = (JSONObject) data.get(i);
                    listImages listImages = new listImages();
                    listImages.setNama_images(userObj.getString("nama"));///namaimages
                    listImagesArrayList.add(listImages);
                }
            } catch (JSONException e) {
                response = e.getMessage();
                Log.e("JSON parsing error", response);
            }
        } else {
            toastMessage("Swipe Down to Refresh");
        }
        listImagesAdapter.notifyDataSetChanged();
    }

    public void dataPekerjaaan() {
        getHistoryPekerjaan(SharedPreference.getInstance(getApplicationContext()).getActtoFrag());
        if (listHistoryPekerjaanArrayList.size() == 0) {
            String text = "No Data Available";
            emptyView.setText(text);
            recyclerViewPekerjaan.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerViewPekerjaan.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
        ViewResponKeluhanAdapter.clear();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void dataInput() {
        getHistoryBahan(SharedPreference.getInstance(getApplicationContext()).getActtoFrag());
        if (listInputBahanArrayList.size() == 0) {
            String text = "No Data Available";
            emptyView2.setText(text);
            recyclerViewInput.setVisibility(View.GONE);
            emptyView2.setVisibility(View.VISIBLE);
        } else {
            recyclerViewInput.setVisibility(View.VISIBLE);
            emptyView2.setVisibility(View.GONE);
        }
        InputBahanAdapter.clear();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void dataImages() {
        listImages(SharedPreference.getInstance(getApplicationContext()).getActtoFrag());
        if (listImagesArrayList.size() == 0) {
            String text = "No Data Available";
            emptyView3.setText(text);
            recyclerViewImages.setVisibility(View.GONE);
            emptyView3.setVisibility(View.VISIBLE);
        } else {
            recyclerViewImages.setVisibility(View.VISIBLE);
            emptyView3.setVisibility(View.GONE);
        }
        listImagesAdapter.clear();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        dataPekerjaaan();
        dataInput();
        dataImages();
    }

    @Override
    public void onBackPressed() {
        if (getIntent().hasExtra("dikerjakan")){
            Intent dikerjakan = new Intent(getApplicationContext(), LaporanDikerjakanActivity.class);
            startActivity(dikerjakan);
            finish();
        } else if (getIntent().hasExtra("pending")){
            Intent pending = new Intent(getApplicationContext(), LaporanPendingActivity.class);
            startActivity(pending);
            finish();
        } else if (getIntent().hasExtra("selesai")){
            Intent selesai = new Intent(getApplicationContext(), LaporanSelesaiActivity.class);
            startActivity(selesai);
            finish();
        } else {
            Intent myIntent = new Intent(getApplicationContext(), ListResponKeluhanActivity.class);
            startActivityForResult(myIntent, 0);
            finish();
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onButtonClicked(String text) {
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}

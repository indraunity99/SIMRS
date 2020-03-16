package com.mobile.rsupsanglah.simrsmobile.activity.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.mobile.rsupsanglah.simrsmobile.R;
import com.mobile.rsupsanglah.simrsmobile.activity.control.HttpRequest;
import com.mobile.rsupsanglah.simrsmobile.activity.control.SharedPreference;
import com.mobile.rsupsanglah.simrsmobile.activity.control.SimrsmConstant;
import com.mobile.rsupsanglah.simrsmobile.activity.helper.MyValueFormatter;
import com.mobile.rsupsanglah.simrsmobile.activity.model.DataNumberDashboard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

//Copyright 2018 Philipp Jahoda
//
//        Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
//
//        http://www.apache.org/licenses/LICENSE-2.0
//
//        Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
//


public class DashboardActivity extends AppCompatActivity {

    PieChart pieChartView;
    TextView txtContent, txtTmptTgs;
    private Animation animationUp;
    private Animation animationDown;
    LinearLayout linearLayout;
    Toolbar toolbar;
    TextView total, menunggu, dikerjakan, pending, selesai;
    CardView card_respon, card_dikerjakan, card_pending, card_selesai;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_kerusakan_dashboard);
        pieChartView = findViewById(R.id.piechart);
        txtContent = (TextView) findViewById(R.id.total_dash);
        linearLayout = findViewById(R.id.linear1);
        linearLayout.setVisibility(View.GONE);
        txtTmptTgs = findViewById(R.id.text_view_tmpt_tgs);

        total = findViewById(R.id.number_total_dash);
        menunggu = findViewById(R.id.number_menunggu_dash);
        dikerjakan = findViewById(R.id.number_dikerjakan_dash);
        pending = findViewById(R.id.number_pending_dash);
        selesai = findViewById(R.id.number_selesai_dash);

        card_respon = findViewById(R.id.card_respon);
        card_dikerjakan = findViewById(R.id.card_dikerjakan);
        card_pending = findViewById(R.id.card_pending);
        card_selesai = findViewById(R.id.card_selesai);

        animationUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        animationDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);

        txtTmptTgs.setText(SharedPreference.getInstance(getApplicationContext()).getTempatTugas());
        getListPieChart(SharedPreference.getInstance(getApplicationContext()).getAppUser());

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Laporan Kerusakan Dashboard");

        pieChartView.getDescription().setEnabled(false);
        pieChartView.setExtraOffsets(5,15,5,0);
        pieChartView.offsetLeftAndRight(0);

        pieChartView.setDragDecelerationFrictionCoef(0.95f);

        pieChartView.setDrawHoleEnabled(true);
        pieChartView.setHoleColor(Color.WHITE);
        pieChartView.setTransparentCircleRadius(61f);
        pieChartView.setCenterText("Laporan");
        pieChartView.setCenterTextColor(Color.LTGRAY);
        pieChartView.setEntryLabelColor(Color.TRANSPARENT);

        pieChartView.setOnChartGestureListener(new OnChartGestureListener() {

            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {
                // TODO Auto-generated method stub
                if(linearLayout.isShown()){
                    linearLayout.setVisibility(View.GONE);
                    linearLayout.startAnimation(animationUp);
                }
                else{
                    linearLayout.setVisibility(View.VISIBLE);
                    linearLayout.startAnimation(animationDown);
                }
            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onChartLongPressed(MotionEvent me) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2,
                                     float velocityX, float velocityY) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {
                // TODO Auto-generated method stub

            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        card_respon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, ListResponKeluhanActivity.class);
                startActivity(intent);
                finish();
            }
        });

        card_dikerjakan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, LaporanDikerjakanActivity.class);
                startActivity(intent);
                finish();
            }
        });

        card_pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, LaporanPendingActivity.class);
                startActivity(intent);
                finish();
            }
        });

        card_selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, LaporanSelesaiActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void getListPieChart(final String id_pegawai) {
        final String ipAddress = SharedPreference.getInstance(getApplicationContext()).getIpAddress();
        @SuppressLint("StaticFieldLeak")
        class getListPieChart extends AsyncTask<Void, Void, String> {
            private final WeakReference<DashboardActivity> mActivityRef;

            private getListPieChart(DashboardActivity activity) {
                mActivityRef = new WeakReference<>(activity);
            }
            protected String doInBackground(Void[] params) {
                String response = "";
                HashMap<String, String> map = new HashMap<>();
                try {
                    HttpRequest req = new HttpRequest(ipAddress + SimrsmConstant.ServiceType.URLGETDATANUMBERDASHBOARD + id_pegawai);/////////////////////////+ pegawai id (show based on id)
                    response = req.prepare(HttpRequest.Method.POST).withData(map).sendAndReadString();
                    Log.e("data for pie chart", ipAddress + SimrsmConstant.ServiceType.URLGETDATANUMBERDASHBOARD + id_pegawai);
                } catch (IOException e) {
                    response = e.getMessage();
                }
                return response;
            }
            protected void onPostExecute(String result) {
                onTaskCompleted(result);
            }
        }
        new getListPieChart(DashboardActivity.this).execute();
    }

    public void onTaskCompleted(String response) {
        if (response.equals("")) {
            toastMessage("No Data Available");
        } else {
            ArrayList<PieEntry> yValues = new ArrayList<>();
            try {
                JSONObject obj = new JSONObject(response);
                JSONArray userArray = obj.getJSONArray("data");
                for (int i = 0; i < userArray.length(); i++) {
                    JSONObject userObj = (JSONObject) userArray.get(i);
                    DataNumberDashboard dataNumberDashboard = new DataNumberDashboard();
                    dataNumberDashboard.setTotal(userObj.getInt("masuk"));
                    dataNumberDashboard.setMenunggu(userObj.getInt("menunggu"));
                    dataNumberDashboard.setPending(userObj.getInt("pending"));
                    dataNumberDashboard.setDikerjakan(userObj.getInt("dikerjakan"));
                    dataNumberDashboard.setSelesai(userObj.getInt("finish"));

                    int dataNumber[] = {dataNumberDashboard.getTotal(), dataNumberDashboard.getMenunggu(), dataNumberDashboard.getDikerjakan(),
                                    dataNumberDashboard.getPending(),
                            dataNumberDashboard.getSelesai()};
                    String LegendTitle[] = {"Total", "Menunggu", "Dikerjakan", "Pending", "Selesai"};

                    total.setText(String.valueOf(dataNumberDashboard.getTotal()));
                    menunggu.setText(String.valueOf(dataNumberDashboard.getMenunggu()));
                    dikerjakan.setText(String.valueOf(dataNumberDashboard.getDikerjakan()));
                    pending.setText(String.valueOf(dataNumberDashboard.getPending()));
                    selesai.setText(String.valueOf(dataNumberDashboard.getSelesai()));

                    for (i = 0; i < dataNumber.length; i++){
                        yValues.add(new PieEntry(dataNumber[i], LegendTitle[i]));
                    }

                    PieDataSet pieDataSet = new PieDataSet(yValues, "");
                    pieDataSet.setSliceSpace(0f);
                    pieDataSet.setSelectionShift(2f);
                    pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);

                    pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
                    pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
                    pieDataSet.setValueLinePart1OffsetPercentage(100f); /** When valuePosition is OutsideSlice, indicates offset as percentage out of the slice size */
                    pieDataSet.setValueLinePart1Length(0.6f); /** When valuePosition is OutsideSlice, indicates length of first half of the line */
                    pieDataSet.setValueLinePart2Length(0.6f); /** When valuePosition is OutsideSlice, indicates length of second half of the line */


                    PieData data = new PieData(pieDataSet);
                    data.setValueTextSize(12f);
                    data.setValueTextColor(Color.DKGRAY);

                    pieDataSet.setValueFormatter(new MyValueFormatter());

                    Legend legend = pieChartView.getLegend();
                    legend.setForm(Legend.LegendForm.SQUARE);
                    legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                    legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                    legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                    legend.setDrawInside(false);
                    legend.setTextSize(12f);

                    pieChartView.setData(data);
                }
            } catch (JSONException e) {
                response = e.getMessage();
                Log.e("JSON parsing error", response);
            }
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        finish();
    }
}

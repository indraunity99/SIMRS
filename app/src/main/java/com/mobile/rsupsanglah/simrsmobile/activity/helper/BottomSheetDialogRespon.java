package com.mobile.rsupsanglah.simrsmobile.activity.helper;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.rsupsanglah.simrsmobile.R;
import com.mobile.rsupsanglah.simrsmobile.activity.adapter.AutoCompleteAdapter;
import com.mobile.rsupsanglah.simrsmobile.activity.control.HttpRequest;
import com.mobile.rsupsanglah.simrsmobile.activity.control.SharedPreference;
import com.mobile.rsupsanglah.simrsmobile.activity.control.SimrsmConstant;
import com.mobile.rsupsanglah.simrsmobile.activity.model.ViewResponKeluhan;
import com.mobile.rsupsanglah.simrsmobile.activity.model.listNamaPelaksana;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class BottomSheetDialogRespon extends BottomSheetDialogFragment implements AdapterView.OnItemSelectedListener {
    EditText start, end, work_type;
    String[] listItem, listValues;
    final ViewResponKeluhan ViewResponKeluhan = new ViewResponKeluhan();
    Button fin;
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private Handler handler;
    private AutoCompleteAdapter autoCompleteAdapter;


    public BottomSheetDialogRespon() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.bottom_sheet_respon, container, false);

        start = v.findViewById(R.id.input_time_start);
        end = v.findViewById(R.id.input_time_end);
        work_type = v.findViewById(R.id.input_work_type);
        Spinner s = v.findViewById(R.id.response_spinner);
        listItem = getResources().getStringArray(R.array.response_array);
        fin = v.findViewById(R.id.button_bottom_sheet_respon);

        final DatePickerFragment date = new DatePickerFragment();
        Calendar calender = Calendar.getInstance();
        final Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_spinner_item, listItem);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);

        final AutoCompleteTextView autoCompleteTextView = v.findViewById(R.id.autoCompleteTextView);
        final TextView selectedText = v.findViewById(R.id.selected_item);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date.setArguments(args);
                date.setCallBack(startdate);
                date.show(getFragmentManager(), "Date Picker start");

            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date.setArguments(args);
                date.setCallBack(enddate);
                date.show(getFragmentManager(), "Date Picker end");
            }
        });

        autoCompleteAdapter = new AutoCompleteAdapter(this.getActivity(), android.R.layout.simple_dropdown_item_1line);
        autoCompleteTextView.setThreshold(2);
        autoCompleteTextView.setAdapter(autoCompleteAdapter);
        autoCompleteTextView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String result = autoCompleteAdapter.getObject(position);
                        ViewResponKeluhan.setNama_pelaksana(result);
                    }
                });

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(autoCompleteTextView.getText())) {
                        getListNamaPelaksana(autoCompleteTextView.getText().toString());
                    }
                }
                return false;
            }
        });
        String work = work_type.getText().toString();
        ViewResponKeluhan.setJenis_pekerjaan(work);

        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                HashMap<String,Integer> hashMap = new HashMap<String,Integer>();
                hashMap.put("Dikerjakan",2);
                hashMap.put("Pending: Menunggu Bahan", 3);
                hashMap.put("Pending: Usulan Pihak III", 4);
                int selectv = hashMap.get(selectedItemText);
                Log.i("MAP STATUS ORDER", hashMap.get(selectedItemText).toString());
                ViewResponKeluhan.setRespon(selectv);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        fin.setOnClickListener(new View.OnClickListener() {////////////////////////////button fin
            @Override
            public void onClick(View v) {
                if (start.getText().toString().equals("") || end.getText().toString().equals(" ") || work_type.getText().toString().equals(" ")) {
                    toastMessage("Please fill all of this field");
                } else {
                    String id = SharedPreference.getInstance(getContext()).getActtoFrag();
                    Log.i("EIWDNEOUIDCHNOIEVCiub", work_type.getText().toString() );
                    saveHistoryPekerjaan(id, work_type.getText().toString().replace(" ", "%20"), ViewResponKeluhan.getId_pelaksana(),
                            ViewResponKeluhan.getWaktu_mulai(), ViewResponKeluhan.getWaktu_selesai(), ViewResponKeluhan.getRespon());
                }
             }
        });
        return v;
    }

    DatePickerDialog.OnDateSetListener startdate = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            start.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear + 1) + "-" + String.valueOf(year));
            String t_start = start.getText().toString();/////////////////////////////////////////////////value
            ViewResponKeluhan.setWaktu_mulai(t_start);
        }
    };

    DatePickerDialog.OnDateSetListener enddate = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            end.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(0 + monthOfYear + 1) + "-" + String.valueOf(year));
            String t_end = end.getText().toString();
            ViewResponKeluhan.setWaktu_selesai(t_end);
        }
    };

    public interface BottomSheetListener {
        void onButtonClicked(String jenis_pekerjaan);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }

    private void toastMessage(String message) {
        Toast.makeText(this.getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void getListNamaPelaksana(final String text) {
        final String ipAddress = SharedPreference.getInstance(getContext()).getIpAddress();
        @SuppressLint("StaticFieldLeak")
        class getListNamaPelaksana extends AsyncTask<Void, Void, String> {
            private final WeakReference<BottomSheetDialogRespon> mActivityRef;

            private getListNamaPelaksana(BottomSheetDialogRespon activity) {
                mActivityRef = new WeakReference<>(activity);
            }

            protected String doInBackground(Void[] params) {
                String response = "";
                HashMap<String, String> map = new HashMap<>();
                try {
                    HttpRequest req = new HttpRequest(ipAddress + SimrsmConstant.ServiceType.URLGETNAMAPELAKSANA + text.replace(" ", "%20"));////////////////////////////////////////should be search.php?... using search method with text as query
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
        new getListNamaPelaksana(BottomSheetDialogRespon.this).execute();
    }

    public void onTaskCompleted(String response) {
//        Log.d("RESPONSE", response);
        ArrayList<String> list = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(response);
            JSONArray userArray = obj.getJSONArray("pelaksana");
            for (int i = 0; i < userArray.length(); i++) {
                JSONObject userObj = (JSONObject) userArray.get(i);
                listNamaPelaksana listNamaPelaksana = new listNamaPelaksana();
                listNamaPelaksana.setName(userObj.getString("nama"));
                listNamaPelaksana.setIdPelaksana(userObj.getString("ID"));
                ViewResponKeluhan.setNama_pelaksana(userObj.getString("nama"));
                ViewResponKeluhan.setId_pelaksana(userObj.getString("ID"));
                list.add(userObj.getString("nama"));
            }
        } catch (JSONException e) {
            response = e.getMessage();
            Log.e("JSON parsing error", response);
        }
        autoCompleteAdapter.setData(list);
        autoCompleteAdapter.notifyDataSetChanged();
    }

    private void saveHistoryPekerjaan(final String order_id, final String pekerjaan, final String id_pelaksana, final String tgl_mulai, final String tgl_selesai, final int respon) {
        final String ipAddress = SharedPreference.getInstance(getContext()).getIpAddress();
        @SuppressLint("StaticFieldLeak")
        class saveHistoryPekerjaan extends AsyncTask<Void, Void, String> {
            private final WeakReference<BottomSheetDialogRespon> mActivityRef;

            private saveHistoryPekerjaan(BottomSheetDialogRespon activity) {
                mActivityRef = new WeakReference<>(activity);
            }

            protected String doInBackground(Void[] params) {
                String response = "";
                HashMap<String, String> map = new HashMap<>();
                try {
                    HttpRequest req = new HttpRequest(ipAddress + SimrsmConstant.ServiceType.URLSAVEHISTORYPEKERJAAN + order_id +"&pekerjaan=" + pekerjaan + "&pelaksana="
                            + id_pelaksana + "&tgl_mulai=" + tgl_mulai +"&tgl_selesai=" + tgl_selesai + "&respon=" + respon);/////////////+-///////////////////////////should be search.php?... using search method with text as query
                    response = req.prepare(HttpRequest.Method.POST).withData(map).sendAndReadString();
                    Log.e("REQ", ipAddress + SimrsmConstant.ServiceType.URLSAVEHISTORYPEKERJAAN + order_id +"&pekerjaan=" + pekerjaan + "&pelaksana="
                            + id_pelaksana + "&tgl_mulai=" + tgl_mulai +"&tgl_selesai=" + tgl_selesai + "&respon=" + respon);
                } catch (IOException e) {
                    response = e.getMessage();
                }
                return response;
            }

            protected void onPostExecute(String result) {
                onTaskCompletedsave(result);
            }
        }
        new saveHistoryPekerjaan(BottomSheetDialogRespon.this).execute();
    }

    public void onTaskCompletedsave(String response) {
//        Log.d("RESPONSE", response);
        if (response.contains("sukses")) {
            toastMessage("Succesfully save data");
            dismiss();
        } else {
            toastMessage("Error on saving data");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
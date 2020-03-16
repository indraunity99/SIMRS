package com.mobile.rsupsanglah.simrsmobile.activity.helper;

import android.annotation.SuppressLint;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.rsupsanglah.simrsmobile.R;
import com.mobile.rsupsanglah.simrsmobile.activity.adapter.AutoCompleteAdapter;
import com.mobile.rsupsanglah.simrsmobile.activity.control.HttpRequest;
import com.mobile.rsupsanglah.simrsmobile.activity.control.SharedPreference;
import com.mobile.rsupsanglah.simrsmobile.activity.control.SimrsmConstant;
import com.mobile.rsupsanglah.simrsmobile.activity.model.listInputBahan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class BottomSheetDialogInput extends BottomSheetDialogFragment implements AdapterView.OnItemSelectedListener {
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private Handler handler;
    private AutoCompleteAdapter autoCompleteAdapter;
    EditText jml_bahan;
    Button fin;
    listInputBahan listInputBahan = new listInputBahan();

    public BottomSheetDialogInput() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_input_bahan, container, false);

        final AutoCompleteTextView autoCompleteTextView = v.findViewById(R.id.autoCompleteTextView_bahan);
        final TextView selectedText = v.findViewById(R.id.selected_item_bahan);
        jml_bahan = v.findViewById(R.id.input_jumlah_bahan);
        fin = v.findViewById(R.id.button_save_jumlah_bahan);

        listInputBahan.setJumlah_bahan(jml_bahan.getText().toString());

        Date c = Calendar.getInstance().getTime();
        Log.i("Current time => ", c.toString());

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        final String formattedDate = df.format(c);

        //Setting up the adapter for AutoSuggest
        autoCompleteAdapter = new AutoCompleteAdapter(this.getActivity(), android.R.layout.simple_dropdown_item_1line);
        autoCompleteTextView.setThreshold(2);
        autoCompleteTextView.setAdapter(autoCompleteAdapter);
        autoCompleteTextView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        String result = autoCompleteAdapter.getObject(position);
                        listInputBahan.setNama_bahan(result);

                    }
                });

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(autoCompleteTextView.getText())) {
                        getListNamaBahan(autoCompleteTextView.getText().toString());
                    }
                }
                return false;
            }
        });

        fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (autoCompleteTextView.getText().toString().equals("") || jml_bahan.getText().toString().equals(" ")) {
                    toastMessage("Please fill all of this field");
                } else {
                    String id = SharedPreference.getInstance(getContext()).getActtoFrag();
                    saveListNamaBahan(id, listInputBahan.getIdBahan(), jml_bahan.getText().toString(), formattedDate);
                }
            }
        });

        return v;
    }


    public interface BottomSheetListener {
        void onButtonClicked(String text);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void getListNamaBahan(final String text) {
        final String ipAddress = SharedPreference.getInstance(getContext()).getIpAddress();
        @SuppressLint("StaticFieldLeak")
        class getListNamaBahan extends AsyncTask<Void, Void, String> {
            private final WeakReference<BottomSheetDialogInput> mActivityRef;

            private getListNamaBahan(BottomSheetDialogInput activity) {
                mActivityRef = new WeakReference<>(activity);
            }

            protected String doInBackground(Void[] params) {
                String response = "";
                HashMap<String, String> map = new HashMap<>();
                try {
                    HttpRequest req = new HttpRequest(ipAddress + SimrsmConstant.ServiceType.URLGETLISTBAHAN + text.replace(" ", "%20"));////////////////////////////////////////should be search.php?... using search method with text as query
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
        new getListNamaBahan(BottomSheetDialogInput.this).execute();
    }

    public void onTaskCompleted(String response) {
        Log.d("RESPONSE", response);
        ArrayList<String> list = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(response);
            JSONArray userArray = obj.getJSONArray("list_bahan");
            for (int i = 0; i < userArray.length(); i++) {
                JSONObject userObj = (JSONObject) userArray.get(i);
                listInputBahan.setIdBahan(userObj.getString("ID"));
                listInputBahan.setNama_bahan(userObj.getString("nama"));
                list.add(userObj.getString("nama"));
                Log.e("IDBAHAN", listInputBahan.getIdBahan());
            }
        } catch (JSONException e) {
            response = e.getMessage();
            Log.e("JSON parsing error", response);
        }
        autoCompleteAdapter.setData(list);
        autoCompleteAdapter.notifyDataSetChanged();
    }

    private void saveListNamaBahan(final String id_bahan, final String nama_bahan, final String jml_bahan, final String tgl_order) {
        final String ipAddress = SharedPreference.getInstance(getContext()).getIpAddress();
        @SuppressLint("StaticFieldLeak")
        class saveListNamaBahan extends AsyncTask<Void, Void, String> {
            private final WeakReference<BottomSheetDialogInput> mActivityRef;

            private saveListNamaBahan(BottomSheetDialogInput activity) {
                mActivityRef = new WeakReference<>(activity);
            }

            protected String doInBackground(Void[] params) {
                String response = "";
                HashMap<String, String> map = new HashMap<>();
                try {
                    HttpRequest req = new HttpRequest(ipAddress+ SimrsmConstant.ServiceType.URLSAVELISTBAHAN + id_bahan +"&id_bahan="+ nama_bahan +"&jml="+ jml_bahan +"&tgl_order=" + tgl_order);////////////////////////////////////////should be search.php?... using search method with text as query
                    Log.e("REQBAHAN", ipAddress+ SimrsmConstant.ServiceType.URLSAVELISTBAHAN + id_bahan +"&id_bahan="+ nama_bahan +"&jml="+ jml_bahan +"&tgl_order=" + tgl_order);
                    response = req.prepare(HttpRequest.Method.POST).withData(map).sendAndReadString();
                } catch (IOException e) {
                    response = e.getMessage();
                }
                return response;
            }

            protected void onPostExecute(String result) {
                onTaskCompletedsave(result);
            }
        }
        new saveListNamaBahan(BottomSheetDialogInput.this).execute();
    }

    public void onTaskCompletedsave(String response) {
        Log.d("RESPONSE", response);
        if (response.contains("sukses")){
            toastMessage("Succesfully save data");
            dismiss();
        } else {
            toastMessage("Error on saving data");
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this.getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}


package com.example.smartlight.fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartlight.Factory;
import com.example.smartlight.R;
import com.example.smartlight.activities.MainActivity;
import com.example.smartlight.interfaces.MyFragment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PowerFragment extends Fragment implements MyFragment, View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private ProgressDialog loadingDialog = null;
    private View view;
    private Button backBtn;
    private ImageButton lightningMenuBtn;
    private SeekBar powerSeek;
    private TextView powerTv;
    private BarChart powerGraph;

    private List<String> times;
    private List<BarEntry> entries;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_power, container, false);
        try {
            initUI();
        } catch (ParseException e) {}
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        lightningMenuBtn.setImageResource(R.drawable.ic_baseline_bolt_24);
    }

    @Override
    public void onResume() {
        super.onResume();
        lightningMenuBtn.setImageResource(R.drawable.ic_baseline_bolt_selected_24);
    }

    private void initUI() throws ParseException {
        lightningMenuBtn = getActivity().findViewById(R.id.btn_menu_lightning);
        lightningMenuBtn.setImageResource(R.drawable.ic_baseline_bolt_selected_24);

        backBtn = (Button) view.findViewById(R.id.btn_back);
        backBtn.setOnClickListener(this);

        powerSeek = (SeekBar) view.findViewById(R.id.seek_power);
        powerSeek.setOnSeekBarChangeListener(this);
        powerTv = (TextView) view.findViewById(R.id.tv_power);

        powerSeek.setProgress(Factory.device.getPower());
        powerTv.setText(Factory.device.getPower() + "W");

        powerGraph = (BarChart) view.findViewById(R.id.graph_power);
        powerGraph.getDescription().setEnabled(false);
        powerGraph.getXAxis().setDrawGridLines(false);
        powerGraph.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        powerGraph.setPinchZoom(false);
        powerGraph.setDoubleTapToZoomEnabled(false);
        powerGraph.getAxisRight().setEnabled(false);
        powerGraph.getLegend().setEnabled(false);
        powerGraph.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return times.get((int) value).substring(0, 5);
            }
        });
        powerGraph.getAxisLeft().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "$" + value;
            }
        });

        loadingDialog = new ProgressDialog(getActivity());
        loadingDialog.setMessage("");
        loadingDialog.setIndeterminate(true);
        loadingDialog.show();

        getData();
    }

    private void updateChart(){
        BarDataSet dataSet = new BarDataSet(entries, "Lượng tiêu thụ điện");
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "$" + value;
            }
        });
        dataSet.setValueTextSize(10);
        dataSet.setColor(Color.argb(255, 0, 0, 255));
        BarData barData = new BarData(dataSet);
        powerGraph.setData(barData);

        powerGraph.invalidate();
    }

    private void getData() {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getBaseContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Factory.HOST + "/?action=get_power&id=" + Factory.device.getId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if(Factory.debug){
                                Log.d("MinhLV", response);
                            }
                            loadingDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            int i = 0;
                            times = new ArrayList<>();
                            entries = new ArrayList<>();
                            for (Iterator<String> it = jsonObject.keys(); it.hasNext(); ) {
                                String key = it.next();
                                times.add(key);
                                entries.add(new BarEntry(i++, (int) (jsonObject.getDouble(key) * 20)));
                            }
                            updateChart();
                        } catch (JSONException e) {
                            if(Factory.debug) {
                                Log.d("MinhLV", e.getMessage());
                            }
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                    Log.d("MinhLV", error.getMessage());
                    }
                })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private void setControl(int power){
        RequestQueue queue = Volley.newRequestQueue(getActivity().getBaseContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Factory.HOST + "/?action=setparam",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if(Factory.debug) {
                                Log.d("MinhLV", response);
                            }
                            JSONObject jsonObject = new JSONObject(response);
                        } catch (JSONException e) {
                            if(Factory.debug) {
                                Log.d("MinhLV", e.getMessage());
                            }
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                    Log.d("MinhLV", error.getMessage());
                    }
                })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("id", "" + Factory.device.getId());
                params.put("param", "power");
                params.put("value", "" + power);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private void loadFragment(Fragment fragment) {
        MainActivity.FRAG_ID = ((MyFragment) fragment).getTAG();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layout_main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_back) {
            loadFragment(new ControlFragment());
        }
    }

    @Override
    public String getTAG() {
        return "Power";
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        int progress = seekBar.getProgress();
        powerTv.setText(progress + "W");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int progress = seekBar.getProgress();
        setControl(progress);
    }
}

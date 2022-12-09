package com.example.smartlight.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartlight.Config;
import com.example.smartlight.R;
import com.example.smartlight.activities.MainActivity;
import com.example.smartlight.components.RotaryKnobView;
import com.example.smartlight.interfaces.MyFragment;
import com.example.smartlight.models.User;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LightFragment extends Fragment implements MyFragment, View.OnClickListener, RotaryKnobView.RotaryKnobListener {

    private View view;
    private Button backBtn;
    private RotaryKnobView lightKnob;
    private TextView lightTv;
    private LineChart lightGraph;
    private ImageButton lightningMenuBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_light, container, false);
        initUI();
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        lightningMenuBtn.setImageResource(R.drawable.ic_baseline_bolt_24);
    }

    private void initUI() {
        lightningMenuBtn = getActivity().findViewById(R.id.btn_menu_lightning);
        lightningMenuBtn.setImageResource(R.drawable.ic_baseline_bolt_selected_24);

        backBtn = (Button) view.findViewById(R.id.btn_back);
        backBtn.setOnClickListener(this);

        lightKnob = (RotaryKnobView) view.findViewById(R.id.knob_light);
        lightKnob.setListener(this);
        lightKnob.setValue(Config.device.getLight());

        lightTv = (TextView) view.findViewById(R.id.tv_light);
        lightTv.setText(Config.device.getLight() + "%");

        lightGraph = (LineChart) view.findViewById(R.id.graph_light);
        lightGraph.getDescription().setEnabled(false);
        lightGraph.getXAxis().setDrawGridLines(false);
        lightGraph.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lightGraph.setPinchZoom(false);
        lightGraph.setDoubleTapToZoomEnabled(false);
        lightGraph.getAxisRight().setEnabled(false);
        lightGraph.getLegend().setEnabled(false);
        
        List<String> times = new ArrayList<String>();
        times.add("21:30:00");
        times.add("21:30:30");
        times.add("21:31:00");
        times.add("21:31:30");
        times.add("21:32:00");
        times.add("21:32:30");
        times.add("21:33:00");
        times.add("21:33:30");
        times.add("21:34:00");
        times.add("21:34:30");

        List<Entry> entries = new ArrayList<Entry>();
        entries.add(new Entry(0, 60));
        entries.add(new Entry(1, 62));
        entries.add(new Entry(2, 64));
        entries.add(new Entry(3, 63));
        entries.add(new Entry(4, 67));
        entries.add(new Entry(5, 50));
        entries.add(new Entry(6, 50));
        entries.add(new Entry(7, 51));
        entries.add(new Entry(8, 55));
        entries.add(new Entry(9, 70));

        LineDataSet dataSet = new LineDataSet(entries, "");
        LineData lineData = new LineData(dataSet);
        lightGraph.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return times.get((int) value);
            }
        });
        lightGraph.setData(lineData);
        lightGraph.invalidate();
    }

    private void setControl(){
        RequestQueue queue = Volley.newRequestQueue(getActivity().getBaseContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.HOST + "/?action=setparam",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if(Config.debug) {
                                Log.d("MinhLV", response);
                            }
                            JSONObject jsonObject = new JSONObject(response);
                        } catch (JSONException e) {
                            if(Config.debug) {
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
                params.put("id", "" + Config.device.getId());
                params.put("param", "light");
                params.put("value", "" + lightKnob.getValue());
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
    public void onRotate(int var1) {
        lightTv.setText(var1 + "%");
    }

    @Override
    public void onTouch(@NonNull MotionEvent e) {
        if(e.getAction() == MotionEvent.ACTION_UP) {
            setControl();
        }
    }

    @Override
    public String getTAG() {
        return "Light";
    }
}

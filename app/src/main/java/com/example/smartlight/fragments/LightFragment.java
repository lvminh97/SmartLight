package com.example.smartlight.fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartlight.Factory;
import com.example.smartlight.NukeSSLCerts;
import com.example.smartlight.R;
import com.example.smartlight.activities.MainActivity;
import com.example.smartlight.components.RotaryKnobView;
import com.example.smartlight.interfaces.MyFragment;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class LightFragment extends Fragment implements MyFragment, View.OnClickListener, RotaryKnobView.RotaryKnobListener {

    private ProgressDialog loadingDialog = null;
    private View view;
    private Button backBtn;
    private RotaryKnobView lightKnob;
    private TextView lightTv;
    private LineChart lightGraph;
    private ImageButton lightningMenuBtn;

    private List<String> times;
    private List<Entry> entries;

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


    private void initUI() {
        // rearrange the bottom menu
        ConstraintSet set = new ConstraintSet();
        set.clone((ConstraintLayout) getActivity().findViewById(R.id.menu_bottom));
        set.constrainPercentWidth(R.id.btn_add, 0.0f);
        set.constrainPercentWidth(R.id.btn_menu_home, 0.5f);
        set.constrainPercentWidth(R.id.btn_menu_user, 0.5f);
        set.applyTo((ConstraintLayout) getActivity().findViewById(R.id.menu_bottom));

        backBtn = (Button) view.findViewById(R.id.btn_back);
        backBtn.setOnClickListener(this);

        lightKnob = (RotaryKnobView) view.findViewById(R.id.knob_light);
        lightKnob.setLock(!Factory.user.isAppControl());
        lightKnob.setListener(this);
        lightKnob.setValue(Factory.device.getLight());

        lightTv = (TextView) view.findViewById(R.id.tv_light);
        lightTv.setText(Factory.device.getLight() + "%");

        lightGraph = (LineChart) view.findViewById(R.id.graph_light);
        lightGraph.getDescription().setEnabled(false);
        lightGraph.getXAxis().setDrawGridLines(false);
        lightGraph.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lightGraph.setPinchZoom(false);
        lightGraph.setDoubleTapToZoomEnabled(false);
        lightGraph.getAxisRight().setEnabled(false);
        lightGraph.getLegend().setEnabled(false);
        lightGraph.getAxisLeft().setAxisMaximum(100);
        lightGraph.getAxisLeft().setAxisMinimum(0);
        lightGraph.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return times.get((int) value);
            }
        });

        loadingDialog = new ProgressDialog(getActivity());
        loadingDialog.setMessage("");
        loadingDialog.setIndeterminate(true);

        getData();
    }

    private void updateChart(){
        LineDataSet dataSet = new LineDataSet(entries, "");
        dataSet.setDrawValues(false);
        dataSet.setDrawCircles(false);
        dataSet.setLineWidth(2);
        dataSet.setColor(Color.argb(255, 255, 0, 0));
        LineData lineData = new LineData(dataSet);
        lightGraph.setData(lineData);
        lightGraph.invalidate();
    }

    private void getData() {
        new NukeSSLCerts().nuke();
        RequestQueue queue = Volley.newRequestQueue(getActivity().getBaseContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Factory.HOST + "/?action=get_light&id=" + Factory.device.getId(),
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        loadingDialog.dismiss();
                        JSONArray jsonArray = new JSONArray(response);
                        times = new ArrayList<>();
                        entries = new ArrayList<>();
                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject json = jsonArray.getJSONObject(i);
                            times.add(json.getString("time").split(" ")[1]);
                            entries.add(new Entry(i, Integer.parseInt(json.getString("light"))));
                            updateChart();
                        }
                    } catch (JSONException e) {
                        if(Factory.debug) {
                            Log.d(Factory.debugTag, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    Log.d(Factory.debugTag, error.getMessage());
                }
            })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                return params;
            }
        };
        loadingDialog.show();
        queue.add(stringRequest);
    }

    private void setControl(){
        Factory.device.setLight(lightKnob.getValue());
        new NukeSSLCerts().nuke();
        RequestQueue queue = Volley.newRequestQueue(getActivity().getBaseContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Factory.HOST + "/?action=setparam",
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        if(Factory.debug) {
                            Log.d(Factory.debugTag, response);
                        }
                        JSONObject jsonObject = new JSONObject(response);
                    } catch (JSONException e) {
                        if(Factory.debug) {
                            Log.d(Factory.debugTag, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    Log.d(Factory.debugTag, error.getMessage());
                }
            })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("apikey", "" + Factory.device.getApiKey());
                params.put("param", "light");
                params.put("value", "" + Factory.device.getLight());
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
            if(Factory.user.isAppControl())
                setControl();
            else
                Toast.makeText(getContext(), "Quyền điều khiển từ App đã bị khóa, mở khóa để tiếp tục", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public String getTAG() {
        return "Light";
    }
}

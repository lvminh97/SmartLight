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
import com.example.smartlight.interfaces.MyFragment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
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

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class PowerFragment extends Fragment implements MyFragment, View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private ProgressDialog loadingDialog = null;
    private View view;
    private Button backBtn, dailyBtn, monthlyBtn, yearlyBtn;
    private ImageButton lightningMenuBtn;
    private SeekBar powerSeek;
    private TextView powerTv, totalPowerTv, totalCostTv;
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

    private void initUI() throws ParseException {
        // rearrange the bottom menu
        ConstraintSet set = new ConstraintSet();
        set.clone((ConstraintLayout) getActivity().findViewById(R.id.menu_bottom));
        set.constrainPercentWidth(R.id.btn_add, 0.0f);
        set.constrainPercentWidth(R.id.btn_menu_home, 0.5f);
        set.constrainPercentWidth(R.id.btn_menu_user, 0.5f);
        set.applyTo((ConstraintLayout) getActivity().findViewById(R.id.menu_bottom));

        backBtn = (Button) view.findViewById(R.id.btn_back);
        backBtn.setOnClickListener(this);

        dailyBtn = (Button) view.findViewById(R.id.btn_tab_daily);
        dailyBtn.setOnClickListener(this);
        monthlyBtn = (Button) view.findViewById(R.id.btn_tab_monthly);
        monthlyBtn.setOnClickListener(this);
        yearlyBtn = (Button) view.findViewById(R.id.btn_tab_yearly);
        yearlyBtn.setOnClickListener(this);

        powerSeek = (SeekBar) view.findViewById(R.id.seek_power);
        powerSeek.setOnSeekBarChangeListener(this);
        powerTv = (TextView) view.findViewById(R.id.tv_power);

        powerSeek.setProgress(Factory.device.getPower());
        powerTv.setText(Factory.device.getPower() + "W");

        totalPowerTv = (TextView) view.findViewById(R.id.tv_total_power_value);
        totalPowerTv.setText("0 kWh");
        totalCostTv = (TextView) view.findViewById(R.id.tv_total_cost_value);
        totalCostTv.setText("0 VND");

        powerGraph = (BarChart) view.findViewById(R.id.graph_power);
        powerGraph.getDescription().setEnabled(false);
        powerGraph.getXAxis().setDrawGridLines(false);
        powerGraph.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        powerGraph.setPinchZoom(false);
        powerGraph.setDoubleTapToZoomEnabled(false);
        powerGraph.getAxisRight().setEnabled(false);
        powerGraph.getLegend().setEnabled(false);
        powerGraph.getXAxis().setGranularityEnabled(true);
        powerGraph.getAxisLeft().setAxisMinimum(0);
        powerGraph.getAxisLeft().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if(value == 0.0)
                    return "0";
                else if(value < 1000000)
                    return String.format("%dk", (int) (value / 1000));
                else
                    return String.format("%dM", (int) (value / 1000000));
            }
        });

        loadingDialog = new ProgressDialog(getActivity());
        loadingDialog.setMessage("");
        loadingDialog.setIndeterminate(true);
        loadingDialog.show();

        getData("daily");
    }

    private void updateChart(){
        BarDataSet dataSet = new BarDataSet(entries, "Lượng tiêu thụ điện");
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if(value <= 0)
                    return "";
                if(value < 1000000)
                    return String.format("%.1fk", (value / 1000));
                else
                    return String.format("%.2fM", (value / 1000000));
            }
        });
        dataSet.setValueTextSize(10);
        dataSet.setColor(Color.argb(255, 0, 0, 255));
        BarData barData = new BarData(dataSet);
        powerGraph.setData(barData);

        powerGraph.invalidate();
    }

    private void getData(String type) {
        new NukeSSLCerts().nuke();
        RequestQueue queue = Volley.newRequestQueue(getActivity().getBaseContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Factory.HOST + "/?action=get_power&id=" + Factory.device.getId() + "&type=" + type,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if(Factory.debug){
                                Log.d(Factory.debugTag, response);
                            }
                            loadingDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject powerJsonObject = jsonObject.getJSONObject("data");
                            times = new ArrayList<>();
                            entries = new ArrayList<>();
                            int i = 0;
                            double total_power = 0, total_cost = 0, max_value = 0;
                            for (Iterator<String> it = powerJsonObject.keys(); it.hasNext(); ) {
                                String key = it.next();
                                times.add(key);
                                entries.add(new BarEntry(i++, (int) (powerJsonObject.getDouble(key) * 3000)));
                                total_power += powerJsonObject.getDouble(key);
                                total_cost += powerJsonObject.getDouble(key) * 3000;
                                max_value = Math.max(max_value, powerJsonObject.getDouble(key) * 3000);
                            }
                            if(type.equals("daily")) {
                                powerGraph.setScaleMinima(3, 1);
                                powerGraph.getXAxis().setValueFormatter(new ValueFormatter() {
                                    @Override
                                    public String getFormattedValue(float value) {
                                        try {
                                            return times.get((int) value).substring(0, 5);
                                        }
                                        catch (Exception error) {
                                            return null;
                                        }
                                    }
                                });
                            }
                            else {
                                powerGraph.fitScreen();
                                powerGraph.getXAxis().setValueFormatter(new ValueFormatter() {
                                    @Override
                                    public String getFormattedValue(float value) {
                                        try {
                                            return times.get((int) value);
                                        }
                                        catch (Exception error) {
                                            return null;
                                        }
                                    }
                                });
                            }
                            powerGraph.getAxisLeft().setAxisMaximum((float) (max_value * 1.5));
                            updateChart();

                            totalPowerTv.setText(String.format("%.1f kWh", total_power));
                            totalCostTv.setText(String.format("%d VND", (int) total_cost));
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
        queue.add(stringRequest);
    }

    private void setControl(int power){
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
                params.put("param", "power");
                params.put("value", "" + Factory.device.getPower());
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

    private void setPowerTab(String type) {
        if(type.equals("daily")) {
            dailyBtn.setBackground(getResources().getDrawable(R.drawable.bg_power_tab));
            dailyBtn.setTextColor(Color.argb(255, 81, 184, 230));
            monthlyBtn.setBackgroundColor(Color.argb(0, 0, 0, 0));
            monthlyBtn.setTextColor(Color.argb(255, 255, 255, 255));
            yearlyBtn.setBackgroundColor(Color.argb(0, 0,0, 0));
            yearlyBtn.setTextColor(Color.argb(255, 255, 255, 255));
        }
        else if(type.equals("monthly")) {
            dailyBtn.setBackgroundColor(Color.argb(0, 0, 0, 0));
            dailyBtn.setTextColor(Color.argb(255, 255, 255, 255));
            monthlyBtn.setBackground(getResources().getDrawable(R.drawable.bg_power_tab));
            monthlyBtn.setTextColor(Color.argb(255, 81, 184, 230));
            yearlyBtn.setBackgroundColor(Color.argb(0, 0,0, 0));
            yearlyBtn.setTextColor(Color.argb(255, 255, 255, 255));
        }
        else if(type.equals("yearly")) {
            dailyBtn.setBackgroundColor(Color.argb(0, 0, 0, 0));
            dailyBtn.setTextColor(Color.argb(255, 255, 255, 255));
            monthlyBtn.setBackgroundColor(Color.argb(0, 0, 0, 0));
            monthlyBtn.setTextColor(Color.argb(255, 255, 255, 255));
            yearlyBtn.setBackground(getResources().getDrawable(R.drawable.bg_power_tab));
            yearlyBtn.setTextColor(Color.argb(255, 81, 184, 230));
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_back) {
            loadFragment(new ControlFragment());
        }
        else if(v.getId() == R.id.btn_tab_daily) {
            getData("daily");
            setPowerTab("daily");
        }
        else if(v.getId() == R.id.btn_tab_monthly) {
            getData("monthly");
            setPowerTab("monthly");
        }
        else if(v.getId() == R.id.btn_tab_yearly) {
            getData("yearly");
            setPowerTab("yearly");
        }
    }

    @Override
    public String getTAG() {
        return "Power";
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        if(Factory.user.isAppControl()) {
            powerTv.setText(progress + "W");
        }
        else {
           seekBar.setProgress(Factory.device.getPower());
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int progress = seekBar.getProgress();
        if(Factory.user.isAppControl()) {
            Factory.device.setPower(powerSeek.getProgress());
            setControl(progress);
        }
        else
            Toast.makeText(getContext(), "Quyền điều khiển từ App đã bị khóa, mở khóa để tiếp tục", Toast.LENGTH_SHORT).show();
    }
}

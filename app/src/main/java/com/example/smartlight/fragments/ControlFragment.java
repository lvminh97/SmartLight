package com.example.smartlight.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import com.example.smartlight.components.RotaryKnobView;
import com.example.smartlight.interfaces.MyFragment;
import com.example.smartlight.models.Device;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ControlFragment extends Fragment implements MyFragment, View.OnClickListener, RotaryKnobView.RotaryKnobListener {

    private View view;
    private RotaryKnobView tempKnob;
    private TextView tempTv;
    private Button backBtn, lightBtn, powerBtn, setupBtn;
    private ImageButton lightningMenuBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_control, container, false);
        initUI();
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

    private void initUI() {
        lightningMenuBtn = getActivity().findViewById(R.id.btn_menu_lightning);
        lightningMenuBtn.setImageResource(R.drawable.ic_baseline_bolt_selected_24);
        tempKnob = (RotaryKnobView) view.findViewById(R.id.knob);
        tempKnob.setListener(this);
        tempKnob.setValue(0);
        tempTv = (TextView) view.findViewById(R.id.tv_temp);
        tempTv.setText("0 °C");
        backBtn = (Button) view.findViewById(R.id.btn_back);
        backBtn.setOnClickListener(this);
        lightBtn = (Button) view.findViewById(R.id.btn_light);
        lightBtn.setOnClickListener(this);
        powerBtn = (Button) view.findViewById(R.id.btn_power);
        powerBtn.setOnClickListener(this);
        setupBtn = (Button) view.findViewById(R.id.btn_setup);
        setupBtn.setOnClickListener(this);

        getDevice();
    }

    @Override
    public String getTAG() {
        return "Control";
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_back){
            loadFragment(new HomeFragment());
        }
        else if(view.getId() == R.id.btn_light) {
            loadFragment(new LightFragment());
        }
        else if(view.getId() == R.id.btn_power){
            loadFragment(new PowerFragment());
        }
        else if(view.getId() == R.id.btn_setup){
            new AlertDialog.Builder(getContext())
                    .setTitle("Thông báo")
                    .setMessage("Cài đặt các thông số mặc định cho hệ thống thành công!")
                    .setPositiveButton("OK", null)
                    .show();
        }
    }

    @Override
    public void onRotate(int value) {
        tempTv.setText(value + " °C");
    }

    @Override
    public void onTouch(@NonNull MotionEvent e) {
        if(e.getAction() == MotionEvent.ACTION_UP) {
            setControl();
        }
    }

    private void getDevice() {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getBaseContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Factory.HOST + "/?action=get_devices&room_id=" + Factory.room.getId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if(Factory.debug) {
                                Log.d("MinhLV", response);
                            }
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject deviceJson = jsonArray.getJSONObject(0);
                            Factory.device = new Device(Integer.parseInt(deviceJson.getString("id")),
                                                        Integer.parseInt(deviceJson.getString("room_id")),
                                                        Integer.parseInt(deviceJson.getString("type")));
                            Factory.device.setTemp(Integer.parseInt(deviceJson.getString("temp")));
                            Factory.device.setLight(Integer.parseInt(deviceJson.getString("light")));
                            Factory.device.setPower(Integer.parseInt(deviceJson.getString("power")));
                            tempKnob.setValue(Factory.device.getTemp());
                            tempTv.setText(Factory.device.getTemp() + " °C");

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

    private void setControl(){
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
                params.put("param", "temp");
                params.put("value", "" + tempKnob.getValue());
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
}

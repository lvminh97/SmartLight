package com.example.smartlight.fragments;

import android.app.AlertDialog;
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
import com.example.smartlight.models.Device;
import com.example.smartlight.models.Room;
import com.example.smartlight.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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

    private void initUI() {
        lightningMenuBtn = getActivity().findViewById(R.id.btn_menu_lightning);
        lightningMenuBtn.setImageResource(R.drawable.ic_baseline_bolt_selected_24);
        tempKnob = (RotaryKnobView) view.findViewById(R.id.knob);
        tempKnob.setListener(this);
        tempKnob.setValue(30);
        tempTv = (TextView) view.findViewById(R.id.tv_temp);
        tempTv.setText("30 oC");
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
        tempTv.setText(value + " oC");
    }

    @Override
    public void onTouch(@NonNull MotionEvent e) {
        if(e.getAction() == MotionEvent.ACTION_UP) {
            setControl();
        }
    }

    private void getDevice() {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getBaseContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.HOST + "/?action=get_devices&room_id=" + Config.room.getId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if(Config.debug) {
                                Log.d("MinhLV", response);
                            }
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject deviceJson = jsonArray.getJSONObject(0);
                            Config.device = new Device(Integer.parseInt(deviceJson.getString("id")),
                                                        Integer.parseInt(deviceJson.getString("room_id")),
                                                        Integer.parseInt(deviceJson.getString("type")));
                            Config.device.setTemp(Integer.parseInt(deviceJson.getString("temp")));
                            Config.device.setLight(Integer.parseInt(deviceJson.getString("light")));
                            Config.device.setPower(Integer.parseInt(deviceJson.getString("power")));
                            tempKnob.setValue(Config.device.getTemp());
                            tempTv.setText(Config.device.getTemp() + " oC");

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
                return params;
            }
        };
        queue.add(stringRequest);
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

package com.example.smartlight.fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
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
import com.example.smartlight.models.Device;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class ControlFragment extends Fragment implements MyFragment, View.OnClickListener, RotaryKnobView.RotaryKnobListener, AdapterView.OnItemSelectedListener, SeekBar.OnSeekBarChangeListener {

    private ProgressDialog loadingDialog = null;
    private View view;
    private RotaryKnobView tempKnob;
    private TextView roomNameTv, tempTv, lightTv;
    private ImageView lightRayImg;
    private SeekBar lightSeek;
    private Button backBtn, lightBtn, powerBtn, setupBtn, warmToneBtn, coldToneBtn;
    private ImageButton homeBtn, addBtn, userBtn;
    private Spinner deviceSpn;

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

    private void initUI() {
        int px = (int) (Factory.displayMetrics.densityDpi / 160f);
        // rearrange the bottom menu
        ConstraintSet set = new ConstraintSet();
        set.clone((ConstraintLayout) getActivity().findViewById(R.id.menu_bottom));
        set.constrainPercentWidth(R.id.btn_add, 0.34f);
        set.constrainPercentWidth(R.id.btn_menu_home, 0.33f);
        set.constrainPercentWidth(R.id.btn_menu_user, 0.33f);
        set.applyTo((ConstraintLayout) getActivity().findViewById(R.id.menu_bottom));

        roomNameTv = (TextView) view.findViewById(R.id.tv_room_name);
        roomNameTv.setText(Factory.room.getName());
        tempKnob = (RotaryKnobView) view.findViewById(R.id.knob);
        tempKnob.setLock(!Factory.user.isAppControl());
        tempKnob.setListener(this);
        tempKnob.setValue(0);
        tempTv = (TextView) view.findViewById(R.id.tv_temp);
        tempTv.setText("0 °C");
        lightTv = (TextView) view.findViewById(R.id.tv_light_value);
        lightTv.setText("0%");
        backBtn = (Button) view.findViewById(R.id.btn_back);
        backBtn.setOnClickListener(this);
        addBtn = (ImageButton) getActivity().findViewById(R.id.btn_add);
        addBtn.setOnClickListener(this);
        lightRayImg = (ImageView) view.findViewById(R.id.img_lightray);
        lightSeek = (SeekBar) view.findViewById(R.id.seek_light);
        lightSeek.setOnSeekBarChangeListener(this);
        powerBtn = (Button) view.findViewById(R.id.btn_power);
        powerBtn.setOnClickListener(this);
        warmToneBtn = (Button) view.findViewById(R.id.btn_tone_warm);
        warmToneBtn.setOnClickListener(this);
        coldToneBtn = (Button) view.findViewById(R.id.btn_tone_cold);
        coldToneBtn.setOnClickListener(this);

        if(Factory.displayMetrics.heightPixels < 1600){
            ViewGroup.MarginLayoutParams lightBtnLayoutParam = (ViewGroup.MarginLayoutParams) lightBtn.getLayoutParams();
            lightBtnLayoutParam.setMargins(20 * px, 10 * px, 20 * px, 10 * px);
            ViewGroup.MarginLayoutParams powerBtnLayoutParam = (ViewGroup.MarginLayoutParams) powerBtn.getLayoutParams();
            powerBtnLayoutParam.setMargins(20 * px, 0, 20* px, 10 * px);
        }
        else {
//            ViewGroup.MarginLayoutParams lightBtnLayoutParam = (ViewGroup.MarginLayoutParams) lightBtn.getLayoutParams();
//            lightBtnLayoutParam.setMargins(20 * px, 50 * px, 20 * px, 20 * px);
//            ViewGroup.MarginLayoutParams powerBtnLayoutParam = (ViewGroup.MarginLayoutParams) powerBtn.getLayoutParams();
//            powerBtnLayoutParam.setMargins(20 * px, 0, 20* px, 50 * px);
        }

        loadingDialog = new ProgressDialog(getActivity());
        loadingDialog.setMessage("");
        loadingDialog.setIndeterminate(true);

        getDeviceList();
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
        else if(view.getId() == R.id.btn_add) {
            if(MainActivity.FRAG_ID.equals("Control")) {
                loadFragment(new AddDeviceFragment());
            }
        }
//        else if(view.getId() == R.id.btn_light) {
//            loadFragment(new LightFragment());
//        }
        else if(view.getId() == R.id.btn_power){
            loadFragment(new PowerFragment());
        }
        else if(view.getId() == R.id.btn_tone_warm) {
            if(Factory.device.getLight() != 50) {
                lightSeek.setProgress(50);
                setToneGlow(true);
                Factory.device.setLight(50);
                setControl("light", 50);
            }
        }
        else if(view.getId() == R.id.btn_tone_cold) {
            lightSeek.setProgress(30);
            setToneGlow(false);
            Factory.device.setLight(30);
            setControl("light", 30);
        }
    }

    @Override
    public void onRotate(int value) {
        if(Factory.user.isAppControl() == true) {
            tempTv.setText(value + " °C");
        }
    }

    @Override
    public void onTouch(@NonNull MotionEvent e) {
        if(e.getAction() == MotionEvent.ACTION_UP) {
            Factory.device.setTemp(tempKnob.getValue());
            if(Factory.user.isAppControl())
                setControl("temp", Factory.device.getTemp());
            else
                Toast.makeText(getContext(), "Quyền điều khiển từ App đã bị khóa, mở khóa để tiếp tục", Toast.LENGTH_SHORT).show();
        }
    }

    private void getDeviceList() {
        if(Factory.deviceList == null || Factory.deviceList.size() == 0) {
            new NukeSSLCerts().nuke();
            RequestQueue queue = Volley.newRequestQueue(getActivity().getBaseContext());
            StringRequest stringRequest = new StringRequest(Request.Method.GET, Factory.HOST + "/?action=get_devices&room_id=" + Factory.room.getId(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                loadingDialog.dismiss();
                                if (Factory.debug) {
                                    Log.d("SmartLight_Debug", response);
                                }
                                JSONArray jsonArray = new JSONArray(response);
                                if(Factory.deviceList == null) {
                                    Factory.deviceList = new ArrayList<>();
                                }
                                for(int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject deviceJson = jsonArray.getJSONObject(i);
                                    Device device = new Device(Integer.parseInt(deviceJson.getString("id")),
                                            Integer.parseInt(deviceJson.getString("room_id")),
                                            Integer.parseInt(deviceJson.getString("type")));

                                    device.setApiKey(deviceJson.getString("api_key"));
                                    device.setTemp(Integer.parseInt(deviceJson.getString("temp")));
                                    device.setLight(Integer.parseInt(deviceJson.getString("light")));
                                    device.setPower(Integer.parseInt(deviceJson.getString("power")));
                                    Factory.deviceList.add(device);
                                }

                                Factory.device = Factory.deviceList.get(0);
                                tempKnob.setValue(Factory.device.getTemp());
                                tempTv.setText(Factory.device.getTemp() + " °C");
                                lightSeek.setProgress(Factory.device.getLight());
                                lightTv.setText(Factory.device.getLight() + "%");
                                if(Factory.device.getLight() < 50)
                                    setToneGlow(false);
                                else
                                    setToneGlow(true);
//                                DeviceAdapter adapter = new DeviceAdapter(getActivity(), (ArrayList<Device>) Factory.deviceList);
//                                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, deviceNames);
//                                deviceSpn.setAdapter(adapter);
                            } catch (JSONException e) {
                                if (Factory.debug) {
                                    Log.d("SmartLight_Debug", e.getMessage());
                                }
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //                    Log.d("SmartLight_Debug", error.getMessage());
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    return params;
                }
            };
            loadingDialog.show();
            queue.add(stringRequest);
        }
        else {
            if(Factory.device == null) {
                Factory.device = Factory.deviceList.get(0);
            }
            tempKnob.setValue(Factory.device.getTemp());
            tempTv.setText(Factory.device.getTemp() + " °C");
            int id = 0;
            int select = 0;
            for(Device dev: Factory.deviceList) {
                if(dev.getId() == Factory.device.getId()){
                    select = id;
                }
            }
//            DeviceAdapter adapter = new DeviceAdapter(getActivity(), (ArrayList<Device>) Factory.deviceList);
//            deviceSpn.setAdapter(adapter);
//            deviceSpn.setSelection(select);
        }
    }

    private void setControl(String param, int value){
        new NukeSSLCerts().nuke();
        RequestQueue queue = Volley.newRequestQueue(getActivity().getBaseContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Factory.HOST + "/?action=setparam",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if(Factory.debug) {
                                Log.d("SmartLight_Debug", response);
                            }
                            JSONObject jsonObject = new JSONObject(response);
                        } catch (JSONException e) {
                            if(Factory.debug) {
                                Log.d("SmartLight_Debug", e.getMessage());
                            }
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                    Log.d("SmartLight_Debug", error.getMessage());
                    }
                })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("apikey", "" + Factory.device.getApiKey());
                params.put("param", param);
                params.put("value", "" + value);
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

    private void lightRayColor(int light) {
        int color = 0;
        if(light < 30)
            color = Color.argb(190, 255, 255, 255);
        else if(light < 50)
            color = Color.argb(255, 255, 255, 255);
        else if(light < 80)
            color = Color.argb(255, 255, 253, 202);
        else
            color = Color.argb(255, 254, 221, 0);

        DrawableCompat.setTint(lightRayImg.getBackground(), color);
    }

    private void setToneGlow(boolean isWarm) {
        if(isWarm) {
            warmToneBtn.setBackground(getResources().getDrawable(R.drawable.bg_tone_light_left_selected));
            coldToneBtn.setBackground(getResources().getDrawable(R.drawable.bg_tone_light_right));
        }
        else {
            warmToneBtn.setBackground(getResources().getDrawable(R.drawable.bg_tone_light_left));
            coldToneBtn.setBackground(getResources().getDrawable(R.drawable.bg_tone_light_right_selected));
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Factory.device = Factory.deviceList.get(i);
        tempKnob.setValue(Factory.device.getTemp());
        tempTv.setText(Factory.device.getTemp() + " °C");
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        lightTv.setText(progress + "%");
        lightRayColor(progress);
        //
        if(progress <= 30)
            setToneGlow(false);
        if(progress >= 50)
            setToneGlow(true);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int progress = seekBar.getProgress();
        Factory.device.setLight(progress);
        if(Factory.user.isAppControl())
            setControl("light", Factory.device.getLight());
        else
            Toast.makeText(getContext(), "Quyền điều khiển từ App đã bị khóa, mở khóa để tiếp tục", Toast.LENGTH_SHORT).show();
    }
}

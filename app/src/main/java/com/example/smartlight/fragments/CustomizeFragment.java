package com.example.smartlight.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
import com.example.smartlight.components.CustomizeDeviceButton;
import com.example.smartlight.components.CustomizeRoomView;
import com.example.smartlight.interfaces.MyFragment;
import com.example.smartlight.models.Device;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomizeFragment extends Fragment implements MyFragment, View.OnClickListener, CustomizeRoomView.CustomizeRoomListener, CustomizeDeviceButton.CustomizeDeviceButtonListener {

    private ProgressDialog loadingDialog = null;
    private View view;
    private CustomizeRoomView customizeRoomView;
    private Button updateBtn, delBtn;
    private ImageButton addBtn;
    private TextView deviceNameTv;
    private ArrayList<CustomizeDeviceButton> deviceButtons;
    private CustomizeDeviceButton activeDeviceButton;
    private int btnWidth, btnHeight;
    private int prevX, prevY;
    private boolean[][] matrix = new boolean[15][10];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_customize, container, false);
        initUI();
        return view;
    }

    private void initUI() {
        ConstraintSet set = new ConstraintSet();
        set.clone((ConstraintLayout) getActivity().findViewById(R.id.menu_bottom));
        set.constrainPercentWidth(R.id.btn_add, 0.34f);
        set.constrainPercentWidth(R.id.btn_menu_home, 0.33f);
        set.constrainPercentWidth(R.id.btn_menu_user, 0.33f);
        set.applyTo((ConstraintLayout) getActivity().findViewById(R.id.menu_bottom));

        customizeRoomView = (CustomizeRoomView) view.findViewById(R.id.view_meeting);
        customizeRoomView.setListener(this);
        deviceNameTv = (TextView) view.findViewById(R.id.tv_device_name);
        updateBtn = (Button) view.findViewById(R.id.btn_update);
        updateBtn.setOnClickListener(this);
        addBtn = (ImageButton) getActivity().findViewById(R.id.btn_add);
        addBtn.setOnClickListener(this);
        delBtn = (Button) view.findViewById(R.id.btn_del_device);
        delBtn.setOnClickListener(this);

        loadingDialog = new ProgressDialog(getActivity());
        loadingDialog.setMessage("");
        loadingDialog.setIndeterminate(true);

        getDeviceList();
    }

    private void getDeviceList() {
        if(true /*Factory.deviceList == null || Factory.deviceList.size() == 0*/) {
            new NukeSSLCerts().nuke();
            RequestQueue queue = Volley.newRequestQueue(getActivity().getBaseContext());
            StringRequest stringRequest = new StringRequest(Request.Method.GET, Factory.HOST + "/?action=get_devices&room_id=" + Factory.room.getId(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
//                                loadingDialog.dismiss();
                                if (Factory.debug) {
                                    Log.d(Factory.debugTag, response);
                                }
                                JSONArray jsonArray = new JSONArray(response);
                                Factory.deviceList = new ArrayList<>();
                                for(int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject deviceJson = jsonArray.getJSONObject(i);
                                    Device device = new Device(Integer.parseInt(deviceJson.getString("id")),
                                            Integer.parseInt(deviceJson.getString("room_id")),
                                            Integer.parseInt(deviceJson.getString("type")));

                                    device.setName(deviceJson.getString("name"));
                                    device.setApiKey(deviceJson.getString("api_key"));
                                    device.setTemp(Integer.parseInt(deviceJson.getString("temp")));
                                    device.setLight(Integer.parseInt(deviceJson.getString("light")));
                                    device.setPower(Integer.parseInt(deviceJson.getString("power")));
                                    String[] pos = deviceJson.getString("position").split(";");
                                    if(pos.length == 2) {
                                        device.setX(Integer.parseInt(pos[0]));
                                        device.setY(Integer.parseInt(pos[1]));
                                    }
                                    Factory.deviceList.add(device);
                                }
                                getDevices();
                            } catch (JSONException e) {
                                if (Factory.debug) {
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
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    return params;
                }
            };
//            loadingDialog.show();
            queue.add(stringRequest);
        }
//        else {
//            getDevices();
//        }
    }

    private void updatePosition() {
        new NukeSSLCerts().nuke();
        RequestQueue queue = Volley.newRequestQueue(getActivity().getBaseContext());
        for(int i = 0; i < deviceButtons.size(); i++) {
            CustomizeDeviceButton btn = deviceButtons.get(i);
            int finalI = i;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Factory.HOST + "/?action=update_position",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                if (Factory.debug) {
                                    Log.d(Factory.debugTag, response);
                                }
                                JSONObject jsonData = new JSONObject(response);
                                if(jsonData.getString("response").equals("OK")) {
                                    if(finalI == deviceButtons.size() - 1) {
                                        Toast.makeText(getContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (JSONException e) {
                                if (Factory.debug) {
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
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("apikey", btn.getDevice().getApiKey());
                    params.put("x", btn.getCurX() + "");
                    params.put("y", btn.getCurY() + "");
                    return params;
                }
            };
            queue.add(stringRequest);
        }
    }

    private void deleteDevice() {
        new NukeSSLCerts().nuke();
        RequestQueue queue = Volley.newRequestQueue(getActivity().getBaseContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Factory.HOST + "/?action=del_device",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (Factory.debug) {
                                Log.d(Factory.debugTag, response);
                            }
                            JSONObject jsonData = new JSONObject(response);
                            if(jsonData.getString("response").equals("OK")) {
                                deviceButtons.remove(activeDeviceButton);
                                customizeRoomView.removeView(activeDeviceButton);
                                activeDeviceButton = null;
                            }
                        } catch (JSONException e) {
                            if (Factory.debug) {
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
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("apikey", activeDeviceButton.getDevice().getApiKey());
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private void getDevices() {
        btnWidth = customizeRoomView.getWidth() / 10;
        btnHeight = customizeRoomView.getHeight() / 15;
        deviceButtons = new ArrayList<>();
        for(Device dev: Factory.deviceList) {
            CustomizeDeviceButton button = new CustomizeDeviceButton(getContext());
            button.setSize(btnWidth, btnHeight);
            button.setPosition(dev.getX() * btnWidth, dev.getY() * btnHeight);
            if(dev.getType() == 1)
                button.setImage(R.drawable.lamp2);
            else
                button.setImage(R.drawable.device_icon);
            button.setListener(this);
            button.setDevice(dev);
            deviceButtons.add(button);
            customizeRoomView.getRoomLayout().addView(button);
            matrix[dev.getY()][dev.getX()] = true;
        }
    }

    private void loadFragment(Fragment fragment) {
        MainActivity.FRAG_ID = ((MyFragment) fragment).getTAG();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layout_main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public String getTAG() {
        return "Customize";
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_add) {
            if(MainActivity.FRAG_ID.equals("Customize")) {
                loadFragment(new AddDeviceFragment());
            }
        }
        else if(view.getId() == R.id.btn_update) {
            updatePosition();
        }
        else if(view.getId() == R.id.btn_del_device) {
            if(activeDeviceButton != null) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Thông báo")
                        .setMessage("Bạn có chắc chắn muốn xóa thiết bị này?")
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteDevice();
                            }
                        })
                        .setNegativeButton("Bỏ qua", null)
                        .show();
            }
            else {
                Toast.makeText(getContext(), "Chưa chọn thiết bị cần xóa", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onScroll(float x, float y) {
        if(activeDeviceButton != null) {
            int[] curPos = activeDeviceButton.getPosition();
            int newX = Math.round(curPos[0] - x);
            int newY = Math.round(curPos[1] - y);
            if(newX >= 0 && newX <= 9 * btnWidth && newY >= 0 && newY <= 14 * btnHeight) {
                activeDeviceButton.setPosition(newX, newY);
                customizeRoomView.showHint(true);
                customizeRoomView.setHintPosition(Math.round((float) newX / btnWidth) * btnWidth, Math.round((float) newY / btnHeight) * btnHeight);
            }
        }
    }

    @Override
    public void onSingleTapUp() {
        if(activeDeviceButton != null) {
            activeDeviceButton.setActive(false);
            activeDeviceButton = null;
            deviceNameTv.setText("");
        }
    }

    @Override
    public void onTouch(MotionEvent e) {
        if(e.getAction() == MotionEvent.ACTION_UP && activeDeviceButton != null) {
            if(matrix[activeDeviceButton.getCurY()][activeDeviceButton.getCurX()] == false) {
                activeDeviceButton.setPosition(activeDeviceButton.getCurX() * btnWidth, activeDeviceButton.getCurY() * btnHeight);
                matrix[prevY][prevX] = false;
                prevX = activeDeviceButton.getCurX();
                prevY = activeDeviceButton.getCurY();
                matrix[prevY][prevX] = true;
            }
            else {
                activeDeviceButton.setPosition(prevX * btnWidth, prevY * btnHeight);
            }
            customizeRoomView.showHint(false);
        }
    }

    @Override
    public void onTap(CustomizeDeviceButton button) {
        Factory.device = button.getDevice();
        loadFragment(new ControlFragment2());
    }

    @Override
    public void onHold(CustomizeDeviceButton button) {
        activeDeviceButton = button;
        prevX = activeDeviceButton.getCurX();
        prevY = activeDeviceButton.getCurY();
        for(CustomizeDeviceButton btn: deviceButtons) {
            btn.setActive(false);
        }
        activeDeviceButton.setActive(true);
        deviceNameTv.setText(activeDeviceButton.getDevice().getName());
    }
}

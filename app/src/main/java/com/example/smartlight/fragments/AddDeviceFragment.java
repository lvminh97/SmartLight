package com.example.smartlight.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
import com.example.smartlight.interfaces.MyFragment;
import com.example.smartlight.models.Device;
import com.example.smartlight.models.Type;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddDeviceFragment extends Fragment implements MyFragment, View.OnClickListener {

    private View view;
    private ProgressDialog loadingDialog = null;
    private Spinner typeSpinner;
    private Button submitBtn, cancelBtn, backBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_device, container, false);
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

        loadingDialog = new ProgressDialog(getActivity());
        loadingDialog.setMessage("");
        loadingDialog.setIndeterminate(true);

        typeSpinner = (Spinner) view.findViewById(R.id.spn_type);
        getTypes();

        submitBtn = (Button) view.findViewById(R.id.btn_submit);
        submitBtn.setOnClickListener(this);
        cancelBtn = (Button) view.findViewById(R.id.btn_cancel);
        cancelBtn.setOnClickListener(this);
        backBtn = (Button) view.findViewById(R.id.btn_back);
        backBtn.setOnClickListener(this);
    }

    private void getTypes() {
        List<String> types = new ArrayList<>();
        if(Factory.types == null) {
            new NukeSSLCerts().nuke();
            RequestQueue queue = Volley.newRequestQueue(getActivity().getBaseContext());
            StringRequest stringRequest = new StringRequest(Request.Method.GET, Factory.HOST + "/?action=get_types",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                loadingDialog.dismiss();
                                if (Factory.debug) {
                                    Log.d("MinhLV", response);
                                }
                                JSONArray jsonArray = new JSONArray(response);
                                Factory.types = new ArrayList<>();
                                for(int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject typeJson = jsonArray.getJSONObject(i);
                                    Factory.types.add(new Type(typeJson.getInt("id"), typeJson.getString("name")));
                                    types.add(typeJson.getString("name"));
                                }
                                String[] res = new String[types.size()];
                                types.toArray(res);
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getBaseContext(), android.R.layout.simple_spinner_dropdown_item, res);
                                typeSpinner.setAdapter(adapter);
                            } catch (JSONException e) {
                                if (Factory.debug) {
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
            for(Type type: Factory.types) {
                types.add(type.getName());
            }
            String[] res = new String[types.size()];
            types.toArray(res);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_spinner_dropdown_item, res);
            typeSpinner.setAdapter(adapter);
        }
    }

    private void addDevice() {
        new NukeSSLCerts().nuke();
        RequestQueue queue = Volley.newRequestQueue(getActivity().getBaseContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Factory.HOST + "/?action=add_device",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if(Factory.debug) {
                                Log.d("MinhLV", response);
                            }
                            loadingDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getString("response").equals("OK")) {
                                JSONObject devJson = jsonObject.getJSONObject("device");
                                Device newDevice = new Device(Integer.parseInt(devJson.getString("id")),
                                        Integer.parseInt(devJson.getString("room_id")),
                                        Integer.parseInt(devJson.getString("type")));
                                newDevice.setApiKey(devJson.getString("api_key"));
                                newDevice.setTemp(0);
                                newDevice.setLight(0);
                                newDevice.setPower(0);
                                Factory.deviceList.add(newDevice);
                                Toast.makeText(getActivity().getBaseContext(), "Thêm thiết bị thành công", Toast.LENGTH_SHORT).show();
                                loadFragment(new ControlFragment());
                            }
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
                params.put("room_id", "" + Factory.room.getId());
                params.put("type", "" + typeSpinner.getSelectedItemId());
                return params;
            }
        };
        loadingDialog.show();
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
    public void onClick(View view) {
        if(view.getId() == R.id.btn_submit) {
            addDevice();
        }
        else if(view.getId() == R.id.btn_cancel) {
            loadFragment(new ControlFragment());
        }
        else if(view.getId() == R.id.btn_back) {
            loadFragment(new ControlFragment());
        }
    }

    @Override
    public String getTAG() {
        return "AddDevice";
    }
}

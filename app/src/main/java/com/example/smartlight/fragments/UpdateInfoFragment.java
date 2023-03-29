package com.example.smartlight.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class UpdateInfoFragment extends Fragment implements MyFragment, View.OnClickListener {

    private ProgressDialog loadingDialog = null;
    private View view;
    private EditText fullnameEd, phoneEd;
    private Button updateInfoBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_update_info, container, false);
        initUI();
        return view;
    }

    private void initUI() {
        fullnameEd = (EditText) view.findViewById(R.id.ed_fullname);
        fullnameEd.setText(Factory.user.getFullname());
        phoneEd = (EditText) view.findViewById(R.id.ed_phone);
        phoneEd.setText(Factory.user.getMobile());
        updateInfoBtn = (Button) view.findViewById(R.id.btn_update);
        updateInfoBtn.setOnClickListener(this);

        loadingDialog = new ProgressDialog(getActivity());
        loadingDialog.setMessage("");
        loadingDialog.setIndeterminate(true);
    }

    @Override
    public String getTAG() {
        return "UpdateInfo";
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_update){
            updateInfo();
        }
    }

    private void updateInfo(){
        loadingDialog.show();
        new NukeSSLCerts().nuke();
        RequestQueue queue = Volley.newRequestQueue(getActivity().getBaseContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Factory.HOST + "/?action=update_info",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if(Factory.debug) {
                                Log.d("SmartLight_Debug", response);
                            }
                            loadingDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getString("response") != null && jsonObject.getString("response").equals("OK")){
                                Factory.user.setFullname(fullnameEd.getText().toString());
                                Factory.user.setMobile(phoneEd.getText().toString());
                                Toast.makeText(getActivity().getBaseContext(), "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                                loadFragment(new UserFragment());
                            }
                            else {
                                Toast.makeText(getActivity().getBaseContext(), "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            if(Factory.debug) {
                                Log.d("SmartLight_Debug", e.getMessage());
                            }
                            loadingDialog.dismiss();
                            Toast.makeText(getActivity().getBaseContext(), "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                    Log.d("SmartLight_Debug", error.getMessage());
                        loadingDialog.dismiss();
                        Toast.makeText(getActivity().getBaseContext(), "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("uid", "" + Factory.user.getId());
                params.put("email", Factory.user.getEmail());
                params.put("fullname", fullnameEd.getText().toString());
                params.put("mobile", phoneEd.getText().toString());
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

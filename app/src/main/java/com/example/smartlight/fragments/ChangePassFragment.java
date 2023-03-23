package com.example.smartlight.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePassFragment extends Fragment implements MyFragment, View.OnClickListener {

    private ProgressDialog loadingDialog = null;
    private View view;
    private EditText passEd1, passEd2, passEd3;
    private Button updateBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_change_pass, container, false);
        initUI();
        return view;
    }

    private void initUI() {
        passEd1 = (EditText) view.findViewById(R.id.ed_pass1);
        passEd2 = (EditText) view.findViewById(R.id.ed_pass2);
        passEd3 = (EditText) view.findViewById(R.id.ed_pass3);
        updateBtn = (Button) view.findViewById(R.id.btn_update);
        updateBtn.setOnClickListener(this);

        loadingDialog = new ProgressDialog(getActivity());
        loadingDialog.setMessage("");
        loadingDialog.setIndeterminate(true);
    }

    @Override
    public String getTAG() {
        return "ChangePass";
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_update){
            updatePass();
        }
    }

    private void updatePass(){
        loadingDialog.show();
        new NukeSSLCerts().nuke();
        RequestQueue queue = Volley.newRequestQueue(getActivity().getBaseContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Factory.HOST + "/?action=change_pass",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if(Factory.debug) {
                                Log.d("MinhLV", response);
                            }
                            loadingDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getString("response") != null) {
                                if(jsonObject.getString("response").equals("OK")){
                                    SharedPreferences prefs = getActivity().getSharedPreferences("SMARTLIGHT", MODE_PRIVATE);
                                    prefs.edit().putString("password", passEd2.getText().toString()).commit();
                                    Toast.makeText(getActivity().getBaseContext(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                    loadFragment(new UserFragment());
                                }
                                else if(jsonObject.getString("response").equals("WrongPass")){
                                    Toast.makeText(getActivity().getBaseContext(), "Mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show();
                                }
                                else if(jsonObject.getString("response").equals("ShortPass")){
                                    Toast.makeText(getActivity().getBaseContext(), "Mật khẩu mới quá ngắn, độ dài tối thiểu 8 ký tự", Toast.LENGTH_SHORT).show();
                                }
                                else if(jsonObject.getString("response").equals("Mismatch")){
                                    Toast.makeText(getActivity().getBaseContext(), "Mật khẩu nhập lại không khớp", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(getActivity().getBaseContext(), "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            if(Factory.debug) {
                                Log.d("MinhLV", e.getMessage());
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
//                    Log.d("MinhLV", error.getMessage());
                        loadingDialog.dismiss();
                        Toast.makeText(getActivity().getBaseContext(), "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("id", "" + Factory.user.getId());
                params.put("pass1", passEd1.getText().toString());
                params.put("pass2", passEd2.getText().toString());
                params.put("pass3", passEd3.getText().toString());
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

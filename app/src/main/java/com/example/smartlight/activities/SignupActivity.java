package com.example.smartlight.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartlight.Config;
import com.example.smartlight.R;
import com.example.smartlight.models.Room;
import com.example.smartlight.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText emailEd, fullnameEd, phoneEd, passwordEd;
    private Button signupBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initUI();
    }

    private void initUI() {
        emailEd = (EditText) findViewById(R.id.ed_username);
        fullnameEd = (EditText) findViewById(R.id.ed_fullname);
        phoneEd = (EditText) findViewById(R.id.ed_mobile);
        passwordEd = (EditText) findViewById(R.id.ed_password);
        signupBtn = (Button) findViewById(R.id.btn_signup);
        signupBtn.setOnClickListener(this);
    }

    private void signup() {
        if(emailEd.getText().toString().trim().equals("")){
            Toast.makeText(getBaseContext(), "Email không được để trống", Toast.LENGTH_SHORT).show();
            return;
        }
        String lowerEmail = emailEd.getText().toString().trim().toLowerCase(Locale.ROOT);
        if(Pattern.matches("[a-z0-9]+@[a-z]+[.][a-z]+", lowerEmail) == false){
            Toast.makeText(getBaseContext(), "Định dạng email không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }
        if(fullnameEd.getText().toString().trim().equals("")) {
            Toast.makeText(getBaseContext(), "Họ tên không được để trống", Toast.LENGTH_SHORT).show();
            return;
        }
        if(passwordEd.getText().toString().length() < 8) {
            Toast.makeText(getBaseContext(), "Mật khẩu quá ngắn, độ dài tối thiểu 8 ký tự", Toast.LENGTH_SHORT).show();
            return;
        }
        //
        RequestQueue queue = Volley.newRequestQueue(getBaseContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.HOST + "/?action=signup",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getString("response").equals("OK")){
                                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                                intent.putExtra("email", lowerEmail);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            Log.d("MinhLV", e.getMessage());
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("MinhLV", error.getMessage());
                    }
                })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("email", lowerEmail);
                params.put("fullname", fullnameEd.getText().toString());
                params.put("mobile", phoneEd.getText().toString());
                params.put("password", passwordEd.getText().toString());
                return params;
            }
        };
        queue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_signup) {
            signup();
        }
    }
}

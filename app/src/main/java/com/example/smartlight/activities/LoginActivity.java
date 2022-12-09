package com.example.smartlight.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText usernameEd, passwordEd;
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //
        initUI();
    }

    private void initUI() {
        usernameEd = (EditText) findViewById(R.id.ed_username);
        passwordEd = (EditText) findViewById(R.id.ed_password);

        SharedPreferences prefs = getSharedPreferences("SMARTLIGHT", MODE_PRIVATE);
        String username = prefs.getString("username", null);
        String password = prefs.getString("password", null);
        if(username != null)
            usernameEd.setText(username);
        if(password != null)
            passwordEd.setText(password);

        loginBtn = (Button) findViewById(R.id.btn_login);
        loginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_login) {
            login();
        }
    }

    private void login() {
        RequestQueue queue = Volley.newRequestQueue(getBaseContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.HOST + "/?action=login",
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String resp = jsonObject.getString("response");
                        if(resp.equals("OK")){
                            SharedPreferences prefs = getSharedPreferences("SMARTLIGHT", MODE_PRIVATE);
                            prefs.edit().putString("username", usernameEd.getText().toString()).commit();
                            prefs.edit().putString("password", passwordEd.getText().toString()).commit();

                            JSONObject userData = jsonObject.getJSONObject("user");
                            Config.user = new User(Integer.parseInt(userData.getString("id")),
                                                    userData.getString("fullname"),
                                                    userData.getString("mobile"),
                                                    userData.getString("email"));

                            JSONArray roomJsonArray = jsonObject.getJSONArray("roomList");
                            Config.roomList = new ArrayList<Room>();
                            for(int i = 0; i < roomJsonArray.length(); i++) {
                                JSONObject roomJson = roomJsonArray.getJSONObject(i);
                                Config.roomList.add(new Room(Integer.parseInt(roomJson.getString("id")), null, roomJson.getString("name")));
                            }
                            Intent intent = new Intent(getBaseContext(), MainActivity.class);
                            startActivity(intent);
                        }
                        else {
                            SharedPreferences prefs = getSharedPreferences("SMARTLIGHT", MODE_PRIVATE);
                            prefs.edit().putString("username", usernameEd.getText().toString()).commit();
                            prefs.edit().putString("password", null).commit();
                            Toast.makeText(getBaseContext(), "Sai tên đăng nhập hoặc mật khẩu", Toast.LENGTH_SHORT).show();
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
                params.put("username", usernameEd.getText().toString());
                params.put("password", passwordEd.getText().toString());
                return params;
            }
        };
        queue.add(stringRequest);
    }
}

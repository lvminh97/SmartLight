package com.example.smartlight.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.smartlight.models.Room;
import com.example.smartlight.models.Type;
import com.example.smartlight.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressDialog loadingDialog = null;
    private EditText usernameEd, passwordEd;
    private Button loginBtn, signupBtn;
    private ImageView mainIconImg;
    private int mainIconClickCnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //
        initUI();
    }

    private void initUI() {
        SharedPreferences prefs = getSharedPreferences("SMARTLIGHT", MODE_PRIVATE);

        // get server domain
        Factory.HOST = prefs.getString("host", null);
        if(Factory.HOST == null) {
            Factory.HOST = "http://denthongminh.pro";
        }

        usernameEd = (EditText) findViewById(R.id.ed_username);
        passwordEd = (EditText) findViewById(R.id.ed_password);

        if(this.getIntent().getStringExtra("email") != null) {
            usernameEd.setText(this.getIntent().getStringExtra("email"));
        }
        else {
            String username = prefs.getString("username", null);
            String password = prefs.getString("password", null);
            if (username != null)
                usernameEd.setText(username);
            if (password != null)
                passwordEd.setText(password);
        }

        loginBtn = (Button) findViewById(R.id.btn_login);
        signupBtn = (Button) findViewById(R.id.btn_signup);
        loginBtn.setOnClickListener(this);
        signupBtn.setOnClickListener(this);
        mainIconImg = (ImageView) findViewById(R.id.img_main_icon);
        mainIconImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_login) {
            login();
        }
        else if(view.getId() == R.id.btn_signup) {
            Intent intent = new Intent(this, SignupActivity.class);
            startActivity(intent);
        }
        else if(view.getId() == R.id.img_main_icon) {
            mainIconClickCnt++;
            if(mainIconClickCnt == 10) {
                mainIconClickCnt = 0;
                Intent intent = new Intent(this, SetDomainActivity.class);
                startActivity(intent);
            }
        }
    }

    private void login() {
        new NukeSSLCerts().nuke();
        RequestQueue queue = Volley.newRequestQueue(getBaseContext());
        StringRequest loginRequest = new StringRequest(Request.Method.POST, Factory.HOST + "/?action=login",
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        loadingDialog.dismiss();
                        JSONObject jsonObject = new JSONObject(response);
                        String resp = jsonObject.getString("response");
                        if(resp.equals("OK")){
                            SharedPreferences prefs = getSharedPreferences("SMARTLIGHT", MODE_PRIVATE);
                            prefs.edit().putString("username", usernameEd.getText().toString()).commit();
                            prefs.edit().putString("password", passwordEd.getText().toString()).commit();

                            JSONObject userData = jsonObject.getJSONObject("user");
                            Factory.user = new User(Integer.parseInt(userData.getString("id")),
                                                    userData.getString("fullname"),
                                                    userData.getString("mobile"),
                                                    userData.getString("email"),
                                                    userData.getString("app_control").equals("1"));

                            JSONArray roomJsonArray = jsonObject.getJSONArray("roomList");
                            Factory.roomList = new ArrayList<Room>();
                            for(int i = 0; i < roomJsonArray.length(); i++) {
                                JSONObject roomJson = roomJsonArray.getJSONObject(i);
                                int[] imgIds = {R.drawable.meeting_room, R.drawable.class_room, R.drawable.conference_room, R.drawable.building};
                                Drawable roomImg = ContextCompat.getDrawable(getBaseContext(), imgIds[i]);
                                Factory.roomList.add(new Room(
                                        Integer.parseInt(roomJson.getString("id")),
                                        roomImg,
                                        roomJson.getString("name")));
                            }// add 'Customize' item
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
                        if(Factory.debug)
                            Log.d(Factory.debugTag, e.getMessage());

                        Toast.makeText(getBaseContext(), "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                        e.printStackTrace();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(Factory.debug)
                        Log.d(Factory.debugTag, "" + error.getMessage());
                    Toast.makeText(getBaseContext(), "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
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
        StringRequest getTypesRequest = new StringRequest(Request.Method.GET, Factory.HOST + "/?action=get_types",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (Factory.debug) {
                                Log.d(Factory.debugTag, response);
                            }
                            JSONArray jsonArray = new JSONArray(response);
                            Factory.types = new ArrayList<>();
                            for(int i = 0; i < jsonArray.length(); i++) {
                                JSONObject typeJson = jsonArray.getJSONObject(i);
                                Factory.types.add(new Type(typeJson.getInt("id"), typeJson.getString("name")));
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
//                        Log.d(Factory.debugTag, "" + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        loadingDialog = new ProgressDialog(this);
        loadingDialog.setMessage("");
        loadingDialog.setIndeterminate(true);
        loadingDialog.show();
        queue.add(getTypesRequest);
        queue.add(loginRequest);
    }
}

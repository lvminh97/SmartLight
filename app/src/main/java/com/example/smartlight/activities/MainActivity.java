package com.example.smartlight.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.smartlight.Factory;
import com.example.smartlight.R;
import com.example.smartlight.fragments.ControlFragment;
import com.example.smartlight.fragments.HomeFragment;
import com.example.smartlight.fragments.SettingFragment;
import com.example.smartlight.fragments.UserFragment;
import com.example.smartlight.interfaces.MyFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    private FrameLayout mainLayout;
    private ImageButton homeBtn, lightningBtn, settingBtn, userBtn;

    public static String FRAG_ID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        initUI();
    }

    private void initUI() {
        mainLayout = (FrameLayout) findViewById(R.id.layout_main);

        homeBtn = (ImageButton) findViewById(R.id.btn_menu_home);
        lightningBtn = (ImageButton) findViewById(R.id.btn_menu_lightning);
        settingBtn = (ImageButton) findViewById(R.id.btn_menu_setting);
        userBtn = (ImageButton) findViewById(R.id.btn_menu_user);

        homeBtn.setOnClickListener(this);
        homeBtn.setOnTouchListener(this);
        lightningBtn.setOnClickListener(this);
        lightningBtn.setOnTouchListener(this);
        settingBtn.setOnClickListener(this);
        settingBtn.setOnTouchListener(this);
        userBtn.setOnClickListener(this);
        userBtn.setOnTouchListener(this);

        loadFragment(new HomeFragment());
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_menu_home) {
            loadFragment(new HomeFragment());
        }
        else if(v.getId() == R.id.btn_menu_setting) {
            loadFragment(new SettingFragment());
        }
        else if(v.getId() == R.id.btn_menu_user) {
            loadFragment(new UserFragment());
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.btn_menu_home:
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                    homeBtn.setBackgroundColor(Color.argb(255, 240, 240, 240));
                else if(event.getAction() == MotionEvent.ACTION_UP)
                    homeBtn.setBackgroundColor(Color.argb(255, 255, 255, 255));
                break;
            case R.id.btn_menu_lightning:
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                    lightningBtn.setBackgroundColor(Color.argb(255, 240, 240, 240));
                else if(event.getAction() == MotionEvent.ACTION_UP)
                    lightningBtn.setBackgroundColor(Color.argb(255, 255, 255, 255));
                break;
            case R.id.btn_menu_setting:
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                    settingBtn.setBackgroundColor(Color.argb(255, 240, 240, 240));
                else if(event.getAction() == MotionEvent.ACTION_UP)
                    settingBtn.setBackgroundColor(Color.argb(255, 255, 255, 255));
                break;
            case R.id.btn_menu_user:
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                    userBtn.setBackgroundColor(Color.argb(255, 240, 240, 240));
                else if(event.getAction() == MotionEvent.ACTION_UP)
                    userBtn.setBackgroundColor(Color.argb(255, 255, 255, 255));
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if(FRAG_ID.equals("Home")){
            new AlertDialog.Builder(this)
                    .setTitle("Thông báo")
                    .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                    .setPositiveButton("Đăng xuất", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences prefs = getSharedPreferences("SMARTLIGHT", MODE_PRIVATE);
                            prefs.edit().putStringSet("password", null).commit();
                            Factory.user = null;
                            loadActivity(LoginActivity.class);
                        }
                    })
                    .setNegativeButton("Bỏ qua", null)
                    .show();
        }
        else if(FRAG_ID.equals("Control") || FRAG_ID.equals("Setting")){
            loadFragment(new HomeFragment());
        }
        else if(FRAG_ID.equals("Light") || FRAG_ID.equals("Power")) {
            loadFragment(new ControlFragment());
        }
        else if(FRAG_ID.equals("User")) {
            loadFragment(new SettingFragment());
        }
    }

    private void loadFragment(Fragment fragment) {
        FRAG_ID = ((MyFragment) fragment).getTAG();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layout_main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void loadActivity(Class activityClass){
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }
}
package com.example.smartlight.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.example.smartlight.Factory;
import com.example.smartlight.R;
import com.example.smartlight.fragments.ControlFragment;
import com.example.smartlight.fragments.CustomizeFragment;
import com.example.smartlight.fragments.HomeFragment;
import com.example.smartlight.fragments.SettingFragment;
import com.example.smartlight.fragments.UserFragment;
import com.example.smartlight.interfaces.MyFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    private FrameLayout mainLayout;
    private ImageButton homeBtn, addBtn, userBtn;

    public static String FRAG_ID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        initUI();
    }

    private void initUI() {
        Factory.displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(Factory.displayMetrics);

        mainLayout = (FrameLayout) findViewById(R.id.layout_main);

        homeBtn = (ImageButton) findViewById(R.id.btn_menu_home);
        addBtn = (ImageButton) findViewById(R.id.btn_add);
        userBtn = (ImageButton) findViewById(R.id.btn_menu_user);

        homeBtn.setOnClickListener(this);
        homeBtn.setOnTouchListener(this);
        addBtn.setOnTouchListener(this);
        userBtn.setOnClickListener(this);
        userBtn.setOnTouchListener(this);

        loadFragment(new HomeFragment());
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_menu_home) {
            loadFragment(new HomeFragment());
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
                    homeBtn.setBackgroundColor(Color.argb(255, 190, 222, 244));
                break;
            case R.id.btn_add:
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                    addBtn.setBackgroundColor(Color.argb(255, 240, 240, 240));
                else if(event.getAction() == MotionEvent.ACTION_UP)
                    addBtn.setBackgroundColor(Color.argb(255, 190, 222, 244));
                break;
            case R.id.btn_menu_user:
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                    userBtn.setBackgroundColor(Color.argb(255, 240, 240, 240));
                else if(event.getAction() == MotionEvent.ACTION_UP)
                    userBtn.setBackgroundColor(Color.argb(255, 190, 222, 244));
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
                            FRAG_ID = "";
                            loadActivity(LoginActivity.class);
                        }
                    })
                    .setNegativeButton("Bỏ qua", null)
                    .show();
        }
        else if(FRAG_ID.equals("Control") || FRAG_ID.equals("Customize") || FRAG_ID.equals("Setting")){
            loadFragment(new HomeFragment());
        }
        else if(FRAG_ID.equals("Light") || FRAG_ID.equals("Power")) {
            loadFragment(new ControlFragment());
        }
        else if(FRAG_ID.equals("AddDevice") || FRAG_ID.equals("Control2")) {
            loadFragment(new CustomizeFragment());
        }
        else if(FRAG_ID.equals("User")) {
            loadFragment(new SettingFragment());
        }
        else if(FRAG_ID.equals("UpdateInfo") || FRAG_ID.equals("ChangePass")) {
            loadFragment(new UserFragment());
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
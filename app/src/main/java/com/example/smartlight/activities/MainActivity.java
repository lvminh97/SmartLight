package com.example.smartlight.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.smartlight.R;
import com.example.smartlight.fragments.HomeFragment;
import com.example.smartlight.interfaces.MyFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    private FrameLayout mainLayout;
    private BottomNavigationView bottomNavigationView;

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
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.menu_bottom);
        bottomNavigationView.setOnItemSelectedListener(this);
        //
        loadFragment(new HomeFragment());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                loadFragment(new HomeFragment());
                break;
            case R.id.navigation_bolt:
//                loadFragment(new BoltFragment());
                break;
            case R.id.navigation_setting:
                loadActivity(SettingActivity.class);
                break;
            case R.id.navigation_user:
//                loadFragment(new UserFragment());
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if(FRAG_ID == "Home"){
            loadActivity(LoginActivity.class);
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
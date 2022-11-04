package com.example.smartlight.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.smartlight.R;
import com.example.smartlight.fragments.HomeFragment;
import com.example.smartlight.interfaces.MyFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    private FrameLayout mainLayout;
    private BottomNavigationView bottomNavigationView;

    private String FRAG_ID = "";

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
                Log.d("MinhLV", "Home tab");
                loadFragment(new HomeFragment());
                break;
            case R.id.navigation_setting:
                Log.d("MinhLV", "Setting tab");
                break;
            case R.id.navigation_bolt:
                Log.d("MinhLV", "Bolt tab");
                break;
            case R.id.navigation_user:
                Log.d("MinhLV", "User tab");
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    private void loadFragment(Fragment fragment) {
        FRAG_ID = ((MyFragment) fragment).getTAG();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layout_main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
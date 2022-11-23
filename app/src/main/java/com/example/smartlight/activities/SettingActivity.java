package com.example.smartlight.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartlight.R;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{

    private Button accountBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //
        initUI();
    }

    private void initUI() {
        accountBtn = (Button) findViewById(R.id.btn_account);
        accountBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_account) {
            Intent intent = new Intent(this, UserActivity.class);
            startActivity(intent);
        }
    }
}

package com.example.smartlight.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.smartlight.Factory;
import com.example.smartlight.R;

import androidx.appcompat.app.AppCompatActivity;

public class SetDomainActivity extends AppCompatActivity implements View.OnClickListener{

    private final String[] HOST_LIST = {"http://smartlight.c1.biz", "http://denthongminh.pro"};

    private RadioGroup radioGroup;
    private RadioButton domain1Radio, domain2Radio;
    private Button saveBtn;

    private String savedDomain = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_domain);
        //
        initUI();
    }

    private void initUI() {
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);

        domain1Radio = (RadioButton) findViewById(R.id.radio_domain1);
        domain1Radio.setChecked(Factory.HOST.equals(HOST_LIST[0]));

        domain2Radio = (RadioButton) findViewById(R.id.radio_domain2);
        domain2Radio.setChecked(Factory.HOST.equals(HOST_LIST[1]));

        saveBtn = (Button) findViewById(R.id.btn_save_domain);
        saveBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        SharedPreferences prefs = getSharedPreferences("SMARTLIGHT", MODE_PRIVATE);
        if(view.getId() == R.id.btn_save_domain) {
            int checkedId = radioGroup.getCheckedRadioButtonId();
            if(checkedId == R.id.radio_domain1)
                Factory.HOST = HOST_LIST[0];
            else if(checkedId == R.id.radio_domain2)
                Factory.HOST = HOST_LIST[1];

            prefs.edit().putString("host", Factory.HOST).commit();

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }
}

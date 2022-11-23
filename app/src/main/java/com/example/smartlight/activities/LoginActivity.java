package com.example.smartlight.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartlight.R;

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
        if(true /*usernameEd.getText().toString().equals("a") && passwordEd.getText().toString().equals("1")*/){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(getBaseContext(), "Wrong username or password.\nTry again.", Toast.LENGTH_SHORT).show();
        }
    }
}

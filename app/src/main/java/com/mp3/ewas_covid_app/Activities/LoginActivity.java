package com.mp3.ewas_covid_app.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.mp3.ewas_covid_app.R;

public class LoginActivity extends AppCompatActivity {
    private Button signUpBTN;
    private Button signInBTN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signUpBTN = findViewById(R.id.btn_signup);
        signInBTN = findViewById(R.id.btn_signin);


        //OnClick Listeners
        signUpBTN.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        });

        signInBTN.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
        });
    }
}
package com.mp3.ewas_covid_app.Activities;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.mp3.ewas_covid_app.R;

public class EditProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        TextInputLayout tilEmail = findViewById(R.id.activity_edit_profile__til_email);
        TextInputLayout tilName = findViewById(R.id.activity_edit_profile__til_name);
        TextInputLayout tilNumber = findViewById(R.id.activity_edit_profile__til_number);
        TextInputLayout tilGender = findViewById(R.id.activity_edit_profile__til_gender);

        Button btnEdit = findViewById(R.id.activity_edit_profile__btn_edit);
        btnEdit.setOnClickListener(view -> {

            /* return to previous activity (which should be activity_edit_profile) */
            finishActivity(0);
        });
    }
}
package com.mp3.ewas_covid_app.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mp3.ewas_covid_app.R;

public class EditProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Intent receivedIntent = getIntent();
        String userUID = receivedIntent.getStringExtra("userUID");
        String firebasePath = receivedIntent.getStringExtra("firebasePath");

        DatabaseReference mRef = FirebaseDatabase
                .getInstance()
                .getReference(firebasePath + userUID);

        TextInputLayout tilEmail = findViewById(R.id.activity_edit_profile__til_email);
        TextInputLayout tilName = findViewById(R.id.activity_edit_profile__til_name);
        TextInputLayout tilNumber = findViewById(R.id.activity_edit_profile__til_number);
        TextInputLayout tilGender = findViewById(R.id.activity_edit_profile__til_gender);
        TextInputLayout tilAge = findViewById(R.id.activity_edit_profile__til_age);

        EditText etEmail = tilEmail.getEditText();
        EditText etName = tilName.getEditText();
        EditText etNumber = tilNumber.getEditText();
        EditText etGender = tilGender.getEditText();
        EditText etAge = tilAge.getEditText();

        //to get email text to display in edit profile
        mRef.child("email").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                etEmail.setText(String.valueOf(snapshot.getValue()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Button btnEdit = findViewById(R.id.activity_edit_profile__btn_edit);
        btnEdit.setOnClickListener(view -> {
            /*
            write objects manually to allow for singular changes,
            without a collective input requirement
            */
            if (etName.getText().length() != 0) {
                mRef.child("name").setValue(etName.getText().toString());
            }

            if (etNumber.getText().length() != 0) {
                if (etNumber.getText().length() < 11)
                    etNumber.setError("Number must be 11 characters in length");
                else
                    mRef.child("number").setValue(etNumber.getText().toString());
            }

            if (etGender.getText().length() != 0) {
                mRef.child("gender").setValue(etGender.getText().toString());

            }

            if (etAge.getText().length() != 0) {
                mRef.child("age").setValue(Integer.valueOf(etAge.getText().toString()));

            }

            /* return to previous activity (which should be activity_edit_profile) */
            finishActivity(0);
        });
    }
}
package com.mp3.ewas_covid_app.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mp3.ewas_covid_app.Models.Transaction;
import com.mp3.ewas_covid_app.Models.User;
import com.mp3.ewas_covid_app.R;

import java.util.ArrayList;

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
        final User[] fetchedUser = new User[1];
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                etEmail.setText(user.getEmail());
                etName.setText(user.getName());
                etNumber.setText(user.getNumber());
                etGender.setText(user.getGender());
                etAge.setText(user.getAge().toString());
                fetchedUser[0] = user;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ArrayList<Transaction> fetchedData = new ArrayList<>();
        //getting data from the nested transactions list
        mRef.child("orgTransactions").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()
                ) {
                    fetchedData.add(ds.getValue(Transaction.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Button btnEdit = findViewById(R.id.activity_edit_profile__btn_edit);
        btnEdit.setOnClickListener(view -> {
            if ((etEmail.length() <= 0) ||
                    (etName.length() <= 0) ||
                    (etNumber.length() <= 0) ||
                    (etGender.length() <= 0) ||
                    (etAge.length() <= 0)) {
                Toast.makeText(this, "All fields must not be empty", Toast.LENGTH_SHORT).show();
            } else if (etNumber.length() < 11)
                Toast.makeText(this, "Number field must have 11 digits", Toast.LENGTH_SHORT).show();
            else {
                User toSubmit = fetchedUser[0];
                toSubmit.setAge(Integer.valueOf(etAge.getText().toString()));
                toSubmit.setNumber(etNumber.getText().toString());
                toSubmit.setEmail(etEmail.getText().toString());
                toSubmit.setGender(etGender.getText().toString());
                toSubmit.setName(etName.getText().toString());
                toSubmit.setOrgTransactions(fetchedData);

                mRef.setValue(toSubmit).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });
            }

        });
    }
}
package com.mp3.ewas_covid_app.Activities;

import static com.mp3.ewas_covid_app.helper.Helper.generateBitmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mp3.ewas_covid_app.Models.User;
import com.mp3.ewas_covid_app.R;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    private TextView usernameTV;
    private ImageView userQR;
    private Button profileBTN;
    private User curUser;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference("ewas-users/" + mUser.getUid());

        //Intent to use later for profile page
        Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);

        //initialize
        usernameTV = findViewById(R.id.activity_main__username_tv);
        userQR = findViewById(R.id.activity_main__qr_code);
        profileBTN = findViewById(R.id.activity_main__profile_btn);

        //set image
        Bitmap qrBM = generateBitmap(mUser.getUid(), getBaseContext());
        userQR.setImageBitmap(qrBM);

        //get from FB and set values
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User um = snapshot.getValue(User.class);
                curUser = um;
                usernameTV.setText(um.getName().toUpperCase(Locale.ROOT));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //Listeners
        profileBTN.setOnClickListener(v -> {
            //Save data locally
            profileIntent.putExtra("username", curUser.getName());
            profileIntent.putExtra("email", curUser.getEmail());
            profileIntent.putExtra("number", curUser.getNumber());
            profileIntent.putExtra("gender", curUser.getGender());
            profileIntent.putExtra("age", curUser.getAge().toString());

            startActivity(profileIntent);
        });

    }
}
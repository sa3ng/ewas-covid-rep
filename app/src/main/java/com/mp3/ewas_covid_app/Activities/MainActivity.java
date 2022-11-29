package com.mp3.ewas_covid_app.Activities;

import static com.mp3.ewas_covid_app.helper.Helper.generateBitmap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mp3.ewas_covid_app.Models.User;
import com.mp3.ewas_covid_app.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView usernameTV;
    private ImageView userQR;
    private Button profileBTN;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference("ewas-users/users/" + mUser.getUid());


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
                usernameTV.setText(um.getName().toUpperCase(Locale.ROOT));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //Listeners
        profileBTN.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(i);
        });

        //firebase rtdb reference for "last covid form answer"
        DatabaseReference mRefUserFormAnswerDate = mRef.child("formLastAnswered");
        mRefUserFormAnswerDate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Calendar calendar = Calendar.getInstance();

                try {
                    // if date is equal to an empty string or date does not match
                    if ((snapshot.getValue()).toString().equals("")
                            || !(snapshot.getValue().toString().equals(sdf.format(calendar.getTime())))
                    ) {
                        Intent i = new Intent(MainActivity.this, CovidFormActivity.class);
                        i.putExtra("userPath", "ewas-users/users/" + mUser.getUid());
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    }
                } catch (NullPointerException e) {
                    mRefUserFormAnswerDate.setValue("");

                    Intent i = new Intent(MainActivity.this, CovidFormActivity.class);
                    i.putExtra("userPath", "ewas-users/users/" + mUser.getUid());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
package com.mp3.ewas_covid_app.Activities;

import static com.mp3.ewas_covid_app.helper.Helper.generateBitmap;
import static com.mp3.ewas_covid_app.helper.Helper.setSampleUserInfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mp3.ewas_covid_app.Models.OrgTransaction;
import com.mp3.ewas_covid_app.Models.Transaction;
import com.mp3.ewas_covid_app.Models.User;
import com.mp3.ewas_covid_app.R;
import com.mp3.ewas_covid_app.adapters.OrgListTransacAdapter;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private TextView nameTV;
    private TextView emailTV;
    private TextView ageTV;
    private TextView numberTV;
    private TextView genderTV;
    private ImageView qrIV;
    private User curUser;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mRef;

    private ArrayList<Transaction> orgArrayList;
    private RecyclerView userTransac_rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //initialize
        nameTV = findViewById(R.id.tv_user_name);
        emailTV = findViewById(R.id.tv_email);
        numberTV = findViewById(R.id.tv_contact_num);
        ageTV = findViewById(R.id.tv_ageStr);
        genderTV = findViewById(R.id.tv_gender);
        qrIV = findViewById(R.id.profile_qrcode_location);
        orgArrayList = new ArrayList<>();
        userTransac_rv = findViewById(R.id.rv_user_transac);

        //FB
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference("ewas-users/users/" + mUser.getUid());

        //Fetch deets from FB
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                curUser = snapshot.getValue(User.class);

                //Set Profile deets
                nameTV.setText(curUser.getName());
                emailTV.setText(curUser.getEmail());
                numberTV.setText(curUser.getNumber());
                genderTV.setText(curUser.getGender());
                ageTV.setText(curUser.getAge().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });



        //Set image
        qrIV.setImageBitmap(generateBitmap(mUser.getUid(), getBaseContext()));


        //Setting Adapter
        OrgListTransacAdapter orgAdapter = new OrgListTransacAdapter(orgArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        userTransac_rv.setLayoutManager(layoutManager);
        userTransac_rv.setItemAnimator(new DefaultItemAnimator());
        userTransac_rv.setAdapter(orgAdapter);


        //Sample Array data
        setSampleUserInfo(orgArrayList);
    }

}
package com.mp3.ewas_covid_app.Activities;

import static com.mp3.ewas_covid_app.helper.Helper.generateBitmap;
import static com.mp3.ewas_covid_app.helper.Helper.setSampleUserInfo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mp3.ewas_covid_app.Models.Transaction;
import com.mp3.ewas_covid_app.Models.User;
import com.mp3.ewas_covid_app.R;
import com.mp3.ewas_covid_app.adapters.OrgListTransacAdapter;
import com.mp3.ewas_covid_app.adapters.UserListTransacAdapter;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private TextView nameTV;
    private TextView emailTV;
    private TextView ageTV;
    private TextView numberTV;
    private TextView genderTV;
    private TextView rvLabelTV;
    private ImageView qrIV;
    private User curUser;

    private Button editProfileBTN;
    private Button logoutBTN;
    private String selectedUserUID;
    private String rootPath;
    private boolean isViewing;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mRef;
    private DatabaseReference transacRef;


    private ArrayList<Transaction> orgArrayList;
    private RecyclerView userTransac_rv;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //initialize
        isViewing = false;
        rootPath = "ewas-user/users/";
        nameTV = findViewById(R.id.tv_user_name);
        emailTV = findViewById(R.id.tv_email);
        numberTV = findViewById(R.id.tv_contact_num);
        ageTV = findViewById(R.id.tv_ageStr);
        genderTV = findViewById(R.id.tv_gender);
        qrIV = findViewById(R.id.profile_qrcode_location);
        orgArrayList = new ArrayList<>();
        userTransac_rv = findViewById(R.id.rv_user_transac);
        rvLabelTV = findViewById(R.id.tv_transac_histo);
        editProfileBTN = findViewById(R.id.activity_profile__edit_profile_btn);
        logoutBTN = findViewById(R.id.activity_profile__logout_btn);


        //FB
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        //fetch extras from last intent
        Bundle selectedUserBundle = getIntent().getExtras();
        if(selectedUserBundle != null){
            selectedUserUID = selectedUserBundle.getString("userSelectedUID");
            isViewing = selectedUserBundle.getBoolean("isViewing");
        }

        //Set accordingly if VIEWING or not
        if(isViewing){
            mRef = FirebaseDatabase.getInstance().getReference("ewas-users/users/" + selectedUserUID);

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

            //disable button visibilities
            editProfileBTN.setVisibility(View.GONE);
            logoutBTN.setVisibility(View.GONE);
            userTransac_rv.setVisibility(View.GONE);
            rvLabelTV.setVisibility(View.GONE);

        }
        else{
            mRef = FirebaseDatabase.getInstance().getReference("ewas-users/users/" + mUser.getUid());
            transacRef = FirebaseDatabase.getInstance().getReference("ewas-users/users/" + mUser.getUid() + "/orgTransactions");


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

            //GET TRANSACTION items from FB

            transacRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    orgArrayList.clear();
                    for(DataSnapshot users : snapshot.getChildren()){
                        orgArrayList.add(users.getValue(Transaction.class));
                    }

                    //Setting Adapter
                    OrgListTransacAdapter orgAdapter = new OrgListTransacAdapter(orgArrayList, getBaseContext());
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    userTransac_rv.setLayoutManager(layoutManager);
                    userTransac_rv.setItemAnimator(new DefaultItemAnimator());
                    userTransac_rv.setAdapter(orgAdapter);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }



        //EditProfile btn Functionality
        editProfileBTN.setOnClickListener(view -> {
            Intent i = new Intent(
                    ProfileActivity.this,
                    EditProfileActivity.class
            );
            i.putExtra("userUID", mUser.getUid());
            i.putExtra("firebasePath", "ewas-users/users/");
            ProfileActivity.this.startActivity(i);
        });
        //Set image
        qrIV.setImageBitmap(generateBitmap(mUser.getUid(), getBaseContext()));

        logoutBTN.setOnClickListener(v -> {
            mAuth.signOut();
            Intent signOutIntent = new Intent(ProfileActivity.this, LoginActivity.class);
            signOutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(signOutIntent);
            finish();
        });

    }

}
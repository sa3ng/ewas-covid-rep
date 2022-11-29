package com.mp3.ewas_covid_app.Activities;

import static com.mp3.ewas_covid_app.helper.Helper.setSampleUserInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.mp3.ewas_covid_app.Models.Organization;
import com.mp3.ewas_covid_app.Models.Transaction;
import com.mp3.ewas_covid_app.Models.User;
import com.mp3.ewas_covid_app.R;
import com.mp3.ewas_covid_app.adapters.OrgListTransacAdapter;
import com.mp3.ewas_covid_app.adapters.UserListTransacAdapter;
import com.mp3.ewas_covid_app.helper.DayComparator;

import java.util.ArrayList;
import java.util.Locale;

public class OrgMain extends AppCompatActivity {

    private TextView compNameTV;
    private TextView compEmailTV;
    private TextView compAddressTV;
    private RecyclerView userListRV;
    private Button scanBTN;
    private Button logoutBTN;

    private Organization curOrg;
    private ArrayList<Transaction> userArrayList;
    private boolean isViewing;
    private String selectedOrgUID;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mRef;
    private DatabaseReference transacRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_main);

        //Instatiate
        userArrayList = new ArrayList<>();
        userListRV = findViewById(R.id.org_main__rv);
        compNameTV = findViewById(R.id.org_main__card_view__ll__company_name_tv);
        compEmailTV = findViewById(R.id.org_main__card_view__ll__email_tv);
        compAddressTV = findViewById(R.id.org_main__card_view__ll__address_txt_tv_tv);
        scanBTN = findViewById(R.id.org_main__card_view__ll__scan_user_btn);
        logoutBTN = findViewById(R.id.org_main_profile__logout_btn);


        //Fetch Extras from Intent
        Bundle selectedOrgBundle = getIntent().getExtras();
        if(selectedOrgBundle != null){
            selectedOrgUID = selectedOrgBundle.getString("orgSelectedUID");
            isViewing = selectedOrgBundle.getBoolean("isViewing");
        }



        //FB
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        //isViewing checker

        if(isViewing){
            mRef = FirebaseDatabase.getInstance().getReference("ewas-users/organizations/" + selectedOrgUID);

            //get ORG PROFILE from FB
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Organization org = snapshot.getValue(Organization.class);
                    curOrg = org;

                    //Set values to textviews
                    compNameTV.setText(curOrg.getName());
                    compEmailTV.setText(curOrg.getEmail());
                    compAddressTV.setText(curOrg.getAddress().toUpperCase(Locale.ROOT));
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

            //disable rv and buttons
            userListRV.setVisibility(View.GONE);
            logoutBTN.setVisibility(View.GONE);
            scanBTN.setVisibility(View.GONE);

        }
        else{
            mRef = FirebaseDatabase.getInstance().getReference("ewas-users/organizations/" + mUser.getUid());
            transacRef = FirebaseDatabase.getInstance().getReference("ewas-users/organizations/" + mUser.getUid() + "/userTransactions");


            //get ORG PROFILE from FB
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Organization org = snapshot.getValue(Organization.class);
                    curOrg = org;

                    //Set values to textviews
                    compNameTV.setText(curOrg.getName());
                    compEmailTV.setText(curOrg.getEmail());
                    compAddressTV.setText(curOrg.getAddress().toUpperCase(Locale.ROOT));
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

            //GET TRANSACTION items from FB
            transacRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    userArrayList.clear();
                    for(DataSnapshot users : snapshot.getChildren()){
                        userArrayList.add(users.getValue(Transaction.class));
                    }

                    //Sort by day
                    userArrayList.sort(new DayComparator());

                    //Setting Adapter
                    UserListTransacAdapter userAdapter = new UserListTransacAdapter(userArrayList, getBaseContext());
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    userListRV.setLayoutManager(layoutManager);
                    userListRV.setItemAnimator(new DefaultItemAnimator());
                    userListRV.setAdapter(userAdapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }



        //listeners
        IntentIntegrator qrScan = new IntentIntegrator(this);
        scanBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrScan.setPrompt("Scan");
                qrScan.setCameraId(0); //0 - back cam, 1 - front cam
                qrScan.setBeepEnabled(true);
                qrScan.initiateScan();
            }
        });

        logoutBTN.setOnClickListener(v -> {
            mAuth.signOut();
            Intent signOutIntent = new Intent(OrgMain.this, LoginActivity.class);
            signOutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(signOutIntent);
            finish();
        });

    }



    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(intentResult != null){
            if(intentResult.getContents() == null){
                //TODO cancelled
            }else {
                Toast.makeText(OrgMain.this, intentResult.getContents(), Toast.LENGTH_LONG).show();

                //Set UID - Pass ScanResult
                String selectedUserUID = intentResult.getContents();

                Intent toScanResult = new Intent(OrgMain.this, OrgScanResultsActivity.class);
                toScanResult.putExtra("selectedUserUID", selectedUserUID);
                toScanResult.putExtra("orgName", curOrg.getName());
                startActivity(toScanResult);

            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
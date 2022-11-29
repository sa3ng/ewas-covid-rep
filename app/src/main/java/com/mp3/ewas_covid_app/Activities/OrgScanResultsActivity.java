package com.mp3.ewas_covid_app.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

public class OrgScanResultsActivity extends AppCompatActivity {

    private TextInputLayout nameScanTL;
    private TextInputLayout ageScanTL;
    private TextInputLayout genderScanTL;
    private TextInputLayout emailScanTL;
    private TextInputLayout numberScanTL;
    private TextInputLayout formpScanTL;
    private Button acceptBTN;
    private Button rejectBTN;

    private Bundle uidBundle;
    private String selectedUserUID;
    private User selectedUser;
    private String orgName;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mRef;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_scan_results);

        //Get UID
        uidBundle = getIntent().getExtras();
        if(uidBundle != null){
            selectedUserUID = uidBundle.getString("selectedUserUID");
            orgName = uidBundle.getString("orgName");

        }

        //FB DEETS
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference("ewas-users/users/" + selectedUserUID);


        //Initialize
        nameScanTL = findViewById(R.id.org_scan_results__cntnr_text_fields__name);
        ageScanTL = findViewById(R.id.org_scan_results__cntnr_text_fields__age);
        genderScanTL = findViewById(R.id.org_scan_results__cntnr_text_fields__gender);
        emailScanTL = findViewById(R.id.org_scan_results__cntnr_text_fields__email);
        numberScanTL = findViewById(R.id.org_scan_results__cntnr_text_fields__number);
        formpScanTL = findViewById(R.id.org_scan_results__cntnr_text_fields__score);
        acceptBTN = findViewById(R.id.org_scan_results__btn_Accept);
        rejectBTN = findViewById(R.id.org_scan_results__btn_Reject);
        LocalDateTime now = LocalDateTime.now();



        //Get USER DEETS from FB
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                selectedUser = snapshot.getValue(User.class);

                //Set Values
                nameScanTL.getEditText().setText(selectedUser.getName());
                ageScanTL.getEditText().setText(selectedUser.getAge().toString());
                genderScanTL.getEditText().setText(selectedUser.getGender());
                emailScanTL.getEditText().setText(selectedUser.getEmail());
                numberScanTL.getEditText().setText(selectedUser.getNumber());
                formpScanTL.getEditText().setText(selectedUser.getFormPoints().toString());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        acceptBTN.setOnClickListener(v -> {
            //Push deets as transaction
            createTransacInstance(selectedUserUID, mUser.getUid(), now);
        });

        rejectBTN.setOnClickListener(v -> {
            finish();
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createTransacInstance(String targetUID, String curOrgUID ,LocalDateTime now) {

        DateTimeFormatter ymd = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        DateTimeFormatter hm = DateTimeFormatter.ofPattern("HH:mm");
        Transaction um = new Transaction(nameScanTL.getEditText().getText().toString(), ymd.format(now), hm.format(now), targetUID);

        UUID uuid=UUID.randomUUID();

        //Update FB - ORG
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("ewas-users/organizations/" + curOrgUID + "/userTransactions");
        mRef.child(uuid.toString()).setValue(um);

        //Update FB transac - USER
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("ewas-users/users/" + um.getTransacUID() + "/orgTransactions");
        Transaction umUser = new Transaction(orgName, ymd.format(now), hm.format(now), curOrgUID);
        userRef.child(uuid.toString()).setValue(umUser);

        finish();
    }
}
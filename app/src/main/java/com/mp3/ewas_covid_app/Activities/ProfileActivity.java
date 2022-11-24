package com.mp3.ewas_covid_app.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.mp3.ewas_covid_app.Models.Transaction;
import com.mp3.ewas_covid_app.R;
import com.mp3.ewas_covid_app.adapters.OrgTransacAdapter;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private TextView nameTV;
    private TextView emailTV;
    private TextView ageTV;
    private TextView numberTV;
    private TextView genderTV;
    private Bundle profileExtras;

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
        orgArrayList = new ArrayList<>();
        userTransac_rv = findViewById(R.id.rv_user_transac);

        //get and set local saves of profile deets
        profileExtras = getIntent().getExtras();
        if(profileExtras != null){
            //set values
            nameTV.setText(profileExtras.getString("username"));
            emailTV.setText(profileExtras.getString("email"));
            numberTV.setText(profileExtras.getString("number"));
            genderTV.setText(profileExtras.getString("gender"));
            ageTV.setText(profileExtras.getString("age"));
        }


        //Setting Adapter
        OrgTransacAdapter orgAdapter = new OrgTransacAdapter(orgArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        userTransac_rv.setLayoutManager(layoutManager);
        userTransac_rv.setItemAnimator(new DefaultItemAnimator());
        userTransac_rv.setAdapter(orgAdapter);


        //Sample Array data
        setUserInfo();
    }

    private void setUserInfo(){
        orgArrayList.add(new Transaction("Edgar", "January 6, 2022", "2:00 am"));
        orgArrayList.add(new Transaction("Edgar", "January 6, 2022", "2:00 am"));
        orgArrayList.add(new Transaction("Edgar", "January 6, 2022", "2:00 am"));
        orgArrayList.add(new Transaction("Edgar", "January 6, 2022", "2:00 am"));
        orgArrayList.add(new Transaction("Edgar", "January 6, 2022", "2:00 am"));
        orgArrayList.add(new Transaction("Edgar", "January 6, 2022", "2:00 am"));
        orgArrayList.add(new Transaction("Edgar", "January 6, 2022", "2:00 am"));

    }
}
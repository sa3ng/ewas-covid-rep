package com.mp3.ewas_covid_app.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.mp3.ewas_covid_app.Models.Transaction;
import com.mp3.ewas_covid_app.R;
import com.mp3.ewas_covid_app.adapters.OrgTransacAdapter;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private ArrayList<Transaction> orgArrayList;
    private RecyclerView userTransac_rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        orgArrayList = new ArrayList<>();

        userTransac_rv = findViewById(R.id.rv_user_transac);

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
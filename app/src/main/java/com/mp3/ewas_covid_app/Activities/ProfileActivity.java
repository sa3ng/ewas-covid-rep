package com.mp3.ewas_covid_app.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.mp3.ewas_covid_app.Models.Transaction;
import com.mp3.ewas_covid_app.R;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private ArrayList<Transaction> orgArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        orgArrayList = new ArrayList<>();

        //Sample Array data
        setUserInfo();
    }

    private void setUserInfo(){

    }
}
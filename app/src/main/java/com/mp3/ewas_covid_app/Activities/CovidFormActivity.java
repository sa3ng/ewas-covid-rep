package com.mp3.ewas_covid_app.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.materialswitch.MaterialSwitch;
import com.mp3.ewas_covid_app.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CovidFormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_form);

        //first question container
        MaterialSwitch mwFever =
                findViewById(R.id.activity_covid_form__question_one_container__fever_switch);
        MaterialSwitch mwCough =
                findViewById(R.id.activity_covid_form__question_one_container__cough_switch);
        MaterialSwitch mwBreath =
                findViewById(R.id.activity_covid_form__question_one_container__short_breath_switch);
        MaterialSwitch mwFatigue =
                findViewById(R.id.activity_covid_form__question_one_container__fatigue_switch);
        MaterialSwitch mwBodyAche =
                findViewById(R.id.activity_covid_form__question_one_container__body_ache_switch);
        MaterialSwitch mwSoreThroat =
                findViewById(R.id.activity_covid_form__question_one_container__sore_throat_switch);
        MaterialSwitch mwRunnyNose =
                findViewById(R.id.activity_covid_form__question_one_container__runny_nose_switch);
        MaterialSwitch mwNausea =
                findViewById(R.id.activity_covid_form__question_one_container__nausea_switch);
        MaterialSwitch mwDiarrhea =
                findViewById(R.id.activity_covid_form__question_one_container__diarrhea_switch);
        MaterialSwitch mwCongestion =
                findViewById(R.id.activity_covid_form__question_one_container__congestion_switch);
        MaterialSwitch mwTasteLoss =
                findViewById(R.id.activity_covid_form__question_one_container__loss_of_taste_switch);

        //second question container
        MaterialSwitch mwHadTest =
                findViewById(R.id.activity_covid_form__question_two_container__had_test_switch);

        //third question container
        MaterialSwitch mwWithPositivePerson =
                findViewById(R.id.activity_covid_form__question_three_container__with_positive_person_switch);

        //fourth question container
        MaterialSwitch mwAdvisedWithCovid =
                findViewById(R.id.activity_covid_form__question_four_container__with_advised_quarantine_switch);

        //fifth question container
        MaterialSwitch mwSuspectedCovid =
                findViewById(R.id.activity_covid_form__question_five_container__suspected_covid_switch);

        Button btnSubmit =
                findViewById(R.id.activity_covid_form__submit_btn);
        btnSubmit.setOnClickListener(view ->
        {
            int finalScore = 0;
            //first question container
            if (mwFever.isChecked())
                finalScore += 22.5;
            if (mwCough.isChecked())
                finalScore += 22.5;
            if (mwBreath.isChecked())
                finalScore += 22.5;
            if (mwFatigue.isChecked())
                finalScore += 22.5;
            if (mwBodyAche.isChecked())
                finalScore += 22.5;
            if (mwSoreThroat.isChecked())
                finalScore += 22.5;
            if (mwRunnyNose.isChecked())
                finalScore += 22.5;
            if (mwNausea.isChecked())
                finalScore += 22.5;
            if (mwDiarrhea.isChecked())
                finalScore += 22.5;
            if (mwCongestion.isChecked())
                finalScore += 22.5;
            if (mwTasteLoss.isChecked())
                finalScore += 22.5;

            //second question container
            if (mwHadTest.isChecked())
                finalScore += 20;

            //third question container
            if (mwWithPositivePerson.isChecked())
                finalScore += 100;
            //fourth question container
            if (mwAdvisedWithCovid.isChecked())
                finalScore += 100;

            //fifth question container
            if (mwSuspectedCovid.isChecked())
                finalScore += 100;

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Calendar calendar = Calendar.getInstance();
            Toast.makeText(this, "Date is " + sdf.format(calendar.getTime()) + "\n" + finalScore, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(CovidFormActivity.this, MainActivity.class));
            finish();
        });

    }
}
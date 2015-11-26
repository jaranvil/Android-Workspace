package com.jaredeverett.jared.wearhelloworld;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class StatsActivity extends WearableActivity {

    private TextView tvTitle;
    private ProgressBar prFood;
    private ProgressBar prHygiene;
    private ProgressBar prEnergy;
    private ProgressBar prEntertainment;
    private ProgressBar prEducation;
    private TextView tvEnergy;
    private TextView tvAge;
    private TextView tvMoney;
    private TextView tvIQ;

    // stats
    private int food;
    private int hygiene;
    private int energy;
    private int age;
    private int coins;
    private int entertainment;
    private int education;
    private int iq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stats_layout);
        setAmbientEnabled();

        prFood = (ProgressBar) findViewById(R.id.prFood);
        prHygiene = (ProgressBar) findViewById(R.id.prHygiene);
        prEnergy = (ProgressBar) findViewById(R.id.prEnergy);
        prEntertainment = (ProgressBar) findViewById(R.id.prEntertainment);
        prEducation = (ProgressBar) findViewById(R.id.prEducation);
        tvEnergy = (TextView) findViewById(R.id.tvEnergy);
        tvAge = (TextView) findViewById(R.id.tvAge);
        tvMoney = (TextView) findViewById(R.id.tvMoney);
        tvIQ = (TextView) findViewById(R.id.tvIQ);

        Bundle extras=getIntent().getExtras();
        if(extras != null)//if bundle has content
        {
            this.food = extras.getInt("food");
            this.hygiene = extras.getInt("hygiene");
            this.energy = extras.getInt("energy");
            this.age = extras.getInt("age");
            this.coins = extras.getInt("coins");
            this.entertainment = extras.getInt("entertainment");
            this.education = extras.getInt("education");
            this.iq = extras.getInt("iq");

            prFood.setProgress(food);
            prHygiene.setProgress(hygiene);
            prEnergy.setProgress(energy);
            prEntertainment.setProgress(entertainment);
            prEducation.setProgress(education);

            tvEnergy.setText("Energy");
            tvAge.setText(age + " days");
            tvMoney.setText(Integer.toString(coins));
            tvIQ.setText("IQ " + iq);
        }
    }



    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        updateDisplay();
        super.onExitAmbient();
    }

    private void updateDisplay() {
//        if (isAmbient()) {
//            mContainerView.setBackgroundColor(getResources().getColor(android.R.color.black));
//            mTextView.setTextColor(getResources().getColor(android.R.color.white));
//            mClockView.setVisibility(View.VISIBLE);
//
//            mClockView.setText(AMBIENT_DATE_FORMAT.format(new Date()));
//        } else {
//            mContainerView.setBackground(null);
//            mTextView.setTextColor(getResources().getColor(android.R.color.black));
//            mClockView.setVisibility(View.GONE);
//        }
    }
}

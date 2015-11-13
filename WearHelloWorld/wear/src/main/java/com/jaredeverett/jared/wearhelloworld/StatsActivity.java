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
    private TextView tvEnergy;
    private TextView tvAge;

    // stats
    private int food;
    private int hygiene;
    private int energy;
    private int age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stats_layout);
        setAmbientEnabled();

        prFood = (ProgressBar) findViewById(R.id.prFood);
        prHygiene = (ProgressBar) findViewById(R.id.prHygiene);
        prEnergy = (ProgressBar) findViewById(R.id.prEnergy);
        tvEnergy = (TextView) findViewById(R.id.tvEnergy);
        tvAge = (TextView) findViewById(R.id.tvAge);

        Bundle extras=getIntent().getExtras();
        if(extras != null)//if bundle has content
        {
            this.food = extras.getInt("food");
            this.hygiene = extras.getInt("hygiene");
            this.energy = extras.getInt("energy");
            this.age = extras.getInt("age");

            prFood.setProgress(food);
            prHygiene.setProgress(hygiene);
            prEnergy.setProgress(energy);

            tvEnergy.setText("Energy " + energy + "/" + "100");
            tvAge.setText("Age: " + age + "m");
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

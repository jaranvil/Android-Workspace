package com.example.jared.munchkin_counter;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends WearableActivity {

    Button btnLevelUp;
    Button btnLevelDown;
    Button btnGearUp;
    Button btnGearDown;
    TextView tvLevel;
    TextView tvGear;
    TextView tvLevelTitle;
    TextView tvGearTitle;
    TextView tvTotal;

    private int level = 0;
    private int gear = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        btnLevelUp = (Button) findViewById(R.id.btnLevelUp);
        btnLevelDown = (Button) findViewById(R.id.btnLevelDown);
        btnGearUp = (Button) findViewById(R.id.btnGearUp);
        btnGearDown = (Button) findViewById(R.id.btnGearDown);
        tvLevel = (TextView) findViewById(R.id.tvLevel);
        tvGear = (TextView) findViewById(R.id.tvGear);
        tvLevelTitle = (TextView) findViewById(R.id.tvLevelTitle);
        tvGearTitle = (TextView) findViewById(R.id.tvGearTitle);
        tvTotal = (TextView) findViewById(R.id.tvTotal);

        updateCounter();

        btnLevelUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                level++;
                updateCounter();
            }
        });
        btnLevelDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                level--;
                updateCounter();
            }
        });
        btnGearUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gear++;
                updateCounter();
            }
        });
        btnGearDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gear--;
                updateCounter();
            }
        });
    }

    public void updateCounter()
    {
        if (level<0)
            level = 0;
        if (gear<0)
            gear = 0;

        tvLevel.setText(Integer.toString(level));
        tvGear.setText(Integer.toString(gear));
        tvTotal.setText(Integer.toString(level + gear));
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
        if (isAmbient()) {
            // hide wigets
            tvLevelTitle.setVisibility(View.GONE);
            tvGearTitle.setVisibility(View.GONE);
            tvLevel.setVisibility(View.GONE);
            tvGear.setVisibility(View.GONE);
            btnLevelUp.setVisibility(View.GONE);
            btnLevelDown.setVisibility(View.GONE);
            btnGearDown.setVisibility(View.GONE);
        btnGearUp.setVisibility(View.GONE);

            tvTotal.setTextSize(125);
        } else {
            tvLevelTitle.setVisibility(View.VISIBLE);
            tvGearTitle.setVisibility(View.VISIBLE);
            tvLevel.setVisibility(View.VISIBLE);
            tvGear.setVisibility(View.VISIBLE);
            btnLevelUp.setVisibility(View.VISIBLE);
            btnLevelDown.setVisibility(View.VISIBLE);
            btnGearDown.setVisibility(View.VISIBLE);
            btnGearUp.setVisibility(View.VISIBLE);

            tvTotal.setTextSize(48);
        }
    }
}

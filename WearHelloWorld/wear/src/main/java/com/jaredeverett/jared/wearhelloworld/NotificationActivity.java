package com.jaredeverett.jared.wearhelloworld;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class NotificationActivity extends WearableActivity {

    public static final String[] PROMOTIONS = {"Studying has paid off, you got a promotion at work!",
                                                "Your boss promotes you for 'going the extra mile'. Salary increases"};

    private Button btnOkay;
    private TextView tvPrompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_layout);
        setAmbientEnabled();

        btnOkay = (Button) findViewById(R.id.btnOkay);
        tvPrompt = (TextView) findViewById(R.id.tvPrompt);

        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Bundle extras=getIntent().getExtras();
        if(extras != null)//if bundle has content
        {
            String prompt = extras.getString("prompt", "");
            Random r = new Random();

            if (prompt.equals("welcome")) {
                tvPrompt.setText("A new kritter is about to hatch! Take good care of it and watch it grow");
            } else if (prompt.equals("promotion")) {
                tvPrompt.setText(PROMOTIONS[r.nextInt(PROMOTIONS.length)]);
            }
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

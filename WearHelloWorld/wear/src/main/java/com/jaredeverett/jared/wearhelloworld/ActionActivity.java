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

public class ActionActivity extends WearableActivity {
    private Button btnFeed;
    private Button btnEntertain;
    private Button btnRead;
    private Button btnShower;
    private Button btnLight;
    private Button btnMed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.action_layout);
        setAmbientEnabled();

        btnFeed = (Button) findViewById(R.id.btnFeed);
        btnEntertain = (Button) findViewById(R.id.btnEntertain);
        btnRead = (Button) findViewById(R.id.btnRead);
        btnShower = (Button) findViewById(R.id.btnShower);
        btnLight = (Button) findViewById(R.id.btnLight);
        btnMed = (Button) findViewById(R.id.btnMed);

        btnFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishWithResult("feed");
            }
        });

        btnShower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishWithResult("shower");
            }
        });

        btnLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishWithResult("light");
            }
        });
    }

    private void finishWithResult(String action)
    {
        Bundle conData = new Bundle();
        conData.putString("action", action);
        Intent intent = new Intent();
        intent.putExtras(conData);
        setResult(RESULT_OK, intent);
        finish();
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

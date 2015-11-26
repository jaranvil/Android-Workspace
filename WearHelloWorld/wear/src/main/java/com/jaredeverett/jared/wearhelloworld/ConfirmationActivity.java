package com.jaredeverett.jared.wearhelloworld;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ConfirmationActivity extends WearableActivity {

    TextView tvAction;
    TextView tvAmount;
    TextView tvGain;
    Button btnCancel;
    Button btnConfirm;
    String action = "";
    int cost = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirmation_layout);
        setAmbientEnabled();

        tvAction = (TextView) findViewById(R.id.tvAction);
        tvAmount = (TextView) findViewById(R.id.tvAmount);
        tvGain = (TextView) findViewById(R.id.tvGain);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        btnCancel= (Button) findViewById(R.id.btnCancel);

        Bundle extras=getIntent().getExtras();
        if(extras != null)//if bundle has content
        {
            this.action = extras.getString("action", "");

            if (action.equals("feed")) {
                tvAction.setText("Feed");
                tvAmount.setText(Integer.toString(MainActivity.FEED_COST) + " coins");
                tvGain.setText("hunger");
            } else if (action.equals("shower")) {
                tvAction.setText("Bath");
                tvAmount.setText("1 min");
                tvGain.setText("hygiene");
            } else if (action.equals("light")) {
                tvAction.setText("Sleep");
                tvAmount.setText("10 min");
                tvGain.setText("energy");
            } else if (action.equals("play")) {
                tvAction.setText("Play");
                tvAmount.setText("5 min");
                tvGain.setText("entertain");
            } else if (action.equals("read")) {
                tvAction.setText("Read");
                tvAmount.setText("5 min");
                tvGain.setText("education");
            } else if (action.equals("work")) {
                tvAction.setText("Work");
                tvAmount.setText("15 min");
                tvGain.setText("70 coin");
            }

        }


        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishWithResult(true);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishWithResult(false);
            }
        });

    }

    private void finishWithResult(boolean result)
    {
        Bundle conData = new Bundle();
        conData.putBoolean("confirm", result);
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

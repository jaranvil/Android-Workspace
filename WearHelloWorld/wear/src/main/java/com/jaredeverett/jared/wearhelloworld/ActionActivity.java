package com.jaredeverett.jared.wearhelloworld;

import android.content.Intent;
import android.content.SharedPreferences;
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
    private Button btnWork;

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
        btnWork = (Button) findViewById(R.id.btnWork);

        btnFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent("ConfirmationActivity");//create intent object
                Bundle extras = new Bundle();//create bundle object
                extras.putString("action", "feed");
                i.putExtras(extras);
                startActivityForResult(i, 2);
            }
        });

        btnShower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent("ConfirmationActivity");//create intent object
                Bundle extras = new Bundle();//create bundle object
                extras.putString("action", "shower");
                i.putExtras(extras);
                startActivityForResult(i, 3);
            }
        });

        btnLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent("ConfirmationActivity");//create intent object
                Bundle extras = new Bundle();//create bundle object
                extras.putString("action", "light");
                i.putExtras(extras);
                startActivityForResult(i, 4);
            }
        });

        btnEntertain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent("ConfirmationActivity");//create intent object
                Bundle extras = new Bundle();//create bundle object
                extras.putString("action", "play");
                i.putExtras(extras);
                startActivityForResult(i, 5);
            }
        });

        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent("ConfirmationActivity");//create intent object
                Bundle extras = new Bundle();//create bundle object
                extras.putString("action", "read");
                i.putExtras(extras);
                startActivityForResult(i, 6);
            }
        });

        btnWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent("ConfirmationActivity");//create intent object
                Bundle extras = new Bundle();//create bundle object
                extras.putString("action", "work");
                i.putExtras(extras);
                startActivityForResult(i, 7);
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case 2:
                if (resultCode == RESULT_OK) {
                    Bundle res = data.getExtras();
                    boolean confirm = res.getBoolean("confirm", false);

                    if (confirm)
                        finishWithResult("feed");
                }
                break;
            case 3:
                if (resultCode == RESULT_OK) {
                    Bundle res = data.getExtras();
                    boolean confirm = res.getBoolean("confirm", false);

                    if (confirm)
                        finishWithResult("shower");
                }
                break;
            case 4:
                if (resultCode == RESULT_OK) {
                    Bundle res = data.getExtras();
                    boolean confirm = res.getBoolean("confirm", false);

                    if (confirm)
                        finishWithResult("light");
                }
                break;
            case 5:
                if (resultCode == RESULT_OK) {
                    Bundle res = data.getExtras();
                    boolean confirm = res.getBoolean("confirm", false);

                    if (confirm)
                        finishWithResult("play");
                }
                break;
            case 6:
                if (resultCode == RESULT_OK) {
                    Bundle res = data.getExtras();
                    boolean confirm = res.getBoolean("confirm", false);

                    if (confirm)
                        finishWithResult("read");
                }
                break;
            case 7:
                if (resultCode == RESULT_OK) {
                    Bundle res = data.getExtras();
                    boolean confirm = res.getBoolean("confirm", false);

                    if (confirm)
                        finishWithResult("work");
                }
                break;
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

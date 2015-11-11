package com.jaredeverett.jared.wearhelloworld;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends WearableActivity {

    public static final String MyPREFERENCES = "MyPrefs";
    public static final int FEED_AMOUNT = 25;

    SharedPreferences sharedpreferences;

    private Button btnFood;
    private Button btnPoop;
    private Button btnLight;
    private Button btnList;
    private TextView tvAge;
    private TextView tvMood;
    private FrameLayout frLayout;

    private ImageView ivCharacter;
    private boolean day = true;
    Character character;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        setupCharacter();

        btnFood = (Button) findViewById(R.id.btnFood);
        btnPoop = (Button) findViewById(R.id.btnPoop);
        btnLight = (Button) findViewById(R.id.btnLight);
        btnList = (Button) findViewById(R.id.btnList);
        tvMood = (TextView) findViewById(R.id.tvMood);
        tvAge = (TextView) findViewById(R.id.tvAge);
        frLayout = (FrameLayout) findViewById(R.id.frLayout);

        ivCharacter = (ImageView) findViewById(R.id.ivCharacter);

        setupListeners();

        mHandler.postAtTime(rDealAndWait, SystemClock.uptimeMillis() + 400);

    }

    public void updateTextViews()
    {
        tvAge.setText("Age: " + character.getAge() + "d");
        tvMood.setText(character.getMood());
    }

    public void setupCharacter()
    {
        int food = sharedpreferences.getInt("food", 100);
        int hygiene = sharedpreferences.getInt("hygiene", 100);
        int energy = sharedpreferences.getInt("energy", 100);

        character = new Character(food, hygiene, energy);
    }

    // thread for character animation
    private final Handler mHandler = new Handler();
    private final Runnable rDealAndWait = new Runnable()
    {
        public void run()
        {
            character.draw(ivCharacter);
            mHandler.postAtTime(this, SystemClock.uptimeMillis() + 300);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        // get time elasped
        int timeInterval = updateTimestamp();

        // update day
        day = sharedpreferences.getBoolean("day", true);
        if (day)
            frLayout.setBackgroundResource(R.drawable.background);
        else
            frLayout.setBackgroundResource(R.drawable.night_bg);

        // update character
        character.updateStats(timeInterval, day);
        updateSharedPrefs();
        updateTextViews();
    }

    // updates the last seen value in shared prefs
    // returns the differcene, in unix time, from the last timestamp
    public int updateTimestamp()
    {
        // get last recorded time
        String lastTime = (sharedpreferences.getString("time", "0"));
        int last = Integer.parseInt(lastTime);

        //get current time
        int currentTime = (int) (long) (System.currentTimeMillis() / 1000L);
        int difference = currentTime - last;

        // save current time
        String time = String.valueOf(currentTime);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("time", time);
        editor.apply();

        return difference;
    }

    public void updateSharedPrefs()
    {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt("food", character.getFood());
        editor.putInt("hygiene", character.getHygiene());
        editor.putInt("energy", character.getEnergy());
        editor.putBoolean("day", day);
        editor.apply();
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

    public void setupListeners()
    {
        btnFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                character.feed(FEED_AMOUNT);
                updateSharedPrefs();
                updateTextViews();
            }
        });

        btnPoop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "[bath animation]", Toast.LENGTH_LONG).show();
                character.bath(FEED_AMOUNT);
                updateSharedPrefs();
                updateTextViews();
            }
        });

        btnLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                day = !day;

                if (day)
                {
                    frLayout.setBackgroundResource(R.drawable.background);

                    updateSharedPrefs();
                    updateTextViews();
                } else {
                    frLayout.setBackgroundResource(R.drawable.night_bg);

                    updateSharedPrefs();
                    updateTextViews();
                }



            }
        });

        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent("StatsActivity");//create intent object
                Bundle extras = new Bundle();//create bundle object
                extras.putInt("food", character.getFood());
                extras.putInt("hygiene", character.getHygiene());
                extras.putInt("energy", character.getEnergy());
                i.putExtras(extras);
                startActivityForResult(i, 1);
            }
        });
    }

}

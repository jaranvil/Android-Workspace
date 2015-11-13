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

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends WearableActivity {

    public static final String MyPREFERENCES = "MyPrefs";
    public static final int FEED_AMOUNT = 25;

    SharedPreferences sharedpreferences;

    private Button btnList;
    private Button btnAction;
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

        int DoB = sharedpreferences.getInt("dob", 0);
        if (DoB == 0) {
            newCharacter();
        } else {
            setupCharacter();
        }

        btnAction = (Button) findViewById(R.id.btnActions);
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
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm  aa");
        Date today = new Date();
        String todayStr = sdf.format(today);

        tvAge.setText(todayStr);

    }

    public void newCharacter()
    {
        int DoB = (int) (long) (System.currentTimeMillis() / 1000L);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt("food", 100);
        editor.putInt("hygiene", 100);
        editor.putInt("energy", 100);
        editor.putInt("age", 0);
        editor.putInt("dob", DoB);
        editor.putBoolean("day", true);
        editor.apply();
        setupCharacter();

        btnAction.setVisibility(View.VISIBLE);
        btnList.setVisibility(View.VISIBLE);
    }

    public void setupCharacter()
    {
        int food = sharedpreferences.getInt("food", 100);
        int hygiene = sharedpreferences.getInt("hygiene", 100);
        int energy = sharedpreferences.getInt("energy", 100);
        int age = sharedpreferences.getInt("age", 0);

        character = new Character(food, hygiene, energy, age);
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
        if (day) {

        } else {
            makeNight();
            int currentTime = (int) (long) (System.currentTimeMillis() / 1000L);
            int sleepTime = sharedpreferences.getInt("sleepTime", 0);
            if ((currentTime-sleepTime) > 100) {
                day = true;
                character.setEnergy(100);
                updateSharedPrefs();
                //onResume();
            }

        }

        // update character
        character.updateStats(timeInterval, day);
        character.setAge(sharedpreferences.getInt("dob", 0));
        updateSharedPrefs();
        updateTextViews();

        if (character.isDead()) {
            character.dead = true;
            btnAction.setVisibility(View.GONE);
            btnList.setVisibility(View.GONE);
        }

        // update mood
        tvMood.setText(character.getMood());
    }


    public void makeNight() {
        frLayout.setBackgroundResource(R.drawable.night_bg);
        btnAction.setVisibility(View.GONE);
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


        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent("StatsActivity");//create intent object
                Bundle extras = new Bundle();//create bundle object
                extras.putInt("food", character.getFood());
                extras.putInt("hygiene", character.getHygiene());
                extras.putInt("energy", character.getEnergy());
                extras.putInt("age", character.getAge());
                i.putExtras(extras);
                startActivityForResult(i, 1);
            }
        });

        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent("ActionActivity");//create intent object
                //Bundle extras = new Bundle();//create bundle object
                //extras.putInt("food", character.getFood());
                //i.putExtras(extras);
                startActivityForResult(i, 2);
            }
        });

        ivCharacter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (character.dead) {
                    newCharacter();
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case 2:
                if (resultCode == RESULT_OK) {
                    Bundle res = data.getExtras();
                    String result = res.getString("action");

                    if (result.equals("feed")) {
                        character.feed(FEED_AMOUNT);
                        updateSharedPrefs();
                    } else if (result.equals("shower")) {
                        Toast.makeText(getApplicationContext(), "[bath animation]", Toast.LENGTH_LONG).show();
                        character.bath(FEED_AMOUNT);
                        updateSharedPrefs();
                    } else if (result.equals("light")) {
                        day = false;
                        makeNight();

                        int currentTime = (int) (long) (System.currentTimeMillis() / 1000L);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putInt("sleepTime", currentTime);
                        editor.putBoolean("day", day);
                        editor.apply();
                    }
                }
                break;
        }
    }
}

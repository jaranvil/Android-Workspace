package com.jaredeverett.jared.wearhelloworld;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
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

    // production times
    public static final int NOTIFICATION_DELAY = 7200;
    public static final int SLEEP_DURATION = 300;
    public static final int SHOWER_DURATION = 30;
    public static final int PLAY_DURATION = 150;
    public static final int READ_DURATION = 150;
    public static final int WORK_DURATION = 300;

    // testing times
//    public static final int NOTIFICATION_DELAY = 300;
//    public static final int SLEEP_DURATION = 60;
//    public static final int SHOWER_DURATION = 60;
//    public static final int PLAY_DURATION = 120;
//    public static final int READ_DURATION = 120;
//    public static final int WORK_DURATION = 120;

    // costs
    public static final int FEED_COST = 25;

    Character character;
    SharedPreferences sharedpreferences;

    // widgets
    private Button btnList;
    private Button btnAction;
    private Button btnShop;
    private TextView tvAge;
    private TextView tvMood;
    private FrameLayout frLayout;
    private ImageView ivCharacter;



    Context c = this;

    // timers for character stats drain
    protected int foodTimer = 0;
    protected int hygieneTimer = 0;
    protected int energyTimer = 0;
    protected int entertainmentTimer = 0;
    protected int educationTimer = 0;

    private int[] egg_images = {R.drawable.egg_1, R.drawable.egg_2, R.drawable.egg_3, R.drawable.egg_4, R.drawable.egg_5, R.drawable.egg_6, R.drawable.egg_7, R.drawable.egg_8};
    private int eggNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        btnAction = (Button) findViewById(R.id.btnActions);
        btnList = (Button) findViewById(R.id.btnList);
        btnShop = (Button) findViewById(R.id.btnShop);
        tvMood = (TextView) findViewById(R.id.tvMood);
        tvAge = (TextView) findViewById(R.id.tvAge);
        frLayout = (FrameLayout) findViewById(R.id.frLayout);

        ivCharacter = (ImageView) findViewById(R.id.ivCharacter);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        int DoB = sharedpreferences.getInt("dob", 0);
        if (DoB == 0) {
            newCharacter();
        } else {
            setupCharacter();
        }

        setupListeners();

        mHandler.postAtTime(rDealAndWait, SystemClock.uptimeMillis() + 400);
        notificationHandler.postAtTime(notificationRunnable, SystemClock.uptimeMillis() + 10000);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // update character
        //character.updateStats(timeInterval, day);
        character.setAge(sharedpreferences.getInt("dob", 0));
        updateSharedPrefs();
        updateTextViews();// set time

        if (character.isDead()) {
            character.dead = true;
            resetAction();
            hideControls();
        }

        if (character.sleeping)
            makeNight();

        // update mood
        tvMood.setText(character.getMood());
    }

    // thread for character animation
    private final Handler mHandler = new Handler();
    private final Runnable rDealAndWait = new Runnable()
    {
        public void run()
        {
            // egg stage
            if (character.generation == 0) {
                if (eggNum < egg_images.length)
                    ivCharacter.setBackgroundResource(egg_images[eggNum]);
                if (eggNum > egg_images.length-1) {
                    eggNum = 0;
                    character.generation++;
                    showControls();
                    tvMood.setText(character.getMood());
                }
            }

            // draw charavter
            character.draw(ivCharacter);

            // repeat
            mHandler.postAtTime(this, SystemClock.uptimeMillis() + 300);
        }
    };
    private int last = 0;
    // thread for notifications
    private final Handler notificationHandler = new Handler();
    private final Runnable notificationRunnable = new Runnable()
    {
        public void run()
        {
            // reduce stats

            int timeInterval = updateTimestamp();
            foodTimer += timeInterval;
            hygieneTimer += timeInterval;
            energyTimer += timeInterval;
            entertainmentTimer += timeInterval;
            educationTimer += timeInterval;

            if (foodTimer >= Character.REDUCE_FOOD_FACTOR) {
                character.reduceFood(foodTimer);
                foodTimer = 0;
            }
            if (hygieneTimer >= Character.REDUCE_HYGIENE_FACTOR) {
                character.reduceHygiene(hygieneTimer);
                hygieneTimer = 0;
            }
            if (energyTimer >= Character.REDUCE_ENERGY_FACTOR) {
                character.reduceEnergy(energyTimer);
                energyTimer = 0;
            }
            if (entertainmentTimer >= Character.REDUCE_ENTERTAINMENT_FACTOR) {
                character.reduceEntertainment(entertainmentTimer);
                entertainmentTimer = 0;
            }
            if (educationTimer >= Character.REDUCE_EDUCATION_FACTOR) {
                character.reduceEducation(educationTimer);
                educationTimer = 0;
            }


            // sleep monitoring
            if (character.sleeping) {
                int currentTime = (int) (long) (System.currentTimeMillis() / 1000L);
                int sleepTime = sharedpreferences.getInt("actionStart", 0);
                if ((currentTime-sleepTime) > SLEEP_DURATION) {
                    makeDay();
                    // reset animation
                    character.sleeping = false;
                    character.counter = 0;
                    character.setEnergy(100);

                    notification("Krio Kritters", "Your kritter is awake!");
                    resetAction();
                    tvMood.setText(character.getMood());
                } else {
                    int timeLeft = ((SLEEP_DURATION - (currentTime - sleepTime)) / 60)+1;
                    if (timeLeft == 0)
                        tvMood.setText("<1 min left");
                    else
                        tvMood.setText(timeLeft + " mins left");
                }
            }

            // showering
            if (character.showering) {
                int currentTime = (int) (long) (System.currentTimeMillis() / 1000L);
                int startTime = sharedpreferences.getInt("actionStart", 0);
                if ((currentTime-startTime) > SHOWER_DURATION) {
                    // reset animation
                    character.showering = false;
                    character.counter = 0;
                    character.setHygiene(100);

                    showControls();

                    notification("Krio Kritters", "All clean");
                    resetAction();
                    tvMood.setText(character.getMood());
                } else {
                    int timeLeft = ((SHOWER_DURATION - (currentTime - startTime)) / 60)+1;
                    if (timeLeft == 0)
                        tvMood.setText("<1 min left");
                    else
                        tvMood.setText(timeLeft + " mins left");
                }
            }

            // playing
            if (character.playing) {
                int currentTime = (int) (long) (System.currentTimeMillis() / 1000L);
                int startTime = sharedpreferences.getInt("actionStart", 0);
                if ((currentTime-startTime) > PLAY_DURATION) {
                    // reset animation
                    character.playing = false;
                    character.counter = 0;
                    character.play();

                    showControls();

                    notification("Krio Kritters", "Finished playing");
                    resetAction();
                    tvMood.setText(character.getMood());
                } else {
                    int timeLeft = ((PLAY_DURATION - (currentTime - startTime)) / 60)+1;
                    if (timeLeft == 0)
                        tvMood.setText("<1 min left");
                    else
                        tvMood.setText(timeLeft + " mins left");
                }
            }

            // reading
            if (character.reading) {
                int currentTime = (int) (long) (System.currentTimeMillis() / 1000L);
                int startTime = sharedpreferences.getInt("actionStart", 0);
                if ((currentTime-startTime) > READ_DURATION) {
                    // reset animation
                    character.reading = false;
                    character.counter = 0;

                    int prevIQ = character.iq;
                    character.read();

                    showControls();

                    notification("Krio Kritters", "Finished reading");
                    resetAction();
                    tvMood.setText(character.getMood());

                    if (!(prevIQ % 10 == 0) && character.iq == 0) {
                        info("promotion");
                    }
                } else {
                    int timeLeft = ((READ_DURATION - (currentTime - startTime)) / 60)+1;
                    if (timeLeft == 0)
                        tvMood.setText("<1 min left");
                    else
                        tvMood.setText(timeLeft + " mins left");
                }
            }

            // working
            if (character.working) {
                int currentTime = (int) (long) (System.currentTimeMillis() / 1000L);
                int startTime = sharedpreferences.getInt("actionStart", 0);
                if ((currentTime-startTime) > WORK_DURATION) {
                    // reset animation
                    character.working = false;
                    character.counter = 0;
                    character.work();

                    showControls();

                    notification("Krio Kritters", "Back from work!");
                    resetAction();
                    tvMood.setText(character.getMood());
                } else {
                    int timeLeft = ((WORK_DURATION - (currentTime - startTime)) / 60)+1;
                    if (timeLeft == 0)
                        tvMood.setText("<1 min left");
                    else
                        tvMood.setText(timeLeft + " mins left");
                }
            }

            // stats notifications
            int currentTime = (int) (long) (System.currentTimeMillis() / 1000L);
            int lastNotification = sharedpreferences.getInt("lastNotification", 0);
            if (currentTime > (lastNotification + NOTIFICATION_DELAY)) {
                if (character.food > 1 && character.food < 10) {
                    System.out.println("Notification at food: " + character.food);
                    notification("Krio Kritters", "Your kritter is hungry!");
                }
            }

            updateSharedPrefs();

            notificationHandler.postAtTime(this, SystemClock.uptimeMillis() + 10000);
        }
    };

    public void updateTextViews()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm  aa");
        Date today = new Date();
        String todayStr = sdf.format(today);

        tvAge.setText(todayStr);

    }

    //---- Shared Preferences Methods --------

    public void newCharacter()
    {
        int DoB = (int) (long) (System.currentTimeMillis() / 1000L);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt("food", 100);
        editor.putInt("hygiene", 100);
        editor.putInt("energy", 100);
        editor.putInt("education", 10);
        editor.putInt("entertainment", 10);
        editor.putInt("age", 0);
        editor.putInt("coins", 100);
        editor.putInt("dob", DoB);
        editor.putInt("generation", 0);
        editor.putInt("iq", 75);
        editor.apply();
        setupCharacter();

        tvMood.setText(character.getMood());

        frLayout.setBackgroundResource(R.drawable.background);

        info("welcome");
    }

    public void setupCharacter()
    {
        int food = sharedpreferences.getInt("food", 100);
        int hygiene = sharedpreferences.getInt("hygiene", 100);
        int energy = sharedpreferences.getInt("energy", 100);
        int age = sharedpreferences.getInt("age", 0);
        int entertainment = sharedpreferences.getInt("entertainment", 100);
        int education = sharedpreferences.getInt("education", 100);
        int coins = sharedpreferences.getInt("coins", 0);
        int generation = sharedpreferences.getInt("generation", 0);
        int iq = sharedpreferences.getInt("iq", 70);
        String action = sharedpreferences.getString("action", "");

        if (!action.equals("") || generation == 0) {
            hideControls();
        }

        character = new Character(food, hygiene, energy, age, coins, entertainment, education, action, generation, iq);
    }

    public void updateSharedPrefs()
    {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt("food", character.getFood());
        editor.putInt("hygiene", character.getHygiene());
        editor.putInt("energy", character.getEnergy());
        editor.putInt("entertainment", character.entertainment);
        editor.putInt("education", character.education);
        editor.putInt("coins", character.coins);
        editor.putInt("generation", character.generation);
        editor.putInt("iq", character.iq);
        editor.apply();
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

    public void setActionStart(String action)
    {
        int currentTime = (int) (long) (System.currentTimeMillis() / 1000L);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt("actionStart", currentTime);
        editor.putString("action", action);
        editor.apply();

        btnAction.setBackgroundResource(R.drawable.cancel_button);
    }

    public void setNotificationTime()
    {
        int currentTime = (int) (long) (System.currentTimeMillis() / 1000L);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt("lastNotification", currentTime);
        editor.apply();
    }

    public void resetAction()
    {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("action", "");
        editor.apply();

        btnAction.setBackgroundResource(R.drawable.actions);
    }

    public void makeNight() {
        frLayout.setBackgroundResource(R.drawable.night_bg);
        hideControls();
    }

    public void makeDay() {
        frLayout.setBackgroundResource(R.drawable.background);
        showControls();
    }

    public void hideControls()
    {
        btnAction.setVisibility(View.GONE);
        btnList.setVisibility(View.GONE);
        btnShop.setVisibility(View.GONE);
    }

    public void showControls()
    {
        btnAction.setVisibility(View.VISIBLE);
        btnList.setVisibility(View.VISIBLE);
        btnShop.setVisibility(View.VISIBLE);
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
                extras.putInt("entertainment", character.entertainment);
                extras.putInt("education", character.education);
                extras.putInt("age", character.getAge());
                extras.putInt("coins", character.coins);
                extras.putInt("iq", character.iq);
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
                if (character.dead)
                    newCharacter();
                if (character.generation == 0)
                    eggNum++;
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
                        if (character.coins >= FEED_COST) {
                            character.feed();
                            character.coins -= FEED_COST;
                        } else
                            Toast.makeText(getApplicationContext(), "Not enough coin", Toast.LENGTH_SHORT).show();

                    } else if (result.equals("shower")) {
                        character.showering = true;
                        setActionStart("shower");
                        hideControls();
                    } else if (result.equals("read")) {
                        character.reading = true;
                        setActionStart("reading");
                        hideControls();
                    } else if (result.equals("play")) {
                        character.playing = true;
                        setActionStart("playing");
                        hideControls();
                    } else if (result.equals("light")) {
                        character.sleeping = true;
                        setActionStart("sleep");
                        makeNight();
                    } else if (result.equals("work")) {
                        character.working = true;
                        setActionStart("working");
                        hideControls();
                    }
                    updateSharedPrefs();
                }
                break;
        }
    }

    public void info(String prompt)
    {
        Intent i = new Intent("NotificationActivity");//create intent object
        Bundle extras = new Bundle();//create bundle object
        extras.putString("prompt", prompt);
        i.putExtras(extras);
        startActivityForResult(i, 2);
    }

    public void notification(String title, String text)
    {
        setNotificationTime();

        int notificationId = 101;
        // Build intent for notification content
        Intent viewIntent = new Intent(this, MainActivity.class);
        PendingIntent viewPendingIntent = PendingIntent.getActivity(this, 0, viewIntent, 0);
        //Building notification layout
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(c)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle(title)
                .setContentText(text)
                .setVibrate(new long[]{1000, 1000})
                .setAutoCancel(true)
                .setContentIntent(viewPendingIntent);

        // instance of the NotificationManager service
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // Build the notification and notify it using notification manager.
        notificationManager.notify(notificationId, notificationBuilder.build());
    }
}

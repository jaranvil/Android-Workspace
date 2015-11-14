package com.jaredeverett.jared.wearhelloworld;

import android.widget.ImageView;

import java.util.Random;

public class Character {
    // stats decrease rates
    private static final int REDUCE_FOOD_FACTOR = 120;
    private static final int REDUCE_HYGIENE_FACTOR = 175;
    private static final int REDUCE_ENERGY_FACTOR = 200;
    private static final int REDUCE_ENTERTAINMENT_FACTOR = 90;
    private static final int REDUCE_EDUCATION_FACTOR = 105;

    private static final int COIN_INCREASE = 360;
    // stats increase rates
    private static final int FEED_AMT = 25;
    private static final int SHOWER_AMT = 50;
    private static final int READ_AMT = 10;
    private static final int PLAY_AMT = 40;

    // animation values
    private int[] eatingImages = {R.drawable.eat1, R.drawable.eat2, R.drawable.eat3, R.drawable.eat4};

    private int[] dogImages = {R.drawable.happy1, R.drawable.happy2};
    private int[] deadImages = {R.drawable.rip};
    private int[] bathImages = {R.drawable.bath1, R.drawable.bath2, R.drawable.bath3, R.drawable.bath4, R.drawable.bath5};
    private int[] readImages = {R.drawable.read1, R.drawable.read2, R.drawable.read3, R.drawable.read4, R.drawable.read5};
    private int[] sleepImages = {R.drawable.sleep1, R.drawable.sleep2, R.drawable.sleep3, R.drawable.sleep4};
    private int[] playImages = {R.drawable.play1, R.drawable.play2, R.drawable.play3, R.drawable.play4};
    private boolean eating = false;
    private boolean showering = false;
    private boolean reading = false;
    protected boolean sleeping = false;
    private boolean playing = false;
    protected int counter = 0;

    private int age = 0;
    protected int coins = 100;

    // health stats
    private int food = 0;
    private int hygiene = 0;
    private int energy = 0;
    protected int entertainment = 0;
    protected int education = 0;
    private boolean sick = false;
    protected boolean dead = false;

    private int lsatUnixTime = 0;

    // contructor
    public Character(int food, int hygiene, int energy, int age, boolean day, int coins, int entertainment, int education)
    {
        this.food = food;
        this.hygiene = hygiene;
        this.energy = energy;
        this.age = age;
        this.coins = coins;
        this.entertainment = entertainment;
        this.education = education;

        if (!day)
            sleeping = true;
    }

    public void draw(ImageView iv)
    {
        int[] images;
        if (eating)
            images = eatingImages;
        else if (showering)
            images = bathImages;
        else if (reading)
            images = readImages;
        else if (playing)
            images = playImages;
        else if (dead) {
            images = deadImages;
            counter = 0;
        }
        else if (sleeping)
            images = sleepImages;
        else
            images = dogImages;

        iv.setBackgroundResource(images[counter]);
        counter++;
        if (counter > images.length-1)
        {
            counter = 0;
            if (eating)
                eating = false;
            if (showering)
                showering = false;
            if (reading)
                reading = false;
            if (playing)
                playing = false;
        }
    }

    public void setAge(int dob)
    {
        int currentTime = (int) (long) (System.currentTimeMillis() / 1000L);
        if (dob != 0) {
            int min = (currentTime - dob) / 60;
            int hours = min / 60;
            this.age = hours/24;
        }
    }

    public boolean isDead()
    {
        int emptyRows = 0;
        if (food == 0)
            emptyRows++;
        if (hygiene == 0)
            emptyRows++;
        if (energy == 0)
            emptyRows++;
        if (entertainment == 0)
            emptyRows++;

        if (emptyRows > 1)
            return true;
        else
            return false;
    }

    public void updateStats(int timeInterval, boolean day)
    {
        coins += timeInterval/COIN_INCREASE;

        // reduce hunger
        int factor = timeInterval/REDUCE_FOOD_FACTOR;
        if (factor < 0)
            factor = 0;
        food -= factor;
        if (food < 0 )
            food = 0;

        // reduce hygiene
        factor = timeInterval/REDUCE_HYGIENE_FACTOR;
        if (factor < 0)
            factor = 0;
        hygiene -= factor;
        if (hygiene < 0 )
            hygiene = 0;

        // reduce energy
        factor = timeInterval/REDUCE_ENERGY_FACTOR;
        if (factor < 0)
            factor = 0;
        if (day)
            energy -= factor;
        if (energy < 0 )
            energy = 0;

        // reduce entertainment
        factor = timeInterval/REDUCE_ENTERTAINMENT_FACTOR;
        if (factor < 0)
            factor = 0;
        entertainment -= factor;
        if (entertainment < 0 )
            entertainment = 0;

        // reduce education
        factor = timeInterval/REDUCE_EDUCATION_FACTOR;
        if (factor < 0)
            factor = 0;
        education -= factor;
        if (education < 0 )
            education = 0;
    }

    public String getNotificationActions(int interval)
    {
        int foodTemp = food;
        int hygieneTemp = hygiene;
        int energyTemp = energy;

        // reduce hunger
        int factor = interval/REDUCE_FOOD_FACTOR;
        if (factor < 0)
            factor = 0;
        foodTemp -= factor;
        if (foodTemp < 10)
            return "food";

        // reduce hygiene
        factor = interval/REDUCE_HYGIENE_FACTOR;
        if (factor < 0)
            factor = 0;
        hygieneTemp -= factor;
        if (hygieneTemp < 10)
            return "hygiene";

        // reduce energy
        factor = interval/REDUCE_ENERGY_FACTOR;
        if (factor < 0)
            factor = 0;
        energyTemp -= factor;
        if (energyTemp < 10)
            return "energy";

        return "";
    }

    public String getMood()
    {
        if (dead)
            return "";
        if (sleeping)
            return "Sleeping";

        String[] HAPPY = {"Happy", "Content", "Excited", "Satisfied", "Pleased"};
        String[] HUNGRY = {"Hungry", "Starving", "Famished"};
        String[] DIRTY = {"Dirty"};
        String[] TIRED = {"Tired", "Sleepy", "Fatigued"};

        Random r = new Random();

        if (food > 50 && hygiene > 50 && energy > 50) {
            return HAPPY[r.nextInt(HAPPY.length)];
        } else if (food < hygiene && food < energy) {
            return HUNGRY[r.nextInt(HUNGRY.length)];
        } else if (hygiene < food && hygiene < energy) {
            return DIRTY[r.nextInt(DIRTY.length)];
        } else if (energy < food && energy < hygiene) {
            return TIRED[r.nextInt(TIRED.length)];
        }
        return "";
    }

    public void feed()
    {
        // increase food. limit at 100
        this.food += FEED_AMT;
        if (food > 100)
            food = 100;

        // for character drawing
        counter = 0;
        eating = true;
    }

    public void bath()
    {
        this.hygiene += SHOWER_AMT;

        if (hygiene > 100)
            hygiene = 100;

        // for character drawing
        counter = 0;
        showering = true;
    }

    public void read()
    {
        this.education += READ_AMT;

        if (education > 100)
            education = 100;

        // for character drawing
        counter = 0;
        reading = true;
    }

    public void play()
    {
        this.entertainment += PLAY_AMT;

        if (entertainment > 100)
            entertainment = 100;

        // for character drawing
        counter = 0;
        playing = true;
    }

    // assessor and mutators
    public int getFood()
    {
        return this.food;
    }

    public void setFood(int food)
    {
        this.food = food;
    }

    public int getHygiene()
    {
        return this.hygiene;
    }

    public void setHygiene(int hygiene)
    {
        this.hygiene = hygiene;
    }

    public int getEnergy()
    {
        return this.energy;
    }

    public void setEnergy(int energy)
    {
        this.energy = energy;
    }

    public int getAge()
    {
        return this.age;
    }

}

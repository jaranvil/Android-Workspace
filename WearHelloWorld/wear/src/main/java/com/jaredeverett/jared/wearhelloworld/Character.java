package com.jaredeverett.jared.wearhelloworld;

import android.widget.ImageView;

import java.util.Random;

public class Character {

    // Constants

    // stats decrease rates
    public static final int REDUCE_FOOD_FACTOR = 100;
    public static final int REDUCE_HYGIENE_FACTOR = 200;
    public static final int REDUCE_ENERGY_FACTOR = 125;
    public static final int REDUCE_ENTERTAINMENT_FACTOR = 90;
    public static final int REDUCE_EDUCATION_FACTOR = 1900;
    // stats increase rates
    private static final int FEED_AMT = 25;
    private static final int READ_AMT = 90;
    private static final int PLAY_AMT = 40;
    private static final int WORK_SALARY = 75;

    // Graphics
    private int[] normal_images;
    private int[] sleeping_images;
    private int[] eating_images;
    private int[] reading_images;
    private int[] bath_images;
    private int[] work_images;
    private int[] play_images;

    // baby kritter
    private int[] kritter_normal = {R.drawable.kritter_normal_1, R.drawable.kritter_normal_2, R.drawable.kritter_normal_3, R.drawable.kritter_normal_4, R.drawable.kritter_normal_5, R.drawable.kritter_normal_6};
    private int[] kritter_sleep = {R.drawable.kritter_sleep_1, R.drawable.kritter_sleep_2, R.drawable.kritter_sleep_3, R.drawable.kritter_sleep_4};
    private int[] kritter_eat = {R.drawable.kriiter_eat_1, R.drawable.kriiter_eat_2, R.drawable.kriiter_eat_3, R.drawable.kriiter_eat_4, R.drawable.kriiter_eat_5, R.drawable.kriiter_eat_6};
    // doe
    private int[] doe_main = {R.drawable.doe1, R.drawable.doe2, R.drawable.doe3, R.drawable.doe4, R.drawable.doe5};
    private int[] doe_work = {R.drawable.doe_work_1, R.drawable.doe_work_2, R.drawable.doe_work_3, R.drawable.doe_work_4, R.drawable.doe_work_5};
    private int[] doe_reading = {R.drawable.doe_reading_1, R.drawable.doe_reading_2, R.drawable.doe_reading_3, R.drawable.doe_reading_4, R.drawable.doe_reading_5, R.drawable.doe_reading_6};
    private int[] doe_bath = {R.drawable.doe_bath_1, R.drawable.doe_bath_2, R.drawable.doe_bath_3, R.drawable.doe_bath_4, R.drawable.doe_bath_5};
    private int[] doe_sleep = {R.drawable.doe_sleep_1, R.drawable.doe_sleep_2, R.drawable.doe_sleep_3, R.drawable.doe_sleep_4};
    private int[] doe_play = {R.drawable.doe_play_1, R.drawable.doe_play_2, R.drawable.doe_play_3, R.drawable.doe_play_4, R.drawable.doe_play_5, R.drawable.doe_play_6, R.drawable.doe_play_7};
    private int[] doe_eat = {R.drawable.doe_eat_1, R.drawable.doe_eat_2, R.drawable.doe_eat_3, R.drawable.doe_eat_4, R.drawable.doe_eat_5, R.drawable.doe_eat_6, R.drawable.doe_eat_7, R.drawable.doe_eat_8, R.drawable.doe_eat_9};
    // RIP
    private int[] deadImages = {R.drawable.rip};

    private boolean eating = false;
    public boolean showering = false;
    protected boolean reading = false;
    protected boolean sleeping = false;
    protected boolean playing = false;
    protected boolean working = false;
    protected int counter = 0;

    // Stats

    protected int generation = 0;
    private int age = 0;
    protected int coins = 100;
    protected int food = 0;
    protected int hygiene = 0;
    protected int energy = 0;
    protected int entertainment = 0;
    protected int education = 0;
    protected int iq = 70;
    private boolean sick = false;
    protected boolean dead = false;

    // contructor
    public Character(int food, int hygiene, int energy, int age, int coins, int entertainment, int education, String action, int generation, int iq)
    {
        this.food = food;
        this.hygiene = hygiene;
        this.energy = energy;
        this.age = age;
        this.coins = coins;
        this.entertainment = entertainment;
        this.education = education;
        this.generation = generation;
        this.iq = iq;

        // set current action
        if (action.equals("shower"))
            showering = true;
        else if (action.equals("playing"))
            playing = true;
        else if (action.equals("reading"))
            reading = true;
        else if (action.equals("working"))
            working = true;
        else if (action.equals("sleep"))
            sleeping = true;

    }

    public void draw(ImageView iv)
    {
        // if the character is in the egg, the MainActivity draws it
        if (generation == 0)
            return;

        // choose image set
        if (generation == 1) {
            normal_images = kritter_normal;
            sleeping_images = kritter_sleep;
            eating_images = kritter_eat;
            reading_images = doe_reading;
            bath_images = doe_bath;
            work_images = doe_work;
            play_images = doe_play;
        } else if (generation == 2) {
            normal_images = doe_main;
            sleeping_images = doe_sleep;
            eating_images = doe_eat;
            reading_images = doe_reading;
            bath_images = doe_bath;
            work_images = doe_work;
            play_images = doe_play;
        }

        // select image array for current animation
        int[] images;
        if (eating)
            images = eating_images;
        else if (showering)
            images = bath_images;
        else if (reading)
            images = reading_images;
        else if (playing)
            images = play_images;
        else if (dead) {
            images = deadImages;
            counter = 0;
        }
        else if (sleeping)
            images = sleeping_images;
        else if (working)
            images = work_images;
        else
            images = normal_images;

        // double check the counter to prevent out of bounds errors
        if (counter > images.length-1)
            counter = 0;

        iv.setBackgroundResource(images[counter]);
        counter++;

        if (counter > images.length-1)
        {
            counter = 0;
            if (eating)
                eating = false;
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

//    public void updateStats(int timeInterval, boolean day)
//    {
//        coins += timeInterval/COIN_INCREASE;
//
//    }

    public void reduceFood(int interval)
    {
        int temp = (interval/REDUCE_FOOD_FACTOR);
        food -= temp;
        if (food < 0)
            food = 0;
        System.out.println("Interval: " + interval);
        System.out.println("temp: " + temp);
        System.out.println("Setting hunger to: " + food);
    }

    public void reduceHygiene(int interval)
    {
        if (playing)
            hygiene -= interval/(REDUCE_HYGIENE_FACTOR / 2);
        else
            hygiene -= interval/REDUCE_HYGIENE_FACTOR;

        if (hygiene < 0 )
            hygiene = 0;
    }

    public void reduceEnergy(int interval)
    {
        if (working || playing)
            energy -= interval/(REDUCE_ENERGY_FACTOR / 2);
        else
            energy -= interval/REDUCE_ENERGY_FACTOR;

        if (energy < 0 )
            energy = 0;
    }

    public void reduceEntertainment(int interval)
    {
        if (working)
            entertainment -= interval/(REDUCE_ENTERTAINMENT_FACTOR / 3);
        else
            entertainment -= interval/REDUCE_ENTERTAINMENT_FACTOR;

        if (entertainment < 0 )
            entertainment = 0;
    }

    public void reduceEducation(int interval)
    {
        education -= interval/REDUCE_EDUCATION_FACTOR;
        if (education < 0 )
            education = 0;
    }

    public String getMood()
    {
        if (generation == 0)
            return "Hatching";
        if (dead)
            return "Dead";
        if (sleeping)
            return "Sleeping";
        if (showering)
            return "Bathing";
        if (playing)
            return "Playing";
        if (reading)
            return "Reading";
        if (working)
            return "Working";

        if (food > 50 && hygiene > 50 && energy > 50) {
            return "Content";
        } else if (food < hygiene && food < energy) {
            if (food < 10)
                return "Starving";
            else if (food < 25)
                return "Famished";
            else
                return "Hungry";
        } else if (hygiene < food && hygiene < energy) {
            return "Dirty";
        } else if (energy < food && energy < hygiene) {
            return "Tired";
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

    public void read()
    {
        this.education += READ_AMT;

        if (education >= 100) {
            education = 0;
            iq++;
        }

    }

    public void play()
    {
        this.entertainment += PLAY_AMT;

        if (entertainment > 100)
            entertainment = 100;
    }

    public void work()
    {
        this.coins += WORK_SALARY;
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

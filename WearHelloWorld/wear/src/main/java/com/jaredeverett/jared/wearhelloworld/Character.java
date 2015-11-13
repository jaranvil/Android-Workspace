package com.jaredeverett.jared.wearhelloworld;

import android.widget.ImageView;

import java.util.Random;

public class Character {
    // properties
    private static final int REDUCE_FOOD_FACTOR = 150;
    private static final int REDUCE_HYGIENE_FACTOR = 100;
    private static final int REDUCE_ENERGY_FACTOR = 130;

    // animation values
    private int[] eatingImages = {R.drawable.eat1, R.drawable.eat2, R.drawable.eat3, R.drawable.eat4};
    //private int[] mainImaages = {R.drawable.face1, R.drawable.face2};
    //private int[] deerImages = {R.drawable.deer1, R.drawable.deer2, R.drawable.deer3};
    //private int[] snakeImages = {R.drawable.snake1, R.drawable.snake2, R.drawable.snake3};
    private int[] dogImages = {R.drawable.happy1, R.drawable.happy3};
    private boolean eating = false;
    private int counter = 0;



    private int age = 0;

    // health stats
    private int food = 0;
    private int hygiene = 0;
    private int energy = 0;
    private int entertainment = 0;
    private int education = 0;
    private boolean sick = false;
    protected boolean dead = false;

    private int lsatUnixTime = 0;

    // contructor
    public Character(int food, int hygiene, int energy, int age)
    {
        this.food = food;
        this.hygiene = hygiene;
        this.energy = energy;
        this.age = age;
    }

    public void draw(ImageView iv)
    {
        if (eating)
        {
            iv.setBackgroundResource(eatingImages[counter]);
            counter++;
            if (counter > eatingImages.length-1)
            {
                eating = false;
                counter = 0;
            }
        } else if (dead) {
            iv.setBackgroundResource(R.drawable.rip);
        } else {
            iv.setBackgroundResource(dogImages[counter]);
            counter++;
            if (counter > dogImages.length-1)
                counter = 0;
        }
    }

    public void setAge(int dob)
    {
        int currentTime = (int) (long) (System.currentTimeMillis() / 1000L);
        if (dob != 0) {
            this.age = (currentTime - dob) / 60;
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
        reduceFood(timeInterval);
        reduceHygiene(timeInterval);
        modifyEnergy(timeInterval, day);
    }

    public void reduceFood(int timeInterval)
    {
        int factor = timeInterval/REDUCE_FOOD_FACTOR;
        if (factor < 0)
            factor = 0;
        food -= factor;
        if (food < 0 )
            food = 0;
    }

    public void reduceHygiene(int timeInterval)
    {
        int factor = timeInterval/REDUCE_HYGIENE_FACTOR;
        if (factor < 0)
            factor = 0;
        hygiene -= factor;
        if (hygiene < 0 )
            hygiene = 0;
    }

    public void modifyEnergy(int timeInterval, boolean day)
    {
        int factor = timeInterval/REDUCE_ENERGY_FACTOR;
        if (factor < 0)
            factor = 0;

        if (day)
            energy -= factor;
        else
            energy += factor;

        if (energy < 0 )
            energy = 0;
        if (energy > 100)
            energy = 100;
    }

    public String getMood()
    {
        if (dead)
            return "";

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

    public void feed(int amt)
    {
        // increase food. limit at 100
        this.food += amt;
        if (food > 100)
            food = 100;

        // for character drawing
        counter = 0;
        eating = true;
    }

    public void bath(int amt)
    {
        this.hygiene += amt;

        if (hygiene > 100)
            hygiene = 100;
    }

    public void sleep(int amt)
    {
        this.energy += amt;

        if (energy > 100)
            energy = 100;
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

package com.jaredeverett.jared.wearhelloworld;

import android.widget.ImageView;

public class Character {
    // properties
    private static final int REDUCE_FOOD_FACTOR = 50;
    private static final int REDUCE_HYGIENE_FACTOR = 85;
    private static final int REDUCE_ENERGY_FACTOR = 130;

    // animation values
    private int[] eatingImages = {R.drawable.eat1, R.drawable.eat2, R.drawable.eat3, R.drawable.eat4};
    private int[] mainImaages = {R.drawable.face1, R.drawable.face2};
    private boolean eating = false;
    private int counter = 0;



    private int age = 0;

    // health stats
    private int food = 0;
    private int hygiene = 0;
    private int energy = 0;

    private int lsatUnixTime = 0;

    // contructor
    public Character(int food, int hygiene, int energy)
    {
        this.food = food;
        this.hygiene = hygiene;
        this.energy = energy;
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
        }
        else
        {
            iv.setBackgroundResource(mainImaages[counter]);
            counter++;
            if (counter > mainImaages.length-1)
                counter = 0;
        }


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
        return "Happy";
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

package com.nscc.jared.gamejam;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

import java.util.ArrayList;

/**
 * Created by Jared on 1/29/2016.
 */
public class Character {
    private Context context;
    protected int x = 500;
    protected int y = 500;
    protected int width;
    protected int height;
    private int arrayPosition = 0;
    protected boolean moving = false;

    public Character(int width, int height, BitmapFactory.Options options)
    {
        x = width/2;
        y = height/2;
        this.width = options.outWidth;
        this.height = options.outHeight;
    }

    public void draw(Canvas c, Paint p, int height, int width, Context context, Bitmap[] walking)
    {
        this.context = context;

        x = width/2;
        y = height/2;

        if (moving)
            c.drawBitmap(walking[arrayPosition], x, y, p);
        else
            c.drawBitmap(walking[0], x, y, p);



        arrayPosition++;
        if (arrayPosition > walking.length-1)
            arrayPosition = 0;
    }

}

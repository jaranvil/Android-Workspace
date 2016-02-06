package com.nscc.jared.gamejam;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

/**
 * Created by Jared on 2/1/2016.
 */
public class Wall {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
     int x;
    private int y1Top;
    private int y2Top;
    private int y1Bot;
    private int y2Bot;

    private int width;
    private int height;

    private int gap = 400;

    private int orgY2Top;
    private int orgY1Bot;

    public Wall(int width, int height)
    {
        Random rmd = new Random();
        this.width = width;
        this.height = height;

        x = width;
        y1Top = 0;
        y2Top = rmd.nextInt(height - gap);
        y1Bot = y2Top + gap;
        y2Bot = height;

        orgY2Top = y2Top;
        orgY1Bot = y1Bot;
    }

    public void draw(Canvas c, int width, int height, int speed)
    {
        x -= speed;

        y2Top++;
        y1Bot--;

        paint.setColor(Color.parseColor("#ffffff"));
        paint.setStrokeWidth(5);
        c.drawLine(x, y1Top, x, y2Top, paint);
        c.drawLine(x, y1Bot, x, height, paint);
    }

    public boolean checkCollision(int x, int y, int radius)
    {
        if ((x-radius) <= this.x && (x+radius) >= this.x)
        {
            // horizontal collision
            if ((y-radius) <= y2Top || (y+radius) >= y1Bot)
                return true;
        }

        return false;
//        boolean horizontalCollision = false;
//        if (prevX < x)
//            if (newX >= x)
//                horizontalCollision = true;
//        if (prevX > x)
//            if (newX <= x)
//                horizontalCollision = true;
//
//        if (horizontalCollision)
//        {
//            if (newY - radius < y2Top || newY + radius > y1Bot)
//                return true;
//            else
//                return false;
//        }
//
//        return false;

    }

    public void resetGap()
    {
        this.y2Top = orgY2Top;
        this.y1Bot = orgY1Bot;
    }

    public boolean isOffScreen()
    {
        if (x < 0)
            return true;
        return false;
    }
}

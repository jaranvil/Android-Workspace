package com.nscc.jared.gamejam;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

/**
 * Created by Jared on 1/29/2016.
 */
public class Character {
    private Context context;
    protected int x;
    protected int y;
    private int radius = 20;
    private int horizontalMovement = 0;
    private int verticalMovement = 0;

    public Character(int width, int height)
    {
        x = width/2;
        y = height/2;
    }

    public boolean draw(Canvas c, Paint p, int height, int width, ArrayList<Wall> walls)
    {
        boolean collision = false;
        int prevX = x;
        int prevY = y;
        x += horizontalMovement;
        y += verticalMovement;

        // Check screen boundaries
        if (x < 0 || x > width)
            x = prevX;
        if (y < 0 || y > height)
            y = prevY;

        // check wall collisions
        for (int i =0;i<walls.size();i++)
            if (walls.get(i).checkCollision(prevX, prevY, x, y, radius))
            {
                x = prevX;
                collision = true;
            }

        p.setColor(Color.parseColor("#ffff00"));
        c.drawCircle(x, y, radius, p);

        return collision;
    }

    public void setDurection(int x, int y)
    {
        horizontalMovement = x;
        verticalMovement = y;
    }
}

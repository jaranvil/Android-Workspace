package com.nscc.jared.gamejam;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by jared on 05/02/16.
 */
public class Item {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected int x;
    protected int y;
    protected int size = 25;

    private int[] colors = {Color.parseColor("#2274a5")};


    public Item(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public void draw(Canvas c, int speed)
    {
        x-=speed;
        paint.setColor(colors[0]);
        c.drawRect(x, y, x+(size), y+size, paint);
    }

    public boolean collect(Character c)
    {
        if ((c.x - c.radius) <= this.x && (c.x + c.radius) >= this.x)
            if ((c.y - c.radius) <= this.y && (c.y + c.radius) >= this.y)
                return true;
        return false;
    }
}

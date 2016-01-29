package com.nscc.jared.gamejam;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Jared on 1/29/2016.
 */
public class Joystick {
    private int center;
    private int x;
    private int y;

    public Joystick(int center, int x, int y)
    {
        
    }

    public void draw(Canvas c, Paint paint, int height, int width)
    {
        paint.setColor(Color.parseColor("#cccccc"));
        paint.setAlpha(40);
        c.drawCircle(210, height - 210, 200, paint);
        paint.setColor(Color.parseColor("#000000"));
        c.drawCircle(210, height - 210, 10, paint);
    }
}

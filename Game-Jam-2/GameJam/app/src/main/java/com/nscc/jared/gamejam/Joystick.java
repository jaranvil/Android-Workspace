package com.nscc.jared.gamejam;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Jared on 1/29/2016.
 */
public class Joystick {
    private int radius = 200;
    protected int x = 210;
    protected int y;

    public Joystick()
    {

    }

    public void draw(Canvas c, Paint paint, int height)
    {
        y = height - x;
        paint.setColor(Color.parseColor("#cccccc"));
        paint.setAlpha(40);
        c.drawCircle(x, y, radius, paint);
        paint.setColor(Color.parseColor("#000000"));
        c.drawCircle(x, y, 10, paint);
    }

    public boolean touchOnJoystick(int touchX, int touchY, int height)
    {
        if (touchX > (x-radius) && touchX < (x + radius))
        {
            if (touchY > (height-(radius*2+10)))
            {
                return true;
            }
        }
        return false;
    }
}

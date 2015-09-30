package com.jaredeverett.jared.breakout;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Jared on 9/22/2015.
 */
public class ScoreTiles {
    private int top;
    private int bottom;
    private int right;
    private int left;
    private int width;
    private int height;
    protected int score = 0;
    private int alpha = 0;

    public ScoreTiles(int top, int bottom, int right, int left) {
        this.top = top-1;
        this.bottom = bottom-1;
        this.right = right-1;
        this.left = left-1;

        int width = right - left;
        int height = bottom - top;
        this.width = width;
        this.height = height;
        score = (width * height)/1000;
    }

    public void draw(Canvas c, Paint p) {
        if (alpha < 100)
            alpha+=2;

        p.setAlpha(alpha);
        c.drawRect(left, top, right, bottom, p);

//        p.setAlpha(50);
//        p.setColor(Color.parseColor("#FFFFFF"));
//        p.setTextSize(40);
//        c.drawText(Integer.toString(score), left + (width / 2), top + (height / 2), p);


    }


}

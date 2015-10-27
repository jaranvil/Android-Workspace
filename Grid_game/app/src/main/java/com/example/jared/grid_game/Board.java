package com.example.jared.grid_game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Jared on 10/4/2015.
 */
public class Board {
    private final double SIDE_MARGIN_PERCENT = 0.05;
    private final double TOP_MARGIN_PERCENT = 0.15;
    private final int WHITE = Color.parseColor("#ffffff");

    protected int leftMargin = 0;
    protected int topMargin = 0;
    protected int rightMargin = 0;
    protected int bottomMargin = 0;



    public Board(int width, int height)
    {
        Double d = width*SIDE_MARGIN_PERCENT;
        leftMargin =  d.intValue();
        rightMargin = width - leftMargin;

        d = height*TOP_MARGIN_PERCENT;
        topMargin =  d.intValue();
        bottomMargin = height - leftMargin;

    }

    public void draw(Canvas c, Paint p)
    {
        p.setColor(WHITE);
        c.drawRect(leftMargin, topMargin, rightMargin, bottomMargin, p);
    }
}

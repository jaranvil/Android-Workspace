package com.jaredeverett.jared.breakout;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Jared on 9/22/2015.
 */
public class Line {
    private int x1;
    private int x2;
    private int y1;
    private int y2;
    private boolean vertical;

    public Line(int x1, int x2, int y1, int y2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;

        if (y1 == y2) {
            vertical = false;
        } else {
            vertical = true;
        }
    }

    public void draw(Canvas c, Paint paint) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        c.drawLine(x1, y1, x2, y2, paint);
        paint.setStyle(Paint.Style.FILL);
    }

    public int getX1() {
        return x1;
    }

    public int getY2() {
        return y2;
    }

    public int getY1() {
        return y1;
    }

    public int getX2() {
        return x2;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public boolean isVertical() {
        return vertical;
    }

    public void setVertical(boolean vertical) {
        this.vertical = vertical;
    }
}

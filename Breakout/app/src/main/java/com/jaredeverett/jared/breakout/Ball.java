package com.jaredeverett.jared.breakout;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Jared on 9/22/2015.
 */
public class Ball {
    private int x = 0;
    private int y = 0;
    private boolean right = false;
    private boolean up = false;
    private int SPEED;
    protected int leftBound = 0;
    protected int rightBound;
    protected int topBound = 0;
    protected int bottomBound;
    protected boolean alone = false;

    public Ball(int width, int height, int level) {
        Random rnd = new Random();

        SPEED = 10 + rnd.nextInt(10+(level*2));

        x = rnd.nextInt(width);
        y = rnd.nextInt(height);

        int movingUp = rnd.nextInt(2);
        int movingRight = rnd.nextInt(2);

        if (movingUp == 1) {
            up = true;
        }
        if (movingRight == 1) {
            right = true;
        }

        rightBound = width;
        bottomBound = height-150;

    }

    public void draw(Canvas c, Paint paint, ArrayList<Line> lines, int width, int height) {

        if (alone)
            SPEED = 0;

        if (up) {
            if (y+25 > bottomBound) {
                up = false;
            }
        } else {
            if (y-25 < topBound) {
                up = true;
            }
        }
        if (right) {
            if (x+25 > rightBound) {
                right = false;
            }
        } else {
            if (x-25 < leftBound) {
                right = true;
            }
        }

        // move ball
        if (up) {
            y = y + SPEED;
        } else {
            y = y - SPEED;
        }
        if (right) {
            x = x + SPEED;
        } else {
            x = x - SPEED;
        }

        // draw ball
        c.drawCircle(x, y, 25, paint);
    }

    public void getBoundaries(ArrayList<Line> lines, int width,int  height) {
        boolean breakFlag = false;

        // right boundary
        rightBound = width; // absolute right boundary
        for (int i = x; i < width;i++) {
            for (int ii = 0;ii < lines.size();ii++) {
                int x1 = lines.get(ii).getX1();
                int x2 = lines.get(ii).getX2();
                int y1 = lines.get(ii).getY1();
                int y2 = lines.get(ii).getY2();

                if (lines.get(ii).isVertical()) {
                    if (i == x1 && y >= y1 && y <= y2) {
                        rightBound = x1;
                        breakFlag = true;
                        break;
                    }
                }
            }
            if (breakFlag) {
                breakFlag = false;
                break;
            }
        }

        // left boundary
        leftBound = 0; // absolute right boundary
        for (int i = x; i > 0;i--) {
            for (int ii = 0;ii < lines.size();ii++) {
                int x1 = lines.get(ii).getX1();
                int x2 = lines.get(ii).getX2();
                int y1 = lines.get(ii).getY1();
                int y2 = lines.get(ii).getY2();

                if (lines.get(ii).isVertical()) {
                    if (i == x1 && y >= y1 && y <= y2) {
                        leftBound = x1;
                        breakFlag = true;
                        break;
                    }
                }
            }
            if (breakFlag) {
                breakFlag = false;
                break;
            }
        }

        // top boundary
        topBound = 0; // absolute right boundary
        for (int i = y; i > 0;i--) {
            for (int ii = 0;ii < lines.size();ii++) {
                int x1 = lines.get(ii).getX1();
                int x2 = lines.get(ii).getX2();
                int y1 = lines.get(ii).getY1();
                int y2 = lines.get(ii).getY2();

                if (!lines.get(ii).isVertical()) {
                    if (i == y1 && x >= x1 && x <= x2) {
                        topBound = y1;
                        breakFlag = true;
                        break;
                    }
                }
            }
            if (breakFlag) {
                breakFlag = false;
                break;
            }
        }

        // bottom boundary
        bottomBound = height-150; // absolute right boundary
        for (int i = y; i < height;i++) {
            for (int ii = 0;ii < lines.size();ii++) {
                int x1 = lines.get(ii).getX1();
                int x2 = lines.get(ii).getX2();
                int y1 = lines.get(ii).getY1();
                int y2 = lines.get(ii).getY2();

                if (!lines.get(ii).isVertical()) {
                    if (i == y1 && x >= x1 && x <= x2) {
                        bottomBound = y1;
                        breakFlag = true;
                        break;
                    }
                }
            }
            if (breakFlag) {
                breakFlag = false;
                break;
            }
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isRight() {
        return right;
    }

    public boolean isUp() {
        return up;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setY(int y) {
        this.y = y;
    }
}

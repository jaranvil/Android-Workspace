package com.jaredeverett.jared.breakout;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Jared on 9/22/2015.
 */
public class Ball {
    // location
    private int x = 0;
    private int y = 0;

    // direction
    private boolean right = false;
    private boolean up = false;

    // boundaries
    protected int leftBound = 0;
    protected int rightBound;
    protected int topBound = 150;
    protected int bottomBound;

    // other properties
    protected int color = 0;
    protected int radius = 25;
    protected int SPEED;
    protected boolean alone = false;
    protected boolean stayInBounds = true;

    public Ball(int width, int height, int level, int color) {
        Random rnd = new Random();

        // random speed
        SPEED = 10 + rnd.nextInt(10+(level*2));

        // this levels color
        this.color = color;

        // for smaller screens
        if (width < 500) {
            radius=10;
            topBound = 75;
            SPEED = 2 + rnd.nextInt(10+(level*2));
        }

        // random start position
        x = rnd.nextInt(width);
        y = rnd.nextInt(height);

        // random start direction
        int movingUp = rnd.nextInt(2);
        int movingRight = rnd.nextInt(2);
        if (movingUp == 1) {
            up = true;
        }
        if (movingRight == 1) {
            right = true;
        }

        // set absolute boundaries
        rightBound = width;
        bottomBound = height;
    }

    public void draw(Canvas c, Paint paint, ArrayList<Line> lines, int width, int height) {

        if (alone)
            SPEED = 0;

        if (stayInBounds) {
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
        }

        moveBall(c, paint);

        if (stayInBounds) {
            if (up) {
                if (y+25 > bottomBound) {
                    up = false;
                    moveBall(c, paint);
                }
            } else {
                if (y-25 < topBound) {
                    up = true;
                    moveBall(c, paint);
                }
            }
            if (right) {
                if (x+25 > rightBound) {
                    right = false;
                    moveBall(c, paint);
                }
            } else {
                if (x-25 < leftBound) {
                    right = true;
                    moveBall(c, paint);
                }
            }
        }


    }

    public void moveBall(Canvas c, Paint p) {
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
        p.setColor(color);
        c.drawCircle(x, y, radius, p);
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
        bottomBound = height; // absolute right boundary
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

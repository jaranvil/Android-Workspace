package com.nscc.jared.gamejam;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Jared on 1/29/2016.
 */
public class Barrier {
    protected int left;
    protected int top;
    protected int bottom;
    protected int right;
    protected boolean interactive = false;
    protected int type;
    protected int doorType;

    private int COLLISION_PADDING = 75;

    public Barrier(int x, int y, int width, int height, boolean interactive, int type, int doorType)
    {
        this.right = x + width;
        this.bottom = y + height;
        this.left = x;
        this.top = y;
        this.interactive = interactive;
        this.type = type;
        this.doorType = doorType;
    }

    public boolean isTouchingRight(int characterTop, int characterLeft, int characterRight, int characterBottom)
    {
        // check right
        if (characterRight >= (left-COLLISION_PADDING) && characterRight <= (right))
            if ((top >= characterTop && top <= characterBottom) || (bottom >= characterTop && bottom <= characterBottom))
            {
                return false;
            }
        return true;
    }

    public boolean isTouchingLeft(int characterTop, int characterLeft, int characterRight, int characterBottom)
    {
        if (characterLeft >= left && characterLeft <= (right+COLLISION_PADDING))
            if ((top >= characterTop && top <= characterBottom) || (bottom >= characterTop && bottom <= characterBottom))
                return false;

        return true;
    }

    public boolean isTouchingUp(int characterTop, int characterLeft, int characterRight, int characterBottom)
    {
        if (characterTop >= top && characterTop <= (bottom + COLLISION_PADDING))
            if ((left >= characterLeft && left <= characterRight) || (right >= characterLeft && right <= characterRight))
                return false;

        return true;
    }

    public boolean isTouchingDown(int characterTop, int characterLeft, int characterRight, int characterBottom)
    {
        if (characterBottom >= (top-COLLISION_PADDING) && characterBottom <= (bottom))
            if ((left >= characterLeft && left <= characterRight) || (right >= characterLeft && right <= characterRight))
                return false;

        return true;
    }
}

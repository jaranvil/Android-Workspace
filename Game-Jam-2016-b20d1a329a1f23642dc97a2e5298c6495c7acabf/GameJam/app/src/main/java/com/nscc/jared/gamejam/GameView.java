package com.nscc.jared.gamejam;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GameView extends View {
    private Context mContext;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Animation animation;
    private Joystick joystick;
    private Character character;
    private boolean loading = true;
    private int wallCreationCounter = 0;

    private ArrayList<Wall> walls = new ArrayList<>();

    private int height;
    private int width;

    protected boolean gameOver = false;

    public GameView(Context context, AttributeSet attrs)  {
        super(context, attrs);
        mContext = context;
        joystick = new Joystick();

    }

    protected void onDraw(Canvas c) {
        height = this.getHeight();
        width = this.getWidth();

        if (loading)
        {
            loading = false;
            character = new Character(width, height);
            walls.add(new Wall(width, height));
        }

        // TODO - remove color parsing from onDraw method
        c.drawColor(Color.parseColor("#800000"));

        wallCreationCounter++;
        if (wallCreationCounter > 75)
        {
            walls.add(new Wall(width, height));
            wallCreationCounter = 0;
        }


        for (int i =0;i<walls.size();i++)
        {
            walls.get(i).draw(c, width, height);
            if (walls.get(i).isOffScreen())
                walls.remove(i);
        }

        joystick.draw(c, paint, height);
        boolean collision = character.draw(c, paint, height, width, walls);
        if (collision)
            gameOver = true;
    }

    public boolean onTouchEvent(MotionEvent event) {

        int eventAction = event.getAction();

        int x = (int)event.getX();
        int y = (int)event.getY();


        if (joystick.touchOnJoystick(x, y, this.height))
        {
            int xDiff = (x - joystick.x)/3;
            int yDiff = (y - joystick.y)/3;
            character.setDurection(xDiff, yDiff);
        }


        if (eventAction == MotionEvent.ACTION_UP)
        {
            character.setDurection(0, 0);
        }
        return true;
    }


}
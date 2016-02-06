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
import java.util.Random;

public class GameView extends View {
    private Context mContext;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Animation animation;
    private Joystick joystick;
    private Character character;
    private boolean loading = true;
    private int wallCreationCounter = 0;
    private Random r = new Random();

    private ArrayList<Wall> walls = new ArrayList<>();
    private ArrayList<Item> items = new ArrayList<>();

    private int height;
    private int width;

    private int speed = 5;
    private int itemCreationDlay = 150;

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
        if (wallCreationCounter > itemCreationDlay)
        {
            walls.add(new Wall(width, height));
            wallCreationCounter = 0;

            items.add(new Item(c.getWidth()+r.nextInt(400), r.nextInt(c.getHeight())));
            items.add(new Item(c.getWidth()+r.nextInt(200), r.nextInt(c.getHeight())));

            speed++;
            itemCreationDlay -= 5;
            if (itemCreationDlay < 20)
                itemCreationDlay = 20;
        }

        // draw walls
        for (int i =0;i<walls.size();i++)
        {
            walls.get(i).draw(c, width, height, speed);
            if (walls.get(i).isOffScreen())
                walls.remove(i);
        }

        boolean collision = character.draw(c, paint, height, width, walls);

        // draw itmes
        for (int i =0;i<items.size();i++)
        {
            items.get(i).draw(c, speed);

           if (items.get(i).collect(character))
           {
               items.remove(i);
               for (int ii=0;ii<walls.size();ii++)
                   walls.get(ii).resetGap();
           }


            // TODO - remove items that move offscreen
            //if (items.get(i).isOffScreen())
            //    walls.remove(i);
        }

        joystick.draw(c, paint, height);





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
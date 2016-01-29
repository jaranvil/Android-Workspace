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

public class GameView extends View {
    private Context mContext;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Animation animation;
    private Joystick joystick;

    private int height;
    private int width;

    public GameView(Context context, AttributeSet attrs)  {
        super(context, attrs);
        mContext = context;
        joystick = new Joystick();
    }

    protected void onDraw(Canvas c) {
        height = this.getHeight();
        width = this.getWidth();

        // TODO - remove color parsing from onDraw method

        c.drawColor(Color.parseColor("#800000"));

        joystick.draw(c, paint, height, width);
    }

    public boolean onTouchEvent(MotionEvent event) {

        int eventAction = event.getAction();

        int x = (int)event.getX();
        int y = (int)event.getY();

        if (eventAction == MotionEvent.ACTION_DOWN) {



        }
        return true;
    }


}
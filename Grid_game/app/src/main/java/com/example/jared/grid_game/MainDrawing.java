package com.example.jared.grid_game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Random;

public class MainDrawing extends ImageView{

    private Context mContext;
    private Handler h;
    private final int FRAME_RATE = 30;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private boolean loading = true;

    Board board;

    public MainDrawing(Context context, AttributeSet attrs)  {
        super(context, attrs);
        mContext = context;
        h = new Handler();
    }

    private Runnable r = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };

    protected void onDraw(Canvas c) {

        if (loading) {
            board = new Board(this.getWidth(), this.getHeight());
            loading = false;
        }


        c.drawColor(Color.parseColor("#555555"));

        board.draw(c, paint);



        h.postDelayed(r, FRAME_RATE);
    }


    public boolean onTouchEvent(MotionEvent event) {

        int eventAction = event.getAction();

        int x = (int)event.getX();
        int y = (int)event.getY();


        return true;
    }

}
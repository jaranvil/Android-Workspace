package com.jaredeverett.jared.breakout;

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

public class AnimatedView extends ImageView{
    private Context mContext;
    private Handler h;
    private final int FRAME_RATE = 30;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    // objest lists
    private ArrayList<Ball> balls = new ArrayList<>();
    private ArrayList<Line> lines = new ArrayList<>();
    private ArrayList<ScoreTiles> tiles = new ArrayList<>();

    private int[] colors = {Color.parseColor("#00B800"), Color.parseColor("#CC0000"), Color.parseColor("#8F24B2"), Color.parseColor("#FF9900"), Color.parseColor("#0000FF"), Color.parseColor("#FFFF00")};
    private int levelColor;
    private int level = 1;
    private int lvlScore = 0;

    Bitmap gold_star = BitmapFactory.decodeResource(getResources(), R.drawable.gold_star);
    Bitmap gray_star = BitmapFactory.decodeResource(getResources(), R.drawable.gray_star);

    private boolean lvlScreen = true;
    private boolean lvlComplete = false;
    private boolean gameOver = false;

    public AnimatedView(Context context, AttributeSet attrs)  {
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
        c.drawColor(Color.parseColor("#555555"));

        if (lvlScreen) {
            drawLvlScreen(c, paint);
        } else {
            drawStats(c, paint);

            // draw balls
            paint.setColor(levelColor);
            paint.setStyle(Paint.Style.FILL);
            for (int i = 0;i < balls.size();i++) {
                balls.get(i).draw(c, paint, lines, this.getWidth(), this.getHeight());
            }

            // draw lines
            paint.setColor(Color.parseColor("#ffffff"));
            for (int i = 0;i < lines.size();i++) {
                lines.get(i).draw(c, paint);
            }

            // draw score tiles
            for (int i = 0;i < tiles.size();i++) {
                paint.setColor(levelColor);
                tiles.get(i).draw(c, paint);
            }

            if (checkGameOver()) {
                drawGameOver(c, paint);
            }

            if (checkForWin()) {
                lvlComplete = true;
                drawLvlComplete(c, paint);
            }
        }



       // c.drawColor(Color.BLACK);
        h.postDelayed(r, FRAME_RATE);
    }

    public void drawLvlScreen(Canvas c, Paint p) {
        p.setColor(Color.parseColor("#FFFFFF"));
        p.setStyle(Paint.Style.STROKE);
        p.setTextSize(setTextSize("Level " + level, (this.getWidth()) - 200, p));
        c.drawText("Level " + level, 100, 500, paint);

        p.setTextSize(75);
        p.setColor(Color.parseColor("#FFFFFF"));
        p.setStyle(Paint.Style.STROKE);
        p.setTextSize(setTextSize("tap to start", (this.getWidth()) - 400, p));

        c.drawText("tap to start", 200, 750, paint);
    }

    public void drawLvlComplete(Canvas c, Paint p) {
        paint.setColor(Color.parseColor("#000000"));
        paint.setAlpha(95);
        float left = 0;
        float top = 0;
        float right = this.getWidth();
        float bottom = this.getHeight()-150;
        c.drawRect(left, top, right, bottom, paint);

        paint.setTextSize(setTextSize("Level " + level + " Complate", (this.getWidth()) - 300, paint));
        paint.setColor(Color.parseColor("#FFFFFF"));
        c.drawText("Level " + level + " Complete", 150, 500, paint);

        int totalPossibleScore = (this.getWidth() * this.getHeight())/1000;
        int percent = ((lvlScore*100)/totalPossibleScore);

        if (percent < 33) {
            c.drawBitmap(gold_star, 250, 550, paint);
            c.drawBitmap(gray_star, 450, 550, paint);
            c.drawBitmap(gray_star, 650, 550, paint);
        } else if (percent < 66) {
            c.drawBitmap(gold_star, 250, 550, paint);
            c.drawBitmap(gold_star, 450, 550, paint);
            c.drawBitmap(gray_star, 650, 550, paint);
        } else {
            c.drawBitmap(gold_star, 250, 550, paint);
            c.drawBitmap(gold_star, 450, 550, paint);
            c.drawBitmap(gold_star, 650, 550, paint);
        }
    }

    public void drawGameOver(Canvas c, Paint p) {
        c.drawText("game over", 100, 100, p);
    }

    public float setTextSize(String text, int width, Paint p) {
        int desiredSize = width;
        float testTextSize = 48f;
        p.setTextSize(testTextSize);
        Rect bounds = new Rect();
        p.getTextBounds(text, 0, text.length(), bounds);
        float desiredTextSize;
        desiredTextSize = testTextSize * desiredSize / bounds.width();
        return desiredTextSize;
    }

    public void drawStats(Canvas c, Paint p) {
        p.setColor(Color.parseColor("#111111"));
        c.drawRect(0, this.getHeight() - 150, this.getWidth(), this.getHeight(), p);

        lvlScore = 0;
        for (int i = 0; i<tiles.size();i++) {
            lvlScore = lvlScore + tiles.get(i).score;
        }

        p.setColor(Color.parseColor("#FFFFFF"));
        p.setTextSize(75);
        c.drawText("Level: " + level, 150, this.getHeight() - 70, p);
        c.drawText("Score: "+lvlScore, 600, this.getHeight()-70, p);
    }

    public void startLvl() {
        balls.clear();
        lines.clear();
        tiles.clear();

        Random rnd = new Random();
        levelColor = colors[rnd.nextInt(colors.length)];

        for (int i = 0;i < (level*2);i++) {
            balls.add(new Ball(this.getWidth(), this.getHeight(), level));
        }
    }

    public boolean onTouchEvent(MotionEvent event) {

        int eventAction = event.getAction();

        int x = (int)event.getX();
        int y = (int)event.getY();

        if (eventAction == MotionEvent.ACTION_DOWN) {

            if (lvlScreen) {
                lvlScreen = false;
                startLvl();
            } else if (lvlComplete) {
                lvlComplete = false;
                //lvlScreen = true;
                level++;
                startLvl();
            } else {
                createLine(x, y);
            }

        }
        return true;
    }

    public boolean checkForWin() {
        for (int i = 0;i<balls.size();i++) {
            if (!balls.get(i).alone) {
                return false;
            }
        }
        return true;
    }

    public boolean checkGameOver() {
        for (int i = 0;i<balls.size();i++) {
            int width = balls.get(i).rightBound - balls.get(i).leftBound;
            int height = balls.get(i).bottomBound - balls.get(i).topBound;

            if (width < 100 || height < 100) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public void createLine(int x, int y) {
        // find boundaries around touch
            boolean breakFlag = false;
            int rightBound = 0;
            int leftBound = 0;
            int topBound = 0;
            int bottomBound = 0;

            // right boundary
            rightBound = this.getWidth(); // absolute right boundary
            for (int i = x; i < this.getWidth();i++) {
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
            bottomBound = this.getHeight()-150; // absolute right boundary
            for (int i = y; i < this.getHeight();i++) {
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

        // determine line location
        int cellW = rightBound - leftBound;
        int cellH = bottomBound - topBound;
        boolean vertical;

        if (cellW > cellH)
            vertical = true;
        else
            vertical = false;

        int x1;
        int x2;
        int y1;
        int y2;

        if (vertical) {
            //x1 = leftBound + (cellW/2);
            x1 = x;
            x2 = x1;
            y1 = topBound;
            y2 = bottomBound;
        } else {
            x1 = leftBound;
            x2 = rightBound;
            //y1 = topBound + (cellH/2);
            y1 = y;
            y2 = y1;
        }

        lines.add(new Line(x1, x2, y1, y2));

        for (int i = 0;i < balls.size();i++) {
            // reset balls bondaries
            balls.get(i).getBoundaries(lines, this.getWidth(), this.getHeight());

            // check if balls are alone
            int r1 = balls.get(i).rightBound;
            int l1 = balls.get(i).leftBound;
            int t1 = balls.get(i).topBound;
            int b1 = balls.get(i).bottomBound;

            int counter = 0;
            for (int ii = 0;ii < balls.size();ii++) {
                int xPos = balls.get(ii).getX();
                int yPos = balls.get(ii).getY();

                if (yPos < b1 && yPos > t1 && xPos > l1 && xPos < r1) {
                    counter++;
                }
            }
            if (counter == 1) {
                if (!balls.get(i).alone) {
                    balls.get(i).alone = true;
                    tiles.add(new ScoreTiles(t1, b1, r1, l1));


                }

            }
        }

    }
}
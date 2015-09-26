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

//                                          green                           red                         purple                      orange                      blue                            yellow
    private int[] colors = {Color.parseColor("#00B800"), Color.parseColor("#FF4D4D"), Color.parseColor("#B166C9"), Color.parseColor("#FF9900"), Color.parseColor("#00CCFF"), Color.parseColor("#FFFF00")};
    private int[] avaliableColors = colors;
    private int levelColor;
    private int level = 1;
    private int lvlScore = 0;
    private int totalScre = 0;
    private int moves = 0;

    Bitmap gold_star = BitmapFactory.decodeResource(getResources(), R.drawable.gold_star);
    Bitmap gray_star = BitmapFactory.decodeResource(getResources(), R.drawable.gray_star);
    Bitmap title = BitmapFactory.decodeResource(getResources(), R.drawable.title);


    private boolean lvlScreen = true;
    private boolean lvlComplete = false;
    private boolean gameOver = false;
    private boolean newGame = true;
    private boolean loading = true;

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


        if (loading) {
            loading = false;
            setupHomescreen();
        }

        if (lvlScreen) {
            drawLvlScreen(c, paint);
        } else {
            c.drawColor(Color.parseColor("#555555"));
            drawStats(c, paint);

            paint.setColor(levelColor);
            //paint.setTextSize(setTextSize(Integer.toString(moves), (this.getWidth()) - 500, paint));
            paint.setTextSize(getResources().getDimensionPixelSize(R.dimen.movesTextSize));
            paint.setAlpha(50);
            if (moves > 9) {
                c.drawText(Integer.toString(moves), 10, this.getHeight()-100, paint);
            } else {
                c.drawText(Integer.toString(moves), this.getWidth()/4, this.getHeight()-100, paint);
            }


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



            if (checkForWin()) {
                lvlComplete = true;
                drawLvlComplete(c, paint);
            }

            if (moves <= 0 && !lvlComplete) {
                drawGameOver(c, paint);
                gameOver = true;
            }
        }



       // c.drawColor(Color.BLACK);
        h.postDelayed(r, FRAME_RATE);
    }

    public void drawLvlScreen(Canvas c, Paint p) {
        c.drawColor(Color.parseColor("#000000"));

        // draw balls
        paint.setColor(levelColor);
        paint.setStyle(Paint.Style.FILL);
        for (int i = 0;i < balls.size();i++) {
            balls.get(i).draw(c, paint, lines, this.getWidth(), this.getHeight());
        }

        p.setColor(Color.parseColor("#FFFFFF"));
        p.setStyle(Paint.Style.STROKE);

        c.drawBitmap(title, 0, 100, paint);

        p.setColor(Color.parseColor("#ffffff"));
        p.setStyle(Paint.Style.STROKE);

        int y = this.getHeight()/2;

        paint.setTextSize(getResources().getDimensionPixelSize(R.dimen.instructionsTextSize));
        c.drawText("Separate balls by dividing cells.", 0, y, paint);
        c.drawText("There are a limited number of ", 0, y+50, paint);
        c.drawText("allowed moves per level. ", 0, y+100, paint);

        p.setTextSize(setTextSize("tap to start", (this.getWidth()) - 500, p));
        c.drawText("tap to start", 10, this.getHeight()-150, paint);
    }

    public void drawLvlComplete(Canvas c, Paint p) {
        grayOutScreen(c, p);

       // paint.setTextSize(setTextSize("Level " + level + " Complate", (this.getWidth()) - 300, paint));grayOutScreen(c, p);
        paint.setTextSize(getResources().getDimensionPixelSize(R.dimen.lvlTextSize));
        paint.setColor(Color.parseColor("#FFFFFF"));
        c.drawText("Level " + level, 10, this.getHeight() / 3, paint);
        paint.setTextSize(getResources().getDimensionPixelSize(R.dimen.lvlCompleteTextSize));
        c.drawText("Complete!", 10, (this.getHeight() / 3)+100, paint);

        int totalPossibleScore = (this.getWidth() * this.getHeight())/1000;
        int percent = ((lvlScore*100)/totalPossibleScore);

        int cell = this.getWidth() / 5;
        int starY = (this.getHeight()/2) + 50;

        if (percent < 50) {
            c.drawBitmap(gold_star, 10, starY, paint);
            c.drawBitmap(gray_star, 10 + cell, starY, paint);
            c.drawBitmap(gray_star, 10 + (cell*2), starY, paint);
        } else if (percent < 80) {
            c.drawBitmap(gold_star, 10, starY, paint);
            c.drawBitmap(gold_star, 10+cell, starY, paint);
            c.drawBitmap(gray_star, 10+(cell*2), starY, paint);
        } else {
            c.drawBitmap(gold_star, 10, starY, paint);
            c.drawBitmap(gold_star, 10+cell, starY, paint);
            c.drawBitmap(gold_star, 10 + (cell*2), starY, paint);
        }
    }

    public void drawGameOver(Canvas c, Paint p) {
        grayOutScreen(c, p);
        int totalPossibleScore = ((this.getWidth() * this.getHeight())/1000) * level;
        int percent = ((totalScre*100)/totalPossibleScore);

        //paint.setTextSize(setTextSize("No More Moves", (this.getWidth()) - 300, paint));
        paint.setColor(Color.parseColor("#FFFFFF"));
        paint.setTextSize(getResources().getDimensionPixelSize(R.dimen.lvlCompleteTextSize));
        c.drawText("No More Moves :(", 10, this.getHeight()/3, p);
        paint.setTextSize(getResources().getDimensionPixelSize(R.dimen.finalScoreTextSize));
        c.drawText("Final Score: " + totalScre + " (" + percent + "%)", 10, (this.getHeight()/3)+100, p);


//        int starsY = 675;
//        if (percent < 50) {
//            c.drawBitmap(gold_star, 250, starsY, paint);
//            c.drawBitmap(gray_star, 450, starsY, paint);
//            c.drawBitmap(gray_star, 650, starsY, paint);
//        } else if (percent < 80) {
//            c.drawBitmap(gold_star, 250, starsY, paint);
//            c.drawBitmap(gold_star, 450, starsY, paint);
//            c.drawBitmap(gray_star, 650, starsY, paint);
//        } else {
//            c.drawBitmap(gold_star, 250, starsY, paint);
//            c.drawBitmap(gold_star, 450, starsY, paint);
//            c.drawBitmap(gold_star, 650, starsY, paint);
//        }


        // stop moving balls
        for (int i = 0; i < balls.size();i++) {
            balls.get(i).SPEED = 0;
        }
    }

    public void setupHomescreen() {
        Random rnd = new Random();
        balls.clear();
        for (int i = 0;i < 30;i++) {
            balls.add(new Ball(this.getWidth(), this.getHeight(), level, colors[rnd.nextInt(colors.length)]));
        }
    }

    public float setTextSize(String text, int width, Paint p) {

        // choose a font size to make string "text" as wide as "width"

        if (text == "1")
            text = "0";

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
        int yVal = 100;
        if (this.getWidth() < 500) {
            yVal = 50;
            c.drawRect(0, 0, this.getWidth(), 75, p);
        } else
            c.drawRect(0, 0, this.getWidth(), 150, p);

        p.setColor(levelColor);
        int possibleScore = ((this.getWidth() * this.getHeight()) / 1000) - ((this.getWidth()*150)/1000);
        int percent = ((lvlScore * 100) / possibleScore);
        //paint.setTextSize(setTextSize("Level score " + lvlScore + " (" + percent + "%)", (this.getWidth()) - 300, p));
        paint.setTextSize(getResources().getDimensionPixelSize(R.dimen.statsTextSize));
        c.drawText("Level score " + lvlScore + " (" + percent + "%)", 25, yVal, p);
    }

    public void grayOutScreen(Canvas c, Paint p) {
        paint.setColor(Color.parseColor("#000000"));
        paint.setAlpha(95);
        float left = 0;
        float top = 150;
        float right = this.getWidth();
        float bottom = this.getHeight();
        if (this.getWidth() < 500)
            top = 75;

        c.drawRect(left, top, right, bottom, paint);
    }

    public void startLvl() {
        Random rnd = new Random();
        balls.clear();
        lines.clear();
        tiles.clear();

        if (newGame) {
            newGame = false;
            level = 1;
        }

        levelColor = colors[rnd.nextInt(colors.length)];
        moves = 1 + level*2;

        totalScre += lvlScore;
        lvlScore = 0;

        for (int i = 0;i < 1+(level*2);i++) {
            balls.add(new Ball(this.getWidth(), this.getHeight(), level, levelColor));
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
            } else if (gameOver) {
                gameOver = false;
                lvlScreen = true;
                newGame = true;
                setupHomescreen();
            } else {
                moves--;
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
            if (this.getWidth() < 500)
                topBound = 75;
            else
                topBound = 150;
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
            bottomBound = this.getHeight(); // absolute right boundary
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
                    lvlScore += tiles.get(tiles.size()-1).score;

                }

            }
        }

    }
}
package com.jaredeverett.jared.breakout;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.jaredeverett.jared.breakout.R;

public class GameActivity extends AppCompatActivity {

    private Handler h;
    protected SharedPreferences prefs;
    private final int FRAME_RATE = 30;


    private AnimatedView gameCanvas;
    private TextView tvStats;
    private TextView tvMoves;

    private int score = 0;
    private int newScore = 0;
    private int oldScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        h = new Handler();
        gameCanvas = (AnimatedView) findViewById(R.id.canvas);
        tvStats = (TextView) findViewById(R.id.tvStats);
        tvMoves = (TextView) findViewById(R.id.tvMoves);

        prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);

        h.postAtTime(r, SystemClock.uptimeMillis() + 400);
    }

    private Runnable r = new Runnable() {
        @Override
        public void run() {
            // give canvas some info
            gameCanvas.width = gameCanvas.getWidth();
            gameCanvas.height = gameCanvas.getHeight();
            gameCanvas.tvMoves = tvMoves;

            // draw
            gameCanvas.invalidate();

            if (gameCanvas.score > score)
                increaseScore();

            score = gameCanvas.score;

            if (gameCanvas.gameOver) {
                h.removeCallbacks(r);
                gameOver();
            } else {
                h.postDelayed(r, FRAME_RATE);
            }
        }
    };

    public void gameOver()
    {
        int count = prefs.getInt("count", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(Integer.toString(count+1), score);
        editor.putInt("count", count+1);
        editor.apply();

        finish();
    }

    public void increaseScore()
    {
        oldScore = score;
        newScore = gameCanvas.score;

        new Thread(new Runnable() {

            public void run() {
                while (oldScore < newScore) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    tvStats.post(new Runnable() {
                        public void run() {
                            tvStats.setText(oldScore + " pixels");
                            tvStats.setTextColor(Color.parseColor("#33cc00"));
                            //tvStats.setAlpha(0.5f);
                        }
                    });
                    oldScore++;
                }
                tvStats.post(new Runnable() {
                    public void run() {
                        tvStats.setTextColor(Color.parseColor("#ff9900"));
                        //tvStats.setAlpha(1f);
                    }
                });
            }
        }).start();
    }

    public void updateGameStats()
    {
//        int possibleScore = ((gameCanvas.width * gameCanvas.height) / 1000) - ((gameCanvas.width*150)/1000);
//        int percent = ((gameCanvas.lvlScore * 100) / possibleScore);
//        if (percent == 99)
//            percent = 100;

        int totalPossibleScore = ((gameCanvas.getWidth() * gameCanvas.getHeight())/1000) * gameCanvas.level;
        int score = ((gameCanvas.score * 100) / totalPossibleScore);

        tvStats.setText(gameCanvas.score + " pixels");
        // tvStats.setText("level: " + gameCanvas.level + " Score: " + percent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

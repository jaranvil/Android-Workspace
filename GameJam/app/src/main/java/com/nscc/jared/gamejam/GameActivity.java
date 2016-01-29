package com.nscc.jared.gamejam;

import android.app.Activity;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Handler;

public class GameActivity extends Activity {
    private final int FRAME_RATE = 30;
    private GameView canvas;
    private Handler h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        canvas = (GameView) findViewById(R.id.canvas);

        h = new Handler();
        h.postAtTime(r, SystemClock.uptimeMillis() + 400);
    }

    private Runnable r = new Runnable() {
        @Override
        public void run() {


            // draw
            canvas.invalidate();

            h.postDelayed(r, FRAME_RATE);

//            if (gameCanvas.gameOver) {
//                h.removeCallbacks(r);
//                gameOver();
//            } else {
//                h.postDelayed(r, FRAME_RATE);
//            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
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

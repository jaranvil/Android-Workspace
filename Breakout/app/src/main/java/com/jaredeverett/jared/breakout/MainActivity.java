package com.jaredeverett.jared.breakout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.jaredeverett.jared.breakout.R;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    protected SharedPreferences prefs;
    private Button btnStartGame;
    private Button btnClear;
    private ListView lvHighScores;

    private ArrayList<Integer> scores = new ArrayList<>();
    private int lastScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStartGame = (Button) findViewById(R.id.btnStartGame);
        //btnClear = (Button) findViewById(R.id.btnClear);
        lvHighScores = (ListView) findViewById(R.id.lvHighScores);

        prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);

        btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent("GameActivity");
                //Bundle extras = new Bundle();   //create bundle object
                //extras.putInt("id", videos.get(position).id);
                //i.putExtras(extras);
                startActivity(i);
            }
        });

//        btnClear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SharedPreferences.Editor editor = prefs.edit();
//                editor.putInt("count", 0);
//                //editor.putInt("count", count + 1);
//                editor.apply();
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getHighScores();
        setupScoreList();
    }

    public void getHighScores()
    {
        scores.clear();
        int count = prefs.getInt("count", -1) + 1;
        if (count != 0)
        {
            for (int i = 1;i < count;i++)
            {
                scores.add(prefs.getInt(Integer.toString(i), 0));
            }

            lastScore = scores.get(count-2);
            Collections.sort(scores, Collections.reverseOrder());
       }
    }

    public void setupScoreList()
    {
        // the list adapater needs a array the size of the list
        // no idea why
        String[] test;
        if (scores.size() < 11)
            test = new String[scores.size()];
        else
            test = new String[11];

        HighScoreListAdapter adapter = new HighScoreListAdapter(MainActivity.this, scores, test, lastScore);
        lvHighScores.setAdapter(adapter);
        lvHighScores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
//                //Toast.makeText(MainActivity.this, "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();
//                Intent i = new Intent("VideoActivity");
//                Bundle extras = new Bundle();   //create bundle object
//                extras.putInt("id", videos.get(position).id);
//                i.putExtras(extras);
//                startActivity(i);
            }
        });
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

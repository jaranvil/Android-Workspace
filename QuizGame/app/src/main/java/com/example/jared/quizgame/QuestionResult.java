package com.example.jared.quizgame;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class QuestionResult extends Activity {
    TextView tvResult;
    TextView tvAnswer;
    Button btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_result);

        tvResult = (TextView) findViewById(R.id.tvResult);
        tvAnswer = (TextView) findViewById(R.id.tvAnswer);
        btnHome = (Button) findViewById(R.id.btnHome);

        Bundle extras=getIntent().getExtras();
        int score = 0;
        int questions = 0;
        if(extras != null)//if bundle has content
        {
            score = extras.getInt("SCORE");
            questions = extras.getInt("QUESTIONS");
            tvResult.setText(score + " of " + questions);
                tvAnswer.setText("");
        }

        btnHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivityForResult(new Intent("MainActivity"), 1);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_question_result, menu);
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

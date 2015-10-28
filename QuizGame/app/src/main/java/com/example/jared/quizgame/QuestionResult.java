package com.example.jared.quizgame;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Text;

public class QuestionResult extends Activity {
    TextView tvResult;
    TextView tvAnswer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_result);

        tvResult = (TextView) findViewById(R.id.tvResult);
        tvAnswer = (TextView) findViewById(R.id.tvAnswer);

        Bundle extras=getIntent().getExtras();
        String name = "";
        String answer = "";
        if(extras != null)//if bundle has content
        {
            name = extras.getString("RESULT");
            answer = extras.getString("ANSWER");
            tvResult.setText(name);
            if (name.equals("Wrong"))
                tvAnswer.setText("The answer was: " + answer);
        }

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

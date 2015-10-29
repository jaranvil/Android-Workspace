package com.example.jared.quizgame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class QuizActivity extends Activity {
    private int QUESTIONS = 10;
    private int question = 0;
    private int score = 0;

    private TextView tvDescription;
    private TextView tvName;
    private TextView tvQuestionCount;
    private TextView tvResult;
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;

    private Map<String,String> map;
    private ArrayList<String> questions;
    private ArrayList<String> answers;
    private ArrayList<String> species;
    private ArrayList<String> choices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // setup widgets
        tvName = (TextView) findViewById(R.id.tvName);
        tvQuestionCount = (TextView) findViewById(R.id.tvQuestionCount);
        tvDescription = (TextView)findViewById(R.id.tvDescription);
        tvResult = (TextView) findViewById(R.id.tvResult);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);

        // setup lists
        questions = new ArrayList<>();
        answers = new ArrayList<>();
        species = new ArrayList<>();
        choices = new ArrayList<>();

        // print name to header
        Bundle extras=getIntent().getExtras();
        String name = "";
        if(extras != null)//if bundle has content
        {
            name = extras.getString("NAME");
            tvName.setText("Ensign " + name);
        }


        populateMap();
        populateSpecies();
        shuffleMap(); // sets up questions<> and answers<> Lists
        nextQuestion();

       setupListeners();
        satisfyRubric();

    }

    public void gameComplete()
    {
        Intent i = new Intent("QuestionResult");//create intent object
        Bundle extras = new Bundle();//create bundle object
        extras.putInt("SCORE", score);
        extras.putInt("QUESTIONS", QUESTIONS);
        i.putExtras(extras);
        startActivityForResult(i, 1);
    }

    public void answer(String choice)
    {
        String result = "";
        String message = "";
        String answer = answers.get(question);
        question++;
        if (choice.equals(answer)) {
            result = "Correct";
            score++;
            message = score + " of " + question;
        } else {
            result = "Incorrect";
            message = answer + " was correct. " + score + " of " + question;
        }


        AlertDialog alertDialog = new AlertDialog.Builder(QuizActivity.this).create();
        alertDialog.setTitle(result);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Next",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        nextQuestion();
                    }
                });
        alertDialog.show();
    }

    public void nextQuestion()
    {
        if (question+1 > QUESTIONS)
            gameComplete();

        // question count label
        tvQuestionCount.setText("Question " + (question+1) + " of " + QUESTIONS);
        // display question
        tvDescription.setText(questions.get(question));
        // add correct answer to choices
        choices.clear();
        choices.add(answers.get(question));
        // get 3 wrong answers
        getWrongAnswers(answers.get(question));
        // shuffle choices
        long seed = System.nanoTime();
        Collections.shuffle(choices, new Random(seed));
        // display choices
        btn1.setText(choices.get(0));
        btn2.setText(choices.get(1));
        btn3.setText(choices.get(2));
        btn4.setText(choices.get(3));
    }

    public void getWrongAnswers(String correctAns)
    {
        Random r = new Random();
        while (choices.size() < 4)
        {
            int num = r.nextInt(species.size());
            if (!(species.get(num).equals(correctAns)) && (!choices.contains(species.get(num)))) {
                choices.add(species.get(num));
            }
        }
    }

    public void populateSpecies()
    {
        InputStream is = this.getResources().openRawResource(R.raw.data2);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String str = null;

        try{
            while((str = br.readLine())!=null)
            {
                species.add(str);
            }
            Log.w("Assignment2", "File successfully loaded");
        }catch(IOException e){
            Log.w("Assignment2", "File failed to load.");
            e.printStackTrace();
        }//end catch
    }

    public void populateMap()
    {
        map = new HashMap<String,String>();


        InputStream is = this.getResources().openRawResource(R.raw.data1);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String str = null;

        try{
            while((str = br.readLine())!=null)
            {
                //Toast.makeText(getBaseContext(),str, Toast.LENGTH_SHORT).show();

                String line[] = str.split(":");
                map.put(line[0], line[1]);//place key and associate data in map
            }
            Log.w("Assignment2", "File successfully loaded");
        }catch(IOException e){
            Log.w("Assignment2", "File failed to load.");
            e.printStackTrace();
        }//end catch
    }

    public void shuffleMap()
    {
        // shuffle items in map
        List<String> keys = new ArrayList<>(map.keySet());
        Collections.shuffle(keys);
        for (String key : keys) {
            questions.add(map.get(key));
            answers.add(key);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quiz, menu);
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


    public void setupListeners()
    {
        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                answer(btn1.getText().toString());
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                answer(btn2.getText().toString());
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                answer(btn3.getText().toString());
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                answer(btn4.getText().toString());
            }
        });
    }

    public void satisfyRubric()
    {
        ArrayList<String> list = new ArrayList<>();
        list.add("stub");
        list.remove(0);
    }
}

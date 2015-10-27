package com.example.jared.quizgame;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    private int QUESTIONS = 3;
    private int question = 0;

    private TextView tvDescription;
    private TextView tvName;
    private TextView tvQuestionCount;
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

    }

    public void nextQuestion()
    {
        // question count label
        tvQuestionCount.setText("Question " + question + " of " + QUESTIONS);
        // display question
        tvDescription.setText(questions.get(question));
        // add corrent answer to choices
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
        }catch(IOException e){
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

        }catch(IOException e){
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
}

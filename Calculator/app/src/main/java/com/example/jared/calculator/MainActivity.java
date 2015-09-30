package com.example.jared.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.view.View.*;
import android.widget.*;

public class MainActivity extends AppCompatActivity {

    // control variables
    Button btn0;
    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;
    Button btn5;
    Button btn6;
    Button btn7;
    Button btn8;
    Button btn9;
    Button btnClear;
    Button btnSign;
    Button btnBackspace;
    Button btnPlus;
    Button btnMinus;
    Button btnMultiply;
    Button btnDivide;
    Button btnEquals;
    Button btnDecimal;
    TextView tvOutput;
    TextView tvHistory;


    Calculator bo = new Calculator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // cast variables to xml elements
        btn0 = (Button) findViewById(R.id.btn0);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        btn5 = (Button) findViewById(R.id.btn5);
        btn6 = (Button) findViewById(R.id.btn6);
        btn7 = (Button) findViewById(R.id.btn7);
        btn8 = (Button) findViewById(R.id.btn8);
        btn9 = (Button) findViewById(R.id.btn9);
        btnClear = (Button) findViewById(R.id.btnClear);
        btnSign = (Button) findViewById(R.id.btnSign);
        btnBackspace = (Button) findViewById(R.id.btnBackspace);
        btnPlus = (Button) findViewById(R.id.btnPlus);
        btnMinus = (Button) findViewById(R.id.btnMinus);
        btnMultiply = (Button) findViewById(R.id.btnMultiply);
        btnDivide = (Button) findViewById(R.id.btnDivide);
        btnEquals = (Button) findViewById(R.id.btnEquals);
        btnDecimal = (Button) findViewById(R.id.btnDecimal);
        tvOutput = (TextView) findViewById(R.id.tvOutput);
        tvHistory = (TextView) findViewById(R.id.tvHistory);

        tvOutput.setText("0");

        // button press listeners

        OnClickListener click0 = new OnClickListener() {
            @Override
            public void onClick(View v) {
                keyPress("0");
            }
        };
        btn0.setOnClickListener(click0);

        OnClickListener click1 = new OnClickListener() {
            @Override
            public void onClick(View v) {
                keyPress("1");
            }
        };
        btn1.setOnClickListener(click1);

        OnClickListener click2 = new OnClickListener() {
            @Override
            public void onClick(View v) {
                keyPress("2");
            }
        };
        btn2.setOnClickListener(click2);

        OnClickListener click3 = new OnClickListener() {
            @Override
            public void onClick(View v) {
                keyPress("3");
            }
        };
        btn3.setOnClickListener(click3);

        OnClickListener click4 = new OnClickListener() {
            @Override
            public void onClick(View v) {
                keyPress("4");
            }
        };
        btn4.setOnClickListener(click4);

        OnClickListener click5 = new OnClickListener() {
            @Override
            public void onClick(View v) {
                keyPress("5");
            }
        };
        btn5.setOnClickListener(click5);

        OnClickListener click6 = new OnClickListener() {
            @Override
            public void onClick(View v) {
                keyPress("6");
            }
        };
        btn6.setOnClickListener(click6);

        OnClickListener click7 = new OnClickListener() {
            @Override
            public void onClick(View v) {
                keyPress("7");
            }
        };
        btn7.setOnClickListener(click7);

        OnClickListener click8 = new OnClickListener() {
            @Override
            public void onClick(View v) {
                keyPress("8");
            }
        };
        btn8.setOnClickListener(click8);

        OnClickListener click9 = new OnClickListener() {
            @Override
            public void onClick(View v) {
                keyPress("9");
            }
        };
        btn9.setOnClickListener(click9);

        OnClickListener clickClear = new OnClickListener() {
            @Override
            public void onClick(View v) {
                keyPress("clear");
            }
        };
        btnClear.setOnClickListener(clickClear);

        OnClickListener clickSign = new OnClickListener() {
            @Override
            public void onClick(View v) {
                keyPress("sign");
            }
        };
        btnSign.setOnClickListener(clickSign);

        OnClickListener clickBackspace = new OnClickListener() {
            @Override
            public void onClick(View v) {
                keyPress("backspace");
            }
        };
        btnBackspace.setOnClickListener(clickBackspace);

        OnClickListener clickDecimal = new OnClickListener() {
            @Override
            public void onClick(View v) {
                keyPress(".");
            }
        };
        btnDecimal.setOnClickListener(clickDecimal);

        OnClickListener clickPlus = new OnClickListener() {
            @Override
            public void onClick(View v) {
                keyPress("plus");
            }
        };
        btnPlus.setOnClickListener(clickPlus);

        OnClickListener clickMinus = new OnClickListener() {
            @Override
            public void onClick(View v) {
                keyPress("minus");
            }
        };
        btnMinus.setOnClickListener(clickMinus);

        OnClickListener clickMultiply = new OnClickListener() {
            @Override
            public void onClick(View v) {
                keyPress("multiply");
            }
        };
        btnMultiply.setOnClickListener(clickMultiply);

        OnClickListener clickDivide = new OnClickListener() {
            @Override
            public void onClick(View v) {
                keyPress("divide");
            }
        };
        btnDivide.setOnClickListener(clickDivide);

        OnClickListener clickEquals = new OnClickListener() {
            @Override
            public void onClick(View v) {
                keyPress("equals");
            }
        };
        btnEquals.setOnClickListener(clickEquals);
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

    public void keyPress(String val) {
        tvOutput.setText(bo.keyPress(val));
        tvHistory.setText(bo.history);
    }
}

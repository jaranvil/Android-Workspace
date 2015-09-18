package com.example.w0269431.tipcalc;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;


public class MainActivity extends Activity {

    private EditText billEditText;
    private TextView customPercent;
    private EditText tip10EditText;
    private EditText tip15EditText;
    private EditText tip20EditText;
    private EditText total10EditText;
    private EditText total15EditText;
    private EditText total20EditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        billEditText = (EditText) findViewById(R.id.billEditText);
        tip10EditText = (EditText) findViewById(R.id.tip10EditText);
        tip15EditText = (EditText) findViewById(R.id.tip15EditText);
        tip20EditText = (EditText) findViewById(R.id.tip20EditText);
        total10EditText = (EditText) findViewById(R.id.total10EditText);
        total15EditText = (EditText) findViewById(R.id.total15EditText);
        total20EditText = (EditText) findViewById(R.id.total20EditText);

        // Enter Bill Text
        billEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                editBillTotal();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        // Custom percent label

        customPercent = (TextView) findViewById(R.id.customPercent);

        // Seeker BAr

        final SeekBar sk = (SeekBar) findViewById(R.id.customSeekBar);
        sk.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                customPercent.setText(progress + "%");
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

    public void editBillTotal() {
        if (!(billEditText.getText().toString() == " ")) {
            double bill = Double.parseDouble(billEditText.getText().toString());
            double tip10 = bill * 0.1;
            double tip15 = bill * 0.15;
            double tip20 = bill * 0.2;
            double total10 = bill + tip10;
            double total15 = bill + tip15;
            double total20 = bill + tip20;

            tip10EditText.setText(Double.toString(Math.round(tip10*100)/100));
            tip15EditText.setText(Double.toString(Math.round(tip15*100)/100));
            tip20EditText.setText(Double.toString(Math.round(tip20*100)/100));
            total10EditText.setText(Double.toString(Math.round(total10)));
            total15EditText.setText(Double.toString(Math.round(total15)));
            total20EditText.setText(Double.toString(Math.round(total20)));
        }

    }
}

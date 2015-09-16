package com.example.w0068332.myapplicationone;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.*;
import android.view.View.*;
import android.widget.*;



public class MainActivity extends Activity {
    //Step #1 make objects for controls
    Button btnDemo;
    TextView tvMyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    //Step #2 connect xml and objects
        btnDemo = (Button)findViewById(R.id.btn_demo);
        tvMyText = (TextView)findViewById(R.id.tv_myText);

     //Step #3 create necessary listeners
     OnClickListener oclBtnDemo = new OnClickListener() {
         @Override
         public void onClick(View v) {
             tvMyText.setText("Button Pressed!");
         }
     };//end inner class

        //Step #4 - connect listeners to controls
        btnDemo.setOnClickListener(oclBtnDemo);


    }//end method onCreate

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

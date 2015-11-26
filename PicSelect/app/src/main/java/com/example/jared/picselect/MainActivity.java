package com.example.jared.picselect;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.Image;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button btnReset;
    protected SharedPreferences prefs;
    private String[] names;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Resources res = getResources();
        names = res.getStringArray(R.array.names);

        btnReset = (Button) findViewById(R.id.btnReset);

        prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        //setupPrefs();

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupPrefs();
                FragmentManager fm = getSupportFragmentManager();
                MyListFragment fragment = (MyListFragment)fm.findFragmentById(R.id.frList);
                fragment.adapter.notifyDataSetChanged();
            }
        });

    }

    public void showImage(int position)
    {
        FragmentManager fm = getSupportFragmentManager();
        MyDialogFragment fragment = (MyDialogFragment)fm.findFragmentById(R.id.frImage);

        fragment.showImage(position);
        removeImage(position);
    }

    public void setupPrefs()
    {
        SharedPreferences.Editor editor = prefs.edit();
        for (int i = 0;i < names.length;i++) {
            editor.putBoolean(names[i], true);
        }
        editor.apply();
    }

    public void removeImage(int pos)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(names[pos], false);
        editor.apply();

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

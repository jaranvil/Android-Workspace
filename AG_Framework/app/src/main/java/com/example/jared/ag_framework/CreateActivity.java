package com.example.jared.ag_framework;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;

public class CreateActivity extends Activity {

    private ImageView ivImage;
    private Button btnConfirm;
    private EditText etTitle;
    private EditText etDescription;

    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        ivImage = (ImageView) findViewById(R.id.ivImage);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        layout = (LinearLayout) findViewById(R.id.mainLayout);
        etTitle = (EditText) findViewById(R.id.etTitle);
        etDescription = (EditText) findViewById(R.id.etDescription);

        // Display full image
        String filePath = "";
        Bundle extras=getIntent().getExtras();
        if(extras != null)//if bundle has content
        {
            filePath = extras.getString("filePath");
        }
        Bitmap bmp = BitmapFactory.decodeFile(filePath);

        ivImage.setImageBitmap(bmp);


        btnConfirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finishWithResult(true);
            }
        });
    }

    private void finishWithResult(boolean confirm)
    {
        String title = "";
        if (etTitle.getText() != null)
            title = etTitle.getText().toString();
        String description = "";
        if (etDescription.getText() != null)
            description = etDescription.getText().toString();

        Bundle conData = new Bundle();
        conData.putBoolean("confirm", confirm);
        conData.putString("title", title);
        conData.putString("description", description);
        Intent intent = new Intent();
        intent.putExtras(conData);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create, menu);
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

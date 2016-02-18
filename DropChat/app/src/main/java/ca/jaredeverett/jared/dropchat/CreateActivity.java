package ca.jaredeverett.jared.dropchat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
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
    private LinearLayout lyInfo;
    private Button btnCancel;

    // private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        ivImage = (ImageView) findViewById(R.id.ivImage);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        //layout = (LinearLayout) findViewById(R.id.mainLayout);
        etTitle = (EditText) findViewById(R.id.etTitle);
        etDescription = (EditText) findViewById(R.id.etDescription);
        lyInfo = (LinearLayout) findViewById(R.id.lyInfo);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        // Display full image
        String filePath = "";
        Bundle extras=getIntent().getExtras();
        if(extras != null)//if bundle has content
        {
            filePath = extras.getString("filePath");
        }
        Bitmap bmp = BitmapFactory.decodeFile(filePath);

        if (bmp != null)
            ivImage.setImageBitmap(rotateImage(bmp, 90));
        else
        {
            // picture somehow didnt save
            Toast.makeText(getApplicationContext(), "Error loading photo.", Toast.LENGTH_SHORT).show();
            finish();
        }


        btnConfirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finishWithResult(true);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finishWithResult(false);
            }
        });
    }

    private void finishWithResult(boolean confirm)
    {
        String title = "Untitled";
        if (etTitle.getText() != null)
            title = etTitle.getText().toString();
        String description = "No description.";
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

    public Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap retVal;

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

        return retVal;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {


        switch (e.getAction()) {
            case MotionEvent.ACTION_UP:
                if (lyInfo.getVisibility() == View.GONE)
                    lyInfo.setVisibility(View.VISIBLE);
                else
                    lyInfo.setVisibility(View.GONE);
        }

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}

package com.example.jared.ag_framework;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PhotoActivity extends Activity {

    private ImageView ivPhoto;
    private TextView tvTitle;
    private LinearLayout lyInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        lyInfo = (LinearLayout) findViewById(R.id.lyInfo);

        String photoFilename = "";
        String title = "";
        String description = "";
        Bundle extras=getIntent().getExtras();
        if(extras != null)//if bundle has content
        {
            String[] data = extras.getString("snippetString").split(":");
            photoFilename = data[0];
            if (data[1]!=null)
                title = data[1];
            if (data[2]!=null)
                description = data[2];
        }

        tvTitle.setText(title);

        String url = "http://jaredeverett.ca/android/images/"+photoFilename+".PNG";
        ImageLoadTask loadTask = new ImageLoadTask(url, ivPhoto);
        loadTask.execute();
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
        getMenuInflater().inflate(R.menu.menu_photo, menu);
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

    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(rotateImage(result, 90));
        }

    }
}

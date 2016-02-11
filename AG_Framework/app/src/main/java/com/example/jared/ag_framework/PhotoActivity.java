package com.example.jared.ag_framework;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PhotoActivity extends Activity {
    private Context context;
    private String photoFilename;
    private ImageView ivPhoto;
    private TextView tvTitle;
    private TextView tvDescription;
    private LinearLayout lyInfo;
    private Button btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        lyInfo = (LinearLayout) findViewById(R.id.lyInfo);
        btnDelete = (Button) findViewById(R.id.btnDelete);

        photoFilename = "";
        String title = "";
        String description = "";
        Bundle extras=getIntent().getExtras();
        if(extras != null)//if bundle has content
        {
            String[] data = extras.getString("snippetString").split(":");
            photoFilename = data[0];
            if (data[1]!=" ")
                title = data[1];
            else
                title = "Untitled";
            if (data[2]!=" ")
                description = data[2];
            else
                description = "No description";
        }

        tvTitle.setText(title);
        tvDescription.setText(description);

        context = this;
        btnDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Remove Photo")
                        .setMessage("This will delete the photo for ALL users.")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DeleteImageTask deleteTask = new DeleteImageTask(photoFilename);
                                deleteTask.execute();
                                Intent intent = new Intent();
                                setResult(RESULT_OK, intent);
                                finish();
                            }

                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });


        ImageLoadTask loadTask = new ImageLoadTask(photoFilename, ivPhoto);
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

        public ImageLoadTask(String file, ImageView imageView) {

            this.url = "http://jaredeverett.ca/android/images/"+file+".PNG";
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

    public class DeleteImageTask extends AsyncTask<Void, Void, Void> {

        private String url;

        public DeleteImageTask(String file) {
            this.url = "http://jaredeverett.ca/android/delete.php?file="+file;
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpClient httpclient = new DefaultHttpClient();
            try {
                httpclient.execute(new HttpGet(url));
            } catch (ClientProtocolException e) {
                //TODO Handle problems..
            } catch (IOException e) {
                //TODO Handle problems..
            }
            return null;
        }
    }
}

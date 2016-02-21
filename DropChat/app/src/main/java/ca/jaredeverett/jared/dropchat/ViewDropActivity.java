package ca.jaredeverett.jared.dropchat;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ViewDropActivity extends Activity {
    private ImageView ivThumbnail;
    private TextView tvTitle;
    private TextView tvText;
    private TextView tvUsername;

    private String user_id = "";
    private String url = "";
    private String title = "";
    private String text = "";
    private String link = "";
    private String type = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drop_view);

        ivThumbnail = (ImageView) findViewById(R.id.ivThumbnail);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvText = (TextView) findViewById(R.id.tvText);
        tvUsername = (TextView) findViewById(R.id.tvUsername);

        Bundle extras=getIntent().getExtras();
        if(extras != null)//if bundle has content
        {
            String[] data = extras.getString("snippetString").split(":");
            user_id = data[0];
            url = data[1];
            title = data[2];
            text = data[3];
            link = data[4];
            type = data[5];
        }

        getUsernameTask task = new getUsernameTask(user_id, tvUsername);
        task.execute();
        setupScreen();
    }

    public void setupScreen()
    {
        tvTitle.setText(title);

        if (type.equals("1"))
        {
            ivThumbnail.setBackgroundResource(R.drawable.text_thumb);
            tvText.setText(text);
        }
        if (type.equals("2"))
        {
            ivThumbnail.setBackgroundResource(R.drawable.link_thumb);
            tvText.setText(link);
        }
        if (type.equals("3"))
        {
            ivThumbnail.setBackgroundResource(R.drawable.camera_thumb);
            tvText.setText("");
            ImageLoadTask task = new ImageLoadTask(url, ivThumbnail);
            task.execute();
        }

    }

    public Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap retVal;

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

        return retVal;
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
            imageView.setBackgroundColor(Color.parseColor("#aaaaaa"));
            imageView.setImageBitmap(rotateImage(result, 90));
        }

    }

    private class getUsernameTask extends AsyncTask<Void, Void, String>
    {
        InputStream is = null;
        String user_id;
        TextView tvUsername;

        public getUsernameTask(String user_id, TextView tvUsername)
        {
            this.user_id = user_id;
            this.tvUsername = tvUsername;
        }

        @Override
        protected String doInBackground(Void... params) {
            String json = "";
            // Making HTTP request
            try {
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                String url = "http://www.jaredeverett.ca/android/getUsername.php?id=" + user_id;
                HttpGet httpGet = new HttpGet(url);
                HttpResponse httpResponse = httpClient.execute(httpGet);

                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                is.close();
                json = sb.toString();
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }
            return json;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            tvUsername.setText(s);
        }
    }
}

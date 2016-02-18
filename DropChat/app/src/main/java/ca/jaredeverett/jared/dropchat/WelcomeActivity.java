package ca.jaredeverett.jared.dropchat;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends Activity {
    // Shared prefs are so useful
    public static final String PREFS_NAME = "AOP_PREFS";
    SharedPreferences sharedpreferences;

    private EditText etUsername;
    private Button btnJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        sharedpreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        etUsername = (EditText) findViewById(R.id.etUsername);
        btnJoin = (Button) findViewById(R.id.btnJoin);

        btnJoin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String[] params = {etUsername.getText().toString()};
                Toast.makeText(getApplicationContext(), params[0], Toast.LENGTH_SHORT).show();
                CreateUserTask createUser = new CreateUserTask();
                createUser.execute(params);
            }
        });
    }

    public void userCreateTaskComplete(String username)
    {
        if (username.equals(""))
        {
            Toast.makeText(getApplicationContext(), "Username taken or is invalid.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("username", username);
            editor.apply();
            finish();
        }
    }

    public class CreateUserTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            InputStream is = null;
            // Making HTTP request
            try {
                // defaultHttpClient

                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://www.jaredeverett.ca/android/create_user.php");

                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
                nameValuePair.add(new BasicNameValuePair("username", params[0]));


                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String result = "";
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                is.close();
                result = sb.toString();
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            userCreateTaskComplete(s);
        }

    }
}

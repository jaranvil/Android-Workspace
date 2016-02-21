package ca.jaredeverett.jared.dropchat;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WebService {

    // Main list of map markers
    protected ArrayList<PhotoMarker> allMarkers = new ArrayList<>();
    protected String username = "";
    protected boolean markersLoaded = false;

    static InputStream is = null;
    static JSONObject jObj = null;

    public void loadMarkers(double lat, double lng) {
        markersLoaded = false;
        FetchAllMarkers taskFetchAll = new FetchAllMarkers();
        String[] params = {Double.toString(lat), Double.toString(lng)};
        taskFetchAll.execute(params);
    }

    public void lookUpCivicAddress(double lat, double lng) {
        GetCivicAddress getCivicAddress = new GetCivicAddress();
        String[] params = {Double.toString(lat), Double.toString(lng)};
        getCivicAddress.execute(params);
    }

    public void saveImage(String encodedString, String encodedThumbnail, String lat, String lng, String title, String text, String link, String user_id, int type) {
        SaveImage taskSave = new SaveImage();
        String[] params = {encodedString, encodedThumbnail, lat, lng, title, text, link, user_id, Integer.toString(type)};
        taskSave.execute(params);
    }

    public void getUsernameTaskComplete(String username)
    {
        this.username = username;
    }

    // parse result from http response in 'task'
    public void parseJSON(String json) {
        allMarkers.clear();
        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        String data = "";
        try {
            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jObj.optJSONArray("photo");

            if (jsonArray != null) {
                //Iterate the jsonArray and print the info of JSONObjects
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    int id = Integer.parseInt(jsonObject.optString("id"));
                    double lat = Double.parseDouble(jsonObject.optString("lat"));
                    double lng = Double.parseDouble(jsonObject.optString("lng"));
                    String url = jsonObject.optString("url");
                    String title = jsonObject.optString("title");
                    String description = jsonObject.optString("description");

                    allMarkers.add(new PhotoMarker(id, lat, lng, url, title, description));
                }
            }
            markersLoaded = true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void parseCivicAddressJSON(String json) {

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        String data = "";
        try {
            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jObj.optJSONArray("photo");

            if (jsonArray != null) {
                //Iterate the jsonArray and print the info of JSONObjects
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    int id = Integer.parseInt(jsonObject.optString("id"));
                    double lat = Double.parseDouble(jsonObject.optString("lat"));
                    double lng = Double.parseDouble(jsonObject.optString("lng"));
                    String url = jsonObject.optString("url");
                    String title = jsonObject.optString("title");
                    String description = jsonObject.optString("description");

                    allMarkers.add(new PhotoMarker(id, lat, lng, url, title, description));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //                                           Param, Progress, Return
    private class FetchAllMarkers extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String json = "";
            // Making HTTP request
            try {
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://www.jaredeverett.ca/android/getDropsInArea.php");

                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
                nameValuePair.add(new BasicNameValuePair("lat", params[0]));
                nameValuePair.add(new BasicNameValuePair("lng", params[1]));


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
            parseJSON(s);
        }
    }


    private class SaveImage extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://www.jaredeverett.ca/android/createDrop.php");

                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

                nameValuePair.add(new BasicNameValuePair("fullImage", params[0]));
                nameValuePair.add(new BasicNameValuePair("thumbnail", params[1]));
                nameValuePair.add(new BasicNameValuePair("lat", params[2]));
                nameValuePair.add(new BasicNameValuePair("lng", params[3]));
                nameValuePair.add(new BasicNameValuePair("title", params[4]));
                nameValuePair.add(new BasicNameValuePair("text", params[5]));
                nameValuePair.add(new BasicNameValuePair("link", params[6]));
                nameValuePair.add(new BasicNameValuePair("user", params[7]));
                nameValuePair.add(new BasicNameValuePair("type", params[8]));

                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePair));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                // httppost.setEntity(new StringEntity(params[0]));
                HttpResponse resp = httpclient.execute(httppost);
                HttpEntity ent = resp.getEntity();

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    //AIzaSyDbo8E3fYsOfBgIzR0FDLapFUqs3_Pczzs
    private class GetCivicAddress extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... params) {
            String json = "";
            // Making HTTP request
            try {
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng="+params[0]+","+params[1]+"&key=AIzaSyAHJBNmCGipEr1gNFIiLRX4ZHSGbMKrIvk";
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
            parseCivicAddressJSON(s);
        }
    }

    private class getUsername extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... params) {
            String json = "";
            // Making HTTP request
            try {
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                String url = "https://www.jaredeverett.ca/android/getUsername.php?id=" + params[0];
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
            getUsernameTaskComplete(s);
        }
    }
}
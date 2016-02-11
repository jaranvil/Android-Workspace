package com.example.jared.ag_framework;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Jared on 2/7/2016.
 */
public class PhotoMarker {
    protected MarkerOptions marker;



    public PhotoMarker(int id, double lat, double lng, String url, String title, String description)
    {


        String snippetText = url+":"+title+" :"+description+" ";
        LatLng temp = new LatLng(lat, lng);
        this.marker = new MarkerOptions()
                .position(temp)
                .title("test")
                .snippet(snippetText);

        String path = "http://jaredeverett.ca/android/images/"+url+"_thumbnail.PNG";
        ThumbnailLoadTask load = new ThumbnailLoadTask(path);
        load.execute();
    }

    public void createMarker(Bitmap thumbnail)
    {
        marker.icon(BitmapDescriptorFactory.fromBitmap(rotateImage(thumbnail, 90)));
    }

    // TODO -- move this method to photoUtil
    public Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap retVal;

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

        return retVal;
    }

    public class ThumbnailLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;

        public ThumbnailLoadTask(String url) {
            this.url = url;
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
            createMarker(result);
        }

    }
}

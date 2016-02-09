package com.example.jared.ag_framework;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.os.Handler;
import android.os.SystemClock;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MapAnimation {
    // handler for animation thread
    private Handler h;
    // redraw rate
    private int FRAME_RATE = 60;
    // the map
    GoogleMap map;
    // current overlays
    ArrayList<GroundOverlay> overlays = new ArrayList<>();
    private boolean clearOverlays = false;

    CircleAnimation circle = new CircleAnimation();

    private Location location;


    public MapAnimation(GoogleMap map)
    {
        this.map = map;
        h = new Handler();
    }

    public void start()
    {
        h.postAtTime(r, SystemClock.uptimeMillis() + 400);
    }

    public void stop()
    {
        h.removeCallbacks(r);
    }

    private Runnable r = new Runnable() {
        @Override
        public void run() {


            for (int i=0;i<overlays.size();i++)
            {
                overlays.get(i).remove();
            }
            overlays.clear();

            if (location != null)
                overlays.add(map.addGroundOverlay(circle.draw(location.getLatitude(), location.getLongitude())));



            h.postDelayed(r, FRAME_RATE);
        }
    };

    public void updateLocation(Location location)
    {
        this.location = location;
    }


    public void drawCircleAroundUser()
    {

    }

}

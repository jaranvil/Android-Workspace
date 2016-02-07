package com.example.jared.ag_framework;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Jared on 2/7/2016.
 */
public class PhotoMarker {
    protected MarkerOptions marker;
    String url;

    public PhotoMarker(int id, double lat, double lng, String url)
    {

        this.url = url;

        LatLng temp = new LatLng(lat, lng);
        this.marker = new MarkerOptions()
                        .position(temp)
                        .title("test")
                        .snippet(url);
        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.picture_icon));

    }

}

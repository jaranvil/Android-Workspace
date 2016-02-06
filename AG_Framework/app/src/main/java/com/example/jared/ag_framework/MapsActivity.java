package com.example.jared.ag_framework;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap map;
    public LocationManager locationManager;
    public LocationUpdateListener listener;

    private Marker prevUserPosition;
    private GroundOverlay prevOverlay;

    private TextView loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        loading = (TextView)findViewById(R.id.tvLoading);
    }

    public void drawCircleAroundUser(Location location)
    {
        int radius = 100;
        LatLng position = new LatLng(location.getLatitude(), location.getLongitude());

        int d = 500;
        Bitmap bm = Bitmap.createBitmap(d, d, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint();
        p.setColor(Color.parseColor("#800000"));
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(10);
        c.drawCircle(d / 2, d / 2, d / 2, p);

        BitmapDescriptor bmD = BitmapDescriptorFactory.fromBitmap(bm);

        if (prevOverlay != null)
            prevOverlay.remove();

        GroundOverlayOptions drawOptions = new GroundOverlayOptions().
                        image(bmD).
                        position(position, radius * 2, radius * 2).
                        transparency(0.4f);

        prevOverlay = map.addGroundOverlay(drawOptions);


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setScrollGesturesEnabled(false);
        map.getUiSettings().setZoomControlsEnabled(false);
        map.getUiSettings().setAllGesturesEnabled(false);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new LocationUpdateListener();

        if ( ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {

//            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
//                    LocationService.MY_PERMISSION_ACCESS_COURSE_LOCATION);

        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);


    }



    class LocationUpdateListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            // TODO Auto-generated method stub

            loading.setVisibility(View.GONE);

            if (prevUserPosition != null)
                prevUserPosition.remove();

            LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());

            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(currentPosition, 17.0f);
            map.animateCamera(yourLocation);

            MarkerOptions userMarker = new MarkerOptions().position(currentPosition).title("You are here");
            userMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
            prevUserPosition = map.addMarker(userMarker);

            drawCircleAroundUser(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub

        }

    }

}

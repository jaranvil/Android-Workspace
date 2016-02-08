package com.example.jared.ag_framework;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.io.File;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    // camera constant
    static final int REQUEST_IMAGE_CAPTURE = 1;

    // map stuff
    private GoogleMap map;
    public LocationManager locationManager;
    public LocationUpdateListener listener;
    public Location userLocation;

    // for drawing a canvas over the map
    private GroundOverlay prevOverlay;

    // Widgets
    private TextView tvloading;
    private Button btnDrop;
    private Button btnRefresh;

    WebService remote;
    private boolean loading = true;

    // Stores the thumbnail captured by the user
    // holding onto it while waiting for confirmation actvivity
    private Bitmap photo;

    public MapsActivity() {
        remote = new WebService();
    }

    @Override
    public void onPause()
    {
        if ( ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            // TODO - request permissions
        }
        if (locationManager != null)
            locationManager.removeUpdates(listener);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ( ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            // TODO - request permissions
        }
        if (locationManager != null)
        {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Setup Map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // XML widgets
        tvloading = (TextView)findViewById(R.id.tvLoading);
        btnDrop = (Button) findViewById(R.id.btnDrop);
        btnRefresh = (Button) findViewById(R.id.btnRefresh);

        // listener for drop button
        btnDrop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // open camera and take picture
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

//                File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
//                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }

            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Refreshing map...", Toast.LENGTH_SHORT).show();
                if (userLocation != null)
                    remote.loadMarkers(userLocation.getLatitude(), userLocation.getLongitude());
            }
        });
    }

    // When the camera activity finished it encodes the image as a bitmap in the result data
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            // Get image from intent data
            Bundle extras = data.getExtras();
            photo = (Bitmap) extras.get("data");


            // open confirmation activity
            Intent i = new Intent("CreateActivity");//create intent object
            i.putExtra("image", photo); // TODO - save image as a local file and pass its URL to the activity.
            startActivityForResult(i, 3);
        }
        else if (requestCode == 3 && resultCode == RESULT_OK)
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            String encodedImage = Base64.encodeToString(imageBytes, Base64.NO_WRAP);

            if (userLocation != null)
            {
                remote.saveThumbnail(encodedImage, userLocation.getLatitude(), userLocation.getLongitude());
                Toast.makeText(getApplicationContext(), "Uploading...", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(getApplicationContext(), "Could not get location.", Toast.LENGTH_SHORT).show();

            if (userLocation != null)
                remote.loadMarkers(userLocation.getLatitude(), userLocation.getLongitude());
        }
    }

    public void drawCircleAroundUser(Location location)
    {
        int radius = 100;
        LatLng position = new LatLng(location.getLatitude(), location.getLongitude());

        int d = 500;
        Bitmap bm = Bitmap.createBitmap(d, d, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint();
        p.setColor(Color.parseColor("#0000ff"));
        p.setAlpha(30);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        // disable control gentures to lock the map in place
        map.getUiSettings().setScrollGesturesEnabled(false);
        map.getUiSettings().setZoomControlsEnabled(false);
        map.getUiSettings().setAllGesturesEnabled(false);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new LocationUpdateListener();

        if ( ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            // TODO - request permissions
        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker arg0) {
                if (!arg0.getSnippet().equals("user")) {
                    Intent i = new Intent("PhotoActivity");//create intent object
                    Bundle extras = new Bundle();//create bundle object
                    extras.putString("filename", arg0.getSnippet());
                    i.putExtras(extras);
                    startActivity(i);
                }

                return true;
            }

        });
    }

    public void drawMarkers()
    {
        map.clear();
        for (int i = 0;i<remote.allMarkers.size();i++)
        {
            map.addMarker(remote.allMarkers.get(i).marker);

        }
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

//        if (marker.equals(pictureMarker))
//        {
//            Toast.makeText(getApplicationContext(), "msg msg", Toast.LENGTH_SHORT).show();
//        }
        return false;
    }

    class LocationUpdateListener implements LocationListener
    {
        @Override
        public void onLocationChanged(Location location) {

            userLocation = location;

            if (loading)
            {
                loading = false;
                remote.loadMarkers(location.getLatitude(), location.getLongitude());
                tvloading.setVisibility(View.GONE);
            }

            map.clear();

            LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());

            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(currentPosition, 17.0f);
            map.moveCamera(yourLocation);

            drawMarkers();

            MarkerOptions userMarker = new MarkerOptions().position(currentPosition).title("You are here").snippet("user");
            userMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
            //map.addMarker(userMarker);
            //drawCircleAroundUser(location);


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

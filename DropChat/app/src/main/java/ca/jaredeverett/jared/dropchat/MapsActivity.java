package ca.jaredeverett.jared.dropchat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    // TODO - Do some bloody refactoring

    // camera constant
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;

    // for background thread to keep markers loaded
    private Handler h;
    private int FRAME_RATE = 1000;

    // Shared prefs are so useful
    public static final String PREFS_NAME = "AOP_PREFS";
    SharedPreferences sharedpreferences;

    // Where the last photo taken is
    private String mCurrentPhotoPath;

    // map stuff
    private GoogleMap map;
    public LocationManager locationManager;
    public LocationUpdateListener listener;
    public Location userLocation;
    private MapAnimation mapAnimation;

    // Widgets
    private TextView tvloading;
    private Button btnDrop;
    private Button btnRefresh;
    private TextView tvLoadingPhotos;
    private TextView tvUsername;
    private LinearLayout lyInfoView;
    private Button btnOkay;
    private Button btnAbout;
    private Button btnZoom1;
    private Button btnZoom2;
    private Button btnZoom3;

    public String username = "";

    private float currentZoom = 17.0f;

    private WebService remote = new WebService();
    private PhotoUtil photoUtil = new PhotoUtil();
    private boolean loading = true;

    // Stores the thumbnail captured by the user
    // holding onto it while waiting for confirmation actvivity
    //private Bitmap photo;
    //private Bitmap photo;

    @Override
    public void onPause()
    {
        if ( ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            // TODO - request permissions
        }
        if (locationManager != null)
            locationManager.removeUpdates(listener);
        //if (mapAnimation != null)
        //mapAnimation.stop();
        // stop thread that keeps markers loaded
        h.removeCallbacks(r);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //if (mapAnimation != null)
        //mapAnimation.start();

        // start thread to keep markers loaded
        h.postAtTime(r, SystemClock.uptimeMillis() + 400);

        if ( ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            Toast.makeText(getApplicationContext(), "Tell Jared: Coarse location. On resume", Toast.LENGTH_LONG).show();
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

        Toast.makeText(getApplicationContext(), "DropChat starting...", Toast.LENGTH_SHORT).show();

        sharedpreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        loginUser();

        // Setup Map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // handler for keeping markers loaded on map
        h = new Handler();

        // XML widgets
        tvloading = (TextView)findViewById(R.id.tvLoading);
        btnDrop = (Button) findViewById(R.id.btnDrop);
        btnRefresh = (Button) findViewById(R.id.btnRefresh);
        tvLoadingPhotos = (TextView) findViewById(R.id.tvLoadingPhotos);
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        lyInfoView = (LinearLayout) findViewById(R.id.lyInfoView);
        btnOkay = (Button) findViewById(R.id.btnOkay);
        btnAbout = (Button) findViewById(R.id.btnAbout);
        btnZoom1 = (Button) findViewById(R.id.btnZoom1);
        btnZoom2 = (Button) findViewById(R.id.btnZoom2);
        btnZoom3 = (Button) findViewById(R.id.btnZoom3);


        // Check if welcome screen is needed
        boolean viewed = sharedpreferences.getBoolean("viewed", false);
        if (!viewed)
            lyInfoView.setVisibility(View.VISIBLE);

        // listener for drop button
        btnDrop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (userLocation == null)
                {
                    Toast.makeText(getApplicationContext(), "Location not available.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent i = new Intent("NewDropActivity");//create intent object
                    Bundle extras = new Bundle();
                    extras.putString("lat", Double.toString(userLocation.getLatitude()));
                    extras.putString("lng", Double.toString(userLocation.getLongitude()));
                    i.putExtras(extras);
                    startActivityForResult(i, 6);
                }
            }
        });

        // Refresh Button
        // grab new photo marker list from web server
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Refreshing photos...", Toast.LENGTH_SHORT).show();
                if (userLocation != null)
                    remote.loadMarkers(userLocation.getLatitude(), userLocation.getLongitude());
            }
        });

        btnOkay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean("viewed", true);
                editor.apply();
                lyInfoView.setVisibility(View.GONE);
            }
        });

        btnAbout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                lyInfoView.setVisibility(View.VISIBLE);
            }
        });

        btnZoom1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (map != null && userLocation != null)
                {
                    currentZoom = 15.0f;
                    LatLng currentPosition = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
                    CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(currentPosition, currentZoom);
                    map.animateCamera(yourLocation);

                    btnZoom1.setBackgroundColor(Color.parseColor("#500000ff"));
                    btnZoom2.setBackgroundColor(Color.parseColor("#80000000"));
                    btnZoom3.setBackgroundColor(Color.parseColor("#80000000"));
                }
            }
        });

        btnZoom2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (map != null && userLocation != null)
                {
                    currentZoom = 17.0f;
                    LatLng currentPosition = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
                    CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(currentPosition, currentZoom);
                    map.animateCamera(yourLocation);

                    btnZoom1.setBackgroundColor(Color.parseColor("#80000000"));
                    btnZoom2.setBackgroundColor(Color.parseColor("#500000ff"));
                    btnZoom3.setBackgroundColor(Color.parseColor("#80000000"));
                }
            }
        });

        btnZoom3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (map != null && userLocation != null)
                {
                    currentZoom = 20.0f;
                    LatLng currentPosition = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
                    CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(currentPosition, currentZoom);
                    map.animateCamera(yourLocation);

                    btnZoom1.setBackgroundColor(Color.parseColor("#80000000"));
                    btnZoom2.setBackgroundColor(Color.parseColor("#80000000"));
                    btnZoom3.setBackgroundColor(Color.parseColor("#500000ff"));
                }
            }
        });
    }

    private Runnable r = new Runnable() {
        @Override
        public void run() {

            if (remote.markersLoaded)
                drawMarkers();

            h.postDelayed(r, FRAME_RATE);
        }
    };

    public void loginUser()
    {
        String username = sharedpreferences.getString("username", "");
        if (username.equals(""))
            startActivity(new Intent("WelcomeActivity"));
        else
            remote.getUsername(sharedpreferences.getString("username", ""));

    }

    // When the camera activity finished it encodes the image as a bitmap in the result data
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        // Return from the new create drop actiity.

        if (userLocation != null)
        {
            Toast.makeText(getApplicationContext(), "Dropping...", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(getApplicationContext(), "Could not get location.", Toast.LENGTH_SHORT).show();

        if (userLocation != null)
            remote.loadMarkers(userLocation.getLatitude(), userLocation.getLongitude());

        if (resultCode == RESULT_OK)
        {
            // return from viewing a photo
            // refresh markers in case user deleted or updated a photo
            if (userLocation != null)
                remote.loadMarkers(userLocation.getLatitude(), userLocation.getLongitude());
        }
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        // disable control gentures to lock the map in place
        map.getUiSettings().setScrollGesturesEnabled(false);
        map.getUiSettings().setAllGesturesEnabled(false);
        map.getUiSettings().setRotateGesturesEnabled(true);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new LocationUpdateListener();

        //mapAnimation = new MapAnimation(map);
        //mapAnimation.start();

        if ( ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            Toast.makeText(getApplicationContext(), "Tell Jared: Coarse location denied. onMapReady.", Toast.LENGTH_LONG).show();
            // TODO - request permissions
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);

        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
//                float maxZoom = 17.0f;
//                if (cameraPosition.zoom < maxZoom)
//                    map.animateCamera(CameraUpdateFactory.zoomTo(maxZoom));
//                if (userLocation != null)
//                {
//                    LatLng currentPosition = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
//                    CameraUpdate yourLocation = CameraUpdateFactory.newLatLng(currentPosition);
//                    map.animateCamera(yourLocation);
//                }

            }
        });

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker arg0) {
                if (!arg0.getSnippet().equals("user")) {
                    Intent i = new Intent("ViewDropActivity");//create intent object
                    Bundle extras = new Bundle();//create bundle object
                    extras.putString("snippetString", arg0.getSnippet());
                    i.putExtras(extras);
                    startActivityForResult(i, 4);
                }

                return true;
            }

        });
    }

    public void drawMarkers()
    {
        if (tvUsername.getText().equals(""))
            tvUsername.setText(remote.username);

        if (remote.markersLoaded)
            tvLoadingPhotos.setText("");
        else
            tvLoadingPhotos.setText("Loading Photos...");

        // reset flag to load markers
        remote.markersLoaded = false;

        map.clear();
        for (int i = 0;i<remote.allMarkers.size();i++)
            if (remote.allMarkers.get(i).marker != null)
                map.addMarker(remote.allMarkers.get(i).marker);

        if (tvUsername.getText().equals(""))
            tvUsername.setText(sharedpreferences.getString("username", ""));

//        LatLng currentPosition = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
//        MarkerOptions userMarker = new MarkerOptions().position(currentPosition).title("You are here").snippet("user");
//        userMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.circle));
//        userMarker.anchor(0.5f, 0.5f);
//        map.addMarker(userMarker);
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

//        if (marker.equals(pictureMarker))
//        {
//            Toast.makeText(getApplicationContext(), "msg msg", Toast.LENGTH_SHORT).show();
//        }
        return false;
    }

        // create file to write new image to
        private File createImageFile() throws IOException {
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";

            // public gallery folder
            //File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

            // private to the app storage
            File storageDir = getExternalFilesDir(null);

            File image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );

            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = image.getAbsolutePath();
            return image;
        }

    class LocationUpdateListener implements LocationListener
    {
        @Override
        public void onLocationChanged(Location location) {

            userLocation = location;

            boolean animate = true;
            if (loading)
            {
                animate = false;
                loading = false;
                remote.loadMarkers(location.getLatitude(), location.getLongitude());
                tvloading.setVisibility(View.GONE);
            }

            LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(currentPosition, currentZoom);
            //CameraUpdate yourLocation = CameraUpdateFactory.newLatLng(currentPosition);
            if (animate)
                map.animateCamera(yourLocation);
            else
                map.moveCamera(yourLocation);



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

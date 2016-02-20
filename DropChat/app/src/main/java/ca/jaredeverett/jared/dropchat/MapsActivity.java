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


    private WebService remote = new WebService();
    private PhotoUtil photoUtil = new PhotoUtil();
    private boolean loading = true;

    // Stores the thumbnail captured by the user
    // holding onto it while waiting for confirmation actvivity
    private Bitmap photo;

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

        tvUsername.setText(sharedpreferences.getString("username", ""));

        // Check if welcome screen is needed
        boolean viewed = sharedpreferences.getBoolean("viewed", false);
        if (!viewed)
            lyInfoView.setVisibility(View.VISIBLE);

        // listener for drop button
        btnDrop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {

                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(photoFile));
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                    }
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

    }

    // When the camera activity finished it encodes the image as a bitmap in the result data
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Return from camera actvity
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {

            //Bitmap photo = photoUtil.getScaledBitmap(mCurrentPhotoPath, 500, 800); // TODO - get view demensions

            // Get Thumbnail
//            Bundle extras = data.getExtras();
//            photo = (Bitmap) extras.get("data");


            // open confirmation activity
            Intent i = new Intent("CreateActivity");//create intent object
            i.putExtra("image", photo); // TODO - save image as a local file and pass its URL to the activity.
            Bundle extras = new Bundle();
            extras.putString("filePath", mCurrentPhotoPath);
            i.putExtras(extras);
            startActivityForResult(i, 3);
        }
        // return from Confirmation activity
        else if (requestCode == 3 && resultCode == RESULT_OK)
        {
            // TODO - file compression and encoding into PhotoUtil class
            File file = new File(mCurrentPhotoPath);

            // encode full image
            Bitmap temp = decodeFile(file, false);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            temp.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            String encodedImage = Base64.encodeToString(imageBytes, Base64.NO_WRAP);

            // encode tumbanil
            Bitmap temp2 = decodeFile(file, true);
            ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
            temp2.compress(Bitmap.CompressFormat.PNG, 100, baos2);
            byte[] imageBytes2 = baos2.toByteArray();
            String encodedThumbnail = Base64.encodeToString(imageBytes2, Base64.NO_WRAP);

            //TODO - delete photo if confirmation activity is canceled or finishs via back button
            file.delete();

            Bundle res = data.getExtras();
            String title = res.getString("title");
            String description = res.getString("description");

            if (userLocation != null)
            {
                remote.saveImage(encodedImage, encodedThumbnail, userLocation.getLatitude(), userLocation.getLongitude(), title, description);
                Toast.makeText(getApplicationContext(), "Uploading...", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(getApplicationContext(), "Could not get location.", Toast.LENGTH_SHORT).show();

            if (userLocation != null)
                remote.loadMarkers(userLocation.getLatitude(), userLocation.getLongitude());
        }
        else if (requestCode == 4 && resultCode == RESULT_OK)
        {
            // return from viewing a photo
            // refresh markers in case user deleted or updated a photo
            if (userLocation != null)
                remote.loadMarkers(userLocation.getLatitude(), userLocation.getLongitude());
        }
    }

    // Decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f, boolean thumbnail) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // The new size we want to scale to
            final int REQUIRED_SIZE;
            if (thumbnail)
                REQUIRED_SIZE=60;
            else
                REQUIRED_SIZE=300;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
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
                    Intent i = new Intent("PhotoActivity");//create intent object
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
            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(currentPosition, 17.0f);
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

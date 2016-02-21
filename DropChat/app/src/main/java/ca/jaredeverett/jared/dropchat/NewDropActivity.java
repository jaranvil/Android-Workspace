package ca.jaredeverett.jared.dropchat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.Toast;

import org.apache.http.entity.StringEntity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewDropActivity extends Activity {

    public static final String PREFS_NAME = "AOP_PREFS";
    SharedPreferences sharedpreferences;

    ImageView ivDropType;
    RadioGroup rgGroup;
    EditText etTitle;
    EditText etText;
    EditText etLink;
    LinearLayout lyPhoto;
    ImageView ivPhoto;
    Button btnCamera;
    Button btnDrop;

    private WebService remote = new WebService();
    private String mCurrentPhotoPath;
    private int dropType = 1;
    private String lat = "";
    private String lng = "";
    String encodedImage = "";
    String encodedThumbnail = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_drop);

        sharedpreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        Bundle extras=getIntent().getExtras();
        if(extras != null)//if bundle has content
        {
            lat = extras.getString("lat", "");
            lng = extras.getString("lng", "");
        }

        ivDropType = (ImageView) findViewById(R.id.ivDropType);
        rgGroup = (RadioGroup) findViewById(R.id.rgGroup);
        etText = (EditText) findViewById(R.id.etText);
        etLink = (EditText) findViewById(R.id.etLink);
        etTitle = (EditText) findViewById(R.id.etTitle);
        lyPhoto = (LinearLayout) findViewById(R.id.lyPhoto);
        btnCamera = (Button) findViewById(R.id.btnCanera);
        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
        btnDrop = (Button) findViewById(R.id.btnDrop);
        etLink.setVisibility(View.GONE);
        lyPhoto.setVisibility(View.GONE);

        btnCamera.setOnClickListener(new View.OnClickListener() {
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
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                        startActivityForResult(takePictureIntent, 1);
                    }
                }
            }
        });

        btnDrop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String title = etTitle.getText().toString();
                String text = etText.getText().toString();
                String link = etLink.getText().toString();
                String user_id = sharedpreferences.getString("username", "");
                remote.saveImage(encodedImage, encodedThumbnail, lat, lng, title, text, link, user_id, dropType);
                finish();
            }
        });

        rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int id)
            {
                switch (id)
                {
                    case R.id.rbText:
                        ivDropType.setBackgroundResource(R.drawable.marker_text);
                        dropType = 1;
                        etLink.setVisibility(View.GONE);
                        lyPhoto.setVisibility(View.GONE);
                        etText.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rbLink:
                        ivDropType.setBackgroundResource(R.drawable.marker_link);
                        dropType = 2;
                        etLink.setVisibility(View.VISIBLE);
                        lyPhoto.setVisibility(View.GONE);
                        etText.setVisibility(View.GONE);
                        break;
                    case R.id.rbPhoto:
                        ivDropType.setBackgroundResource(R.drawable.marker_photo);
                        dropType = 3;
                        etLink.setVisibility(View.GONE);
                        lyPhoto.setVisibility(View.VISIBLE);
                        etText.setVisibility(View.GONE);
                        break;
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // Return from camera actvity
        if (requestCode == 1 && resultCode == RESULT_OK)
        {
            // open confirmation activity
            Intent i = new Intent("CreateActivity");//create intent object
            //i.putExtra("image", photo); // TODO - save image as a local file and pass its URL to the activity.
            Bundle extras = new Bundle();
            extras.putString("filePath", mCurrentPhotoPath);
            i.putExtras(extras);
            startActivityForResult(i, 3);
        }
        else // return from Confirmation activity
            if (requestCode == 3 && resultCode == RESULT_OK)
            {
                // TODO - file compression and encoding into PhotoUtil class
                File file = new File(mCurrentPhotoPath);

                // encode full image
                Bitmap temp = decodeFile(file, false);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                temp.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                encodedImage = Base64.encodeToString(imageBytes, Base64.NO_WRAP);

                // encode tumbanil
                Bitmap temp2 = decodeFile(file, true);
                ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
                temp2.compress(Bitmap.CompressFormat.PNG, 100, baos2);
                byte[] imageBytes2 = baos2.toByteArray();
                encodedThumbnail = Base64.encodeToString(imageBytes2, Base64.NO_WRAP);

                //TODO - delete photo if confirmation activity is canceled or finishs via back button
                file.delete();

                ivPhoto.setBackground(new BitmapDrawable(getResources(), temp));
            }
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

}

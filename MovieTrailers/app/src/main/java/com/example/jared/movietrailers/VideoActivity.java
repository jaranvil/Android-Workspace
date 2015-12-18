package com.example.jared.movietrailers;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class VideoActivity extends AppCompatActivity {

    private VideoView videoView;
    private DBAdapter db;
    private TextView tvTitle;
    private TextView tvDescription;
    private RatingBar rbRating;
    private Button btnDelete;

    private int id;
    private String title;
    private String description;
    private String video;
    private int rating;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        videoView = (VideoView) findViewById(R.id.video);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        rbRating = (RatingBar) findViewById(R.id.rbRating);
        btnDelete = (Button) findViewById(R.id.btnDelete);

        getVideo();
        setupDB();

        tvTitle.setText(title);
        tvDescription.setText(description);
        rbRating.setRating(rating);
        rbRating.setStepSize(1);

        setupVideo();

        rbRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                //Toast.makeText(getApplicationContext(), "rating " + rating, Toast.LENGTH_SHORT).show();
                // UPDATE
                db.open();
                if (db.updateVideo(id, rating))
                    Toast.makeText(getApplicationContext(), "Update successful", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getApplicationContext(), "Update failed", Toast.LENGTH_LONG).show();
                db.close();

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.open();
                db.deleteVideo(id);
                db.close();
                finish();
            }

        });
    }

    private void setupVideo() {
        Context context = videoView.getContext();
        int id = context.getResources().getIdentifier(video, "raw", context.getPackageName());

        Uri video= Uri.parse("android.resource://" + getPackageName() + "/" + id);

        videoView.setVideoURI(video);
        MediaController mediaController = new MediaController(this);

        View v = findViewById(R.id.video);
        mediaController.setAnchorView(v);

        videoView.setMediaController(mediaController);

        videoView.requestFocus();
        videoView.start();
    }

    public void getVideo()
    {
        Bundle extras=getIntent().getExtras();
        if(extras != null)//if bundle has content
        {
            id = extras.getInt("id", 0);
            title = extras.getString("title", "");
            description = extras.getString("description", "");
            video = extras.getString("video", "");
            rating = extras.getInt("rating", 0);
        }

    }

    public void setupDB()
    {

        try {
            String destPath = "/data/data/" + getPackageName() + "/database/MyDB";
            //Alternate way to do destPath:
            //String destPath = Environment.getExternalStorageDirectory().getPath() +
            //getPackageName() + "/database/MyDB";
            File f = new File(destPath);
            if (!f.exists()) {
                CopyDB(getBaseContext().getAssets().open("mydb"),
                        new FileOutputStream(destPath));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        db = new DBAdapter(this);


        // INSERT
//        db.open();
//        long id = db.insertVideo("The Martian","A cool movie about Mars", "martian", "martian");
//        db.close();



    }

    public void CopyDB(InputStream inputStream,OutputStream outputStream)
            throws IOException{
        //copy 1k bytes at a time
        byte[] buffer = new byte[1024];
        int length;
        while((length = inputStream.read(buffer)) > 0)
        {
            outputStream.write(buffer,0,length);
        }
        inputStream.close();
        outputStream.close();

    }//end method CopyDB

}

package com.example.jared.movietrailers;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class VideoList extends ArrayAdapter<String>{
    private final Activity context;
    private ArrayList<Video> videos;



    public VideoList(Activity context, ArrayList<Video> videos, String[] test) {
        super(context, R.layout.list_single, test);
        this.context = context;
        this.videos = videos;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_item, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.tvTitle);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.ivThumbnail);
        TextView tvDescription = (TextView) rowView.findViewById(R.id.tvDescription);

        Context context = imageView.getContext();
        int id = context.getResources().getIdentifier(videos.get(position).thumbnail, "drawable", context.getPackageName());
        imageView.setImageResource(id);

        String description = videos.get(position).description.substring(0, 60) + "...";

        txtTitle.setText(videos.get(position).title);
        tvDescription.setText(description);
        imageView.setImageResource(id);
        return rowView;
    }
}

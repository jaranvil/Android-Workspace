package com.example.jared.movietrailers;

/**
 * Created by jared on 11/12/15.
 */
public class Video {
    protected String title;
    protected String thumbnail;
    protected String video;
    protected String description;
    protected int rating;

    public Video(String title, String description, String thumbnail, String video, int rating)
    {
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;
        this.video = video;
        this.rating = rating;
    }

}

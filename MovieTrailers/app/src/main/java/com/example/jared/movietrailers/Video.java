package com.example.jared.movietrailers;

public class Video {
    protected int id;
    protected String title;
    protected String thumbnail;
    protected String video;
    protected String description;
    protected int rating;

    public Video(int id, String title, String description, String thumbnail, String video, int rating)
    {
        this.id = id;
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;
        this.video = video;
        this.rating = rating;
    }

}

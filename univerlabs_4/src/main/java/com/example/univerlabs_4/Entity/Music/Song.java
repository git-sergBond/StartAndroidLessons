package com.example.univerlabs_4.Entity.Music;

import com.google.gson.annotations.Expose;

import java.util.Date;

public class Song {
    private long id;
    private String name;
    private String artist;
    //@Expose
    //private Date addTime;
    private long duration;
    private String icon;

    public Song(long id, String name, String artist, long durationSec, String icon) {
        this.id = id;
        this.name = name;
        this.artist = artist;
        //this.addTime = new Date();
        setDuration(durationSec);
        this.icon = icon;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
/*
    public Date getAddTime() {
        return addTime;
    }
*/
    //Sec
    public long getDuration() {
        return duration;
    }

    //Sec
    public void setDuration(long sec) {
        this.duration = sec;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
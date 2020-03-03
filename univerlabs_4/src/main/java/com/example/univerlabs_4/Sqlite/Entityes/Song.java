package com.example.univerlabs_4.Sqlite.Entityes;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Song {
    @PrimaryKey(autoGenerate = true)
    private int primatyId;
    private long id;
    private String name;
    private String artist;
    private Date addTime;
    private long duration;
    private String icon;

    public Song(long id, String name, String artist, Date addTime, long duration, String icon) {
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.addTime = addTime;
        this.duration = duration;
        this.icon = icon;
    }

    public int getPrimatyId() {
        return primatyId;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public Date getAddTime() {
        return addTime;
    }

    public long getDuration() {
        return duration;
    }

    public String getIcon() {
        return icon;
    }

    public void setPrimatyId(int primatyId) {
        this.primatyId = primatyId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
package com.example.univerlabs_4.Entity.Music;

public class Song {
    private long id;
    private String name;
    private String artist;
    private String addTime;

    public Song(long id, String name, String artist, String addTime) {
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.addTime = addTime;
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

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }
}

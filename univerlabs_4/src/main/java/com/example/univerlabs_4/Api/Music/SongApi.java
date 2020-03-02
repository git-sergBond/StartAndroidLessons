package com.example.univerlabs_4.Api.Music;

import retrofit2.Call;
import retrofit2.http.GET;

import com.example.univerlabs_4.Entity.Music.Song;

public interface SongApi {
    @GET("api_get_current_song")
    Call<Song> getCurrentSong();
}

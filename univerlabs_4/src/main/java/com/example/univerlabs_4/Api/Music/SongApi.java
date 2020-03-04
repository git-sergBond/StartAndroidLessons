package com.example.univerlabs_4.Api.Music;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import com.example.univerlabs_4.Entity.Music.Song;

public interface SongApi {
    @FormUrlEncoded
    @POST("api_get_current_song")
    Call<Song> getCurrentSong(@Field("login") String login, @Field("password") String password);
}

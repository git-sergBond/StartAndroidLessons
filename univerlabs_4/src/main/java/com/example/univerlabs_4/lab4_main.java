package com.example.univerlabs_4;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.univerlabs_4.Api.Music.SongApi;
import com.example.univerlabs_4.Entity.Music.Song;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class lab4_main extends AppCompatActivity {

    String LOG_TAG = "myLOG";

    String baseUrl = "http://192.168.1.85:8080/";

    SongApi songApi;

    ListView songListView;
    List<Song> songListData = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab4_main);
        bindListMusic();
    }

    void bindListMusic() {
        songListView = findViewById(R.id.songsList);
        MusicAdapter musicAdapter = new MusicAdapter(this, R.layout.item_song, songListData);
        songListView.setAdapter(musicAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        prepareApi();
        callWebService();
    }

    void prepareApi () {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        songApi = retrofit.create(SongApi.class);
    }

    void callWebService() {
        Timer listenChangeSong = new Timer();
        listenChangeSong.schedule(new TimerTask() {
            @Override
            public void run() {
                Call<Song> song =  songApi.getCurrentSong();
                song.enqueue(new Callback<Song> () {

                    @Override
                    public void onResponse(Call<Song> call, Response<Song> response) {
                        Log.d(LOG_TAG, response.body().getName());
                    }

                    @Override
                    public void onFailure(Call<Song> call, Throwable t) {

                    }
                });
            }
        }, 0, 2 * 1000);
    }

    class MusicAdapter extends ArrayAdapter<Song> {

        LayoutInflater inflater;
        int layout;
        List<Song> songList;

        public MusicAdapter(@NonNull Context context, int resource, List<Song> songList) {
            super(context, resource, songList);
            this.inflater = LayoutInflater.from(context);
            this.layout = resource;
            this.songList = songList;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = inflater.inflate(layout, parent, false);

            TextView id = view.findViewById(R.id.id);
            TextView name = view.findViewById(R.id.name);
            TextView artist = view.findViewById(R.id.artist);
            TextView timeAdd = view.findViewById(R.id.timeAdd);

            Song song = songList.get(position);

            id.setText(String.valueOf(song.getId()));
            name.setText(song.getName());
            artist.setText(song.getArtist());
            timeAdd.setText(song.getAddTime());

            return view;
        }
    }
}

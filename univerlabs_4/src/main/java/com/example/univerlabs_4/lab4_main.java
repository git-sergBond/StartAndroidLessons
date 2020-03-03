package com.example.univerlabs_4;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.univerlabs_4.Api.Music.SongApi;
import com.example.univerlabs_4.Entity.Music.Song;
import com.example.univerlabs_4.Sqlite.AppDataBase;
import com.example.univerlabs_4.Sqlite.DAO.SongDAO;
import com.example.univerlabs_4.helpers.gson.DurationGson;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
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
    MusicAdapter musicAdapter;
    List<Song> songListData = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab4_main);
        bindListMusic();
    }

    void bindListMusic() {
        songListView = findViewById(R.id.songsList);
        musicAdapter = new MusicAdapter(this, R.layout.item_song, songListData);
        songListView.setAdapter(musicAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        prepareApi();
        getDataBase();
        readAllRecordsFromLastPageSQLite();
        //callWebService();
    }

    void prepareApi () {
/*
        Gson gsonBuilder = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationGson.DurationDeserializer())
                .create();
*/
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                //.addConverterFactory(GsonConverterFactory.create(gsonBuilder))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        songApi = retrofit.create(SongApi.class);
    }


    void getDataBase() {
        MyApplication.getDataBase();
    }

    void readAllRecordsFromLastPageSQLite() {
        new Thread () {
            @Override
            public void run() {
                super.run();
                AppDataBase dataBase = MyApplication.getDataBase();
                SongDAO songDAO = dataBase.songDAO();

                songDAO.insert(new com.example.univerlabs_4.Sqlite.Entityes.Song(123, "asd", "asd", new Date(), 245345, "asds"));

                List<com.example.univerlabs_4.Sqlite.Entityes.Song> songList = songDAO.getLastPAge();
                Log.d(LOG_TAG, "size songList = " + songList.size());
            }
        }.start();

    }

    void callWebService() {
        Timer listenChangeSong = new Timer();
        listenChangeSong.schedule(new TimerTask() {
            @Override
            public void run() {
                Call<Song> getCurrentSong =  songApi.getCurrentSong();
                getCurrentSong.enqueue(new Callback<Song> () {

                    @Override
                    public void onResponse(Call<Song> call, Response<Song> response) {
                        if(response.isSuccessful()) {
                            final Song song = response.body();
                            if(songListData.size() > 0) {
                                if (songListData.get(songListData.size() - 1).getId() != song.getId()) {
                                    songListData.add(song);
                                    musicAdapter.notifyDataSetChanged();
                                }
                            } else if (songListData.size() < 1){
                                songListData.add(song);
                                musicAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Song> call, Throwable t) {
                        Log.d(LOG_TAG, "onFailure ", t.fillInStackTrace());
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
            ImageView imageView = view.findViewById(R.id.icon);

            Song song = songList.get(position);

            id.setText(String.valueOf(song.getId()));
            name.setText(song.getName());
            artist.setText(song.getArtist());

            Date dateNow = new Date();
            timeAdd.setText(dateNow.toString());


            Picasso.get().load(baseUrl + song.getIcon()).into(imageView);

            return view;
        }
    }
}

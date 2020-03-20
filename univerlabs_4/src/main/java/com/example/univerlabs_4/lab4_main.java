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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
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

import com.example.univerlabs_4.Api.Music.SongApi;
import com.example.univerlabs_4.Entity.Music.Song;
import com.example.univerlabs_4.Sqlite.AppDataBase;
import com.example.univerlabs_4.Sqlite.DAO.SongDAO;
import com.example.univerlabs_4.helpers.gson.DurationGson;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;


public class lab4_main extends AppCompatActivity {

    String LOG_TAG = "myLOG";

    String baseUrl = "http://10.0.2.2:8080/";
    //String baseUrl = "http://localhost:8080/";
    SongApi songApi;

    ListView songListView;
    MusicAdapter musicAdapter;
    List<com.example.univerlabs_4.Sqlite.Entityes.Song> songListData = new ArrayList();

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
        getLastPageSQLite();
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

    void getLastPageSQLite() {
        new Thread () {
            @Override
            public void run() {
                super.run();
                AppDataBase dataBase = MyApplication.getDataBase();
                SongDAO songDAO = dataBase.songDAO();

                List<com.example.univerlabs_4.Sqlite.Entityes.Song> songList = songDAO.getLastPAge();
                DisplayPage(songList);
                boolean isOnline = CheckNetwork();
                if (isOnline) {
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            getCurrentSongFromAPI();
                        }
                    }, 0, 20 * 1000);
                } else {
                    ShowErrorNetwork();
                }
            }
        }.start();
    }

    void insertInDB(com.example.univerlabs_4.Sqlite.Entityes.Song song) {
        final com.example.univerlabs_4.Sqlite.Entityes.Song s = song;
        new Thread() {
            @Override
            public void run() {
                super.run();
                AppDataBase dataBase = MyApplication.getDataBase();
                SongDAO songDAO = dataBase.songDAO();
                songDAO.insert(s);
            }
        }.start();

    }

    void DisplayPage(List<com.example.univerlabs_4.Sqlite.Entityes.Song> songList) {
        final List<com.example.univerlabs_4.Sqlite.Entityes.Song> list = songList;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                songListData.addAll(list);
                musicAdapter.notifyDataSetChanged();
            }
        });
    }

    void DisplaySong(com.example.univerlabs_4.Sqlite.Entityes.Song song) {
        final com.example.univerlabs_4.Sqlite.Entityes.Song s = song;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                songListData.add(s);
                musicAdapter.notifyDataSetChanged();
            }
        });
    }

    //----
    boolean CheckNetwork() {
        try {
            int timeOutMs = 1500;
            Socket socket = new Socket();
            SocketAddress address = new InetSocketAddress("8.8.8.8",53);
            socket.connect(address, timeOutMs);
            socket.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    void ShowErrorNetwork () {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Запуск в автономном режиме (доступен только просмотр внесенных ранее записей)", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //----

    void getCurrentSongFromAPI() {
        Call<Song> getCurrentSong =  songApi.getCurrentSong("4707login", "4707pass");
        getCurrentSong.enqueue(new Callback<Song> () {

            @Override
            public void onResponse(Call<Song> call, Response<Song> response) {
                if(response.isSuccessful()) {
                    final Song currentSong = response.body();
                    if(ruleInsertingCurrentSong(currentSong, songListData)){
                        com.example.univerlabs_4.Sqlite.Entityes.Song songDB = convertSongAPItoSongDB(currentSong);
                        DisplaySong(songDB);
                        insertInDB(songDB);
                    }
                }
            }

            @Override
            public void onFailure(Call<Song> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    com.example.univerlabs_4.Sqlite.Entityes.Song convertSongAPItoSongDB(Song song) {
        return new com.example.univerlabs_4.Sqlite.Entityes.Song(song.getId(), song.getName(), song.getArtist(), new Date(), song.getDuration(), song.getIcon());
    }

    boolean ruleInsertingCurrentSong(Song current, List<com.example.univerlabs_4.Sqlite.Entityes.Song> list) {
        return list.size() > 0 ? isSongMatch(current, getLastSongFromDB(list)) :true;
    }

    com.example.univerlabs_4.Sqlite.Entityes.Song getLastSongFromDB(List<com.example.univerlabs_4.Sqlite.Entityes.Song> list) {
        return  list.get(list.size() - 1);
    }

    boolean isSongMatch(Song a, com.example.univerlabs_4.Sqlite.Entityes.Song b) {
        return a.getId() != b.getId();
    }


    class MusicAdapter extends ArrayAdapter<com.example.univerlabs_4.Sqlite.Entityes.Song> {

        LayoutInflater inflater;
        int layout;
        List<com.example.univerlabs_4.Sqlite.Entityes.Song> songList;

        public MusicAdapter(@NonNull Context context, int resource, List<com.example.univerlabs_4.Sqlite.Entityes.Song> songList) {
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

            com.example.univerlabs_4.Sqlite.Entityes.Song song = songList.get(position);

            id.setText(String.valueOf(song.getId()));
            name.setText(song.getName());
            artist.setText(song.getArtist());



            timeAdd.setText(formatTime(song.getAddTime()));


            Picasso.get().load(baseUrl + song.getIcon()).into(imageView);

            return view;
        }
    }

    String formatTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
}

/*
package com.example.demo;

import com.example.demo.Entityes.Song;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


//TODO: subscribe(scrolToTop) -> getPageSQLite -> DisplayPage
//TODO: Использовать Fragments, чтоюы не дублровать задачи при повороте экрана
//TODO: recicle View
//TODO: rounded icons  https://stackoverflow.com/questions/30704581/make-imageview-with-round-corner-using-picasso
//TODO: progress bar
//TODO: fix represebt ttme + animation when add
//TODO: move prepareApi & prepareSQLite to Dager or Application Class As Singleton
//TODO: подключить Retrofit и Room к Dagger
//TODO: переписать на  RX + MVP / Testing / MVC / MVVVM /
//      решение1 : https://startandroid.ru/ru/blog/492-kak-obernut-metod-v-rxjava.html
//      решение1 : сделать асинхрнными запросы и обновление списка
//      решение1 : Future Java (Promise)
//TODO: NetWorkService.java habr Android ИЛИ
//TODO: добавить схему Schema export directory is not provided to the annotation processor so we cannot export the schema. You can either provide `room.schemaLocation` annotation processor argument OR set exportSchema to false.
//      решение: https://stackoverflow.com/questions/44322178/room-schema-export-directory-is-not-provided-to-the-annotation-processor-so-we
//TODO: DurationGson
//TODO: передача музыкальных файлов
//TODO: поблочное воспроизведение и подгрузка музыкальных файлов
//TODO: логгирование ошибок и DateTime
//TODO: добавление нескольких плейлистов
//TODO: парсер музыки и данных


//TODO: Работает ли ?
//Looper.prepare() // to be able to make toast
//Toast.makeText(context, "not connected", Toast.LENGTH_LONG).show()
//Looper.loop()


@SpringBootApplication
@RestController
public class DemoApplication {

    static PlayList playList1 = new PlayList();
    static Radio radio = new Radio(playList1, 10);

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @GetMapping("/hello1")
    public String hello1(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello1 %s!", name);
    }

    @GetMapping("/hello2")
    public String hello2(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello2 %s!", name);
    }

    @PostMapping("/api_get_current_song")
    public Song getCurrentSong(@RequestParam String login, @RequestParam String password) {
        if(login.equals("4707login") && password.equals("4707pass")){
            return radio.getCurrentSong();
        }
        return null;
    }
}


class User {
    public String login;
    public String password;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

class Radio {

    PlayList playList;
    Date upTime;
    Thread checkoutSong;
    long repeatCount = 1;

    Radio(PlayList playList, long repeatCount ) {
        this.playList = playList;
        this.repeatCount = repeatCount;
        upTime = new Date();
        checkoutSong = new Thread(checkoutSongByTime);
        checkoutSong.setDaemon(true);
        checkoutSong.start();
    }

    Runnable checkoutSongByTime = new Runnable() {
        @Override
        public void run() {
            Timer timer = new Timer();
            long delay = 0;
            for(int i = 0; i < repeatCount; i++) {
                for (Song song :playList.getSongs()) {
                    final long duration = song.getDuration() * 1000;
                    delay += duration;
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            playList.changeSong();
                        }
                    }, delay);
                }
            }
        }
    };


    public Song getCurrentSong() {
        return playList.getCurrentSong();
    }
}

class PlayList {

    private static final String base_url = "images/songs/";

    Song[] songs;
    int currentSong = 0;

    PlayList() {
        songs = new Song[] {
                new Song(0, "i just want leave", "martin", 30, base_url + "Co7SMFQ7Wog.jpg"),
                new Song(1, "Neon Demon", "martin", 17, base_url + "GdVJtYcZyE0.jpg"),
                new Song(2, "Babel (rmx)", "martin", 18, base_url + "vPPvkTI72W4.jpg"),
                new Song(3, "daleko", "martin", 20, base_url + "XMiEiNoJaeo.jpg"),
                new Song(4, "Down", "martin", 40, base_url + "zUJE7hbpsyY.jpg"),
        };
    }

    public Song[] getSongs() {
        return songs;
    }

    public void changeSong() {
        if(currentSong < songs.length -1) {
            currentSong++;
        } else {
            currentSong = 0;
        }
    }

    public Song getCurrentSong(){
        return  songs[currentSong];
    }
}
 */

/*
package com.example.demo.Entityes;

public class Song {
    private long id;
    private String name;
    private String artist;
    private long duration;
    private String icon;

    public Song(long id, String name, String artist, long durationSec, String icon) {
        this.id = id;
        this.name = name;
        this.artist = artist;
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

**/
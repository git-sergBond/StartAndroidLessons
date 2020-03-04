package com.example.univerlabs_4.Sqlite.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.univerlabs_4.Sqlite.Entityes.Song;

import java.util.List;

@Dao
public interface SongDAO {
    @Query("SELECT * FROM (SELECT * FROM Song ORDER BY addTime DESC LIMIT 10) ORDER BY addTime ASC")
    List<Song> getLastPAge();

    @Insert
    void insert(Song song);

    @Query("DELETE FROM Song")
    void nukeTable();
}

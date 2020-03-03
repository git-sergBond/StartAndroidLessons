package com.example.univerlabs_4.Sqlite;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.univerlabs_4.Sqlite.DAO.SongDAO;
import com.example.univerlabs_4.Sqlite.Entityes.Song;
import com.example.univerlabs_4.helpers.Room.DateConverters;

@Database(entities = {Song.class}, version = 2, exportSchema = false)
@TypeConverters({DateConverters.class})
public abstract class AppDataBase extends RoomDatabase {
    public abstract SongDAO songDAO();
}

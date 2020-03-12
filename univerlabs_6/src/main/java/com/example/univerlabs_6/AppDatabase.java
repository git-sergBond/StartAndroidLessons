package com.example.univerlabs_6;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Remainder.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract RemainderDAO remainderDAO();
}
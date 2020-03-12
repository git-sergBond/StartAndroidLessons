package com.example.univerlabs_6;

import android.app.Application;

import androidx.room.Room;

public class App extends Application {
    public static App instance;

    private static AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, AppDatabase.class, "remainderDB")
                .build();
    }

    public static App getInstance() {
        return instance;
    }

    public static AppDatabase getDatabase() {
        return database;
    }
}

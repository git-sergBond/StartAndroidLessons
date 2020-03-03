package com.example.univerlabs_4;

import android.app.Application;

import androidx.room.Room;

import com.example.univerlabs_4.Sqlite.AppDataBase;

public class MyApplication extends Application {
    public static MyApplication instance;
    static AppDataBase appDataBase;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        appDataBase = Room.databaseBuilder(getApplicationContext(), AppDataBase.class, "Lab4.db")
                .fallbackToDestructiveMigration()//TODO: не делай эту хуйню ПЖ в след раз !!!!
                .build();
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public static AppDataBase getDataBase() {
        return appDataBase;
    }
}

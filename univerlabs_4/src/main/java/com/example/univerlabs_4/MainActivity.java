package com.example.univerlabs_4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.univerlabs_4.Sqlite.AppDataBase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void launch1to3(View view) {
        Intent i = new Intent(this, tasks1to3.class);
        startActivity(i);
    }

    public void launchLab4(View view) {
        Intent i = new Intent(this,lab4_main.class);
        startActivity(i);
    }

    public void deleteDataBaseLab4(View view) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                AppDataBase dataBase = MyApplication.getDataBase();
                dataBase.songDAO().nukeTable();
            }
        }.start();
    }
}

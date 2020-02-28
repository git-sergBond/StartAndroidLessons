package com.example.univerlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.univerlabs.store_db.v2.lab3_classmatesDBHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void handlerGoToLab1(View view) {
        startActivity(new Intent(this, Activity1.class));
    }

    public void handlerGoToLab2(View view) {
        startActivity(new Intent(this, lab2_activity1.class));
    }

    public void handlerGoToLab3(View view) {
        startActivity(new Intent(this, lab3_main.class));
    }

    public void handlerGoToLab4(View view) {
        startActivity(new Intent(this, lab3_part2_main.class));
    }

    public void  handlerBtn_deletedb (View v) {
        deleteDatabase(lab3_classmatesDBHelper.DATABASE_NAME);
    }
}
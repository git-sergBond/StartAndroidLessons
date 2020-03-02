package com.example.univerlabs_4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
}

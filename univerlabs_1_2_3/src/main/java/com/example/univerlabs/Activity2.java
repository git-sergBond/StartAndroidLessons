package com.example.univerlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Activity2 extends AppCompatActivity {

    static String KEY_DATA = "com.example.univerlabs.Activity2.KEY_DATA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        String geted_string = getIntent().getStringExtra(KEY_DATA);
        ((TextView)findViewById(R.id.textView)).setText("Переданный параметр: " + geted_string);
    }
}

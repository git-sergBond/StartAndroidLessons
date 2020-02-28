package com.example.univerlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Activity1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1);
    }

    public void handlerBtn1(View view) {
        Intent i = new Intent(this, Activity2.class);
        i.putExtra(Activity2.KEY_DATA, "Бондаренко");
        startActivity(i);
    }
}

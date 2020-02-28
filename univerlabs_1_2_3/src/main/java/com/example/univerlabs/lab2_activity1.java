package com.example.univerlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class lab2_activity1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab2_activity1);
    }

    public void handlerBtn1(View view) {
        Intent i = new Intent(this, lab2_activity2.class);
        startActivity(i);
    }

    public void handlerBtn2(View view) {
        Intent i = new Intent(this, lab2_activity3.class);
        startActivity(i);
    }

    public void handlerBtn3(View view) {
        Intent i = new Intent(this, lab2_activity4.class);
        startActivity(i);
    }

    public void handlerBtn4(View view) {
        moveTaskToBack(true);
        finish();
    }
}

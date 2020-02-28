package com.example.univerlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class lab2_activity4 extends AppCompatActivity {

    Button button21;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab2_activity4);
        button21 = (Button)findViewById(R.id.button21);
        /*
        button21.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    button21.setBackgroundDrawable(R.drawable.my_button_bg_pressed);
                    return  true;
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    button21.setBackgroundDrawable(R.drawable.my_button_bg);
                    return  true;
                }
                return false;
            }
        });*/
    }

    public void handlerBtn1(View view) {

    }
}

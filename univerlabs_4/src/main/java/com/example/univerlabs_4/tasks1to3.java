package com.example.univerlabs_4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class tasks1to3 extends AppCompatActivity {

    TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks1to3);
    }

    public void btnTask1(View view) {
        Task1 t = new Task1();
        t.execute("l");
    }

    static class Task1 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return null;
        }
    }
}

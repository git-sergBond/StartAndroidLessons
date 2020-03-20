package com.example.univerlabs_4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.concurrent.TimeUnit;

public class tasks1to3 extends AppCompatActivity {


    String baseUrl = "http://10.0.2.2:8080/";

    TextView tv1, tv2, tvStats, tvView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks1to3);
        tv1 = findViewById(R.id.textView1);
        tv2 = findViewById(R.id.textView2);
        tvStats = findViewById(R.id.tvStats);
        tvView3 = findViewById(R.id.textView3);
    }

    public void btnTask1(View view) {
        Task1 t = new Task1(this);
        t.execute(baseUrl + "hello1", baseUrl + "hello2");
    }

    public void btnTask2(View view) {
        Task2 t = new Task2(this);
        t.execute(baseUrl + "hello1", baseUrl + "hello2");
    }

    public void btntask3(View view) {
        new Task3(this).execute();
    }
}

class Task1 extends AsyncTask<String, String, Void> {

    tasks1to3 activity;

    Task1(tasks1to3 activity) {
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(String... urls) {
        RequestQueue queue = Volley.newRequestQueue(activity);

        StringRequest req1 = new StringRequest(Request.Method.GET, urls[0], new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                publishProgress("1", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        StringRequest req2 = new StringRequest(Request.Method.GET, urls[1], new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                publishProgress("2", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(req1);
        queue.add(req2);
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        switch(values[0]) {
            case "1":
                activity.tv1.setText(values[1]);
                break;
            case "2":
                activity.tv2.setText(values[1]);
                break;
        }

    }
}

class Task2 extends AsyncTask<String, Integer, Void> {

    tasks1to3 activity;

    Task2(tasks1to3 activity) {
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(String... urls) {
        int cnt = 0;
        for(String s : urls) {
            Log.d("dbg processing", s);
            try {
                cnt++;
                publishProgress(cnt);
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        activity.tvStats.setText("обработано " + values[0] + " параметров");
    }
}

class Task3 extends AsyncTask<Void, Void, Void> {

    tasks1to3 activity;

    Task3(tasks1to3 activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        activity.tvView3.setText("Begin");
    }

    @Override
    protected Void doInBackground(Void... voids) {
        for(int i = 0; i < 3; i++) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        activity.tvView3.setText("End");
    }
}
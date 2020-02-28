package com.example.p0811_handlersimplemessage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    final String myLogs = "myLogs";

    final int STATUS_NONE = 0, //have not connect
              STATUS_CONNECTING = 1,
              STATUS_CONNECTED = 2;

    Handler h;

    TextView tvStatus;
    ProgressBar pbConnect;
    Button btnConnect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvStatus = (TextView) findViewById(R.id.tvStatus);
        pbConnect = (ProgressBar) findViewById(R.id.pbConnect);
        btnConnect = (Button) findViewById(R.id.btnConnect);

        h = new myHandler(this);
        h.sendEmptyMessage(STATUS_NONE);
    }

    public void setStatus(int status){
        switch (status) {
            case STATUS_NONE: setSTATUS_NONE(); break;
            case STATUS_CONNECTING: setSTATUS_CONNECTING(); break;
            case STATUS_CONNECTED: setSTATUS_CONNECTED(); break;
        }
    }

    void setSTATUS_NONE() {
        btnConnect.setEnabled(true);
        tvStatus.setText("Not Connected");
        pbConnect.setVisibility(View.GONE);
    }

    void setSTATUS_CONNECTING(){
        btnConnect.setEnabled(false);
        tvStatus.setText("Connecting");
        pbConnect.setVisibility(View.VISIBLE);
    }

    void setSTATUS_CONNECTED () {
        tvStatus.setText("Connected");
        pbConnect.setVisibility(View.GONE);
    }

    public void connect(View view) {

        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    h.sendEmptyMessage(STATUS_CONNECTING);
                    TimeUnit.SECONDS.sleep(2);
                    h.sendEmptyMessage(STATUS_CONNECTED);
                    TimeUnit.SECONDS.sleep(3);
                    h.sendEmptyMessage(STATUS_NONE);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(r).start();
    }
}

class myHandler extends Handler {
    static public WeakReference<MainActivity> activity;
    myHandler(MainActivity activity) {
        this.activity = new WeakReference<MainActivity>(activity);
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        MainActivity a = activity.get();
        if(a != null) a.setStatus(msg.what);
    }
}
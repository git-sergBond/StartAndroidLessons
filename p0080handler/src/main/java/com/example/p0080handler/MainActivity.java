package com.example.p0080handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    final String LOG_TAG = "myLOGS";

    Myhandler h;
    TextView tvInfo;
    Button btnStart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvInfo = findViewById(R.id.tvInfo);
        btnStart = findViewById(R.id.btnStart);
        h = new Myhandler(this);
    }

    public void setCountDownload(int countDownload) {
        tvInfo.setText("Downloaded files: " + countDownload);
        if(countDownload == 10) btnStart.setEnabled(true);
    }

    public void handlerBtnStart(View view) {

        btnStart.setEnabled(false);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i <= 10; i++) {
                    downloadFile();
                    h.sendEmptyMessage(i);
                    //tvInfo.setText("Downloaded files: " + msg.what);
                    Log.d(LOG_TAG, "Downloaded files: " + i);
                }
            }
        });
        t.start();
    }

    public void handlerBtnTest(View view) {
        Log.d(LOG_TAG,"Test");
    }

    void downloadFile () {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


class Myhandler extends Handler {
    static WeakReference<MainActivity> wrActivity;

    public Myhandler(MainActivity activity) {
        wrActivity = new WeakReference<MainActivity>(activity);
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        MainActivity activity = wrActivity.get();
        if(activity != null) {
            activity.setCountDownload(msg.what);
        }
    }
}

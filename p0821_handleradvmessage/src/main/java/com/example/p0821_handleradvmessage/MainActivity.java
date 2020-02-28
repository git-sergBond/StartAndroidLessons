package com.example.p0821_handleradvmessage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.sql.Time;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    final String LOG_TAG = "myLogs";

    public static final int STATUS_NONE = 0; // нет подключения
    public static final int STATUS_CONNECTING = 1; // подключаемся
    public static final int STATUS_CONNECTED = 2; // подключено
    public static final int STATUS_DOWNLOAD_START = 3; // загрузка началась
    public static final int STATUS_DOWNLOAD_FILE = 4; // файл загружен
    public static final int STATUS_DOWNLOAD_END = 5; // загрузка закончена
    public static final int STATUS_DOWNLOAD_NONE = 6; // нет файлов для загрузки

    Handler h;

    public TextView tvStatus;
    public ProgressBar pbDownload;
    public Button btnConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvStatus = (TextView) findViewById(R.id.tvStatus);
        pbDownload = (ProgressBar) findViewById(R.id.pbDownload);
        btnConnect = (Button) findViewById(R.id.btnConnect);

        h = new myHandler(this);

        h.sendEmptyMessage(STATUS_NONE);
    }

    public void btnConnect(View view) {
        Thread thread = new Thread(new Runnable() {

            Message msg;
            byte[] file;
            Random rand = new Random();

            @Override
            public void run() {
                try {
                    h.sendEmptyMessage(STATUS_CONNECTING);
                    TimeUnit.SECONDS.sleep(1);

                    h.sendEmptyMessage(STATUS_CONNECTED);
                    TimeUnit.SECONDS.sleep(1);

                    int filesCount = rand.nextInt(5);

                    if(filesCount == 0) {
                        h.sendEmptyMessage(STATUS_DOWNLOAD_NONE);
                        TimeUnit.MILLISECONDS.sleep(1500);
                        h.sendEmptyMessage(STATUS_NONE);
                        return;
                    }

                    msg = h.obtainMessage(STATUS_DOWNLOAD_START, filesCount, 0);
                    h.sendMessage(msg);

                    for (int i = 1; i <= filesCount; i++) {
                        file = downloadFile();
                        msg = h.obtainMessage(STATUS_DOWNLOAD_FILE, i, filesCount - i, file);
                        h.sendMessage(msg);
                    }

                    h.sendEmptyMessage(STATUS_DOWNLOAD_END);

                    TimeUnit.MILLISECONDS.sleep(1500);
                    h.sendEmptyMessage(STATUS_NONE);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void saveFile(byte[] file) {

    }

    byte[] downloadFile() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2);
        return  new byte[1024];
    }
}

class myHandler extends Handler {

    WeakReference<MainActivity> activity;

    myHandler(MainActivity main) {
        activity = new WeakReference<MainActivity>(main);
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        MainActivity activ = activity.get();
        switch (msg.what) {
            case MainActivity.STATUS_NONE:
                activ.btnConnect.setEnabled(true);
                activ.tvStatus.setText("Not conected");
                activ.pbDownload.setVisibility(View.GONE);
                break;
            case MainActivity.STATUS_CONNECTING:
                activ.btnConnect.setEnabled(false);
                activ.tvStatus.setText("Connecting");
                break;
            case MainActivity.STATUS_CONNECTED:
                activ.tvStatus.setText("Connected");
                break;
            case MainActivity.STATUS_DOWNLOAD_START:
                activ.tvStatus.setText("Start download " + msg.arg1 + " files");
                activ.pbDownload.setMax(msg.arg1);
                activ.pbDownload.setProgress(0);
                activ.pbDownload.setVisibility(View.VISIBLE);
                break;
            case MainActivity.STATUS_DOWNLOAD_FILE:
                activ.tvStatus.setText("Downloading. Left " + msg.arg2 + " files");
                activ.pbDownload.setProgress(msg.arg1);
                activ.saveFile((byte[]) msg.obj);
                break;
            case MainActivity.STATUS_DOWNLOAD_END:
                activ.tvStatus.setText("Download complete!");
                break;
            case MainActivity.STATUS_DOWNLOAD_NONE:
                activ.tvStatus.setText("No files for download");
                break;
        }
    }
}
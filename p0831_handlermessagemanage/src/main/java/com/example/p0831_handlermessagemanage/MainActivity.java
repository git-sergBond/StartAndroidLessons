package com.example.p0831_handlermessagemanage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    final String LOG_TAG = "myLogs";

    Handler h;

    Handler.Callback cb = new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            Log.d(LOG_TAG, "what " + message.what);
            return false;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        h = new Handler(cb);
        sendMessages();
    }

    public void sendMessages() {
        Log.d(LOG_TAG, "sendMessages");
        h.sendEmptyMessageDelayed(1, 1000);
        h.sendEmptyMessageDelayed(1, 1000);
        h.sendEmptyMessageDelayed(2, 2000);
        h.sendEmptyMessageDelayed(3, 3000);
        h.removeMessages(1);
    }

    @Override
    protected void onDestroy() {
        if(h != null) h.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}

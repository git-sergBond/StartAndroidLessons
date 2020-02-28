package com.example.p0911_asynctaskrotate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.TextView;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    static final String QWE_LOG = "qwe";

    MyTask mt;

    TextView tv; String KEY_STATE_tv = "MainActivity.tv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(QWE_LOG, "create main activity " + this.hashCode());
        tv = findViewById(R.id.tv);
        Object link = getLastCustomNonConfigurationInstance();
        if(link == null) {
            mt = new MyTask();
            mt.execute();
        } else {
            mt = (MyTask)link;
            if(mt.textTriedPublish != null) {
                tv.setText(mt.textTriedPublish);
            }
        }

        mt.link(this);

        Log.d(QWE_LOG, "create task " + mt.hashCode());
    }

    @Nullable
    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        mt.unlink();
        return mt;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        String text = tv.getText().toString();
        outState.putString(KEY_STATE_tv, text);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        tv.setText(savedInstanceState.getString(KEY_STATE_tv));
    }

    static class MyTask extends AsyncTask<String, Integer, Void> {

        MainActivity activity;

        String textTriedPublish;

        void link (MainActivity act) {
            activity = act;
        }

        void unlink () {
            activity = null;
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                for(int i = 1; i<=10; i++) {
                    TimeUnit.SECONDS.sleep(1);
                    publishProgress(i);
                    Log.d(QWE_LOG, "i = " + i
                    + ", myTask " + this.hashCode()
                            + ", mainAciv " + activity.hashCode());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            textTriedPublish = "i = " + values[0];
            if(activity.tv != null) {
                activity.tv.setText("i = " + values[0]);
            }
        }
    }
}

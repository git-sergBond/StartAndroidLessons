package com.example.p0901_asynctaskstatus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    final String LOG_TAG = "myLog";

    MyTask[] mt = new MyTask[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onclick (View v) {
        switch (v.getId()) {
            case R.id.btnStart1:
                mt[0] = startTask(R.id.tvInfo1, "A");
                break;
            case R.id.btnStart2:
                mt[1] = startTask(R.id.tvInfo2, "B");
                break;
            case R.id.btnStart3:
                mt[2] = startTask(R.id.tvInfo3, "C");
                break;
            case R.id.btnStart4:
                mt[3] = startTask(R.id.tvInfo4, "D");
                break;
                //-----------
            case R.id.btnStatus1:
                showStatus(mt[0]);
                break;
            case R.id.btnStatus2:
                showStatus(mt[1]);
                break;
            case R.id.btnStatus3:
                showStatus(mt[2]);
                break;
            case R.id.btnStatus4:
                showStatus(mt[3]);
                break;
                //-----------
            case R.id.btnCancel1:
                cancelTask123(mt[0]);
                break;
            case R.id.btnCancel2:
                cancelTask123(mt[1]);
                break;
            case R.id.btnCancel3:
                cancelTask123(mt[2]);
                break;
            case R.id.btnCancel4:
                cancelTask123(mt[3]);
                break;
        }
    }

    MyTask startTask(int IdViewOutput, String name) {
        MyTask t = new MyTask(IdViewOutput);
        t.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR , name);
        return t;
    }

    void showStatus(MyTask t) {
        if(t != null) {
            if(t.isCancelled()) {
                Toast.makeText(this, "CANCELED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, t.getStatus().toString(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    void cancelTask123(MyTask t) {
        if(t != null) {
            t.cancel(true);
            Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
        }
    }

    class MyTask extends AsyncTask<String, Integer, String> {

        TextView tv;

        MyTask(int idView){
            tv = findViewById(idView);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tv.setText("Begin");
        }

        @Override
        protected String doInBackground(String... str) {
            try {
                for(int i = 0; i < 5; i++) {
                    if(isCancelled()) return null;
                    TimeUnit.SECONDS.sleep(3);
                    publishProgress(i);
                }
            } catch (InterruptedException e) {
                Log.d(LOG_TAG, "interrupted");
                e.printStackTrace();
            }
            return str[0];
        }

        @Override
        protected void onProgressUpdate(Integer... status) {
            super.onProgressUpdate(status);
            tv.setText("state: "+status[0]);
        }

        @Override
        protected void onPostExecute(String id) {
            super.onPostExecute(id);
            tv.setText("End["+id+"]");
        }

        @Override
            protected void onCancelled(String id) {
            Log.d(LOG_TAG, "pre-cancel");
            super.onCancelled(id);
            if(id != null) Log.d(LOG_TAG, "post-cancel["+id+"]");
            Log.d(LOG_TAG, "post-cancel");
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            if(tv!= null) tv.setText("Caneled");
            Log.d(LOG_TAG, "mid-cancel");
        }
    }
}

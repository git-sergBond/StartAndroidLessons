package com.univerlabs_5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ActionBar;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class FileDownloader extends AppCompatActivity {

    private long lastDownload = -1L;
    private int lastFileID = -1;

    String getUrlForIDFile(int id) {
        final String baseUrl = "http://ntv.ifmo.ru/file/journal/";
        final String fileExtension = ".pdf";
        return baseUrl + id + fileExtension;
    }

    final String pdfType = "application/pdf";

    boolean isExtensionPdf(String uri, int idFile) {
        final String uriDownload = uri;
        final int id = idFile;
        lastFileID = id;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().get().url(uri).build();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ShowWarningAboutWrongTypeFile();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.btnDownload).setEnabled(false);
                    }
                });

                if(response.body().contentType().equals(MediaType.parse(pdfType))){

                    Environment.getDataDirectory().mkdirs();

                    Uri uri = Uri.parse(uriDownload);

                    lastDownload = downloadManager.enqueue(new DownloadManager.Request(uri)
                            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                            .setAllowedOverRoaming(false)
                            .setTitle("Download someFile")
                            .setDescription("Not useful file")
                            .setDestinationInExternalFilesDir(getApplicationContext(), Environment.DIRECTORY_DOWNLOADS,id + ".pdf"));

                } else {
                    ShowWarningAboutWrongTypeFile();
                }
            }
        });

        return false;
    }

    void ShowWarningAboutWrongTypeFile() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.btnDownload).setEnabled(true);
                Toast.makeText(getApplicationContext(), "wrong type document. Input another type file", Toast.LENGTH_SHORT).show();
            }
        });
    }

    DownloadManager downloadManager;



    TextView txtIDdDoc;
    ConstraintLayout rootActivity;

    PopupWindow pw;
    final String PREF_SHOW_POPUP = "PREF_SHOW_POPUP";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_downloader);
        txtIDdDoc = findViewById(R.id.txtIDdDoc);


        downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        registerReceiver(onNotivicationClick, new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED));

        sharedPreferences = getPreferences(MODE_PRIVATE);
        showPopUp();
    }

    void showPopUp() {

        if(sharedPreferences.getBoolean(PREF_SHOW_POPUP, true)){
            LayoutInflater inflater = getLayoutInflater();
            View popView = inflater.inflate(R.layout.popup, null, false);
            pw = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rootActivity = findViewById(R.id.DownloadActivity);
            rootActivity.post(new Runnable() {//TODO learn this
                @Override
                public void run() {
                    pw.setTouchable(true);
                    pw.setFocusable(true);
                    pw.showAtLocation(rootActivity, Gravity.CENTER, 0, 0);
                }
            });

            final CheckBox showOnNestTime = popView.findViewById(R.id.chbNotShow);
            popView.findViewById(R.id.btnOK).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(showOnNestTime.isChecked()) {
                        SharedPreferences.Editor editor =  sharedPreferences.edit();
                        editor.putBoolean(PREF_SHOW_POPUP, false);
                        editor.commit();
                    }
                    pw.dismiss();
                }
            });
        }
    }

    public void btnDelete(View view) {

    }

    public void btnWatch(View view) {
        //startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
        /*
        if(lastFileID != -1) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), lastFileID + ".pdf")), pdfType);
            startActivity(intent);
        }*/
    }

    public void btnDowload(View view) {
        int id;
        try{
            id = Integer.parseInt(txtIDdDoc.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Wrong Number", Toast.LENGTH_SHORT).show();
            return;
        }

        String DownloadUrl = getUrlForIDFile(id);
        isExtensionPdf(DownloadUrl, id);
    }

    BroadcastReceiver onComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            findViewById(R.id.btnDownload).setEnabled(true);
        }
    };

    BroadcastReceiver onNotivicationClick = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Ummmm...hi!", Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onDestroy() {
        unregisterReceiver(onComplete);
        unregisterReceiver(onNotivicationClick);
        super.onDestroy();
    }
}

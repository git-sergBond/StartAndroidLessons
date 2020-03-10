package com.univerlabs_5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;

public class MainActivity extends AppCompatActivity {

    OkHttpClient socketClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        socketClient = new OkHttpClient();
    }

    public void btnCheckConnection(View view) {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = false;
        if(networkInfo != null) if(networkInfo.isConnectedOrConnecting()) isConnected = true;
        if(isConnected) {
            Toast.makeText(this, "OK - Connected", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error - Net isn't connect", Toast.LENGTH_SHORT).show();
        }
    }

    public void btnTestWebSokcet(View view) {
        Request request = new Request.Builder().url("ws://192.168.1.85:8080/testsocket").build();
        WebSocketListener listener = new WebSocketListener(this);
        WebSocket ws = socketClient.newWebSocket(request, listener);
        ws.send("World");

        ws.close(WebSocketListener.NORMAL_CLOSURE_SATUS, "goodbye!");
        socketClient.dispatcher().executorService().shutdown();
    }

    public void btnDownloadUrl(View view) {
        WebView webView = findViewById(R.id.webView);
        // включаем поддержку JavaScript
        webView.getSettings().setJavaScriptEnabled(true);
        // указываем страницу загрузки
        webView.loadUrl("https://www.google.ru/");
    }

    public void btnExternFiles(View view) {
        Intent intent = new Intent(this, FileDownloader.class);
        startActivity(intent);
    }
}

class WebSocketListener extends okhttp3.WebSocketListener {

    MainActivity context;

    public WebSocketListener(MainActivity context) {
        this.context = context;
    }

    public static final int NORMAL_CLOSURE_SATUS = 1000;

    @Override
    public void onOpen(WebSocket webSocket, Response response) {

    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        final String t = text;
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context.getApplicationContext(), t, Toast.LENGTH_SHORT).show();
            }
        });

        super.onMessage(webSocket, text);
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        webSocket.close(NORMAL_CLOSURE_SATUS, null);
        super.onClosing(webSocket, code, reason);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        super.onFailure(webSocket, t, response);
    }

    /*
    * @Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(new SocketTextHandler(), "/testsocket").setAllowedOrigins("*");
    }
}
@Component
public class SocketTextHandler extends TextWebSocketHandler {
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        session.sendMessage(new TextMessage("hello! " + payload));
    }
}

    * */
}
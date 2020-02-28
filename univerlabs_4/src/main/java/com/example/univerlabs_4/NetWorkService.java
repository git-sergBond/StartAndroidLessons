package com.example.univerlabs_4;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetWorkService {
    private static NetWorkService instance;
    private static String BASE_URL = "https://jsonplaceholder.typicode.com";
    private Retrofit mRetrofit;

    private NetWorkService () {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static NetWorkService getInstance() {
        if(instance == null) {
            instance = new NetWorkService();
        }
        return instance;
    }
}

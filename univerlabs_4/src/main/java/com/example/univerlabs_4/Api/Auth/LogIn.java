package com.example.univerlabs_4.Api.Auth;

import retrofit2.http.POST;

public interface LogIn {
    @POST("login")
    void LogIn(String login, String pass);
}
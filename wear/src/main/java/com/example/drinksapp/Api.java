package com.example.drinksapp;

import com.google.gson.JsonElement;


import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {

    String BASE_URL = "http://45.33.24.35:2998/api/";
    String DRINKS = "drinks/";
    String LOGIN = "auth/login/";

    @GET(DRINKS + "random")
    Call<Object> getRandomDrink();

    @POST(LOGIN)
    Call<Object> LoginUser(String eT, Object params);
}
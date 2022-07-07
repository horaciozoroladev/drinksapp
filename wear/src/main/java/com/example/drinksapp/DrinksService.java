package com.example.drinksapp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DrinksService {
    private static DrinksService instance = null;
    private Api myApi;

    private DrinksService() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        myApi = retrofit.create(Api.class);
    }

    public static synchronized DrinksService getInstance() {
        if (instance == null) {
            instance = new DrinksService();
        }
        return instance;
    }

    public Api getMyApi() {
        return myApi;
    }
}

/*package com.example.drinksapp;

import com.google.android.gms.common.api.Api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DrinksServiceMobile {
    private static DrinksService instance = null;
    private ApiMobile myApi;

    private DrinksServiceMobile() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL2)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        myApi = retrofit.create(ApiMobile.class);
    }

    public static synchronized DrinksService getInstance() {
        if (instance == null) {
            instance = new DrinksServiceMobile();
        }
        return instance;
    }

    public Api getMyApi() {
        return myApi;
    }
}
*/
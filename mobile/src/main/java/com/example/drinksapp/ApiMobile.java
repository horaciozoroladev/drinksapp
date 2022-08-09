package com.example.drinksapp;


import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiMobile {

    //String BASE_URL2 = "https://127.0.0.1/drinks__api/Validarusuario.php/";

    String BASE_URL2 = "http://45.33.24.35:2998/api/";
    String DRINKS = "drinks/";
    String LOGIN = "auth/login/";
    String LOGOUT = "auth/logout/";
    String FavoriteDrink = "drinks/save-favorite-drink/";
    String Registro = "users/create/";
    String DoneDrink = "drinks/save-done-drink/";

    @GET(DRINKS + "random")
    Call<Object> getRandomDrink();

    @POST(LOGIN)
    Call<Object> LoginUser(@Body JsonObject user);

    @POST(LOGOUT)
    Call<Object> LOGOUT(@Body JsonObject userlogout);

    @POST(FavoriteDrink)
    Call<Object> FavoriteDrinks(@Body JsonObject drinks);

    @POST(DoneDrink)
    Call<Object> DoneDrink(@Body JsonObject donedrinks);

    @POST(Registro)
    Call<Object> Register(@Body JsonObject registrarse);


}
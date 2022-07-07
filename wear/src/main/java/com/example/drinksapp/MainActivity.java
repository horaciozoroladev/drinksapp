package com.example.drinksapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.drinksapp.databinding.ActivityMainBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends Activity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getRandomDrink();
        onClickRefresh();
        onClickSendToMobile();

    }

    public void getRandomDrink() {

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);

        Call<Object> call = api.getRandomDrink();
        call.enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.isSuccessful()) {

                    JSONObject drink = new JSONObject((Map) response.body());
                    setRandomDrink(drink);

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                // System.out.println(t.toString());
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    public void setRandomDrink(JSONObject drink) {
        try {
            TextView strDrink = findViewById(R.id.strDrink);
            strDrink.setText(drink.getString("strDrink"));

            ImageView strDrinkThumb = findViewById(R.id.strDrinkThumb);
            Glide.with(MainActivity.this).load(drink.getString("strDrinkThumb")).into(strDrinkThumb);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickRefresh() {
            Button button = findViewById(R.id.buttonRefrescar);

            button.setOnClickListener(view -> {
                getRandomDrink();
            });
    }

    public void onClickSendToMobile() {
        Button button = findViewById(R.id.buttonEnviar);

        button.setOnClickListener(view -> {
            // send data to mobile DataClient -> DataItem
        });
    }
}
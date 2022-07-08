package com.example.drinksapp;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.drinksapp.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.CapabilityClient;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends Activity implements DataClient.OnDataChangedListener,
        MessageClient.OnMessageReceivedListener,
        CapabilityClient.OnCapabilityChangedListener {

    private ActivityMainBinding binding;
    DataClient dataClient;
    private static final String COUNT_KEY = "drink";
    JSONObject randomDrink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getRandomDrink();
        onClickRefresh();
        onClickSendToMobile();

    }

    @Override
    protected void onResume() {
        super.onResume();

        Wearable.getDataClient(this).addListener(this);
        Wearable.getMessageClient(this).addListener(this);
        Wearable.getCapabilityClient(this)
                .addListener(this, Uri.parse("mobile://"), CapabilityClient.FILTER_REACHABLE);
    }

    @Override
    protected void onPause() {
        super.onPause();

        Wearable.getDataClient(this).removeListener(this);
        Wearable.getMessageClient(this).removeListener(this);
        Wearable.getCapabilityClient(this).removeListener(this);
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
        randomDrink = drink;
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
            dataClient = Wearable.getDataClient(this);
            PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/drink").setUrgent();
            putDataMapReq.getDataMap().putString(COUNT_KEY, randomDrink.toString());
            PutDataRequest putDataReq = putDataMapReq.asPutDataRequest().setUrgent();
            Task<DataItem> putDataTask = dataClient.putDataItem(putDataReq);

            putDataTask.addOnSuccessListener(dataItem -> Toast.makeText(getApplicationContext(), "Revisa tu telefono 🍺", Toast.LENGTH_LONG).show());
        });
    }

    @Override
    public void onCapabilityChanged(@NonNull CapabilityInfo capabilityInfo) {

    }

    @Override
    public void onDataChanged(@NonNull DataEventBuffer dataEventBuffer) {

    }

    @Override
    public void onMessageReceived(@NonNull MessageEvent messageEvent) {

    }
}
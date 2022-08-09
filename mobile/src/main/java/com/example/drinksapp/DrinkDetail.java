package com.example.drinksapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.wearable.CapabilityClient;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Objects;
import java.util.Stack;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DrinkDetail extends AppCompatActivity implements DataClient.OnDataChangedListener,
        MessageClient.OnMessageReceivedListener, CapabilityClient.OnCapabilityChangedListener {

    private static final String COUNT_KEY = "drink";
    DataClient dataClient;
    private String strID = "";
    private String idDrinkEXT = "";
    private String strDrinkTxt = "";
    //private String _id = "";
    private Button Fbtn;
    private Button Dbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_detail);

        FavoriteDrinks();
        Logout();
        DoneDrinks();
        Fbtn = findViewById(R.id.buttonFavoritos);
        Dbtn = findViewById(R.id.buttonDone);
        Fbtn.setVisibility(View.INVISIBLE);
        Dbtn.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Instantiates clients without member variables, as clients are inexpensive to create and
        // won't lose their listeners. (They are cached and shared between GoogleApi instances.)
        Wearable.getDataClient(this).addListener(this);
        Wearable.getMessageClient(this).addListener(this);
        Wearable.getCapabilityClient(this)
                .addListener(this, Uri.parse("wear://"), CapabilityClient.FILTER_REACHABLE);
    }

    @Override
    protected void onPause() {
        super.onPause();

        Wearable.getDataClient(this).removeListener(this);
        Wearable.getMessageClient(this).removeListener(this);
        Wearable.getCapabilityClient(this).removeListener(this);
    }

    @Override
    public void onDataChanged(@NonNull DataEventBuffer dataEventBuffer) {


        Toast.makeText(getApplicationContext(), "Se recibio informacion de la bebida", Toast.LENGTH_LONG).show();
        for (DataEvent event : dataEventBuffer) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {

                DataItem item = event.getDataItem();

                if (item.getUri().getPath().compareTo("/drink") == 0) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    setDrinkInfo(dataMap.getString(COUNT_KEY));

                    Fbtn.setVisibility(View.VISIBLE);
                    Dbtn.setVisibility(View.VISIBLE);

                }

            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                // DataItem deleted
            }
        }
    }

    public void setDrinkInfo(String drinkStr) {

        ImageView strDrinkThumb = findViewById(R.id.strDrinkThumb);

        TextView strDrink = findViewById(R.id.strDrink);
        TextView strInstructions = findViewById(R.id.strInstructions);
        TextView strIngredients = findViewById(R.id.strIngredients);
        String ingredienteCantidad = " ";
        Stack<String> _ingredients = new Stack<String>();
        Stack<String> ingredients = new Stack<String>();
        Stack<String> measures = new Stack<String>();


        try {

            JSONObject drink = new JSONObject(drinkStr);

            // id
            try {
                String idDrink = drink.getString("idDrink");
                idDrinkEXT = idDrink;
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // STR
            try {
                String strDrinkTXT = drink.getString("strDrink");
                strDrinkTxt = strDrinkTXT;
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Glide.with(this).load(drink.getString("strDrinkThumb")).into(strDrinkThumb);
            strDrink.setText(drink.getString("strDrink"));
            strInstructions.setText(drink.getString("strInstructions"));

            for (Iterator<String> i = drink.keys(); i.hasNext(); ) {
                String key = i.next();
                Object value = drink.get(key);
                if (key.contains("strIngredient")) {
                    if (value != null || value != "") {
                        ingredients.push(value.toString());
                    }
                }

                if (key.contains("strMeasure")) {
                    if (value != null || value != "") {
                        measures.push(value.toString());
                    }
                }
            }

            for (int i = 0; i < ingredients.size(); i++) {
                String ing = "";
                String mes = "";

                if (!Objects.equals(ingredients.get(i), "null") && !Objects.equals(ingredients.get(i), "")) {
                    ing = ingredients.get(i);
                }

                if (!Objects.equals(measures.get(i), "null") && !Objects.equals(measures.get(i), "")) {
                    mes = measures.get(i).trim();
                }

                String fin = ing + ", " + mes;

                ingredienteCantidad = ingredienteCantidad + fin + ";\n ";
                ingredienteCantidad = ingredienteCantidad.replaceAll(" , ;", "");
                strIngredients.setText(ingredienteCantidad.trim());


            }

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    public void FavoriteDrinks() {
        Button buttonFavoritos = findViewById(R.id.buttonFavoritos);

        buttonFavoritos.setOnClickListener(view -> {

            Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiMobile.BASE_URL2)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiMobile apiMobile = retrofit.create(ApiMobile.class);

            String _id = getIntent().getStringExtra("_id");

            String eT = idDrinkEXT;
            String eT2 = _id;

            String jsonStr = "{'idDrink': '" + eT + "', 'users__id': '" + eT2 + "'}";

            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = (JsonObject) jsonParser.parse(jsonStr);

            Call<Object> call = apiMobile.FavoriteDrinks(jsonObject);
            call.enqueue(new Callback<Object>() {

                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {

                    if (response.isSuccessful()) {
                        Toast.makeText(DrinkDetail.this, "La Bebida '" + strDrinkTxt + "' se agrego a Favoritos ", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DrinkDetail.this, "Error al guardar la bebida..", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {

                    Toast.makeText(DrinkDetail.this, "Error al guardar la bebida..", Toast.LENGTH_SHORT).show();
                }

            });

        });
    }

    public void Logout() {
        Button buttonLogout = findViewById(R.id.buttonLogout);

        buttonLogout.setOnClickListener(view -> {

            Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiMobile.BASE_URL2)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiMobile apiMobile = retrofit.create(ApiMobile.class);

            String _id = getIntent().getStringExtra("_id");
            String username = getIntent().getStringExtra("username");

            String eT = _id;
            String eT2 = username;

            String jsonStr = "{'username': '" + eT2 + "', '_id': '" + eT + "'}";

            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = (JsonObject) jsonParser.parse(jsonStr);


            Call<Object> call = apiMobile.LOGOUT(jsonObject);

            call.enqueue(new Callback<Object>() {

                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {

                    if (response.isSuccessful()) {
                        Toast.makeText(DrinkDetail.this, "Se cerro la sesion", Toast.LENGTH_SHORT).show();
                        Intent Logoutbtn = new Intent(view.getContext(), MainActivity.class);
                        startActivity(Logoutbtn);
                    } else {
                        Toast.makeText(DrinkDetail.this, "Error al cerrar sesion..", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {

                    Toast.makeText(DrinkDetail.this, "Error al cerrar sesion..", Toast.LENGTH_SHORT).show();
                }

            });

        });
    }

    public void DoneDrinks() {
        Button buttonDone = findViewById(R.id.buttonDone);

        buttonDone.setOnClickListener(view -> {

            Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiMobile.BASE_URL2)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiMobile apiMobile = retrofit.create(ApiMobile.class);

            String _id = getIntent().getStringExtra("_id");

            String eT = idDrinkEXT;
            String eT2 = _id;

            String jsonStr = "{'idDrink': '" + eT + "', 'users__id': '" + eT2 + "'}";
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = (JsonObject) jsonParser.parse(jsonStr);

            Call<Object> call = apiMobile.DoneDrink(jsonObject);
            call.enqueue(new Callback<Object>() {

                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {

                    if (response.isSuccessful()) {
                        Toast.makeText(DrinkDetail.this, "La Bebida '" + strDrinkTxt + "' se Realizo con exito ", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DrinkDetail.this, "Error al guardar la bebida..", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {

                    Toast.makeText(DrinkDetail.this, "Error al guardar la bebida..", Toast.LENGTH_SHORT).show();
                }

            });

        });
    }


    @Override
    public void onMessageReceived(@NonNull MessageEvent messageEvent) {

    }

    @Override
    public void onCapabilityChanged(@NonNull CapabilityInfo capabilityInfo) {

    }

    @Override
    public void onBackPressed() {

    }


}
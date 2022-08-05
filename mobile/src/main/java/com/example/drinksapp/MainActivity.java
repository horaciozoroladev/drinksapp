package com.example.drinksapp;

import static android.provider.Telephony.Mms.Part.TEXT;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Object params;

    EditText username, pass;
    Button buttonLogin;
    private String strTxt = "";
    private String _id = "";
    private String STRusername = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //EditText Text = findViewById(R.id.username);
        //Set Text = "";
        //System.out.println(findViewById(R.id.pass).toString());
        //String Txt2 = Text2;

        onClickLogin();

    }

    public void onClickLogin() {
        Button buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(view -> {

            Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiMobile.BASE_URL2)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiMobile apiMobile = retrofit.create(ApiMobile.class);

            EditText editText = findViewById(R.id.username);
            EditText editText2 = findViewById(R.id.pass);

            String eT = editText.getText().toString();
            String eT2 = editText2.getText().toString();

            String jsonStr = "{'username': '" + eT + "', 'pass': '" + eT2 + "'}";
            System.out.println(jsonStr);
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = (JsonObject) jsonParser.parse(jsonStr);

            Call<Object> call = apiMobile.LoginUser(jsonObject);
            call.enqueue(new Callback<Object>() {

                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {

                    try {
                        //response = client.newCall(request).execute();
                        JSONObject jj = new JSONObject((Map) response.body());
                        _id = jj.getString("_id");
                        //idDrinkEXT = idDrink;
                        System.out.println(_id);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        //response = client.newCall(request).execute();
                        JSONObject jj = new JSONObject((Map) response.body());
                        STRusername = jj.getString("username");
                        System.out.println(STRusername);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }



                    if (response.isSuccessful()) {

                        /*Intent Mylogin = new Intent(view.getContext(), DrinkDetail.class);
                        startActivity(Mylogin);*/
                        Intent intent = new Intent(view.getContext(), DrinkDetail.class);
                        //Bundle extras = new Bundle();
                        //extras.putString("_id", _id);
                        //extras.putString("username", STRusername);
                        intent.putExtra("_id", _id);
                        intent.putExtra("username", STRusername);
                        //intent.putExtras(extras);

                        startActivity(intent);
                        editText.setText("");
                        editText2.setText("");
                    } else {
                        Toast.makeText(MainActivity.this, "Contraseña o nombre incorrecto", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Contraseña o nombre incorrecto", Toast.LENGTH_SHORT).show();
                }

            });

        });
    }

    public void Registrarse(View view) {

        Intent registro = new Intent(this, RegisterUserActivity.class);
        startActivity(registro);
        EditText editText = findViewById(R.id.username);
        EditText editText2 = findViewById(R.id.pass);
        editText.setText("");
        editText2.setText("");
    }

    /*public void GetUserInfo() {
        /*Button buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(view -> {

            Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiMobile.BASE_URL2)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiMobile apiMobile = retrofit.create(ApiMobile.class);

            //EditText editText = findViewById(R.id.username);
            //EditText editText2 = findViewById(R.id.pass);

            String eT = editText.getText().toString();
            String eT2 = editText2.getText().toString();

            String jsonStr = "{'username': '" + eT + "', 'pass': '" + eT2 + "'}";
            System.out.println(jsonStr);
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = (JsonObject) jsonParser.parse(jsonStr);

            Call<Object> call = apiMobile.LoginUser(jsonObject);
            call.enqueue(new Callback<Object>() {

                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {

                    /*try {
                        String str = drink.getString("strDrink");
                        strTxt = str;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    System.out.println(strTxt);*/

                    /*if(response.body() != null ) {
                        String _id = getString(0);
                        System.out.println(_id);
                    }

                    if (response.isSuccessful()) {

                        Intent Mylogin = new Intent(view.getContext(), DrinkDetail.class);
                        startActivity(Mylogin);
                        editText.setText("");
                        editText2.setText("");
                    } else {
                        Toast.makeText(MainActivity.this, "Contraseña o nombre incorrecto", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Contraseña o nombre incorrecto", Toast.LENGTH_SHORT).show();
                }

            });

        //});
    }*/

}
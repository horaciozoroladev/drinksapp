package com.example.drinksapp;

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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Object params;

    EditText username, pass;
    Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        onClickLogin();
        //Inicializamos
        /*username = findViewById(R.id.username);
        pass = findViewById(R.id.pass);
        buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ValidarLogin("http://127.0.0.1/drinks__api/Validarusuario.php");
                //ValidarLogin("https://127.0.0.1/drinks__api/Validarusuario.php");
                //ValidarLogin("http://127.0.0.1:33061/drinks__api/Validarusuario.php");
                //ValidarLogin("https://127.0.0.1:33061/drinks__api/Validarusuario.php");

                //ValidarLogin("http://localhost/drinks__api/Validarusuario.php");
                //ValidarLogin("https://localhost/drinks__api/Validarusuario.php");
                //ValidarLogin("http://localhost:33061/drinks__api/Validarusuario.php");
                //ValidarLogin("https://localhost:33061/drinks__api/Validarusuario.php");

        //ValidarLogin("http://127.0.0.1/drinks__api/Validarusuario.php");
        //ValidarLogin("https://127.0.0.1/drinks__api/Validarusuario.php");
                //ValidarLogin("http://127.0.0.1:33061/drinks__api/Validarusuario.php");
                //ValidarLogin("https://127.0.0.1:33061/drinks__api/Validarusuario.php");

                //ValidarLogin("http://192.168.0.7/drinks__api/Validarusuario.php");
                //ValidarLogin("https://192.168.0.7/drinks__api/Validarusuario.php");
                //ValidarLogin("http://192.168.0.7:33061/drinks__api/Validarusuario.php");
                //ValidarLogin("https://192.168.0.7:33061/drinks__api/Validarusuario.php");

                //ValidarLogin("http://192.168.137.1/drinks__api/Validarusuario.php");
                //ValidarLogin("https://192.168.137.1/drinks__api/Validarusuario.php");
                //ValidarLogin("http://192.168.137.1:33061/drinks__api/Validarusuario.php");
                //ValidarLogin("https://192.168.137.1:33061/drinks__api/Validarusuario.php");

                //Funciona para pixel emulado
                //ValidarLogin("http://10.0.2.2:8080/drinks__api/Validarusuario.php");
                ValidarLogin("https://localhost/drinks__api/Validarusuario.php");
            }
        });*/
    }

    /*private void ValidarLogin(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                if(!response.isEmpty()) {
                    Intent intent = new Intent(getApplicationContext(), DrinkDetail.class);
                    startActivity(intent);

                }else {
                    Toast.makeText(MainActivity.this, "usuario o contraseña incorrectos 2", Toast.LENGTH_SHORT).show();
                }
                System.out.println(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "usuario o contraseña incorrectos 3", Toast.LENGTH_SHORT).show();
                System.out.println(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> Parametros = new HashMap<String, String>();
                Parametros.put("username", username.getText().toString());
                Parametros.put("pass", pass.getText().toString());
                return Parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }*/

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
            //eT = editText.getText().toString();
            String eT2 = editText2.getText().toString();
            //eT2 = editText2.getText().toString();
            System.out.println("sexo");

            String jsonStr = "{'username': '" + eT + "', 'pass': '" + eT2 + "'}";
            System.out.println(jsonStr);
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = (JsonObject) jsonParser.parse(jsonStr);

            System.out.println("success");
            Call<Object> call = apiMobile.LoginUser(jsonObject);
            System.out.println("success 2");
            call.enqueue(new Callback<Object>() {

                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    System.out.println(response);
                    if (response.isSuccessful()) {
                        System.out.println("success");
                        //JSONObject drink = new JSONObject((Map) response.body());
                        //setRandomDrink(drink);
                        /*Intent Mylogin = new Intent(this, DrinkDetail.class);
                        startActivity(Mylogin);*/
                        System.out.println("EMILIANOOOOOOOOOO");
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    System.out.println(t);
                    Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }

            });

        });
    }

    public void Registrarse(View view) {

        Intent registro = new Intent(this, RegisterUserActivity.class);
        startActivity(registro);
    }

}
package com.example.drinksapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        Register();
    }

    public void Register() {
        Button buttonRegister = findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(view -> {

            Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiMobile.BASE_URL2)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiMobile apiMobile = retrofit.create(ApiMobile.class);

            EditText editText = findViewById(R.id.editText);
            EditText editText2 = findViewById(R.id.editText2);
            EditText editText3 = findViewById(R.id.editText3);
            EditText editText4 = findViewById(R.id.editText4);

            String eT = editText.getText().toString();    //name
            String eT2 = editText2.getText().toString();  //pass
            String eT3 = editText3.getText().toString();  //GivenName
            String eT4 = editText4.getText().toString();  //surname

            String jsonStr = "{'givenname': '" + eT3 + "', 'surname': '" + eT4 + "', 'username': '" + eT + "', 'pass': '" + eT2 + "'}";

            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = (JsonObject) jsonParser.parse(jsonStr);

            Call<Object> call = apiMobile.Register(jsonObject);

            call.enqueue(new Callback<Object>() {

                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {

                    if (response.isSuccessful()) {


                        Toast.makeText(view.getContext(), "Registro exitoso", Toast.LENGTH_SHORT).show();
                        Intent MyRegister = new Intent(view.getContext(), MainActivity.class);
                        startActivity(MyRegister);

                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {

                    Toast.makeText(RegisterUserActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }

            });

        });
    }

    @Override
    public void onBackPressed() {

    }

    public void Back(View view) {
        Intent regresar = new Intent(this, MainActivity.class);
        startActivity(regresar);
    }
}
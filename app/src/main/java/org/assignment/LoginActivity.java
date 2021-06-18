package org.assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.assignment.modals.DataModal;
import org.assignment.utils.Api;
import org.assignment.utils.URL;
import org.assignment.utils.UtilsFunction;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText email, password;
    private Button login_button, map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login_button = findViewById(R.id.login_button);
        map = findViewById(R.id.map);

        setListeners();

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, MapsActivity.class));

            }
        });
    }

    private void setListeners() {
        login_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        validation();
    }

    private void validation() {
        String email_str = email.getText().toString();
        String password_str = password.getText().toString();
        if (!UtilsFunction.isEmailValid(email_str)) {
            email.requestFocus();
            email.setError("Please enter valid email");
        } else if (TextUtils.isEmpty(password_str)) {
            password.requestFocus();
            password.setError("Please enter valid password");
        } else {
            loginUser(email_str, password_str);
        }
    }

    private void loginUser(String email, String password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api retrofitAPI = retrofit.create(Api.class);
        DataModal modal = new DataModal(email, password);
        Call<DataModal> call = retrofitAPI.createPost(modal);
        call.enqueue(new Callback<DataModal>() {
            @Override
            public void onResponse(Call<DataModal> call, retrofit2.Response<DataModal> response) {
                String token = String.valueOf(response.body().getToken());
                Log.d("AAA", token);
                if (!TextUtils.isEmpty(token)) {
                    startActivity(new Intent(LoginActivity.this, Home.class));
                }

            }

            @Override
            public void onFailure(Call<DataModal> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

            }
        });


    }

}
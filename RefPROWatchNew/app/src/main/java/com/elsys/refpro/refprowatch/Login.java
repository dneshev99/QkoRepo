package com.elsys.refpro.refprowatch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.elsys.refpro.refprowatch.http.LoginService;
import com.elsys.refpro.refprowatch.http.dto.AccountDto;
import com.elsys.refpro.refprowatch.main.MainActivity;
import com.google.firebase.iid.FirebaseInstanceId;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends WearableActivity {

    EditText username, password;
    Button signButton;
    ProgressBar loading;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        signButton = (Button) findViewById(R.id.loginButton);
        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        loading = (ProgressBar) findViewById(R.id.progressBar);

        preferences = getSharedPreferences("RefPRO" , 0);

        Log.d("Sasho kurva", "Kor: " + FirebaseInstanceId.getInstance().getToken());


        signButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String userString = username.getText().toString().trim();
                final String passString = password.getText().toString().trim();

                if (userString.isEmpty() || passString.isEmpty()) {

                    vibrator.vibrate(300);
                } else {

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://10.0.2.2:8080")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    LoginService service = retrofit.create(LoginService.class);

                    loading.setVisibility(View.VISIBLE);
                    signButton.setVisibility(View.INVISIBLE);

                    service.login(new AccountDto(userString,passString)).enqueue(new Callback<ResponseBody>() {

                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            if (response.isSuccessful()) {

                                SharedPreferences.Editor prefsEditor = preferences.edit();
                                Log.d("Sasho",response.headers().get("authorization"));
                                prefsEditor.putString("WebToken", response.headers().get("authorization"));
                                prefsEditor.apply();

                                Log.d("Sasho1",preferences.getString("WebToken","Sasho e gei"));

                                Intent app = new Intent(getApplicationContext(), MainActivity.class);
                                app.putExtra("Username", userString);
                                app.putExtra("Password", passString);
                                startActivity(app);
                            }
                            else {

                                Toast.makeText(getBaseContext(), "Wrong username or password", Toast.LENGTH_LONG).show();
                            }

                            loading.setVisibility(View.INVISIBLE);
                            signButton.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                            Toast.makeText(getBaseContext(), "Error!",
                                    Toast.LENGTH_LONG).show();
                            Log.i("Failure", t.getMessage() + " ");

                            loading.setVisibility(View.INVISIBLE);
                            signButton.setVisibility(View.VISIBLE);
                        }
                    });

                    vibrator.vibrate(300);
                }
            }
        });
    }

    }

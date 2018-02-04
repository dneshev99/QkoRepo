package com.elsys.refpro.refprowatch.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.elsys.refpro.refprowatch.R;
import com.elsys.refpro.refprowatch.http.DeviceType;
import com.elsys.refpro.refprowatch.http.LoginService;
import com.elsys.refpro.refprowatch.http.UserService;
import com.elsys.refpro.refprowatch.http.dto.UserDTO;
import com.elsys.refpro.refprowatch.main.MainActivity;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
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


        signButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String userString = username.getText().toString().trim();
                final String passString = password.getText().toString().trim();

                if (userString.isEmpty() || passString.isEmpty()) {

                    vibrator.vibrate(300);
                    Intent app = new Intent(getApplicationContext(), MainActivity.class);
                    app.putExtra("Username", userString);
                    app.putExtra("Password", passString);
                    startActivity(app);
                } else {

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://10.0.2.2:8080")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    LoginService service = retrofit.create(LoginService.class);

                    loading.setVisibility(View.VISIBLE);
                    signButton.setVisibility(View.INVISIBLE);

                    service.login(new UserDTO(userString,passString)).enqueue(new Callback<ResponseBody>() {

                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            if (response.isSuccessful()) {

                                SharedPreferences.Editor prefsEditor = preferences.edit();
                                prefsEditor.putString("token", response.headers().get("authorization"));
                                prefsEditor.apply();

                                final String jwtToken = response.headers().get("authorization");
                                final String fcmToken = FirebaseInstanceId.getInstance().getToken();
                                updateCurrentUserFcmToken(jwtToken,fcmToken, DeviceType.MOBILE);

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

    private void updateCurrentUserFcmToken(final String jwtToken,final String fcmToken,final DeviceType deviceType){
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", jwtToken)
                        .addHeader("DeviceType", deviceType.toString())
                        .build();

                return chain.proceed(newRequest);
            }
        }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserService service = retrofit.create(UserService.class);
        service.addFcmTokenForUser(fcmToken).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("asd","asd");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("asd",t.getMessage());
            }
        });
    }

    }

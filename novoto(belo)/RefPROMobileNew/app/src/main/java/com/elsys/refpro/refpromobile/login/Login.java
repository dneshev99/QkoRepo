package com.elsys.refpro.refpromobile.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.elsys.refpro.refpromobile.R;
import com.elsys.refpro.refpromobile.http.FirebaseService;
import com.elsys.refpro.refpromobile.http.LoginService;
import com.elsys.refpro.refpromobile.http.UpdateMatchService;
import com.elsys.refpro.refpromobile.http.dto.AccountDto;
import com.elsys.refpro.refpromobile.http.dto.NotificationDTO;
import com.elsys.refpro.refpromobile.main.MainActivity;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

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

public class Login extends AppCompatActivity {

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
        signButton = (Button) findViewById(R.id.sign);
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
                } else {
                    OkHttpClient firebaseClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                        @Override
                        public okhttp3.Response intercept(Chain chain) throws IOException {
                            Request newRequest = chain.request().newBuilder()
                                    .addHeader("Content-Type","application/json")
                                    .addHeader("Authorization","key=AAAAjh3KD7U:APA91bHcDRfM4Vk96KnYf2TA_AagYbyB2Y23iRvahJEuF5mgsooL--JN7FYN4UPyisZVizN5lIB5Jl768AqiDc0ex_vbZtfg9qNxJHT8n91I8nt2t94UYjx6uJrViLps0d7jC7dB-m1k")
                                    .build();
                            return chain.proceed(newRequest);
                        }
                    }).build();


                    Retrofit firebase = new Retrofit.Builder()
                            .client(firebaseClient)
                            .baseUrl("https://fcm.googleapis.com")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();



                    NotificationDTO notificationDTO = new NotificationDTO();

                    notificationDTO.setRecipient("eGsOcAisQM8:APA91bHB4q9HCMYREtwK4b1lwF6YOq067JO_mqJ-oqCjzTigHMapjArMZi6CwQyLsWWmRZIplmv_4mhu0we23p0B6uZPSPeiKFniE5bOXWlOMC6XMMgs9aWMXHVFqpRJD977fx5OmFsq");
                    notificationDTO.addData("id","5");

                    FirebaseService firebaseService = firebase.create(FirebaseService.class);

                    firebaseService.sendNotification(notificationDTO).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Log.d("Firebase AAA", response.message());
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.d("Firebase AAA ERROR" , t.getMessage());
                        }
                    });

                    Intent app = new Intent(getApplicationContext(), MainActivity.class);
                    app.putExtra("Username", userString);
                    app.putExtra("Password", passString);
                    startActivity(app);



                    /*

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
                                    prefsEditor.putString("token", response.headers().get("authorization"));
                                    prefsEditor.apply();

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

                    vibrator.vibrate(300);*/
                }
            }
        });
    }
}


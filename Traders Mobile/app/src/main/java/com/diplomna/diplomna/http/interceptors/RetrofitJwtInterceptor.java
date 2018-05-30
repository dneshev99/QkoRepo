package com.diplomna.diplomna.http.interceptors;


import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

@Singleton
public class RetrofitJwtInterceptor implements Interceptor {

    private static String jwtToken;

    SharedPreferences sharedPreferences;

    @Inject
    public RetrofitJwtInterceptor( SharedPreferences sharedPreferences){
        this.sharedPreferences=sharedPreferences;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        this.jwtToken=sharedPreferences.getString("token", "N/A");
        Log.d("Interceptor",jwtToken);
        Request newRequest = chain.request().newBuilder().addHeader("Authorization", jwtToken).build();
        return chain.proceed(newRequest);
    }
}
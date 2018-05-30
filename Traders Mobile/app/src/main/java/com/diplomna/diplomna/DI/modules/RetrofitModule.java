package com.diplomna.diplomna.DI.modules;

import android.support.annotation.NonNull;

import com.diplomna.diplomna.http.HttpInfo;
import com.diplomna.diplomna.http.interceptors.RetrofitJwtInterceptor;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.diplomna.diplomna.http.HttpInfo.CONNECT_TIMEOUT_MS;
import static com.diplomna.diplomna.http.HttpInfo.READ_TIMEOUT_MS;
import static com.diplomna.diplomna.http.HttpInfo.WRITE_TIMEOUT_MS;

@Module
public class RetrofitModule {



    @Provides
    @Singleton
    OkHttpClient okHttpClient(RetrofitJwtInterceptor interceptor) {

        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                // .addInterceptor(httpLoggingInterceptor)
                .connectTimeout(CONNECT_TIMEOUT_MS, TimeUnit.MILLISECONDS)
                .writeTimeout(WRITE_TIMEOUT_MS, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT_MS, TimeUnit.SECONDS)
                .build();
    }
    @Provides
    @Singleton
    Retrofit provideRetrofitClient(@NonNull OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(HttpInfo.getRetrofitUrl())
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }



}
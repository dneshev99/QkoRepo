package com.elsys.refpro.refprowatch.events;

import android.content.Context;
import android.widget.Toast;

import com.elsys.refpro.refprowatch.http.EventService;
import com.elsys.refpro.refprowatch.http.StateService;
import com.elsys.refpro.refprowatch.http.dto.MatchEventDTO;
import com.elsys.refpro.refprowatch.http.dto.MatchStateDTO;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateEvent {

    private ArrayList<MatchEventDTO> events = new ArrayList<>();
    private String id;
    Context context;

    public CreateEvent(String id, Context context) {

        this.id = id;
        this.context = context;
    }

    public CreateEvent() {
    }


    public ArrayList<MatchEventDTO> addEvent(String time, String eventType, String team, String player, boolean escape) {

        MatchEventDTO newEvent;

        if (!escape) {

            newEvent = new MatchEventDTO(id, time + "   ", eventType + " - ", team + "  - ", player);
        }
        else {

            newEvent = new MatchEventDTO(id, time, eventType, team, player);
        }

        events.add(newEvent);

        //if (events.size() != 1)
        //    sendEvent(newEvent);

        return events;
    }

    private void sendEvent(MatchEventDTO newEvent) {

        //final String token = preferences.getString("token","N/A");
        final String token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTUxNDM4NDIyMX0";

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", token)
                        .build();
                android.util.Log.i("HERE:", newRequest.toString());
                android.util.Log.i("HERE:", newRequest.header("Authorization"));
                return chain.proceed(newRequest);
            }
        }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("http://10.0.2.2:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        EventService service = retrofit.create(EventService.class);

        service.send(newEvent).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    Toast.makeText(context, "SUCCESS",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context, "NZ MA NE RABOTI",
                            Toast.LENGTH_SHORT).show();
                    android.util.Log.d("Error:", response.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Toast.makeText(context, "FAILURE-EVENT",
                        Toast.LENGTH_SHORT).show();
                android.util.Log.d("Error:",t.getMessage());
            }
        });
    }


    public void addState(boolean isActive, ArrayList<MatchEventDTO> log) {

        MatchStateDTO newEvent;

        newEvent = new MatchStateDTO(id, isActive, log);
        sendState(newEvent);
    }

    private void sendState(MatchStateDTO newEvent) {

        //final String token = preferences.getString("token","N/A");
        final String token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTUxNDM4NDIyMX0";

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", token)
                        .build();
                android.util.Log.i("HERE:", newRequest.toString());
                android.util.Log.i("HERE:", newRequest.header("Authorization"));
                return chain.proceed(newRequest);
            }
        }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("http://10.0.2.2:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        StateService service = retrofit.create(StateService.class);

        service.send(newEvent).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    Toast.makeText(context, "SUCCESS",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context, "NZ MA NE RABOTI",
                            Toast.LENGTH_SHORT).show();
                    android.util.Log.d("Error:", response.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Toast.makeText(context, "FAILURE-STATE",
                        Toast.LENGTH_SHORT).show();
                android.util.Log.d("Error:",t.getMessage());
            }
        });
    }
}

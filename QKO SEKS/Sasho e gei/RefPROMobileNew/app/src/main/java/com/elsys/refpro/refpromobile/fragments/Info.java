package com.elsys.refpro.refpromobile.fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.elsys.refpro.refpromobile.database.LocalDatabase;
import com.elsys.refpro.refpromobile.R;
import com.elsys.refpro.refpromobile.http.FirebaseService;
import com.elsys.refpro.refpromobile.http.UpdateMatchService;
import com.elsys.refpro.refpromobile.http.dto.NotificationDTO;
import com.elsys.refpro.refpromobile.http.dto.UpdateMatchDto;
import com.elsys.refpro.refpromobile.models.Player;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

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

import static android.content.Context.MODE_PRIVATE;

public class Info extends Fragment {

    View createView;
    TextView competition, date, time, teams, player_title;
    EditText number, name;
    int match_id;
    LocalDatabase db;
    ArrayList<Player> homePlayers = new ArrayList<>();
    ArrayList<Player> awayPlayers = new ArrayList<>();
    ArrayList<Player> homeSubs = new ArrayList<>();
    ArrayList<Player> awaySubs = new ArrayList<>();

    int counter = 0;

    ProgressBar loading;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        createView = inflater.inflate(R.layout.match_info, container, false);

        loading = (ProgressBar) createView.findViewById(R.id.progressBar);

        SharedPreferences preferences;
        preferences = getActivity().getSharedPreferences("RefPRO" , 0);

        db = new LocalDatabase(this.getActivity());


        //region INITIALIZE
        LinearLayout layout = (LinearLayout) createView.findViewById(R.id.info_layout);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //Every Title will have Top Margin of 120dp
        params.setMargins(0, 120, 0, 0);

        //GET current match ID and GET the information about match with it's unique key
        SharedPreferences mPrefs = this.getActivity().getPreferences(MODE_PRIVATE);
        match_id = mPrefs.getInt("matchId", 0);


       final Cursor data = db.getRow(match_id);

       data.moveToFirst();


        //GET information about current match and SET as Text
        competition = (TextView) createView.findViewById(R.id.info_name);
        date = (TextView) createView.findViewById(R.id.info_date);
        time = (TextView) createView.findViewById(R.id.info_time);
        teams = (TextView) createView.findViewById(R.id.info_teams);
        player_title = (TextView) createView.findViewById(R.id.players_title);

        data.moveToFirst();

        competition.setText(data.getString(1));
        date.setText(data.getString(7));
        time.setText(data.getString(8));
        teams.setText("" + data.getString(3) + " vs. " + data.getString(4));

        //endregion

        number = (EditText) createView.findViewById(R.id.player_number);
        name = (EditText) createView.findViewById(R.id.player_name);

        final Button create = (Button) createView.findViewById(R.id.send);
        create.setEnabled(false);

        final Button add = (Button) createView.findViewById(R.id.player_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (name.getText().toString().isEmpty() || number.getText().toString().isEmpty()) {

                    number.setError(getResources().getString(R.string.empty_field));
                    name.setError(getResources().getString(R.string.empty_field));
                } else {

                    int player_number = Integer.parseInt(number.getText().toString());
                    String player_name = name.getText().toString();

                    if (counter < Integer.parseInt(data.getString(9)) && counter < 100) {

                        Player player = new Player(player_number, player_name);
                        homePlayers.add(player);
                        counter++;
                        if (counter == Integer.parseInt(data.getString(9))) {
                            counter = 100;
                            player_title.setText(getResources().getString(R.string.away_players));
                        }
                    } else if (counter < (Integer.parseInt(data.getString(9)) + 100) && counter < 200) {

                        Player player = new Player(player_number, player_name);
                        awayPlayers.add(player);
                        counter++;
                        if (counter == (Integer.parseInt(data.getString(9)) + 100)) {
                            counter = 200;
                            player_title.setText(getResources().getString(R.string.home_subs));
                        }
                    } else if (counter < (Integer.parseInt(data.getString(10)) + 200) && counter < 300) {

                        Player player = new Player(player_number, player_name);
                        homeSubs.add(player);
                        counter++;
                        if (counter == (Integer.parseInt(data.getString(10)) + 200)) {
                            counter = 300;
                            player_title.setText(getResources().getString(R.string.away_subs));
                        }
                    } else if (counter < (Integer.parseInt(data.getString(10)) + 300) && counter < 400) {

                        Player player = new Player(player_number, player_name);
                        awaySubs.add(player);
                        counter++;
                        if (counter == (Integer.parseInt(data.getString(10))+ 300)) {

                            add.setEnabled(false);
                            create.setEnabled(true);
                        }
                    }

                    Toast.makeText(getActivity(), "Player added",
                            Toast.LENGTH_LONG).show();
                }
            }
        });


        final SharedPreferences finalPreferences = preferences;
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getActivity(), "Match started",
                        Toast.LENGTH_LONG).show();

                final String token = finalPreferences.getString("token", "N/A");

                OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request newRequest = chain.request().newBuilder()
                                .addHeader("Authorization", token)
                                .build();
                        Log.d("Na sasho tokena", token);
                        return chain.proceed(newRequest);
                    }
                }).build();

                Retrofit retrofit = new Retrofit.Builder()
                        .client(client)
                        .baseUrl("http://10.19.9.87:8080")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();


                UpdateMatchService service = retrofit.create(UpdateMatchService.class);

                loading.setVisibility(View.VISIBLE);
                create.setVisibility(View.GONE);

                String matchId = data.getString(12);

                UpdateMatchDto body = new UpdateMatchDto(matchId, homePlayers, homeSubs, awayPlayers, awaySubs);

                Log.d("DEEEEEEEBA", body.getMatchId());

                service.update(body).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        if (response.isSuccessful()) {

                            loading.setVisibility(View.GONE);
                            create.setVisibility(View.VISIBLE);

                            db.delete(match_id);

                            Toast.makeText(getActivity(), R.string.delete_success,
                                    Toast.LENGTH_LONG).show();

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
                            notificationDTO.addData("id", data.getString(12));

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
                        }
                    }


                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                        loading.setVisibility(View.GONE);
                        create.setVisibility(View.VISIBLE);
                    }
                });

                Menu menu = new Menu();
                android.app.FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, menu).commit();
            }
        });


        return createView;
    }
}

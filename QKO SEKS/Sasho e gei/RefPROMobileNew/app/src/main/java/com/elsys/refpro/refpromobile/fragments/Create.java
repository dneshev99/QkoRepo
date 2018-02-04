package com.elsys.refpro.refpromobile.fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.elsys.refpro.refpromobile.database.LocalDatabase;
import com.elsys.refpro.refpromobile.R;
import com.elsys.refpro.refpromobile.http.CreateMatchService;
import com.elsys.refpro.refpromobile.http.dto.CreateMatchDto;
import com.elsys.refpro.refpromobile.models.Match;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Create extends Fragment implements View.OnClickListener{

    View createView;
    EditText competition, venue, players, subs, length, home, away, homeAbbr, awayAbbr, time, date;
    Button create;
    ProgressBar loading;
    Match match;

    LocalDatabase db;
    SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        createView = inflater.inflate(R.layout.create_fragment, container, false);
        preferences = getActivity().getSharedPreferences("RefPRO" , 0);
        // region INITIALIZE
        competition = (EditText) createView.findViewById(R.id.competition);
        venue = (EditText) createView.findViewById(R.id.venue);
        players = (EditText) createView.findViewById(R.id.players);
        subs = (EditText) createView.findViewById(R.id.subs);
        length = (EditText) createView.findViewById(R.id.lenght);
        home = (EditText) createView.findViewById(R.id.home);
        away = (EditText) createView.findViewById(R.id.away);
        homeAbbr = (EditText) createView.findViewById(R.id.homeabbr);
        awayAbbr = (EditText) createView.findViewById(R.id.awayabbr);
        time = (EditText) createView.findViewById(R.id.time);
        date = (EditText) createView.findViewById(R.id.date);

        create = (Button) createView.findViewById(R.id.createMatch);
        create.setOnClickListener(this);

        loading = (ProgressBar) createView.findViewById(R.id.progressBar);

        //endregiond
        db = new LocalDatabase(this.getActivity());

        return createView;
    }

    @Override
    public void onClick(View v) {

        match = new Match(competition.getText().toString(), venue.getText().toString(), date.getText().toString(),
                time.getText().toString(), home.getText().toString(), away.getText().toString(),
                homeAbbr.getText().toString(), awayAbbr.getText().toString(),
                Integer.parseInt(length.getText().toString()), Integer.parseInt(players.getText().toString()), Integer.parseInt(subs.getText().toString()));

        if (!match.isMatchValidated()) {

            match.setMatchValidated(true);
        }
        else {

            final String token = preferences.getString("token", "N/A");

            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request newRequest = chain.request().newBuilder()
                            .addHeader("Authorization", token)
                            .build();
                    Log.i("HERE:",newRequest.toString());
                    Log.i("HERE:",newRequest.header("Authorization"));
                    return chain.proceed(newRequest);
                }
            }).build();

            Retrofit retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl("http://10.19.9.87:8080")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            CreateMatchService service = retrofit.create(CreateMatchService.class);

            loading.setVisibility(View.VISIBLE);
            create.setVisibility(View.INVISIBLE);

            service.create(match).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getActivity(), "Success", Toast.LENGTH_LONG).show();

                        String mongoID = null;
                        try {
                            mongoID = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(getActivity(), "Success:" + mongoID, Toast.LENGTH_LONG).show();

                        boolean insertData = db.addData(competition.getText().toString(), venue.getText().toString(),
                                home.getText().toString(), away.getText().toString(), homeAbbr.getText().toString()
                                , awayAbbr.getText().toString(), date.getText().toString(), time.getText().toString(),
                                11, 7, 45, mongoID);

                        if (insertData)
                            Toast.makeText(getActivity(), R.string.create_success,
                                    Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getActivity(), R.string.error,
                                    Toast.LENGTH_LONG).show();

                        loading.setVisibility(View.INVISIBLE);
                        create.setVisibility(View.VISIBLE);

                        Menu menu = new Menu();
                        android.app.FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, menu).commit();

                    } else {
                        Toast.makeText(getActivity(), "Error1", Toast.LENGTH_LONG).show();
                        Log.i("HERE", String.valueOf(response.code()));

                        loading.setVisibility(View.INVISIBLE);
                        create.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();

                    loading.setVisibility(View.INVISIBLE);
                    create.setVisibility(View.VISIBLE);
                }
            });

        }
    }
}

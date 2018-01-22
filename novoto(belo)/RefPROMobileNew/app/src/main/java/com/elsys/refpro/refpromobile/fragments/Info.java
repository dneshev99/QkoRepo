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
import com.elsys.refpro.refpromobile.http.UpdateMatchService;
import com.elsys.refpro.refpromobile.http.dto.UpdateMatchDto;
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
    TextView competition, date, time, teams;
    int match_id;
    boolean canCreate = true;
    LocalDatabase db;
    ArrayList<String> homePlayers = new ArrayList<>();
    ArrayList<String> awayPlayers = new ArrayList<>();
    ArrayList<String> homeSubs = new ArrayList<>();
    ArrayList<String> awaySubs = new ArrayList<>();

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

        data.moveToFirst();

        competition.setText(data.getString(1));
        date.setText(data.getString(7));
        time.setText(data.getString(8));
        teams.setText("" + data.getString(3) + " vs. " + data.getString(4));

        //endregion

        //Create form for every player
        CreateForms(Integer.parseInt(data.getString(9)), layout, 1, 100);

        SetTitle(layout, params, 1);
        CreateForms(Integer.parseInt(data.getString(10)), layout, 2, 200);

        SetTitle(layout, params, 2);
        CreateForms(Integer.parseInt(data.getString(9)), layout, 3, 300);

        SetTitle(layout, params, 3);
        CreateForms(Integer.parseInt(data.getString(10)), layout, 4, 400);

        /////

        final Button create = (Button) createView.findViewById(R.id.send);
        final SharedPreferences finalPreferences = preferences;
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int counter = 0; counter < Integer.parseInt(data.getString(9)); counter++) {

                    EditText check;
                    check = (EditText) createView.findViewById(counter + 100);
                    char[] toChar = check.toString().toCharArray();

                    if (check.getText().toString().length() < 1 || (toChar[1] != '.' || toChar[2] != '.')
                            || !(toChar[1] == '.' && Character.isDigit(toChar[0])) ||
                            !(toChar[2] == '.' && Character.isDigit(toChar[0])) && Character.isDigit(toChar[1])) {

                        canCreate = false;
                        check.setError(getResources().getString(R.string.empty_field));
                    }
                    else {

                        homePlayers.add(check.getText().toString());
                    }
                }

                for (int counter = 0; counter < Integer.parseInt(data.getString(10)); counter++) {

                    EditText check;
                    check = (EditText) createView.findViewById(counter + 200);

                    if (check.getText().toString().length() < 1) {

                        canCreate = false;
                        check.setError(getResources().getString(R.string.empty_field));
                    }
                    else {

                        homeSubs.add(check.getText().toString());
                    }
                }

                for (int counter = 0; counter < Integer.parseInt(data.getString(9)); counter++) {

                    EditText check;
                    check = (EditText) createView.findViewById(counter + 300);

                    if (check.getText().toString().length() < 1) {

                        canCreate = false;
                        check.setError(getResources().getString(R.string.empty_field));
                    }
                    else {

                        awayPlayers.add(check.getText().toString());
                    }
                }

                for (int counter = 0; counter < Integer.parseInt(data.getString(10)); counter++) {

                    EditText check;
                    check = (EditText) createView.findViewById(counter + 400);

                    if (check.getText().toString().length() < 1) {

                        canCreate = false;
                        check.setError(getResources().getString(R.string.empty_field));
                    }
                    else {

                        awaySubs.add(check.getText().toString());
                    }
                }

                if (!canCreate) {

                    canCreate = true;
                }
                else {
                    //match.setHome_players(homePlayers);
                   // match.setHome_subs(homeSubs);
                    //match.setAway_players(awayPlayers);
                    //match.setAway_subs(awaySubs);

                    //homePlayers.add("1.az");
                    //awayPlayers.add("2.az");
                    //homeSubs.add("3.az");
                    //awaySubs.add("4.az");


                    Toast.makeText(getActivity(), "Match started",
                            Toast.LENGTH_LONG).show();

                    FirebaseMessaging fm = FirebaseMessaging.getInstance();
                    fm.send(new RemoteMessage.Builder("cgwD38TVwck:APA91bHkOjpSfT_dcBZkvlX1S5inkBztySMKM6uVC-hm1C_h2DV6Uo_5xeNxAtdtN-bA0o-usIyWrQCVCETRS3giRmD3E4pPqosrocJ908mWxUJRHHM8aVCfXeQG4zqlYbzUYzBidLjI" + "@gcm.googleapis.com")
                            .setMessageId(Integer.toString(1))
                            .addData("my_message", "Hello World")
                            .addData("my_action","SAY_HELLO")
                            .build());

                    final String token = finalPreferences.getString("token","N/A");

                    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                        @Override
                        public okhttp3.Response intercept(Chain chain) throws IOException {
                            Request newRequest = chain.request().newBuilder()
                                    .addHeader("Authorization",token)
                                    .build();
                            Log.d("Na sasho tokena",token);
                            return chain.proceed(newRequest);
                        }
                    }).build();

                    Retrofit retrofit = new Retrofit.Builder()
                            .client(client)
                            .baseUrl("http://10.0.2.2:8080")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();



                    UpdateMatchService service = retrofit.create(UpdateMatchService.class);

                    loading.setVisibility(View.VISIBLE);
                    create.setVisibility(View.GONE);

                    String matchId = data.getString(12);

                    UpdateMatchDto body = new UpdateMatchDto(matchId, homePlayers, homeSubs, awayPlayers, awaySubs);

                    Log.d("DEEEEEEEBA",body.getMatchId());

                    service.update(body).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            loading.setVisibility(View.GONE);
                            create.setVisibility(View.VISIBLE);
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
            }
        });


        return createView;
    }


    public void SetTitle(LinearLayout layout, LinearLayout.LayoutParams params, int type) {

        TextView field = new TextView(this.getActivity());
        field.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        if (type == 1)
            field.setText(R.string.home_subs);
        else if (type == 2)
            field.setText(R.string.away_players);
        else
            field.setText(R.string.away_subs);

        field.setTextColor(Color.parseColor("#000000"));
        field.setTextSize(24);
        field.setTypeface(null, Typeface.BOLD);
        layout.addView(field, params);
    }

    public void CreateForms(int range, LinearLayout layout, int type, int IDIncrease) {

        for (int counter = 0; counter < range; counter++) {

            EditText form = new EditText(this.getActivity());
            form.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            form.setHint(getResources().getString(R.string.players_form));

            form.setId(counter + IDIncrease);
            form.setGravity(Gravity.CENTER);

            layout.addView(form);
        }
    }
}

package com.elsys.refpro.refprowatch;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends WearableActivity implements View.OnClickListener {

    private static final SimpleDateFormat AMBIENT_DATE_FORMAT =
            new SimpleDateFormat("HH:mm", Locale.getDefault());

    private BoxInsetLayout mContainerView;
    private TextView mTextView;
    private TextView mClockView;
    //mContainerView = (BoxInsetLayout) findViewById(R.id.container);

    //region INSTANTIATE
    ArrayList<String> info  = new ArrayList<String>();        // MatchInformation - REPLACE WITH STRING FROM MOBILE
    ArrayList<String>  homePlayers = new ArrayList<String>(); // MatchInformation - REPLACE WITH STRING FROM MOBILE
    ArrayList<String>  awayPlayers = new ArrayList<String>(); // MatchInformation - REPLACE WITH STRING FROM MOBILE
    ArrayList<String>  homeSubs = new ArrayList<String>();    // MatchInformation - REPLACE WITH STRING FROM MOBILE
    ArrayList<String>  awaySubs = new ArrayList<String>();    // MatchInformation - REPLACE WITH STRING FROM MOBILE
    ArrayList<Integer> IntInfo = new ArrayList<Integer>();     // MatchInformation - REPLACE WITH STRING FROM MOBILE

    String subName, PlayerToSub;
    String lastClickedPlayer;

    TextView HomeAbbr, AwayAbbr, BigTimer, SmallTimer, Abbr, Log, Half, HalfTeam, HomeResult, AwayResult;
    int homeResult = 0, awayResult = 0;

    int BigSeconds = 0, SmallSeconds = 0, BigMinutes = 0, SmallMinutes = 0;
    int type = 0; // 0 GOAL, 1 SUB, 2 YELLOW, 3 RED
    int undoType = 0; // 1 GOAL, 2 SUB. 3 YELLOW, 4 RED, 5 HALF
    String setTime = "", logText = "";

    Button startButton, teamBackButton, settingsBackButton, goalButton, yellowCardButton, redCardButton, logButton, logBack, endHalf, subButton, undoButton;
    ArrayList<Button> playerButtons = new ArrayList<Button>();
    ArrayList<Button> subsButtons = new ArrayList<Button>();
    boolean isStarted = false, smallT = false, isHome = true, vibrator = false;

    RelativeLayout MainLayout, TeamLayout, timerLayout, SettingsLayout;
    LinearLayout playersLayout, playersView, logLayout, subsLayout, subsView;

    int half = 0; // 0 FH, 1 HT, 2 SH, 3 FT

    ArrayList<MatchEventDTO> events = new ArrayList();
    String id = "";

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        mTextView = (TextView) findViewById(R.id.text);

        SharedPreferences clear = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        clear.edit().clear().commit();               // CLEARS all information about yellow and red cards

        //region BasicInfo REPLACE WITH STRING FROM MOBILE

        id = "nz";

        info.add("La Liga");
        info.add("Camp Nou");
        info.add("FC Barcelona");
        info.add("Real Madrid C.F.");
        info.add("BAR");
        info.add("RMA");
        info.add("19:45");
        info.add("25.12.17");

        IntInfo.add(11);
        IntInfo.add(7);
        IntInfo.add(45);

        awayPlayers.add("1.Keylor Navas"); homePlayers.add("1.Marc-André ter Stegen");
        awayPlayers.add("4.Sergio Ramos"); homePlayers.add("3.Gerard Piqué");
        awayPlayers.add("5.Raphael Varane"); homePlayers.add("4.Ivan Rakitic");
        awayPlayers.add("7.Cristiano Ronaldo"); homePlayers.add("5.Sergio Busquets");
        awayPlayers.add("8.Toni Kroos"); homePlayers.add("8.Andrés Iniesta");
        awayPlayers.add("9.Karim Benzema"); homePlayers.add("9.Luis Suárez");
        awayPlayers.add("10.Luca Modric"); homePlayers.add("10.Lionel Messi");
        awayPlayers.add("12.Marcelo"); homePlayers.add("15.Paulinho Bezerra");
        awayPlayers.add("14.Casemiro"); homePlayers.add("18.Jordi Alba");
        awayPlayers.add("19.Achraf"); homePlayers.add("19.Lucas Digne");
        awayPlayers.add("22.Isco"); homePlayers.add("23.Samuel Umtiti");

        awaySubs.add("13.Kiko Casilla"); homeSubs.add("6.Denis Suarez");
        awaySubs.add("2.Carvajal"); homeSubs.add("7.Arda Turan");
        awaySubs.add("6.Nacho Fernandez"); homeSubs.add("11.Ousmane Dembele");
        awaySubs.add("20.Marco Asensio"); homeSubs.add("12.Rafinha");
        awaySubs.add("22.Mateo Kovacic"); homeSubs.add("13.Jasper Cillessen");
        awaySubs.add("11.Gareth Bale"); homeSubs.add("14.Javier Mascherano");
        awaySubs.add("17.Lucas Vazquez"); homeSubs.add("20.Sergi Roberto");
        //endregion

        Initialize(); // Initialization of all TextView, Button, Layout variables

        timerLayout.setOnLongClickListener(new View.OnLongClickListener() { // STARTS SettingsLayout OnLongClick
            @Override
            public boolean onLongClick(View v) {

                SettingsLayout.setVisibility(View.VISIBLE);
                MainLayout.setVisibility(View.INVISIBLE);
                return true;
            }
        });


        addEvent("", "", "",  info.get(0) + "\n" + info.get(1) + "\n" + info.get(6) + "\n" + info.get(7) + "\n"
                + info.get(2) + " vs. " + info.get(3) + "\n\n", true);

        ListPlayers(); // CREATES Buttons for all players
        ListSubs();    // CREATES Buttons for all substitutes

        CreateTimers(); // CREATES BigTimer and SmallTimer
    }


    private void CreateTimers() {

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isStarted) { // STARTS if CurrentMatch is started

                            BigMinutes = TimerFormat(BigSeconds, BigMinutes);
                            if (BigMinutes == IntInfo.get(2) && !vibrator) {  //VIBRATES if BigMinutes == HalfLength

                                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                v.vibrate(1000);
                                vibrator = true;
                            }

                            BigTimer.setText(setTime);
                            if (BigSeconds == 60)
                                BigSeconds = 0;
                            BigSeconds++;
                        }

                        if (smallT && isStarted) { // STARTS if timerLayout is clicked and CurrentMatch is started

                            SmallMinutes = TimerFormat(SmallSeconds, SmallMinutes);
                            SmallTimer.setText(setTime);
                            if (SmallSeconds == 60)
                                SmallSeconds = 0;
                            SmallSeconds++;
                        }
                    }
                });
            }
        }, 1000, 1000);
    }  // CREATES BigTimer and SmallTimer

    private void ListSubs() {

        for(int counter = 0; counter < IntInfo.get(1); counter++) {


            final Button newSub = new Button(this);
            newSub.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            newSub.setText("Sub");
            newSub.setId(counter);      //CREATES Button for every substitute with Text and ID

            newSub.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    String team;

                    if (isHome) {
                        team = info.get(4);
                    }
                    else {
                        team = info.get(5);
                    }

                    String playerName = newSub.getText().toString();
                    subName = playerName;
                    Replace(PlayerToSub, subName); // REPLACES Player with Substitute
                    subsLayout.setEnabled(false);
                    MainLayout.setEnabled(true);

                    addEvent(BigMinutes + ":" + BigSeconds, "SUBSTITUTION", team, PlayerToSub + " with " + playerName, false);

                    subsLayout.setVisibility(View.INVISIBLE);
                    MainLayout.setVisibility(View.VISIBLE);
                }
            });
            subsButtons.add(newSub);  // ADD current substitute to List of Buttons
            subsView.addView(newSub); // CREATE Button for current substitute
        }
    }   // CREATES Buttons for all substitutes

    private void addEvent(String time, String eventType, String team, String message, boolean escape) {

        MatchEventDTO newEvent = null;

        if (!escape) {

            MatchEventDTO event = new MatchEventDTO(id, time + "   ", eventType + " - ", team + "  - ", message);
            newEvent = event;
        }
        else {

            MatchEventDTO event = new MatchEventDTO(id, time, eventType, team, message);
            newEvent = event;
        }

        events.add(newEvent);

        if (events.size() != 1)
            sendEvent(newEvent);

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


        SendService service = retrofit.create(SendService.class);

        service.send(newEvent).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "SUCCESS",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "NZ MA NE RABOTI",
                            Toast.LENGTH_SHORT).show();
                    android.util.Log.d("Error:", response.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Toast.makeText(getApplicationContext(), "FAILURE",
                        Toast.LENGTH_SHORT).show();
                android.util.Log.d("Error:",t.getMessage());

            }

        });


    }

    private void ListPlayers() {

        for(int counter = 0; counter < IntInfo.get(0); counter++) {

            final Button newPlayer = new Button(this);
            newPlayer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            newPlayer.setText("Player");
            newPlayer.setId(counter); //CREATES Button for every player with Text and ID

            newPlayer.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();

                    String playerName = newPlayer.getText().toString();
                    lastClickedPlayer = playerName;
                    int yellowCard = pref.getInt(playerName, 0);
                    int redCard = pref.getInt(playerName + "R", 0); // GETS yellow and red cards of ClickedPlayer
                    String team;

                    if (isHome) {
                        team = info.get(4);
                    }
                    else {
                        team = info.get(5);
                    }


                    if (yellowCard == 2)  // IF player has 2 Yellow Cards => Disable player button
                        newPlayer.setEnabled(false);
                    else
                        newPlayer.setEnabled(true);


                    if (type == 0) {    // IF GOAL Button is clicked

                        addEvent(setTime, "GOAL", team, playerName, false);
                        changeResult();

                        undoType = 1;
                    }
                    else if (type == 1) { // IF SUBSTITUTE Button is clicked

                        playersLayout.setVisibility(View.INVISIBLE);
                        subsLayout.setVisibility(View.VISIBLE);
                        setSubsNames();
                        PlayerToSub = playerName;

                        undoType = 2;
                    }
                    else if (type == 2){ // IF YELLOW CARD Button is clicked

                        addEvent(setTime, "YELLOW CARD", team, playerName, false);
                        yellowCard++;
                        editor.putInt(playerName, yellowCard);
                        editor.commit(); // SAVE new YellowCard to player

                        undoType = 3;

                        if (yellowCard == 2 || redCard == 1) { // IF player has 2 Yellow or 1 Red Card => Disable his button

                            addEvent(setTime, "RED CARD", team, playerName, false);
                            newPlayer.setEnabled(false);
                        }

                    }
                    else { // IF RED CARD Button is clicked

                        addEvent(setTime, "RED CARD", team, playerName, false);
                        redCard++;

                        editor.putInt(playerName + "R", redCard);
                        editor.commit();
                        newPlayer.setEnabled(false); // SAVE RedCard to player and Disable his button

                        undoType = 4;
                    }

                    if (type != 1) { // IF GOAL, YELLOW or RED CARD is clicked => RETURN to MainLayout
                        playersLayout.setVisibility(View.INVISIBLE);
                        MainLayout.setVisibility(View.VISIBLE);
                    }
                }
            });

            playerButtons.add(newPlayer); // ADD current player to List of Buttons
            playersView.addView(newPlayer); // CREATE Button for current player
        }
    } // CREATES Buttons for all players

    private void Initialize() {

        Log = (TextView) findViewById(R.id.log);                // Prints LOG in LogLayout
        Half = (TextView) findViewById(R.id.half);              // ?!?
        HalfTeam = (TextView) findViewById(R.id.halfTeam);      // Prints HALF in TeamLayout
        HomeResult = (TextView) findViewById(R.id.homeresult);  // Prints Home Team Result
        AwayResult = (TextView) findViewById(R.id.awayresult);  // Prints Away Team Result

        HomeAbbr = (TextView) findViewById(R.id.homeabbr);      // Prints Home Team Abbreaviation in MainLayout
        HomeAbbr.setText(info.get(4));
        AwayAbbr = (TextView) findViewById(R.id.awayabbr);      // Prints Away Team Abbreaviation in MainLayout
        AwayAbbr.setText(info.get(5));

        SmallTimer = (TextView) findViewById(R.id.SmallTimer);  // SmallTimer for ExtraTime
        BigTimer = (TextView) findViewById(R.id.BigTimer);      // BigTimer for Time

        Abbr = (TextView) findViewById(R.id.abbr);              // Team Abbreviation in TeamLayout

        MainLayout = (RelativeLayout) findViewById(R.id.mainLayout);            // MainLayout
        TeamLayout = (RelativeLayout) findViewById(R.id.teamLayout);            // TeamLayout
        timerLayout = (RelativeLayout) findViewById(R.id.timerLayout);          // Layout for Timers which is connected to SettingsLayout
        SettingsLayout = (RelativeLayout) findViewById(R.id.settingsLayout);    // SettingsLayout

        playersLayout = (LinearLayout) findViewById(R.id.playersLayout);    // Layout parent of ScrollView with all players
        subsLayout = (LinearLayout) findViewById(R.id.subsLayout);          // Layout parent of ScrollView with all substitutes
        logLayout = (LinearLayout) findViewById(R.id.logLayout);            // Layout which Prints match logText
        playersView = (LinearLayout) findViewById(R.id.playersView);        // Layout which Lists player names
        subsView = (LinearLayout) findViewById(R.id.subsView);              // Layout which Lists substitute names

        startButton = (Button) findViewById(R.id.start);                // Starts Match and Timers
        startButton.setOnClickListener(this);
        teamBackButton = (Button) findViewById(R.id.back);              // Returns from TeamLayout to MainLayout
        teamBackButton.setOnClickListener(this);
        settingsBackButton = (Button) findViewById(R.id.settingsBack);  // Returns from SettingsLayout to MainLayout
        settingsBackButton.setOnClickListener(this);
        goalButton = (Button) findViewById(R.id.goal);                  // Starts GOAL menu
        goalButton.setOnClickListener(this);
        yellowCardButton = (Button) findViewById(R.id.yellow);          // Starts YELLOW CARD menu
        yellowCardButton.setOnClickListener(this);
        redCardButton = (Button) findViewById(R.id.red);                // Starts RED CARD menu
        redCardButton.setOnClickListener(this);
        subButton = (Button) findViewById(R.id.substitute);             // Starts SUBSTITUTION menu
        subButton.setOnClickListener(this);
        logButton = (Button) findViewById(R.id.logButton);              // Starts LOGLayout
        logButton.setOnClickListener(this);
        logBack = (Button) findViewById(R.id.logBack);                  // Returns from LogLayout to MainLayout
        logBack.setOnClickListener(this);
        endHalf = (Button) findViewById(R.id.endHalf);                  // END HALF, START SECOND HALF and FULL TIME
        endHalf.setOnClickListener(this);
        undoButton = (Button) findViewById(R.id.undo);
        undoButton.setOnClickListener(this);
    } // Initialization of all TextView, Button, Layout variables

    public void Replace(String playerNameString, String subNameString) {

        int PlayerID = 0, SubID = 0;
        String PlayerName = "", SubName = "";

        if (isHome) { // IF it's HomeTeam Menu => GET index of given player and substitute and make SUBSTITUTION
            for (int counter = 0; counter < IntInfo.get(0); counter++) {

                if (homePlayers.get(counter).contains(playerNameString)) {
                    PlayerID = counter;
                    PlayerName = homePlayers.get(counter).toString();
                }
            }

            for (int counter = 0; counter < IntInfo.get(1); counter++) {

                if (homeSubs.get(counter).contains(subNameString)) {
                    SubID = counter;
                    SubName = homeSubs.get(counter).toString();
                }
            }

            homePlayers.set(PlayerID, SubName);
            homeSubs.set(SubID, PlayerName);
        }
        else { // IF it's AwayTeam Menu => GET index of given player and substitute and make SUBSTITUTION

            for (int counter = 0; counter < IntInfo.get(0); counter++) {

                if (awayPlayers.get(counter).contains(playerNameString)) {
                    PlayerID = counter;
                    PlayerName = awayPlayers.get(counter).toString();
                }
            }

            for (int counter = 0; counter < IntInfo.get(1); counter++) {

                if (awaySubs.get(counter).contains(subNameString)) {
                    SubID = counter;
                    SubName = awaySubs.get(counter).toString();
                }
            }

            awayPlayers.set(PlayerID, SubName);
            awaySubs.set(SubID, PlayerName);
        }
    } // REPLACES Player with Substitute


    public void setPlayerNames() {

        for (int counter = 0; counter < IntInfo.get(0); counter++) {

            if (isHome) {

                playerButtons.get(counter).setText(homePlayers.get(counter).toString());
            } else

                playerButtons.get(counter).setText(awayPlayers.get(counter).toString());

            playerButtons.get(counter).setEnabled(true);
            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();

            String playerName =  ((String) playerButtons.get(counter).getText());
            int yellowCard =  pref.getInt(playerName, 0);
            int redCard = pref.getInt(playerName + "R", 0);

            if (yellowCard == 2 || redCard == 1)
                playerButtons.get(counter).setEnabled(false);// ENABLE all buttons, check for YELLOW or RED CARDS and Disable buttons

        }
    } // SETS names of all players

    public void setSubsNames() {

        for (int counter = 0; counter < IntInfo.get(1); counter++) {

            if (isHome) {

                subsButtons.get(counter).setText(homeSubs.get(counter).toString()); // SETS Text for every Home Substitute
            } else

                subsButtons.get(counter).setText(awaySubs.get(counter).toString()); // SETS Text for every Away Substitute
        }
    } // SETS names of all substitutes

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.start:

                BigTimer.setVisibility(View.VISIBLE);
                SmallTimer.setVisibility(View.VISIBLE);
                isStarted = true;
                startButton.setVisibility(View.INVISIBLE);

                Toast.makeText(this, "MATCH STARTED",
                        Toast.LENGTH_SHORT).show();
                addEvent(setTime, "", "", " - MATCH STARTED", true);
                sendMatchState(true);
                break;

            case R.id.back:

                TeamLayout.setVisibility(View.INVISIBLE);
                MainLayout.setVisibility(View.VISIBLE);
                break;

            case R.id.settingsBack:

                SettingsLayout.setVisibility(View.INVISIBLE);
                MainLayout.setVisibility(View.VISIBLE);
                changeHalfString(Half);
                break;

            case R.id.goal:

                TeamLayout.setVisibility(View.INVISIBLE);
                playersLayout.setVisibility(View.VISIBLE);
                type = 0;
                setPlayerNames();
                break;

            case R.id.substitute:

                TeamLayout.setVisibility(View.INVISIBLE);
                playersLayout.setVisibility(View.VISIBLE);
                type = 1;
                setPlayerNames();
                break;

            case R.id.yellow:

                TeamLayout.setVisibility(View.INVISIBLE);
                playersLayout.setVisibility(View.VISIBLE);
                type = 2;
                setPlayerNames();
                break;

            case R.id.red:

                TeamLayout.setVisibility(View.INVISIBLE);
                playersLayout.setVisibility(View.VISIBLE);
                type = 3;
                setPlayerNames();
                break;

            case R.id.logButton:

                SettingsLayout.setVisibility(View.INVISIBLE);
                logLayout.setVisibility(View.VISIBLE);

                logText = "";

                for(int counter = 0; counter <= events.size() - 1; counter++){

                    logText += events.get(counter).toString();
                    logText += "\n\n";
                }

                Log.setText(logText);
                break;

            case R.id.logBack:

                logLayout.setVisibility(View.INVISIBLE);
                SettingsLayout.setVisibility(View.VISIBLE);
                break;

            case R.id.undo:

                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();

                if (undoType == 1) {

                    addEvent(setTime, "", "", " - GOAL CANCELED", true);

                    if (homePlayers.contains(lastClickedPlayer)) {
                        homeResult--;
                        HomeResult.setText(Integer.toString(homeResult));
                    }
                    else {
                        awayResult--;
                        AwayResult.setText(Integer.toString(awayResult));
                    }
                }
                else if (undoType == 2){

                    addEvent(setTime, "", "", " - SUBSTITUTION CANCELED", true);

                    Replace(subName, PlayerToSub);
                }
                else if (undoType == 3) {

                    addEvent(setTime, "", "", " - YELLOW CARD CANCELED", true);

                    int yellowCard = pref.getInt(lastClickedPlayer, 0);
                    yellowCard--;
                    editor.putInt(lastClickedPlayer, yellowCard);
                    editor.commit(); // SAVE new YellowCard to player
                }
                else if (undoType == 4) {

                    addEvent(setTime, "", "", " - RED CARD CANCELED", true);

                    int red = pref.getInt(lastClickedPlayer + "R", 0);
                    red--;
                    editor.putInt(lastClickedPlayer + "R", red);
                    editor.commit(); // SAVE new RedCard to player
                }

                if (undoType == 0)
                    Toast.makeText(this, "YOU CAN'T REMOVE LAST OPERATION",
                            Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "YOU REMOVED LAST OPERATION",
                            Toast.LENGTH_SHORT).show();

                undoType = 0;
                break;

            case R.id.endHalf:

                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setMessage("Are you sure?").setCancelable(true)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                changeHalf();
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                            }

                        })
                        .setNeutralButton("Terminate", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (half < 3) {
                            half = 3;
                            isStarted = false;
                            addEvent(setTime, "", "", " - MATCH TERMINATED", true);
                            Toast.makeText(SettingsLayout.getContext(), "MATCH TERMINATED",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }) ;

                AlertDialog al = alert.create();
                al.setTitle("Are you sure?");
                alert.show();

                break;
        }
    } // CHECKS which button was pressed

    private void sendMatchState(boolean isActive) {

        MatchStateDTO changeEvent = new MatchStateDTO(id, isActive);

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


        isActiveService service = retrofit.create(isActiveService.class);

        service.send(changeEvent).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "SUCCESS",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "NZ MA NE RABOTI",
                            Toast.LENGTH_SHORT).show();
                    android.util.Log.d("Error:", response.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Toast.makeText(getApplicationContext(), "FAILURE",
                        Toast.LENGTH_SHORT).show();
                android.util.Log.d("Error:",t.getMessage());

            }

        });
    }

    private void changeHalf() {

        if (half < 5)
            half++;

        if (half == 1 && isStarted) { // IF it's FIRST HALF

            endHalf.setText("START SECOND HALF");

            isStarted = false;
            addEvent(setTime, "", "", " - END OF FIRST HALF", true);
            Toast.makeText(SettingsLayout.getContext(), "END OF FIRST HALF",
                    Toast.LENGTH_SHORT).show();
        } else if (half == 2) { // IF it's HALF TIME

            endHalf.setText("FULL TIME");

            isStarted = true;
            BigMinutes = IntInfo.get(2);
            BigSeconds = 0;
            SmallMinutes = 0;
            SmallSeconds = 0;

            addEvent(setTime, "", "", " - SECOND HALF", true);
            Toast.makeText(SettingsLayout.getContext(), "SECOND HALF",
                    Toast.LENGTH_SHORT).show();

            vibrator = false;
        } else if (half == 3 && isStarted) { // IF it's SECOND HALF

            isStarted = false;
            addEvent(setTime, "", "", " - FULL TIME", true);
            sendMatchState(false);
            Toast.makeText(SettingsLayout.getContext(), "FULL TIME",
                    Toast.LENGTH_SHORT).show();

            // >>>>>>>>>>>>>>>>>>>>>>> SEND STRING AND CLOSE APP <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        }
    }

    public void changeResult(){

        if (isHome) {

            homeResult++;
            HomeResult.setText(Integer.toString(homeResult));
        }
        else {

            awayResult++;
            AwayResult.setText(Integer.toString(awayResult));
        }
    } // CHANGES Home or Away Team result

    public void changeHalfString(TextView halfText) {

        if (half == 0)
            halfText.setText("FH");
        else if (half == 1)
            halfText.setText("HT");
        else if (half == 2)
            halfText.setText("SH");
        else
            halfText.setText("FT");

    } // CHECKS which Half is and SETS HalfText

    public void startTimer(View v) {

        if (isStarted) {

            smallT = !smallT;
        }
    } //CHECKS if BigTimer is started and starts SmallTimer

    public void changeFragment(View v) {

        String tag = v.getResources().getResourceEntryName(v.getId());

        if (isStarted) {

            if (tag.equals("homeLayout")) {

                isHome = true;
                Abbr.setText(info.get(4));  // CHECKS if it's Home or Away Team menu and GETS TeamName
                TeamLayout.setVisibility(View.VISIBLE);
                MainLayout.setVisibility(View.INVISIBLE);
                changeHalfString(HalfTeam);   // CHECKS which Half is and SETS HalfText
            } else if (tag.equals("awayLayout")) {

                isHome = false;
                Abbr.setText(info.get(5)); // CHECKS if it's Home or Away Team menu and GETS TeamName
                TeamLayout.setVisibility(View.VISIBLE);
                MainLayout.setVisibility(View.INVISIBLE);
                changeHalfString(HalfTeam);   // CHECKS which Half is and SETS HalfText
            }
        }
    } // STARTS TeamLayout for Home or Away team

    public int TimerFormat(int seconds, int minutes) {

        if (seconds >= 60) {

            minutes++;
            seconds = 0;
        }

        if (minutes == 0) {
            if (seconds < 10)
                setTime = "00:0" + seconds;
            else
                setTime = "00:" + seconds;
        } else if (minutes < 10) {
            if (seconds < 10)
                setTime = "0" + minutes + ":0" + seconds;
            else
                setTime = "0" + minutes + ":" + seconds;
        } else {
            if (seconds < 10)
                setTime = minutes + ":0" + seconds;
            else
                setTime = minutes + ":" + seconds;
        }

        return minutes;
    } // GETS minutes and seconds of all timers and change them to format 00:00

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        updateDisplay();
        super.onExitAmbient();
    }

    private void updateDisplay() {
        if (isAmbient()) {
            mContainerView.setBackgroundColor(getResources().getColor(android.R.color.black));
            mTextView.setTextColor(getResources().getColor(android.R.color.white));
            mClockView.setVisibility(View.VISIBLE);

            mClockView.setText(AMBIENT_DATE_FORMAT.format(new Date()));
        } else {
            mContainerView.setBackground(null);
            mTextView.setTextColor(getResources().getColor(android.R.color.black));
            mClockView.setVisibility(View.GONE);
        }
    }
}
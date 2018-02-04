package com.elsys.refpro.refprowatch.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.elsys.refpro.refprowatch.R;
import com.elsys.refpro.refprowatch.events.CreateEvent;
import com.elsys.refpro.refprowatch.http.dto.MatchEventDTO;
import com.elsys.refpro.refprowatch.http.dto.MatchStateDTO;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends WearableActivity implements View.OnClickListener {

    //region INSTANTIATE
    ArrayList<String> info  = new ArrayList<>();        // MatchInformation - REPLACE WITH STRING FROM MOBILE
    ArrayList<String>  homePlayers = new ArrayList<>(); // MatchInformation - REPLACE WITH STRING FROM MOBILE
    ArrayList<String>  awayPlayers = new ArrayList<>(); // MatchInformation - REPLACE WITH STRING FROM MOBILE
    ArrayList<String>  homeSubs = new ArrayList<>();    // MatchInformation - REPLACE WITH STRING FROM MOBILE
    ArrayList<String>  awaySubs = new ArrayList<>();    // MatchInformation - REPLACE WITH STRING FROM MOBILE
    ArrayList<Integer> intInfo = new ArrayList<>();     // MatchInformation - REPLACE WITH STRING FROM MOBILE

    String subName, playerToSub;
    String lastClickedPlayer;

    TextView homeAbbr, awayAbbr, bigTimer, smallTimer, abbreviation, log, Half, halfTeam, homeResultField, awayResultField, clock;
    int homeResult = 0, awayResult = 0, homePlayersRedCards = 0, awayPlayersRedCards = 0;

    int bigSeconds = 0, smallSeconds = 0, bigMinutes = 0, smallMinutes = 0;
    int type = 0; // 0 GOAL, 1 SUB, 2 YELLOW, 3 RED
    int undoType = 0; // 1 GOAL, 2 SUB. 3 YELLOW, 4 RED, 5 HALF
    int extraTimeType = 0;
    String setTime = "00:00", logText = "";

    ImageButton startButton;
    Button  terminateButton, extraTimeButton, teamBackButton, settingsBackButton, goalButton, yellowCardButton, redCardButton, logButton, logBack, endHalf, subButton, undoButton;
    ArrayList<Button> playerButtons = new ArrayList<>();
    ArrayList<Button> subsButtons = new ArrayList<>();
    boolean isStarted = false, smallT = false, isHome = true, vibrator = false;

    RelativeLayout mainLayout, teamLayout, timerLayout, settingsLayout;
    LinearLayout playersLayout, playersView, logLayout, subsLayout, subsView;

    int half = 0; // 0 FH, 1 HT, 2 SH, 3 FT

    ArrayList<MatchEventDTO> events = new ArrayList<>();
    String id = "";
    CreateEvent newEvent;
    boolean ownGoal = false;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        SharedPreferences clear = getApplicationContext().getSharedPreferences("RefPRO", Context.MODE_PRIVATE);
        clear.edit().clear().apply();               // CLEARS all information about yellow and red cards

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

        intInfo.add(11);
        intInfo.add(7);
        intInfo.add(45);

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

        Initialize(); // Initialization of all TextViews, Buttons, Layout variables

        timerLayout.setOnLongClickListener(new View.OnLongClickListener() { // STARTS settingsLayout OnLongClick
            @Override
            public boolean onLongClick(View v) {

                settingsLayout.setVisibility(View.VISIBLE);
                mainLayout.setVisibility(View.INVISIBLE);
                return true;
            }
        });


        newEvent = new CreateEvent(id, getApplicationContext());
        events = newEvent.addEvent("", "", "", info.get(0) + "\n" + info.get(1) + "\n" + info.get(6) + "\n" + info.get(7) + "\n"
                + info.get(2) + " vs. " + info.get(3) + "\n\n", true);

        //addEvent("", "", "",  info.get(0) + "\n" + info.get(1) + "\n" + info.get(6) + "\n" + info.get(7) + "\n"
          //      + info.get(2) + " vs. " + info.get(3) + "\n\n", true);

        ListPlayers(); // CREATES Buttons for all players
        ListSubs();    // CREATES Buttons for all substitutes

        CreateTimers(); // CREATES bigTimer and smallTimer
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

                            bigMinutes = TimerFormat(bigSeconds, bigMinutes);
                            if (bigMinutes == intInfo.get(2) && !vibrator) {  //VIBRATES if bigMinutes == HalfLength

                                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                v.vibrate(1000);
                                vibrator = true;
                            }

                            bigTimer.setText(setTime);
                            if (bigSeconds == 60)
                                bigSeconds = 0;
                            bigSeconds++;
                        }

                        if (smallT && isStarted) { // STARTS if timerLayout is clicked and CurrentMatch is started

                            smallMinutes = TimerFormat(smallSeconds, smallMinutes);
                            smallTimer.setText(setTime);
                            if (smallSeconds == 60)
                                smallSeconds = 0;
                            smallSeconds++;
                        }
                    }
                });
            }
        }, 1000, 1000);
    }  // CREATES bigTimer and smallTimer

    private void ListSubs() {

        for(int counter = 0; counter < intInfo.get(1); counter++) {


            final Button newSub = new Button(this);
            newSub.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            newSub.setText(getResources().getString(R.string.defaultName));
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
                    Replace(playerToSub, subName); // REPLACES Player with Substitute
                    subsLayout.setEnabled(false);
                    mainLayout.setEnabled(true);

                    events = newEvent.addEvent(bigMinutes + ":" + bigSeconds, getResources().getString(R.string.substitutionEvent), team, playerToSub + "/" + playerName, false);
                    //addEvent(bigMinutes + ":" + bigSeconds, getResources().getString(R.string.substitutionEvent), team, playerToSub + "/" + playerName, false);

                    subsLayout.setVisibility(View.INVISIBLE);
                    mainLayout.setVisibility(View.VISIBLE);
                }
            });
            subsButtons.add(newSub);  // ADD current substitute to List of Buttons
            subsView.addView(newSub); // CREATE Button for current substitute
        }
    }   // CREATES Buttons for all substitutes

    private void ListPlayers() {

        for(int counter = 0; counter < intInfo.get(0); counter++) {

            final Button newPlayer = new Button(this);
            newPlayer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            newPlayer.setText(getResources().getString(R.string.defaultName));
            newPlayer.setId(counter); //CREATES Button for every player with Text and ID

            newPlayer.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();

                    final String playerName = newPlayer.getText().toString();
                    lastClickedPlayer = playerName;
                    int yellowCard = pref.getInt(playerName, 0);
                    int redCard = pref.getInt(playerName + "R", 0); // GETS yellow and red cards of ClickedPlayer
                    final String team;

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

                        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                        alert.setMessage("GOAL").setCancelable(true)
                                .setPositiveButton("GOAL", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        ownGoal = false;
                                        changeResult(ownGoal);
                                        events = newEvent.addEvent(bigMinutes + ":" + bigSeconds + " / " + homeResult + ":" + awayResult, getResources().getString(R.string.goalEvent), team, playerName, false);
                                    }
                                })
                                .setNegativeButton("OWN GOAL", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        ownGoal = true;
                                        changeResult(ownGoal);
                                        events = newEvent.addEvent(bigMinutes + ":" + bigSeconds + " / " + homeResult + ":" + awayResult, getResources().getString(R.string.ownGoalEvent), team, playerName, false);
                                    }
                                });

                        AlertDialog al = alert.create();
                        al.setTitle("GOAL");
                        alert.show();

                        undoType = 1;
                    }
                    else if (type == 1) { // IF SUBSTITUTION Button is clicked

                        playersLayout.setVisibility(View.INVISIBLE);
                        subsLayout.setVisibility(View.VISIBLE);
                        setSubsNames();
                        playerToSub = playerName;

                        undoType = 2;
                    }
                    else if (type == 2){ // IF YELLOW CARD Button is clicked

                        events = newEvent.addEvent(bigMinutes + ":" + bigSeconds,  getResources().getString(R.string.yellowCardEvent), team, playerName, false);
                        yellowCard++;
                        editor.putInt(playerName, yellowCard);
                        editor.apply(); // SAVE new YellowCard to player

                        undoType = 3;

                        if (yellowCard == 2 || redCard == 1) { // IF player has 2 Yellow or 1 Red Card => Disable his button

                            events = newEvent.addEvent(bigMinutes + ":" + bigSeconds, getResources().getString(R.string.redCardEvent), team, playerName, false);
                            newPlayer.setEnabled(false);

                            if (isHome)
                                homePlayersRedCards++;
                            else
                                awayPlayersRedCards++;

                            if (homePlayersRedCards >=4 || awayPlayersRedCards >=4)
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.redCardsWarning),
                                        Toast.LENGTH_SHORT).show();
                        }

                    }
                    else { // IF RED CARD Button is clicked

                        events = newEvent.addEvent(bigMinutes + ":" + bigSeconds, getResources().getString(R.string.redCardEvent), team, playerName, false);
                        redCard++;

                        if (isHome)
                            homePlayersRedCards++;
                        else
                            awayPlayersRedCards++;

                        if (homePlayersRedCards >=4 || awayPlayersRedCards >=4)
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.redCardsWarning),
                                    Toast.LENGTH_SHORT).show();

                        editor.putInt(playerName + "R", redCard);
                        editor.commit();
                        newPlayer.setEnabled(false); // SAVE RedCard to player and Disable his button

                        undoType = 4;
                    }

                    if (type != 1) { // IF GOAL, YELLOW or RED CARD is clicked => RETURN to mainLayout
                        playersLayout.setVisibility(View.INVISIBLE);
                        mainLayout.setVisibility(View.VISIBLE);
                    }
                }
            });

            playerButtons.add(newPlayer); // ADD current player to List of Buttons
            playersView.addView(newPlayer); // CREATE Button for current player
        }
    } // CREATES Buttons for all players

    private void Initialize() {

        log = (TextView) findViewById(R.id.displayLog);                // Prints LOG in LogLayout
        Half = (TextView) findViewById(R.id.displayHalf);              // ?!?
        halfTeam = (TextView) findViewById(R.id.displayHalfTeamLayout);      // Prints HALF in teamLayout
        homeResultField = (TextView) findViewById(R.id.homeResult);  // Prints Home Team Result
        awayResultField = (TextView) findViewById(R.id.awayResult);  // Prints Away Team Result
        clock = (TextView) findViewById(R.id.textClock);

        homeAbbr = (TextView) findViewById(R.id.homeAbbr);      // Prints Home Team Abbreaviation in mainLayout
        homeAbbr.setText(info.get(4));
        awayAbbr = (TextView) findViewById(R.id.awayAbbr);      // Prints Away Team Abbreaviation in mainLayout
        awayAbbr.setText(info.get(5));

        smallTimer = (TextView) findViewById(R.id.extraTimeTimer);  // smallTimer for ExtraTime
        bigTimer = (TextView) findViewById(R.id.currentTimeTimer);      // bigTimer for Time

        abbreviation = (TextView) findViewById(R.id.TeamLayoutAbbr);              // Team Abbreviation in teamLayout

        mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);            // mainLayout
        teamLayout = (RelativeLayout) findViewById(R.id.teamLayout);            // teamLayout
        timerLayout = (RelativeLayout) findViewById(R.id.timerLayout);          // Layout for Timers which is connected to settingsLayout
        settingsLayout = (RelativeLayout) findViewById(R.id.settingsLayout);    // settingsLayout

        playersLayout = (LinearLayout) findViewById(R.id.listPlayersLayout);    // Layout parent of ScrollView with all players
        subsLayout = (LinearLayout) findViewById(R.id.listSubsLayout);          // Layout parent of ScrollView with all substitutes
        logLayout = (LinearLayout) findViewById(R.id.logLayout);            // Layout which Prints match logText
        playersView = (LinearLayout) findViewById(R.id.listPlayersView);        // Layout which Lists player names
        subsView = (LinearLayout) findViewById(R.id.listSubsView);              // Layout which Lists substitute names

        startButton = (ImageButton) findViewById(R.id.startMatchButton);                // Starts Match and Timers
        startButton.setOnClickListener(this);
        teamBackButton = (Button) findViewById(R.id.teamLayoutBackButton);              // Returns from teamLayout to mainLayout
        teamBackButton.setOnClickListener(this);
        settingsBackButton = (Button) findViewById(R.id.settingsBackButton);  // Returns from settingsLayout to mainLayout
        settingsBackButton.setOnClickListener(this);
        goalButton = (Button) findViewById(R.id.goalButton);                  // Starts GOAL menu
        goalButton.setOnClickListener(this);
        yellowCardButton = (Button) findViewById(R.id.yellowCardButton);          // Starts YELLOW CARD menu
        yellowCardButton.setOnClickListener(this);
        redCardButton = (Button) findViewById(R.id.redCardButton);                // Starts RED CARD menu
        redCardButton.setOnClickListener(this);
        subButton = (Button) findViewById(R.id.substituteButton);             // Starts SUBSTITUTION menu
        subButton.setOnClickListener(this);
        logButton = (Button) findViewById(R.id.logButton);              // Starts LOGLayout
        logButton.setOnClickListener(this);
        logBack = (Button) findViewById(R.id.logBackButton);                  // Returns from LogLayout to mainLayout
        logBack.setOnClickListener(this);
        endHalf = (Button) findViewById(R.id.endHalfButton);                  // END HALF, START SECOND HALF and FULL TIME
        endHalf.setOnClickListener(this);
        undoButton = (Button) findViewById(R.id.undoButton);
        undoButton.setOnClickListener(this);
        extraTimeButton = (Button) findViewById(R.id.extraTimeButton);
        extraTimeButton.setOnClickListener(this);
        extraTimeButton.setEnabled(false);
        terminateButton = (Button) findViewById(R.id.terminateButton);
        terminateButton.setOnClickListener(this);
    } // Initialization of all TextView, Button, Layout variables

    public void Replace(String playerNameString, String subNameString) {

        int PlayerID = 0, SubID = 0;
        String PlayerName = "", SubName = "";

        if (isHome) { // IF it's HomeTeam Menu => GET index of given player and substitute and make SUBSTITUTION
            for (int counter = 0; counter < intInfo.get(0); counter++) {

                if (homePlayers.get(counter).contains(playerNameString)) {
                    PlayerID = counter;
                    PlayerName = homePlayers.get(counter);
                }
            }

            for (int counter = 0; counter < intInfo.get(1); counter++) {

                if (homeSubs.get(counter).contains(subNameString)) {
                    SubID = counter;
                    SubName = homeSubs.get(counter);
                }
            }

            homePlayers.set(PlayerID, SubName);
            homeSubs.set(SubID, PlayerName);
        }
        else { // IF it's AwayTeam Menu => GET index of given player and substitute and make SUBSTITUTION

            for (int counter = 0; counter < intInfo.get(0); counter++) {

                if (awayPlayers.get(counter).contains(playerNameString)) {
                    PlayerID = counter;
                    PlayerName = awayPlayers.get(counter);
                }
            }

            for (int counter = 0; counter < intInfo.get(1); counter++) {

                if (awaySubs.get(counter).contains(subNameString)) {
                    SubID = counter;
                    SubName = awaySubs.get(counter);
                }
            }

            awayPlayers.set(PlayerID, SubName);
            awaySubs.set(SubID, PlayerName);
        }
    } // REPLACES Player with Substitute

    public void setPlayerNames() {

        for (int counter = 0; counter < intInfo.get(0); counter++) {

            if (isHome) {

                playerButtons.get(counter).setText(homePlayers.get(counter));
            } else

                playerButtons.get(counter).setText(awayPlayers.get(counter));

            playerButtons.get(counter).setEnabled(true);
            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

            String playerName =  ((String) playerButtons.get(counter).getText());
            int yellowCard =  pref.getInt(playerName, 0);
            int redCard = pref.getInt(playerName + "R", 0);

            if (yellowCard == 2 || redCard == 1)
                playerButtons.get(counter).setEnabled(false);// ENABLE all buttons, check for YELLOW or RED CARDS and Disable buttons

        }
    } // SETS names of all players

    public void setSubsNames() {

        for (int counter = 0; counter < intInfo.get(1); counter++) {

            if (isHome) {

                subsButtons.get(counter).setText(homeSubs.get(counter)); // SETS Text for every Home Substitute
            } else

                subsButtons.get(counter).setText(awaySubs.get(counter)); // SETS Text for every Away Substitute
        }
    } // SETS names of all substitutes

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.startMatchButton:

                bigTimer.setVisibility(View.VISIBLE);
                smallTimer.setVisibility(View.VISIBLE);
                isStarted = true;
                startButton.setVisibility(View.INVISIBLE);

                Toast.makeText(this, getResources().getString(R.string.matchStartedText),
                        Toast.LENGTH_SHORT).show();
                events = newEvent.addEvent(clock.getText().toString(), "", "",  " - " +  "MATCH STARTED", true);
                newEvent.addState(true, events);
                break;

            case R.id.teamLayoutBackButton:

                teamLayout.setVisibility(View.INVISIBLE);
                mainLayout.setVisibility(View.VISIBLE);
                break;

            case R.id.settingsBackButton:

                settingsLayout.setVisibility(View.INVISIBLE);
                mainLayout.setVisibility(View.VISIBLE);
                changeHalfString(Half);
                break;

            case R.id.goalButton:

                teamLayout.setVisibility(View.INVISIBLE);
                playersLayout.setVisibility(View.VISIBLE);
                type = 0;
                setPlayerNames();
                break;

            case R.id.substituteButton:

                teamLayout.setVisibility(View.INVISIBLE);
                playersLayout.setVisibility(View.VISIBLE);
                type = 1;
                setPlayerNames();
                break;

            case R.id.yellowCardButton:

                teamLayout.setVisibility(View.INVISIBLE);
                playersLayout.setVisibility(View.VISIBLE);
                type = 2;
                setPlayerNames();
                break;

            case R.id.redCardButton:

                teamLayout.setVisibility(View.INVISIBLE);
                playersLayout.setVisibility(View.VISIBLE);
                type = 3;
                setPlayerNames();
                break;

            case R.id.logButton:

                settingsLayout.setVisibility(View.INVISIBLE);
                logLayout.setVisibility(View.VISIBLE);

                logText = "";

                for(int counter = 0; counter <= events.size() - 1; counter++){

                    logText += events.get(counter).toString();
                    logText += "\n\n";
                }

                log.setText(logText);
                break;

            case R.id.logBackButton:

                logLayout.setVisibility(View.INVISIBLE);
                settingsLayout.setVisibility(View.VISIBLE);
                break;

            case R.id.extraTimeButton:

                if (half < 3) {

                    extraTimeType++;
                    extraTime(extraTimeType);
                }
                break;

            case R.id.terminateButton:

                if (half < 3) {
                    half = 3;
                    isStarted = false;
                    events = newEvent.addEvent(bigMinutes + ":" + bigSeconds, "", "", " - " + getResources().getString(R.string.matchTerminatedText), true);
                    newEvent.addState(false, events);
                    Toast.makeText(settingsLayout.getContext(), getResources().getString(R.string.matchTerminatedText),
                            Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.undoButton:

                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();

                if (undoType == 1) {

                    if ((homePlayers.contains(lastClickedPlayer) && !ownGoal) || (awayPlayers.contains(lastClickedPlayer) && ownGoal)) {
                        homeResult--;
                        homeResultField.setText(Integer.toString(homeResult));
                    }
                    else {
                        awayResult--;
                        awayResultField.setText(Integer.toString(awayResult));
                    }

                    events = newEvent.addEvent(bigMinutes + ":" + bigSeconds + " / " + homeResult + ":" + awayResult, "", "", " - " + getResources().getString(R.string.goalEvent) + getResources().getString(R.string.canceledEvent), true);
                }
                else if (undoType == 2){

                    events = newEvent.addEvent(bigMinutes + ":" + bigSeconds, "", "", " - " + getResources().getString(R.string.substitutionEvent) + getResources().getString(R.string.canceledEvent), true);

                    Replace(subName, playerToSub);
                }
                else if (undoType == 3) {

                    events = newEvent.addEvent(bigMinutes + ":" + bigSeconds, "", "", " - " + getResources().getString(R.string.yellowCardEvent) + getResources().getString(R.string.canceledEvent), true);

                    int yellowCard = pref.getInt(lastClickedPlayer, 0);
                    yellowCard--;
                    editor.putInt(lastClickedPlayer, yellowCard);
                    editor.apply(); // SAVE new YellowCard to player
                }
                else if (undoType == 4) {

                    events = newEvent.addEvent(bigMinutes + ":" + bigSeconds, "", "", " - " + getResources().getString(R.string.redCardEvent) + getResources().getString(R.string.canceledEvent), true);

                    int red = pref.getInt(lastClickedPlayer + "R", 0);
                    red--;
                    editor.putInt(lastClickedPlayer + "R", red);
                    editor.commit(); // SAVE new RedCard to player
                }

                if (undoType == 0)
                    Toast.makeText(this,  getResources().getString(R.string.removeEventError),
                            Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this,  getResources().getString(R.string.removeEventSuccess),
                            Toast.LENGTH_SHORT).show();

                undoType = 0;
                break;

            case R.id.endHalfButton:

                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setMessage(getResources().getString(R.string.question)).setCancelable(true)
                        .setPositiveButton(getResources().getString(R.string.positiveAnswer), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                changeHalf();
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.negativeAnswer), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                            }

                        });

                AlertDialog al = alert.create();
                al.setTitle(getResources().getString(R.string.question));
                alert.show();

                break;
        }
    } // CHECKS which button was pressed

    private void changeHalf() {

        if (half < 5)
            half++;

        if (half == 1 && isStarted) { // IF it's FIRST HALF

            endHalf.setText(getResources().getString(R.string.secondHalfText));

            isStarted = false;
            events = newEvent.addEvent(bigMinutes + ":" + bigSeconds, "", "", " - " + getResources().getString(R.string.firstHalfEnd), true);
            Toast.makeText(settingsLayout.getContext(), getResources().getString(R.string.firstHalfEnd),
                    Toast.LENGTH_SHORT).show();
        } else if (half == 2) { // IF it's HALF TIME

            endHalf.setText(getResources().getString(R.string.fullTimeText));

            extraTimeButton.setEnabled(true);
            isStarted = true;
            bigMinutes = intInfo.get(2);
            bigSeconds = 0;
            smallMinutes = 0;
            smallSeconds = 0;

            events = newEvent.addEvent(bigMinutes + ":" + bigSeconds, "", "", " - " + getResources().getString(R.string.secondHalfStart), true);
            Toast.makeText(settingsLayout.getContext(), getResources().getString(R.string.secondHalfStart),
                    Toast.LENGTH_SHORT).show();

            vibrator = false;
        } else if (half == 3 && isStarted) { // IF it's SECOND HALF

            isStarted = false;
            events = newEvent.addEvent(bigMinutes + ":" + bigSeconds, "", "", " - " + getResources().getString(R.string.fullTimeText), true);
            newEvent.addState(false, events);
            Toast.makeText(settingsLayout.getContext(), getResources().getString(R.string.fullTimeText),
                    Toast.LENGTH_SHORT).show();

            // >>>>>>>>>>>>>>>>>>>>>>> SEND STRING AND CLOSE APP <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        }
    }

    public void extraTime(int extraTimetype) {

        vibrator = true;

        if (extraTimetype == 1) {

            endHalf.setEnabled(false);
            isStarted = false;

            extraTimeButton.setText(getResources().getString(R.string.startExtraTimeButton));
        }
        else if (extraTimetype == 2) {

            bigMinutes = intInfo.get(2) * 2;
            bigSeconds = 0;
            smallMinutes = 0;
            smallSeconds = 0;
            isStarted = true;
            extraTimeButton.setText(getResources().getString(R.string.endExtraTimeButton));

            events = newEvent.addEvent(bigMinutes + ":" + bigSeconds, "", "", " - " + getResources().getString(R.string.extraTimeButton), true);
            Toast.makeText(settingsLayout.getContext(), getResources().getString(R.string.extraTimeButton),
                    Toast.LENGTH_SHORT).show();
        }
        else if (extraTimetype == 3) {

            extraTimeButton.setText(getResources().getString(R.string.secondExtraTimeButton));
            isStarted = false;

            events = newEvent.addEvent(bigMinutes + ":" + bigSeconds, "", "", " - " + getResources().getString(R.string.endExtraTimeButton), true);
            Toast.makeText(settingsLayout.getContext(), getResources().getString(R.string.endExtraTimeButton),
                    Toast.LENGTH_SHORT).show();
        }
        else  if (extraTimetype == 4) {

            bigMinutes = intInfo.get(2) * 2 + 15;
            bigSeconds = 0;
            smallMinutes = 0;
            smallSeconds = 0;
            isStarted = true;
            extraTimeButton.setText(getResources().getString(R.string.penaltiesButton));
            endHalf.setEnabled(true);

            events = newEvent.addEvent(bigMinutes + ":" + bigSeconds, "", "", " - " + getResources().getString(R.string.endExtraTimeButton), true);
            Toast.makeText(settingsLayout.getContext(), getResources().getString(R.string.secondExtraTimeButton),
                    Toast.LENGTH_SHORT).show();
        }
        else {

            bigMinutes = 0;
            bigSeconds = 0;
            smallMinutes = 0;
            smallSeconds = 0;

            events = newEvent.addEvent(bigMinutes + ":" + bigSeconds, "", "", " - " + getResources().getString(R.string.penaltiesButton), true);
            Toast.makeText(settingsLayout.getContext(), getResources().getString(R.string.penaltiesButton),
                    Toast.LENGTH_SHORT).show();

            extraTimeButton.setEnabled(false);
        }
    }

    public void changeResult(boolean ownGoal){

        if (ownGoal)
            isHome = !isHome;

        if (isHome) {

            homeResult++;
            homeResultField.setText(Integer.toString(homeResult));
        }
        else {

            awayResult++;
            awayResultField.setText(Integer.toString(awayResult));
        }
    } // CHANGES Home or Away Team result

    public void changeHalfString(TextView halfText) {

        if (half == 0)
            halfText.setText(getResources().getString(R.string.displayHalf));
        else if (half == 1)
            halfText.setText(getResources().getString(R.string.halfTimeTextShort));
        else if (half == 2 && extraTimeType == 0)
            halfText.setText(getResources().getString(R.string.secondHalfTextShort));
        else if ((half == 2 && extraTimeType == 1) || (half == 2 && extraTimeType == 2))
            halfText.setText(getResources().getString(R.string.extraTimeTextShort));
        else if ((half == 2 && extraTimeType == 3) || (half == 2 && extraTimeType == 4))
            halfText.setText(getResources().getString(R.string.extraTimeTextShort));
        else if ((half == 2 && extraTimeType == 5))
            halfText.setText(getResources().getString(R.string.penaltiesTimeTextShort));
        else
            halfText.setText(getResources().getString(R.string.fullTimeTextShort));

    } // CHECKS which Half is and SETS HalfText

    public void startTimer(View v) {

        if (isStarted) {

            smallT = !smallT;
        }
    } //CHECKS if bigTimer is started and starts smallTimer

    public void changeFragment(View v) {

        String tag = v.getResources().getResourceEntryName(v.getId());

        if (isStarted) {

            if (tag.equals("homeLayout")) {

                isHome = true;
                abbreviation.setText(info.get(4));  // CHECKS if it's Home or Away Team menu and GETS TeamName
                teamLayout.setVisibility(View.VISIBLE);
                mainLayout.setVisibility(View.INVISIBLE);
                changeHalfString(halfTeam);   // CHECKS which Half is and SETS HalfText
            } else if (tag.equals("awayLayout")) {

                isHome = false;
                abbreviation.setText(info.get(5)); // CHECKS if it's Home or Away Team menu and GETS TeamName
                teamLayout.setVisibility(View.VISIBLE);
                mainLayout.setVisibility(View.INVISIBLE);
                changeHalfString(halfTeam);   // CHECKS which Half is and SETS HalfText
            }
        }
    } // STARTS teamLayout for Home or Away team

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
    } // GETS minutes and seconds of all timers and change them to format HH:mm
}
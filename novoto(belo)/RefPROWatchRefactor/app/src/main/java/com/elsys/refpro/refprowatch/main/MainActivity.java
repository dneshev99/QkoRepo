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

import com.elsys.refpro.refprowatch.models.Buttons;
import com.elsys.refpro.refprowatch.models.Event;
import com.elsys.refpro.refprowatch.models.Match;
import com.elsys.refpro.refprowatch.models.Player;
import com.elsys.refpro.refprowatch.R;
import com.elsys.refpro.refprowatch.models.Team;
import com.elsys.refpro.refprowatch.events.CreateEvent;
import com.elsys.refpro.refprowatch.http.dto.MatchEventDTO;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends WearableActivity implements View.OnClickListener {

    Team homeTeam = new Team(true, "FC Barcelona", "BAR", new ArrayList<Player>(), new ArrayList<Player>());
    Team awayTeam = new Team(false, "Real Madrid CF", "RMA", new ArrayList<Player>(), new ArrayList<Player>());

    Match match = new Match("La liga", "Camp nou", "19:45", "2018-01-25", 45, 11, 7, homeTeam, awayTeam);

    com.elsys.refpro.refprowatch.models.Timer timers = new com.elsys.refpro.refprowatch.models.Timer();
    Buttons buttonManager = new Buttons();

    TextView homeAbbr, awayAbbr, bigTimer, smallTimer, abbreviation, log, halfText, halfTeamMenuText, homeResultField, awayResultField, clock;

    boolean isHome = true, vibrator = false;

    RelativeLayout mainLayout, teamLayout, timerLayout, settingsLayout;
    LinearLayout playersLayout, playersView, logLayout, subsLayout, subsView;

    ArrayList<MatchEventDTO> events = new ArrayList<>();
    String id = "";
    CreateEvent newEvent;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        SharedPreferences clear = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        clear.edit().clear().apply();               // CLEARS all information about yellow and red cards

        ArrayList<Player> homePlayer = new ArrayList<>();
        ArrayList<Player> awayPlayer = new ArrayList<>();
        ArrayList<Player> homeSubstitutes = new ArrayList<>();
        ArrayList<Player> awaySubstitutes = new ArrayList<>();

        for (int count = 0; count < 11; count++) {
            Player defaultPlayer = new Player(1, "Alexander Verbovskiy" + count);
            homePlayer.add(defaultPlayer);
            Player defaultPlayer2 = new Player(2, "Dimitur Neshev" + count);
            awayPlayer.add(defaultPlayer2);
        }
        for (int count = 0; count < 7; count++) {
            Player defaultPlayer = new Player(3, "Valentin Varbanov" + count);
            homeSubstitutes.add(defaultPlayer);
            Player defaultPlayer2 = new Player(4, "Chikata" + count);
            awaySubstitutes.add(defaultPlayer2);
        }
        match.getHome().setPlayers(homePlayer);
        match.getHome().setSubstitutes(homeSubstitutes);
        match.getAway().setPlayers(awayPlayer);
        match.getAway().setSubstitutes(awaySubstitutes);

        initialize(); // Initialization of all TextViews, Buttons, Layout variables

        timerLayout.setOnLongClickListener(new View.OnLongClickListener() { // STARTS settingsLayout OnLongClick
            @Override
            public boolean onLongClick(View v) {

                settingsLayout.setVisibility(View.VISIBLE);
                mainLayout.setVisibility(View.INVISIBLE);
                return true;
            }
        });

        timers.setMainTimer((TextView) findViewById(R.id.currentTimeTimer));
        timers.setExtraTimer((TextView) findViewById(R.id.extraTimeTimer));
        createTimers(); // CREATES mainTimer and extraTimer

        listPlayers();
        listSubs();

        newEvent = new CreateEvent(id, getApplicationContext());
        events = newEvent.addEvent("", "", "", match.getCompetition() + "\n" + match.getVenue() + "\n" + match.getDate()
                + "\n" + match.getTime() + "\n" + match.getHome().getName() + " vs. " + match.getAway().getName() + "\n\n", true);
    }

    private void createTimers() {

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (match.isStarted()) { // STARTS if CurrentMatch is started

                            timers.MainTimerFormat();

                            if (timers.getMainTimerMinutes() == match.getHalfLength() && !vibrator) {  //VIBRATES if bigMinutes == HalfLength

                                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                v.vibrate(1000);
                                vibrator = true;
                            }

                            timers.setMainTimerText();
                        }

                        if (timers.isExtraTimerStarted() && match.isStarted()) { // STARTS if timerLayout is clicked and CurrentMatch is started

                            timers.ExtraTimerFormat();
                            timers.setExtraTimerText();
                        }
                    }
                });
            }
        }, 1000, 1000);
    }  // CREATES mainTimer and extraTimer    --------

    public void startTimer(View v) {

        if (match.isStarted() && !timers.isExtraTimerStarted()) {

            timers.setExtraTimerStarted(true);
        }
        else if (match.isStarted() && timers.isExtraTimerStarted()) {

            timers.setExtraTimerStarted(false);
        }
    } //CHECKS if mainTimer is started and starts extraTimer  --------

    private void listSubs() {

        for(int counter = 0; counter < match.getSubstitutesNumber(); counter++) {

            final Button newSub = new Button(this);
            newSub.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            newSub.setText(getResources().getString(R.string.defaultName));
            newSub.setId(counter);      //CREATES Button for every substitute with Text and ID

            final int finalCounter = counter;
            newSub.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    String team;

                    if (match.isHomeTeamPressed()) {
                        team = match.getHome().getName();
                    }
                    else {
                        team = match.getAway().getName();
                    }

                    String elements = newSub.getText().toString();
                    String split[] = elements.split("\\.");
                    Player playerName = new Player(Integer.parseInt(split[0]), split[1]);
                    match.setSubstituteName(playerName);
                    replace(match.getPlayerForSubstitution(), match.getSubstituteName()); // REPLACES Player with Substitute
                    subsLayout.setEnabled(false);
                    mainLayout.setEnabled(true);

                    events = newEvent.addEvent(timers.getMainTimerMinutes() + ":" + timers.getMainTimerSeconds(), getResources().getString(R.string.substitutionEvent), team, match.getPlayerForSubstitution().getName() + "/" + playerName.getName(), false);
                    //addEvent(bigMinutes + ":" + bigSeconds, getResources().getString(R.string.substitutionEvent), team, playerToSub + "/" + playerName, false);

                    subsLayout.setVisibility(View.INVISIBLE);
                    mainLayout.setVisibility(View.VISIBLE);
                }
            });
            buttonManager.subsButtons.add(newSub);  // ADD current substitute to List of Buttons
            subsView.addView(newSub); // CREATE Button for current substitute
        }
    }   // CREATES Buttons for all substitutes

    public void replace(Player old, Player subName) {

        int PlayerID = 0, SubID = 0;
        Player PlayerName = null, SubName = null;

        if (match.isHomeTeamPressed()) { // IF it's HomeTeam Menu => GET index of given player and substitute and make SUBSTITUTION
            int indexOfOld = match.getHome().getPlayers().indexOf(old);
            int indexOfSub = match.getHome().getSubstitutes().indexOf(subName);

            match.getHome().getPlayers().set(indexOfOld, subName);
            match.getHome().getSubstitutes().set(indexOfSub, old);
        }
        else { // IF it's AwayTeam Menu => GET index of given player and substitute and make SUBSTITUTION

            int indexOfOld = match.getAway().getPlayers().indexOf(old);
            int indexOfSub = match.getAway().getSubstitutes().indexOf(subName);

            match.getAway().getPlayers().set(indexOfOld, subName);
            match.getAway().getSubstitutes().set(indexOfSub, old);
        }
    } // REPLACES Player with Substitute

    private void listPlayers() {

        for(int counter = 0; counter < match.getPlayersNumber(); counter++) {

            final Button newPlayer = new Button(this);
            newPlayer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            newPlayer.setText(getResources().getString(R.string.defaultName));
            newPlayer.setId(counter); //CREATES Button for every player with Text and ID

            final int finalCounter = counter;
            newPlayer.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();

                    String elements = newPlayer.getText().toString();
                    String split[] = elements.split("\\.");

                    Player playerName = new Player(Integer.parseInt(split[0]), split[1]);
                    match.setLastClickedPlayer(playerName);
                    int yellowCard = pref.getInt(playerName.getName(), 0);
                    int redCard = pref.getInt(playerName.getName() + "R", 0); // GETS yellow and red cards of ClickedPlayer
                    final String team;

                    if (match.isHomeTeamPressed()) {
                        team = match.getHome().getName();
                    }
                    else {
                        team = match.getAway().getName();
                    }


                    if (yellowCard == 2)  // IF player has 2 Yellow Cards => Disable player button
                        newPlayer.setEnabled(false);
                    else
                        newPlayer.setEnabled(true);


                    if (match.getEventType() == 0) {    // IF GOAL Button is clicked

                        final Player playerNameString = playerName;

                        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                        alert.setMessage("GOAL").setCancelable(true)
                                .setPositiveButton("GOAL", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        match.setOwnGoal(false);
                                        changeResult(match.isOwnGoal());
                                        events = newEvent.addEvent(timers.getMainTimerMinutes() + ":" + timers.getMainTimerSeconds() + " / " + match.getHome().getGoals() + ":" + match.getAway().getGoals(), Event.GOAL.toString(), team, playerNameString.getName(), false);
                                    }
                                })
                                .setNegativeButton("OWN GOAL", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        match.setOwnGoal(true);
                                        changeResult(match.isOwnGoal());
                                        events = newEvent.addEvent(timers.getMainTimerMinutes() + ":" + timers.getMainTimerSeconds() + " / " + match.getHome().getGoals() + ":" + match.getAway().getGoals(), Event.OWNGOAL.toString(), team, playerNameString.getName(), false);
                                    }
                                });

                        AlertDialog al = alert.create();
                        al.setTitle("GOAL");
                        alert.show();

                        match.setUndoType(1);
                    }
                    else if (match.getEventType() == 1) { // IF SUBSTITUTION Button is clicked

                        playersLayout.setVisibility(View.INVISIBLE);
                        subsLayout.setVisibility(View.VISIBLE);
                        setSubsNames();
                        match.setPlayerForSubstitution(playerName);

                        match.setUndoType(2);
                    }
                    else if (match.getEventType() == 2){ // IF YELLOW CARD Button is clicked

                        events = newEvent.addEvent(timers.getMainTimerMinutes() + ":" + timers.getMainTimerSeconds(),  getResources().getString(R.string.yellowCardEvent), team, playerName.getName(), false);
                        yellowCard++;
                        editor.putInt(playerName.getName(), yellowCard);
                        editor.apply(); // SAVE new YellowCard to player

                        match.setUndoType(3);

                        if (yellowCard == 2 || redCard == 1) { // IF player has 2 Yellow or 1 Red Card => Disable his button

                            events = newEvent.addEvent(timers.getMainTimerMinutes() + ":" + timers.getMainTimerSeconds(), getResources().getString(R.string.redCardEvent), team, playerName.getName(), false);
                            newPlayer.setEnabled(false);

                            if (isHome)
                                match.getHome().setRedCards(match.getHome().getRedCards() + 1);
                            else
                                match.getAway().setRedCards(match.getHome().getRedCards() + 1);

                            if (match.getHome().getRedCards()  >=4 || match.getAway().getRedCards() >=4)
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.redCardsWarning),
                                        Toast.LENGTH_SHORT).show();
                        }

                    }
                    else { // IF RED CARD Button is clicked

                        events = newEvent.addEvent(timers.getMainTimerMinutes() + ":" + timers.getMainTimerSeconds(), getResources().getString(R.string.redCardEvent), team, playerName.getName(), false);
                        redCard++;

                        if (match.isHomeTeamPressed())
                            match.getHome().setRedCards(match.getHome().getRedCards() + 1);
                        else
                            match.getAway().setRedCards(match.getHome().getRedCards() + 1);

                        if (match.getHome().getRedCards()  >=4 || match.getAway().getRedCards() >=4)
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.redCardsWarning),
                                    Toast.LENGTH_SHORT).show();

                        editor.putInt(playerName.getName() + "R", redCard);
                        editor.commit();
                        newPlayer.setEnabled(false); // SAVE RedCard to player and Disable his button

                        match.setUndoType(4);
                    }

                    if (match.getEventType() != 1) { // IF GOAL, YELLOW or RED CARD is clicked => RETURN to mainLayout
                        playersLayout.setVisibility(View.INVISIBLE);
                        mainLayout.setVisibility(View.VISIBLE);
                    }
                }
            });

            buttonManager.playerButtons.add(newPlayer); // ADD current player to List of Buttons
            playersView.addView(newPlayer); // CREATE Button for current player
        }
    } // CREATES Buttons for all players

    private void initialize() {

        log = (TextView) findViewById(R.id.displayLog);                // Prints LOG in LogLayout
        halfText = (TextView) findViewById(R.id.displayHalf);              // ?!?
        halfTeamMenuText = (TextView) findViewById(R.id.displayHalfTeamLayout);      // Prints HALF in teamLayout
        homeResultField = (TextView) findViewById(R.id.homeResult);  // Prints Home Team Result
        awayResultField = (TextView) findViewById(R.id.awayResult);  // Prints Away Team Result
        clock = (TextView) findViewById(R.id.textClock);

        homeAbbr = (TextView) findViewById(R.id.homeAbbr);      // Prints Home Team Abbreaviation in mainLayout
        homeAbbr.setText(match.getHome().getAbbreaviature());
        awayAbbr = (TextView) findViewById(R.id.awayAbbr);      // Prints Away Team Abbreaviation in mainLayout
        awayAbbr.setText(match.getAway().getAbbreaviature());

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


        Buttons setButtons = new Buttons(
                (ImageButton) findViewById(R.id.startMatchButton),
                (Button) findViewById(R.id.teamLayoutBackButton),
                (Button) findViewById(R.id.settingsBackButton),
                (Button) findViewById(R.id.goalButton),
                (Button) findViewById(R.id.yellowCardButton),
                (Button) findViewById(R.id.redCardButton),
                (Button) findViewById(R.id.substituteButton),
                (Button) findViewById(R.id.logButton),
                (Button) findViewById(R.id.logBackButton),
                (Button) findViewById(R.id.endHalfButton),
                (Button) findViewById(R.id.undoButton),
                (Button) findViewById(R.id.extraTimeButton),
                (Button) findViewById(R.id.terminateButton));

        buttonManager = setButtons;

        buttonManager.startButton.setOnClickListener(this);
        buttonManager.teamBackButton.setOnClickListener(this);
        buttonManager.settingsBackButton.setOnClickListener(this);
        buttonManager.goalButton.setOnClickListener(this);
        buttonManager.yellowCardButton.setOnClickListener(this);
        buttonManager.redCardButton.setOnClickListener(this);
        buttonManager.subButton.setOnClickListener(this);
        buttonManager.logButton.setOnClickListener(this);
        buttonManager.logBack.setOnClickListener(this);
        buttonManager.endHalf.setOnClickListener(this);
        buttonManager.undoButton.setOnClickListener(this);
        buttonManager.extraTimeButton.setOnClickListener(this);
        buttonManager.extraTimeButton.setEnabled(false);
        buttonManager.terminateButton.setOnClickListener(this);
    } // Initialization of all TextView, Button, Layout variables   --------

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.startMatchButton:

                bigTimer.setVisibility(View.VISIBLE);
                smallTimer.setVisibility(View.VISIBLE);
                match.setStarted(true);
                buttonManager.startButton.setVisibility(View.INVISIBLE);

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
                changeHalfString(halfText);
                break;

            case R.id.logBackButton:

                logLayout.setVisibility(View.INVISIBLE);
                settingsLayout.setVisibility(View.VISIBLE);
                break;

            case R.id.logButton:

                settingsLayout.setVisibility(View.INVISIBLE);
                logLayout.setVisibility(View.VISIBLE);

                match.setLogText("");

                for(int counter = 0; counter <= events.size() - 1; counter++)
                    match.setLogText(match.getLogText() + events.get(counter).toString() + "\n\n");

                log.setText(match.getLogText());
                break;

             case R.id.extraTimeButton:

                if (match.getHalf() < 3) {

                    match.setExtraTime(match.getExtraTime() + 1);
                    extraTime();
                }
                break;

            case R.id.terminateButton:

                if (match.getHalf() < 3) {
                    match.setHalf(3);
                    match.setStarted(false);
                    events = newEvent.addEvent(timers.getMainTimerMinutes() + ":" + timers.getMainTimerSeconds(), "", "", " - " + Event.TERMINATED, true);
                    newEvent.addState(false, events);
                    Toast.makeText(settingsLayout.getContext(), getResources().getString(R.string.matchTerminatedText),
                            Toast.LENGTH_SHORT).show();
                }

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
            case R.id.goalButton:

                teamLayout.setVisibility(View.INVISIBLE);
                playersLayout.setVisibility(View.VISIBLE);
                match.setEventType(0);
                setPlayerNames();
                break;

            case R.id.substituteButton:

                teamLayout.setVisibility(View.INVISIBLE);
                playersLayout.setVisibility(View.VISIBLE);
                match.setEventType(1);
                setPlayerNames();
                break;

            case R.id.yellowCardButton:

                teamLayout.setVisibility(View.INVISIBLE);
                playersLayout.setVisibility(View.VISIBLE);
                match.setEventType(2);
                setPlayerNames();
                break;

            case R.id.redCardButton:

                teamLayout.setVisibility(View.INVISIBLE);
                playersLayout.setVisibility(View.VISIBLE);
                match.setEventType(3);
                setPlayerNames();
                break;

             case R.id.undoButton:

                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();

                if (match.getUndoType() == 1) {

                    if ((match.getHome().getPlayers().contains(match.getLastClickedPlayer()) && !match.isOwnGoal()) || (match.getAway().getPlayers().contains(match.getLastClickedPlayer()) && match.isOwnGoal())) {
                        match.getHome().setGoals(match.getHome().getGoals() - 1);
                        homeResultField.setText(Integer.toString(match.getHome().getGoals()));
                    }
                    else {
                        match.getAway().setGoals(match.getHome().getGoals() - 1);
                        awayResultField.setText(Integer.toString(match.getAway().getGoals()));
                    }

                    events = newEvent.addEvent(timers.getMainTimerMinutes() + ":" + timers.getMainTimerSeconds() + " / " + match.getHome().getGoals() + ":" + match.getAway().getGoals(), "", "", " - " + getResources().getString(R.string.goalEvent) + getResources().getString(R.string.canceledEvent), true);
                }
                else if (match.getUndoType() == 2){

                    events = newEvent.addEvent(timers.getMainTimerMinutes() + ":" + timers.getMainTimerSeconds(), "", "", " - " + getResources().getString(R.string.substitutionEvent) + getResources().getString(R.string.canceledEvent), true);

                    replace(match.getSubstituteName(), match.getPlayerForSubstitution());
                }
                else if (match.getUndoType() == 3) {

                    events = newEvent.addEvent(timers.getMainTimerMinutes() + ":" + timers.getMainTimerSeconds(), "", "", " - " + getResources().getString(R.string.yellowCardEvent) + getResources().getString(R.string.canceledEvent), true);

                    int yellowCard = pref.getInt(match.getLastClickedPlayer().getName(), 0);
                    yellowCard--;
                    editor.putInt(match.getLastClickedPlayer().getName(), yellowCard);
                    editor.apply(); // SAVE new YellowCard to player
                }
                else if (match.getUndoType() == 4) {

                    events = newEvent.addEvent(timers.getMainTimerMinutes() + ":" + timers.getMainTimerSeconds(), "", "", " - " + getResources().getString(R.string.redCardEvent) + getResources().getString(R.string.canceledEvent), true);

                    int red = pref.getInt(match.getLastClickedPlayer().getName() + "R", 0);
                    red--;
                    editor.putInt(match.getLastClickedPlayer().getName() + "R", red);
                    editor.commit(); // SAVE new RedCard to player
                }

                if (match.getUndoType() == 0)
                    Toast.makeText(this,  getResources().getString(R.string.removeEventError),
                            Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this,  getResources().getString(R.string.removeEventSuccess),
                            Toast.LENGTH_SHORT).show();

                 match.setUndoType(0);
                break;

        }
    } // CHECKS which button was pressed

    public void changeHalfString(TextView halfText) {

        if (match.getHalf() == 0)
            match.setHalfName(getResources().getString(R.string.displayHalf));
        else if (match.getHalf() == 1)
            match.setHalfName(getResources().getString(R.string.halfTimeTextShort));
        else if (match.getHalf() == 2 && match.getExtraTime() == 0)
            match.setHalfName(getResources().getString(R.string.secondHalfTextShort));
        else if ((match.getHalf() == 2 && match.getExtraTime() == 1) || (match.getHalf() == 2 && match.getExtraTime() == 2))
            match.setHalfName(getResources().getString(R.string.extraTimeTextShort));
        else if ((match.getHalf() == 2 && match.getExtraTime() == 3) || (match.getHalf() == 2 && match.getExtraTime() == 4))
            match.setHalfName(getResources().getString(R.string.extraTimeTextShort));
        else if ((match.getHalf() == 2 && match.getExtraTime() == 5))
            match.setHalfName(getResources().getString(R.string.penaltiesTimeTextShort));
        else
            match.setHalfName(getResources().getString(R.string.fullTimeTextShort));

        halfText.setText(match.getHalfName());
    } // CHECKS which halfText is and SETS HalfText  -------

    private void changeHalf() {

        if (match.getHalf() < 5)
            match.setHalf(match.getHalf() + 1);

        if (match.getHalf() == 1 && match.isStarted()) { // IF it's FIRST HALF

            buttonManager.endHalf.setText(getResources().getString(R.string.secondHalfText));

            match.setStarted(false);
            events = newEvent.addEvent(timers.getMainTimerMinutes() + ":" + timers.getMainTimerSeconds(), "", "", " - " + Event.HALFTIME, true);
            Toast.makeText(settingsLayout.getContext(), getResources().getString(R.string.firstHalfEnd),
                    Toast.LENGTH_SHORT).show();
        } else if (match.getHalf() == 2) { // IF it's HALF TIME

            buttonManager.endHalf.setText(getResources().getString(R.string.fullTimeText));

            buttonManager.extraTimeButton.setEnabled(true);
            match.setStarted(true);
            timers.setMainTimerMinutes(match.getHalfLength());
            timers.setMainTimerSeconds(0);
            timers.setExtraTimerMinutes(0);
            timers.setExtraTimerSeconds(0);

            events = newEvent.addEvent(timers.getMainTimerMinutes() + ":" + timers.getMainTimerSeconds(), "", "", " - " + Event.SECONDHALF, true);
            Toast.makeText(settingsLayout.getContext(), getResources().getString(R.string.secondHalfStart),
                    Toast.LENGTH_SHORT).show();

            vibrator = false;
        } else if (match.getHalf() == 3 && match.isStarted()) { // IF it's SECOND HALF

            match.setStarted(false);
            events = newEvent.addEvent(timers.getMainTimerMinutes() + ":" + timers.getMainTimerSeconds(), "", "", " - " + Event.FULLTIME, true);
            newEvent.addState(false, events);
            Toast.makeText(settingsLayout.getContext(), getResources().getString(R.string.fullTimeText),
                    Toast.LENGTH_SHORT).show();

            // >>>>>>>>>>>>>>>>>>>>>>> SEND STRING AND CLOSE APP <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        }
    }

    public void extraTime() {

        vibrator = true;

        if (match.getExtraTime() == 1) {

            buttonManager.endHalf.setEnabled(false);
            match.setStarted(false);

            buttonManager.extraTimeButton.setText(getResources().getString(R.string.startExtraTimeButton));
        }
        else if (match.getExtraTime() == 2) {

            timers.setMainTimerMinutes(match.getHalfLength() * 2);
            timers.setMainTimerSeconds(0);
            timers.setExtraTimerMinutes(0);
            timers.setExtraTimerSeconds(0);
            match.setStarted(true);
            buttonManager.extraTimeButton.setText(getResources().getString(R.string.endExtraTimeButton));

            events = newEvent.addEvent(timers.getMainTimerMinutes() + ":" + timers.getMainTimerSeconds(), "", "", " - " + Event.EXTRATIME, true);
            Toast.makeText(settingsLayout.getContext(), getResources().getString(R.string.extraTimeButton),
                    Toast.LENGTH_SHORT).show();
        }
        else if (match.getExtraTime() == 3) {

            buttonManager.extraTimeButton.setText(getResources().getString(R.string.secondExtraTimeButton));
            match.setStarted(false);

            events = newEvent.addEvent(timers.getMainTimerMinutes() + ":" + timers.getMainTimerSeconds(), "", "", " - " + Event.EXTRATIME, true);
            Toast.makeText(settingsLayout.getContext(), getResources().getString(R.string.endExtraTimeButton),
                    Toast.LENGTH_SHORT).show();
        }
        else  if (match.getExtraTime() == 4) {

            timers.setMainTimerMinutes(match.getHalfLength() * 2 + 15);
            timers.setMainTimerSeconds(0);
            timers.setExtraTimerMinutes(0);
            timers.setExtraTimerSeconds(0);
            match.setStarted(true);
            buttonManager.extraTimeButton.setText(getResources().getString(R.string.penaltiesButton));
            buttonManager.endHalf.setEnabled(true);

            events = newEvent.addEvent(timers.getMainTimerMinutes() + ":" + timers.getMainTimerSeconds(), "", "", " - " + Event.EXTRATIME, true);
            Toast.makeText(settingsLayout.getContext(), getResources().getString(R.string.secondExtraTimeButton),
                    Toast.LENGTH_SHORT).show();
        }
        else {

            timers.setMainTimerMinutes(0);
            timers.setMainTimerSeconds(0);
            timers.setExtraTimerMinutes(0);
            timers.setExtraTimerSeconds(0);

            events = newEvent.addEvent(timers.getMainTimerMinutes() + ":" + timers.getMainTimerSeconds(), "", "", " - " + Event.PENALTIES, true);
            Toast.makeText(settingsLayout.getContext(), getResources().getString(R.string.penaltiesButton),
                    Toast.LENGTH_SHORT).show();

            buttonManager.extraTimeButton.setEnabled(false);
        }
    }

    public void changeFragment(View v) {

        String tag = v.getResources().getResourceEntryName(v.getId());

        if (match.isStarted()) {

            if (tag.equals("homeLayout")) {

                match.setHomeTeamPressed(true);
                abbreviation.setText(match.getHome().getAbbreaviature());  // CHECKS if it's Home or Away Team menu and GETS TeamName
                teamLayout.setVisibility(View.VISIBLE);
                mainLayout.setVisibility(View.INVISIBLE);
                changeHalfString(halfTeamMenuText);   // CHECKS which halfText is and SETS HalfText
            } else if (tag.equals("awayLayout")) {

                match.setHomeTeamPressed(false);
                abbreviation.setText(match.getAway().getAbbreaviature()); // CHECKS if it's Home or Away Team menu and GETS TeamName
                teamLayout.setVisibility(View.VISIBLE);
                mainLayout.setVisibility(View.INVISIBLE);
                changeHalfString(halfTeamMenuText);   // CHECKS which halfText is and SETS HalfText
            }
        }
    } // STARTS teamLayout for Home or Away team                   -------------

    public void changeResult(boolean ownGoal){

        if (ownGoal)
            match.setHomeTeamPressed(!match.isHomeTeamPressed());

        if (match.isHomeTeamPressed()) {

            match.getHome().setGoals(match.getHome().getGoals() + 1);
            homeResultField.setText(Integer.toString(match.getHome().getGoals()));
        }
        else {

            match.getAway().setGoals(match.getHome().getGoals() + 1);
            awayResultField.setText(Integer.toString(match.getAway().getGoals()));;
        }
    } // CHANGES Home or Away Team result

    public void setPlayerNames() {

        for (int counter = 0; counter < match.getPlayersNumber(); counter++) {

            if (match.isHomeTeamPressed()) {
                buttonManager.playerButtons.get(counter).setText(match.getHome().getPlayers().get(counter).getNumberAndName());
            } else
                buttonManager.playerButtons.get(counter).setText(match.getAway().getPlayers().get(counter).getNumberAndName());

            buttonManager.playerButtons.get(counter).setEnabled(true);
            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

            String playerName =  ((String) buttonManager.playerButtons.get(counter).getText());
            int yellowCard =  pref.getInt(playerName, 0);
            int redCard = pref.getInt(playerName + "R", 0);

            if (yellowCard == 2 || redCard == 1)
                buttonManager.playerButtons.get(counter).setEnabled(false);// ENABLE all buttons, check for YELLOW or RED CARDS and Disable buttons

        }
    } // SETS names of all players

    public void setSubsNames() {

        for (int counter = 0; counter < match.getSubstitutesNumber(); counter++) {

            if (match.isHomeTeamPressed()) {

                buttonManager.subsButtons.get(counter).setText(match.getHome().getSubstitutes().get(counter).getNumberAndName()); // SETS Text for every Home Substitute
            } else
                buttonManager.subsButtons.get(counter).setText(match.getAway().getSubstitutes().get(counter).getNumberAndName()); // SETS Text for every Away Substitute
        }
    } // SETS names of all substitutes

}
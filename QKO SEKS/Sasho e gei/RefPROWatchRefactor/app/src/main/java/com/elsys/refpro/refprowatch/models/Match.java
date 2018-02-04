package com.elsys.refpro.refprowatch.models;

import android.content.Context;

import com.elsys.refpro.refprowatch.R;

public class Match {

    private String competition, venue, time, date, halfName, logText;
    private int halfLength, playersNumber, substitutesNumber;
    private int half, extraTime; // 0 FH, 1 HT, 2 SH, 3 FT
    private int undoType, eventType;

    private Team home, away;
    private boolean isStarted, isHomeTeamPressed, isOwnGoal;

    private Player substituteName, playerForSubstitution, lastClickedPlayer;

    public Match(String competition, String venue, String time, String date, int halfLength, int playersNumber, int substitutesNumber, Team home, Team away) {

        this.competition = competition;
        this.venue = venue;
        this.time = time;
        this.date = date;
        this.halfLength = halfLength;
        this.playersNumber = playersNumber;
        this.substitutesNumber = substitutesNumber;
        this.home = home;
        this.away = away;
        this.isStarted = false;
        this.isHomeTeamPressed = true;
        this.half = 0;
        this.halfName = "FH";
        this.logText = "";
        this.extraTime = 0;
        this.undoType = 0;
        this.eventType = 0;
        this.isOwnGoal = false;
    }

    public String getCompetition() {
        return competition;
    }

    public String getVenue() {
        return venue;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public int getHalfLength() {
        return halfLength;
    }

    public int getPlayersNumber() {
        return playersNumber;
    }

    public int getSubstitutesNumber() {
        return substitutesNumber;
    }

    public Team getHome() {
        return home;
    }

    public Team getAway() {
        return away;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean started) {
        isStarted = started;
    }

    public boolean isHomeTeamPressed() {
        return isHomeTeamPressed;
    }

    public void setHomeTeamPressed(boolean homeTeamPressed) {
        isHomeTeamPressed = homeTeamPressed;
    }

    public int getHalf() {
        return half;
    }

    public void setHalf(int half) {
        this.half = half;
    }

    public String getHalfName() {
        return halfName;
    }

    public void checkHalf(Context context) {

        if (getHalf() == 0)
            setHalfName(context.getResources().getString(R.string.displayHalf));
        else if (getHalf() == 1)
            setHalfName(context.getResources().getString(R.string.halfTimeTextShort));
        else if (getHalf() == 2 && getExtraTime() == 0)
            setHalfName(context.getResources().getString(R.string.secondHalfTextShort));
        else if ((getHalf() == 2 && getExtraTime() == 1) || (getHalf() == 2 && getExtraTime() == 2))
            setHalfName(context.getResources().getString(R.string.extraTimeTextShort));
        else if ((getHalf() == 2 && getExtraTime() == 3) || (getHalf() == 2 && getExtraTime() == 4))
            setHalfName(context.getResources().getString(R.string.extraTimeTextShort));
        else if ((getHalf() == 2 && getExtraTime() == 5))
            setHalfName(context.getResources().getString(R.string.penaltiesTimeTextShort));
        else
            setHalfName(context.getResources().getString(R.string.fullTimeTextShort));
    }

    public void setHalfName(String halfName) {
        this.halfName = halfName;
    }

    public String getLogText() {
        return logText;
    }

    public void setLogText(String logText) {
        this.logText = logText;
    }

    public int getExtraTime() {
        return extraTime;
    }

    public void setExtraTime(int extraTime) {
        this.extraTime = extraTime;
    }

    public Player getSubstituteName() {
        return substituteName;
    }

    public void setSubstituteName(Player substituteName) {
        this.substituteName = substituteName;
    }

    public Player getPlayerForSubstitution() {
        return playerForSubstitution;
    }

    public void setPlayerForSubstitution(Player playerForSubstitution) {
        this.playerForSubstitution = playerForSubstitution;
    }

    public Player getLastClickedPlayer() {
        return lastClickedPlayer;
    }

    public void setLastClickedPlayer(Player lastClickedPlayer) {
        this.lastClickedPlayer = lastClickedPlayer;
    }

    public int getUndoType() {
        return undoType;
    }

    public void setUndoType(int undoType) {
        this.undoType = undoType;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public boolean isOwnGoal() {
        return isOwnGoal;
    }

    public void setOwnGoal(boolean ownGoal) {
        isOwnGoal = ownGoal;
    }

    public void undoYellowCard() {

        int index;

        if (isHomeTeamPressed()) {
            index = getHome().getPlayers().indexOf(getLastClickedPlayer());
            getHome().getPlayers().get(index).substractYellowCard();
        }
        else {

            index = getAway().getPlayers().indexOf(getLastClickedPlayer());
            getAway().getPlayers().get(index).substractYellowCard();
        }
    }

    public void undoRedCard() {

        int index;

        if (isHomeTeamPressed()) {
            index = getHome().getPlayers().indexOf(getLastClickedPlayer());
            getHome().getPlayers().get(index).substractRedCard();
        }
        else {

            index = getAway().getPlayers().indexOf(getLastClickedPlayer());
            getAway().getPlayers().get(index).substractRedCard();
        }
    }

    public void addGoal() {

        if (isHomeTeamPressed())
            getHome().addGoal();
        else
            getAway().addGoal();
    }

    public void replace(Player old, Player subName) {

        if (isHomeTeamPressed()) { // IF it's HomeTeam Menu => GET index of given player and substitute and make SUBSTITUTION
            int indexOfOld = getHome().getPlayers().indexOf(old);
            int indexOfSub = getHome().getSubstitutes().indexOf(subName);

            getHome().getPlayers().set(indexOfOld, subName);
            getHome().getSubstitutes().set(indexOfSub, old);
        }
        else { // IF it's AwayTeam Menu => GET index of given player and substitute and make SUBSTITUTION

            int indexOfOld = getAway().getPlayers().indexOf(old);
            int indexOfSub = getAway().getSubstitutes().indexOf(subName);

            getAway().getPlayers().set(indexOfOld, subName);
            getAway().getSubstitutes().set(indexOfSub, old);
        }
    } // REPLACES Player with Substitute
}

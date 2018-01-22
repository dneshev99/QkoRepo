package com.elsys.refpro.refprowatch.models;

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
}

package com.elsys.refpro.refprowatch;

import java.util.ArrayList;
import java.util.List;

public class MatchInfo {

    private String competition, venue, home, away, homeabbr, awayabbr, time, date;
    private int players, subs, lenght;

    private ArrayList<String> home_players = new ArrayList<String>();
    private ArrayList<String> away_players = new ArrayList<String>();
    private ArrayList<String> home_subs = new ArrayList<String>();
    private ArrayList<String> away_subs = new ArrayList<String>();

    public MatchInfo(String competition, String venue, String home, String away, String homeabbr, String awayabbr, String time, String date, int players, int subs, int lenght, ArrayList<String> home_players, ArrayList<String> away_players, ArrayList<String> home_subs, ArrayList<String> away_subs) {
        this.competition = competition;
        this.venue = venue;
        this.home = home;
        this.away = away;
        this.homeabbr = homeabbr;
        this.awayabbr = awayabbr;
        this.time = time;
        this.date = date;
        this.players = players;
        this.subs = subs;
        this.lenght = lenght;
        this.home_players = home_players;
        this.away_players = away_players;
        this.home_subs = home_subs;
        this.away_subs = away_subs;
    }

    public MatchInfo() {

    }



    // region GETTERS AND SETTERS

    public String getCompetition() {
        return competition;
    }

    public void setCompetition(String competition) {
        this.competition = competition;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getAway() {
        return away;
    }

    public void setAway(String away) {
        this.away = away;
    }

    public String getHomeabbr() {
        return homeabbr;
    }

    public void setHomeabbr(String homeabbr) {
        this.homeabbr = homeabbr;
    }

    public String getAwayabbr() {
        return awayabbr;
    }

    public void setAwayabbr(String awayabbr) {
        this.awayabbr = awayabbr;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getPlayers() {
        return players;
    }

    public void setPlayers(int players) {
        this.players = players;
    }

    public int getSubs() {
        return subs;
    }

    public void setSubs(int subs) {
        this.subs = subs;
    }

    public int getLenght() {
        return lenght;
    }

    public void setLenght(int lenght) {
        this.lenght = lenght;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getHome_players() {
        return home_players;
    }

    public void setHome_players(ArrayList<String> home_players) {
        this.home_players = home_players;
    }

    public ArrayList<String> getAway_players() {
        return away_players;
    }

    public void setAway_players(ArrayList<String> away_players) {
        this.away_players = away_players;
    }

    public ArrayList<String> getHome_subs() {
        return home_subs;
    }

    public void setHome_subs(ArrayList<String> home_subs) {
        this.home_subs = home_subs;
    }

    public ArrayList<String> getAway_subs() {
        return away_subs;
    }

    public void setAway_subs(ArrayList<String> away_subs) {
        this.away_subs = away_subs;
    }


//endregion

}

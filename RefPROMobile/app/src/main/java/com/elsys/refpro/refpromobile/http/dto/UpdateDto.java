package com.elsys.refpro.refpromobile.http.dto;

import java.util.ArrayList;

/**
 * Created by Mitko on 16.12.2017 г..
 */

public class UpdateDto {

    private String MatchID;
    private ArrayList<String> playersHome;
    private ArrayList<String> playersAway;
    private ArrayList<String> subsHome;
    private ArrayList<String> subsAway;

    public UpdateDto(String MatchID, ArrayList<String> playersHome, ArrayList<String> playersAway, ArrayList<String> subsHome, ArrayList<String> subsAway) {
        this.MatchID = MatchID;
        this.playersHome = playersHome;
        this.playersAway = playersAway;
        this.subsHome = subsHome;
        this.subsAway = subsAway;
    }

    public UpdateDto() {

    }


    public String getMatchID() {
        return MatchID;
    }

    public ArrayList<String> getPlayersHome() {
        return playersHome;
    }

    public void setPlayersHome(ArrayList<String> playersHome) {
        this.playersHome = playersHome;
    }

    public ArrayList<String> getPlayersAway() {
        return playersAway;
    }

    public void setPlayersAway(ArrayList<String> playersAway) {
        this.playersAway = playersAway;
    }

    public ArrayList<String> getSubsHome() {
        return subsHome;
    }

    public void setSubsHome(ArrayList<String> subsHome) {
        this.subsHome = subsHome;
    }

    public ArrayList<String> getSubsAway() {
        return subsAway;
    }

    public void setSubsAway(ArrayList<String> subsAway) {
        this.subsAway = subsAway;
    }
}

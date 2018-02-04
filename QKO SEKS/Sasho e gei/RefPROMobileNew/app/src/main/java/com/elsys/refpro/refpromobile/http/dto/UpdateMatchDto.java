package com.elsys.refpro.refpromobile.http.dto;

import com.elsys.refpro.refpromobile.models.Player;

import java.util.ArrayList;

public class UpdateMatchDto {

    private String MatchId;
    private ArrayList<Player> playersHome;
    private ArrayList<Player> playersAway;
    private ArrayList<Player> subsHome;
    private ArrayList<Player> subsAway;

    public UpdateMatchDto(String MatchId, ArrayList<Player> playersHome, ArrayList<Player> playersAway, ArrayList<Player> subsHome, ArrayList<Player> subsAway) {
        this.MatchId = MatchId;
        this.playersHome = playersHome;
        this.playersAway = playersAway;
        this.subsHome = subsHome;
        this.subsAway = subsAway;
    }

    public UpdateMatchDto() {

    }

    public String getMatchId() {
        return MatchId;
    }

    public ArrayList<Player> getPlayersHome() {
        return playersHome;
    }

    public void setPlayersHome(ArrayList<Player> playersHome) {
        this.playersHome = playersHome;
    }

    public ArrayList<Player> getPlayersAway() {
        return playersAway;
    }

    public void setPlayersAway(ArrayList<Player> playersAway) {
        this.playersAway = playersAway;
    }

    public ArrayList<Player> getSubsHome() {
        return subsHome;
    }

    public void setSubsHome(ArrayList<Player> subsHome) {
        this.subsHome = subsHome;
    }

    public ArrayList<Player> getSubsAway() {
        return subsAway;
    }

    public void setSubsAway(ArrayList<Player> subsAway) {
        this.subsAway = subsAway;
    }
}

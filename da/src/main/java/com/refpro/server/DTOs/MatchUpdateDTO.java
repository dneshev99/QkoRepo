package com.refpro.server.DTOs;

import java.util.ArrayList;

public class MatchUpdateDTO {
    private String matchId;
    private ArrayList<PlayerDTO> playersHome;
    private ArrayList<PlayerDTO> playersAway;
    private ArrayList<PlayerDTO> subsHome;
    private ArrayList<PlayerDTO> subsAway;

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public ArrayList<PlayerDTO> getPlayersHome() {
        return playersHome;
    }

    public void setPlayersHome(ArrayList<PlayerDTO> playersHome) {
        this.playersHome = playersHome;
    }

    public ArrayList<PlayerDTO> getPlayersAway() {
        return playersAway;
    }

    public void setPlayersAway(ArrayList<PlayerDTO> playersAway) {
        this.playersAway = playersAway;
    }

    public ArrayList<PlayerDTO> getSubsHome() {
        return subsHome;
    }

    public void setSubsHome(ArrayList<PlayerDTO> subsHome) {
        this.subsHome = subsHome;
    }

    public ArrayList<PlayerDTO> getSubsAway() {
        return subsAway;
    }

    public void setSubsAway(ArrayList<PlayerDTO> subsAway) {
        this.subsAway = subsAway;
    }
}

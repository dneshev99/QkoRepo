package com.refpro.server.DTOs;

import com.refpro.server.models.Player;
import com.refpro.server.models.Team;

import java.util.List;

public class MatchInfoDTO {
    private String competition;
    private String venue;
    private String date;
    private String time;

    private TeamDto home;
    private TeamDto away;

    private String homeAbbr;
    private String awayAbbr;

    private List<PlayerDTO> homePlayers;
    private List<PlayerDTO> awayPlayers;
    private int length;

    private List<PlayerDTO> subsHome;
    private List<PlayerDTO> subsAway;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public TeamDto getHome() {
        return home;
    }

    public void setHome(TeamDto home) {
        this.home = home;
    }

    public TeamDto getAway() {
        return away;
    }

    public void setAway(TeamDto away) {
        this.away = away;
    }

    public String getHomeAbbr() {
        return homeAbbr;
    }

    public void setHomeAbbr(String homeAbbr) {
        this.homeAbbr = homeAbbr;
    }

    public String getAwayAbbr() {
        return awayAbbr;
    }

    public void setAwayAbbr(String awayAbbr) {
        this.awayAbbr = awayAbbr;
    }

    public List<PlayerDTO> getHomePlayers() {
        return homePlayers;
    }

    public void setHomePlayers(List<PlayerDTO> homePlayers) {
        this.homePlayers = homePlayers;
    }

    public List<PlayerDTO> getAwayPlayers() {
        return awayPlayers;
    }

    public void setAwayPlayers(List<PlayerDTO> awayPlayers) {
        this.awayPlayers = awayPlayers;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public List<PlayerDTO> getSubsHome() {
        return subsHome;
    }

    public void setSubsHome(List<PlayerDTO> subsHome) {
        this.subsHome = subsHome;
    }

    public List<PlayerDTO> getSubsAway() {
        return subsAway;
    }

    public void setSubsAway(List<PlayerDTO> subsAway) {
        this.subsAway = subsAway;
    }
}

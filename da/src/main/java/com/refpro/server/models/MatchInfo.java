package com.refpro.server.models;

import com.refpro.server.DTOs.MatchEventDTO;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.List;

public class MatchInfo {
    @Id
    @Indexed
    String ID;

    @Indexed
    private boolean isActive;

    private String competition;
    private String venue;
    private String date;
    private String time;
    private Team home;
    private Team away;
    private String homeAbbr;
    private String awayAbbr;

    @Indexed
    private List<Player> homePlayers= new ArrayList<>();
    @Indexed
    private List<Player> awayPlayers=new ArrayList<>();
    private int length;

    private List<Player> subsHome=new ArrayList<>();
    private List<Player> subsAway=new ArrayList<>();

    private List<MatchEvent> eventList=new ArrayList<>();

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

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

    public Team getHome() {
        return home;
    }

    public void setHome(Team home) {
        this.home = home;
    }

    public Team getAway() {
        return away;
    }

    public void setAway(Team away) {
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

    public List<Player> getHomePlayers() {
        return homePlayers;
    }

    public void setHomePlayers(List<Player> homePlayers) {
        this.homePlayers = homePlayers;
    }

    public List<Player> getAwayPlayers() {
        return awayPlayers;
    }

    public void setAwayPlayers(List<Player> awayPlayers) {
        this.awayPlayers = awayPlayers;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public List<Player> getSubsHome() {
        return subsHome;
    }

    public void setSubsHome(List<Player> subsHome) {
        this.subsHome = subsHome;
    }

    public List<Player> getSubsAway() {
        return subsAway;
    }

    public void setSubsAway(List<Player> subsAway) {
        this.subsAway = subsAway;
    }

    public List<MatchEvent> getEventList() {
        return eventList;
    }

    public void setEventList(List<MatchEvent> eventList) {
        this.eventList = eventList;
    }

    public void addEvent(MatchEvent matchEvent) {
        this.eventList.add(matchEvent);
    }
}

package com.elsys.refpro.refprowatch.http.dto;


public class MatchEventDTO {

    private String id;
    private String time;
    private String eventType;
    private String team;
    private String player;

    public MatchEventDTO(String id, String time, String eventType, String team, String player) {

        this.id = id;
        this.time = time;
        this.eventType = eventType;
        this.team = team;
        this.player = player;
    }

    public String getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String message) {
        this.player = message;
    }


    @Override
    public String toString() {

        return time + eventType + team + player;
    }
}

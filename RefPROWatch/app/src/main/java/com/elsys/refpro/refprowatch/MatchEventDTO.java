package com.elsys.refpro.refprowatch;

/**
 * Created by user on 17.12.2017 г..
 */

public class MatchEventDTO {

    private String id;
    private String time;
    private String eventType;
    private String team;
    private String message;

    public MatchEventDTO(String id, String time, String eventType, String team, String message) {
        this.id = id;
        this.time = time;
        this.eventType = eventType;
        this.team = team;
        this.message = message;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {

        return time + eventType + team + message;
    }
}

package com.refpro.server.models;

import com.refpro.server.enums.MatchEventTypes;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class MatchEvent {

    @Id
    @Indexed
    String id;
    private String time;
    private MatchEventTypes eventType;
    private String message;



    public MatchEvent(String time, MatchEventTypes eventType, String message) {
        this.time = time;
        this.eventType = eventType;
        this.message = message;
    }

    public MatchEvent() {
    }



    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public MatchEventTypes getEventType() {
        return eventType;
    }

    public void setEventType(MatchEventTypes eventType) {
        this.eventType = eventType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

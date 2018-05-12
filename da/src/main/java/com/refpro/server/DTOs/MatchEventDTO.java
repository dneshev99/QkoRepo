package com.refpro.server.DTOs;

import com.refpro.server.enums.MatchEventTypes;

public class MatchEventDTO {
    private String time;
    private MatchEventTypes eventType;
    private String message;

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

    @Override
    public String toString() {
        return "MatchEventDTO{" +
                "time='" + time + '\'' +
                ", eventType=" + eventType +
                ", message='" + message + '\'' +
                '}';
    }
}

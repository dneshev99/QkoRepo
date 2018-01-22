package com.elsys.refpro.refprowatch.http.dto;

import java.util.ArrayList;

public class MatchStateDTO {

    private String id;
    private boolean isActive;
    private ArrayList<MatchEventDTO> log;

    public MatchStateDTO(String id, boolean isActive, ArrayList<MatchEventDTO> log) {
        this.id = id;
        this.isActive = isActive;
        this.log = log;
    }

    public MatchStateDTO() {
    }

    public String getId() {
        return id;
    }


    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public ArrayList<MatchEventDTO> getLog() {
        return log;
    }

    public void setLog(ArrayList<MatchEventDTO> log) {
        this.log = log;
    }
}

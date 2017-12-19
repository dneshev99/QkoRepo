package com.elsys.refpro.refprowatch;

/**
 * Created by Mitko on 17.12.2017 г..
 */

public class MatchStateDTO {

    private String id;
    private boolean isActive;

    public MatchStateDTO(String id, boolean isActive) {
        this.id = id;
        this.isActive = isActive;
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
}

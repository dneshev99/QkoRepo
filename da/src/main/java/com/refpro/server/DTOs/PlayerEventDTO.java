package com.refpro.server.DTOs;

import com.refpro.server.enums.MatchEventTypes;
import com.refpro.server.models.MatchEvent;
import com.refpro.server.models.Player;

public class PlayerEventDTO extends MatchEventDTO {
    private String message;
    private PlayerDTO player;

    public PlayerEventDTO() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PlayerDTO getPlayer() {
        return player;
    }

    public void setPlayer(PlayerDTO player) {
        this.player = player;
    }
}

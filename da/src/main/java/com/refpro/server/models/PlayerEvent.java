package com.refpro.server.models;



public class PlayerEvent extends MatchEvent {
    private Player committerPlayer;
    private Player affectedPlayer;

    public PlayerEvent() {

    }

    public Player getCommiterPlayer() {
        return committerPlayer;
    }

    public void setCommiterPlayer(Player commiterPlayer) {
        this.committerPlayer = commiterPlayer;
    }

    public Player getAffectedPlayer() {
        return affectedPlayer;
    }

    public void setAffectedPlayer(Player affectedPlayer) {
        this.affectedPlayer = affectedPlayer;
    }
}

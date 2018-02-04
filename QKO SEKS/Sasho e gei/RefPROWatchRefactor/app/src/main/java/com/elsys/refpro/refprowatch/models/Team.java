package com.elsys.refpro.refprowatch.models;


import java.util.ArrayList;

public class Team {

    private boolean isHome;
    private String name;
    private String abbreaviature;

    private int goals;
    private int yellowCards;
    private int redCards;

    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Player> substitutes = new ArrayList<>();

    public Team(boolean isHome, String name, String abbreaviature , ArrayList<Player> players, ArrayList<Player> substitutes) {

        this.isHome = isHome;
        this.name = name;
        this.abbreaviature = abbreaviature;
        this.players = players;
        this.substitutes = substitutes;
    }

    public boolean isHome() {
        return isHome;
    }

    public String getName() {
        return name;
    }

    public String getAbbreaviature() {
        return abbreaviature;
    }

    public int getGoals() {
        return goals;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }

    public int getYellowCards() {
        return yellowCards;
    }

    public void setYellowCards(int yellowCards) {
        this.yellowCards = yellowCards;
    }

    public int getRedCards() {
        return redCards;
    }

    public void setRedCards(int redCards) {
        this.redCards = redCards;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public ArrayList<Player> getSubstitutes() {
        return substitutes;
    }

    public void setSubstitutes(ArrayList<Player> substitutes) {
        this.substitutes = substitutes;
    }

    public void addGoal() {

        goals++;
    }

}

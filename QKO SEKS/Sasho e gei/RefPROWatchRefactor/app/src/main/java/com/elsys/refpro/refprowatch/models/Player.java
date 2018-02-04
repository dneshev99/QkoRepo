package com.elsys.refpro.refprowatch.models;

public class Player {

    private int number;
    private String name;

    private int yellowCards;
    private int redCards;
    private int goals;
    private int minutesPlayed;

    public Player(int number, String name) {

        this.number = number;
        this.name = name;
        this.yellowCards = 0;
        this.redCards = 0;
        this.goals = 0;
        this.minutesPlayed = 0;
    }

    public String getNumberAndName() {

        return getNumber() + "." + getName();
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
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

    public int getGoals() {
        return goals;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }

    public int getMinutesPlayed() {
        return minutesPlayed;
    }

    public void setMinutesPlayed(int minutesPlayed) {
        this.minutesPlayed = minutesPlayed;
    }

    public void substractGoal () {

        goals--;
    }

    public void substractYellowCard () {

        yellowCards--;
    }

    public void substractRedCard () {

        redCards--;
    }

    public boolean isPlayerSuspended() {

        if (yellowCards == 2 || redCards == 1)
            return true;
        else
            return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;

        Player player = (Player) o;

        if (getNumber() != player.getNumber()) return false;
        return getName().equals(player.getName());

    }

    @Override
    public int hashCode() {
        int result = number;
        result = 31 * result + name.hashCode();
        result = 31 * result + yellowCards;
        result = 31 * result + redCards;
        result = 31 * result + goals;
        result = 31 * result + minutesPlayed;
        return result;
    }
}

package com.refpro.server.DTOs;

public class NewMatchInfoDTO {

    private boolean isActive;

    private String competition;
    private String venue;
    private String date;
    private String time;
    private String home;
    private String away;

    private int halfLength;

    public NewMatchInfoDTO(boolean isActive, String competition, String venue, String date, String time, String home, String away, String homeAbbreviature, String awayAbbreviature, int playersNumber, int substitutesNumber, int halfLength) {
        this.isActive = isActive;
        this.competition = competition;
        this.venue = venue;
        this.date = date;
        this.time = time;
        this.home = home;
        this.away = away;
        this.halfLength = halfLength;
    }

    public NewMatchInfoDTO() {
    }

    public boolean isActive() {
        return isActive;
    }

    public String getCompetition() {
        return competition;
    }

    public String getVenue() {
        return venue;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getHome() {
        return home;
    }

    public String getAway() {
        return away;
    }


    public int getHalfLength() {
        return halfLength;
    }

}

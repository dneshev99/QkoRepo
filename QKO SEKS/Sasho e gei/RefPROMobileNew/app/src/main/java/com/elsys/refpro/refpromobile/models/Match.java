package com.elsys.refpro.refpromobile.models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Match {

    private String competition;
    private String venue;
    private String time;
    private String date;
    private String home;
    private String away;
    private String homeAbbreviature;
    private String awayAbbreviature;

    private int halfLength;
    private int playersNumber;
    private int substitutesNumber;

    private boolean isMatchValidated;

    public Match(String competition, String venue, String time, String date, String home, String away, String homeAbbreviature, String awayAbbreviature, int halfLength, int playersNumber, int substitutesNumber) {
        this.competition = competition;
        this.venue = venue;
        this.time = time;
        this.date = date;
        this.home = home;
        this.away = away;
        this.homeAbbreviature = homeAbbreviature;
        this.awayAbbreviature = awayAbbreviature;
        this.halfLength = halfLength;
        this.playersNumber = playersNumber;
        this.substitutesNumber = substitutesNumber;
        this.isMatchValidated = false;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public void setAway(String away) {
        this.away = away;
    }

    public String getCompetition() {
        return competition;
    }

    public void setCompetition(String competition) {
        this.competition = competition;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHome() {
        return home;
    }

    public String getAway() {
        return away;
    }

    public String getHomeAbbreviature() {
        return homeAbbreviature;
    }

    public void setHomeAbbreviature(String homeAbbreviature) {
        this.homeAbbreviature = homeAbbreviature;
    }

    public String getAwayAbbreviature() {
        return awayAbbreviature;
    }

    public void setAwayAbbreviature(String awayAbbreviature) {
        this.awayAbbreviature = awayAbbreviature;
    }

    public int getHalfLength() {
        return halfLength;
    }

    public void setHalfLength(int halfLength) {
        this.halfLength = halfLength;
    }

    public int getPlayersNumber() {
        return playersNumber;
    }

    public void setPlayersNumber(int playersNumber) {
        this.playersNumber = playersNumber;
    }

    public int getSubstitutesNumber() {
        return substitutesNumber;
    }

    public void setSubstitutesNumber(int substitutesNumber) {
        this.substitutesNumber = substitutesNumber;
    }

    public void validateTime(String time) {

        if (time.isEmpty()) {

            isMatchValidated = false;
        }
        else {

            String check = time;

            char [] elements = check.toCharArray();

            if (Integer.parseInt(String.valueOf(elements[0])) > 2 || (Integer.parseInt(String.valueOf(elements[1])) > 3 && Integer.parseInt(String.valueOf(elements[0])) == 2)
                    || Integer.parseInt(String.valueOf(elements[3])) > 5 || elements.length != 5) {

                isMatchValidated = false;
            }
        }
    }

    public void validateDate(String date) {

        if (date.isEmpty() || date.length() != 10)   {

            isMatchValidated = false;
        }
        else {

            final String DATE_FORMAT = "yyyy-MM-dd";
            String check = date;

            try {

                DateFormat df = new SimpleDateFormat(DATE_FORMAT);
                df.setLenient(false);
                df.parse(check);

            } catch (ParseException e) {

                isMatchValidated = false;
            }
        }
    }

    public boolean isMatchValidated() {
        return isMatchValidated;
    }

    public void setMatchValidated(boolean matchValidated) {
        isMatchValidated = matchValidated;
    }
}

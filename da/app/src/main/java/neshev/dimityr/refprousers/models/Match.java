package neshev.dimityr.refprousers.models;

public class Match {
    private String id;
    private int awayTeamLogo;
    private int homeTeamLogo;
    private String awayTeamName;
    private String homeTeamName;
    private String venue;
    private String date;
    private String time;

    public Match(String id,int awayTeamLogo, int homeTeamLogo, String awayTeamName, String homeTeamName, String venue, String date, String time) {
        this.id = id;
        this.awayTeamLogo = awayTeamLogo;
        this.homeTeamLogo = homeTeamLogo;
        this.awayTeamName = awayTeamName;
        this.homeTeamName = homeTeamName;
        this.venue = venue;
        this.date = date;
        this.time = time;
    }

    public Match() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAwayTeamLogo() {
        return awayTeamLogo;
    }

    public void setAwayTeamLogo(int awayTeamLogo) {
        this.awayTeamLogo = awayTeamLogo;
    }

    public int getHomeTeamLogo() {
        return homeTeamLogo;
    }

    public void setHomeTeamLogo(int homeTeamLogo) {
        this.homeTeamLogo = homeTeamLogo;
    }

    public String getHomeTeamName() {
        return homeTeamName;
    }

    public void setHomeTeamName(String homeTeamName) {
        this.homeTeamName = homeTeamName;
    }

    public String getAwayTeamName() {
        return awayTeamName;
    }

    public void setAwayTeamName(String awayTeamName) {
        this.awayTeamName = awayTeamName;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

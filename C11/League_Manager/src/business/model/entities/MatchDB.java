package business.model.entities;

import com.google.cloud.firestore.annotation.PropertyName;

import java.util.ArrayList;

/**
 * This class represents a match into the database.
 */
public class MatchDB {

    // Attributes
    private String team1;
    private String team2;
    private int team1score = 0;
    private int team2score = 0;
    private String id;
    private String league;
    private String date;
    private String startHour;
    @PropertyName("played")
    private int isPlayed;
    private ArrayList<String> matchEvents;

    /**
     * Constructor of MatchDB.
     */
    public MatchDB() { }

    // Getters and setters
    public String getTeam1() {
        return team1;
    }

    public String getTeam2() {
        return team2;
    }

    public int getTeam1score() {
        return team1score;
    }

    public void setTeam1score(int team1score) {
        this.team1score = team1score;
    }

    public int getTeam2score() {
        return team2score;
    }

    public void setTeam2score(int team2score) {
        this.team2score = team2score;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPlayed() {
        return isPlayed;
    }


    public void setIsPlayed(int isPlayed) {
        this.isPlayed = isPlayed;
    }

    public String getStartHour() {
        return startHour;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public ArrayList<String> getMatchEvents() {
        return matchEvents;
    }

    public void setMatchEvents(ArrayList<String> matchEvents) {
        this.matchEvents = matchEvents;
    }
}

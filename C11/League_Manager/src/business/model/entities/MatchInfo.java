package business.model.entities;

import java.util.ArrayList;

/**
 * This class represents a match.
 */
public class MatchInfo {

    // Attributes
    private String id;
    private String team1Path;
    private String team2Path;
    private String team1Name;
    private String team2Name;
    private int scoreTeam1;
    private int scoreTeam2;
    private ArrayList<String> matchEvents;

    /**
     * Constructor of MatchInfo.
     * @param id id of the match
     * @param team1Path path of the image of the team 1
     * @param team2Path path of the image of the team 2
     * @param team1Name name of the team 1
     * @param team2Name name of the team 2
     * @param scoreTeam1 score of the team 1
     * @param scoreTeam2 score of the team 2
     * @param matchEvents events of the match
     */
    public MatchInfo(String id, String team1Path, String team2Path, String team1Name, String team2Name, int scoreTeam1, int scoreTeam2, ArrayList<String> matchEvents) {
        this.id = id;
        this.team1Path = team1Path;
        this.team2Path = team2Path;
        this.team1Name = team1Name;
        this.team2Name = team2Name;
        this.scoreTeam1 = scoreTeam1;
        this.scoreTeam2 = scoreTeam2;
        this.matchEvents = matchEvents;
    }

    // Getters and setters
    public String getTeam1Path() {
        return team1Path;
    }

    public String getTeam2Path() {
        return team2Path;
    }

    public String getTeam1Name() {
        return team1Name;
    }

    public String getTeam2Name() {
        return team2Name;
    }

    public int getScoreTeam1() {
        return scoreTeam1;
    }

    public int getScoreTeam2() {
        return scoreTeam2;
    }

    public String getId() {
        return id;
    }

    public void setScoreTeam1(int scoreTeam1) {
        this.scoreTeam1 = scoreTeam1;
    }

    public void setScoreTeam2(int scoreTeam2) {
        this.scoreTeam2 = scoreTeam2;
    }

    public ArrayList<String> getMatchEvents() {
        return matchEvents;
    }

    public void setEvents(ArrayList<String> matchEvents) {
        this.matchEvents = matchEvents;
    }
}

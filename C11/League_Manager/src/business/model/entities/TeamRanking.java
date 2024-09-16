package business.model.entities;

import java.util.ArrayList;

/**
 * This class represents a team ranking.
 */
public class TeamRanking {
    // Attributes
    private String id;
    private String teamID;
    private transient Team team;
    private String leagueID;
    private int points;
    private ArrayList<Integer> pointsPerMatch;
    private int wonMatches;
    private int tiedMatches;
    private int lostMatches;

    /**
     * Constructor of TeamRanking.
     * @param id id of the team ranking
     * @param teamID id of the team
     * @param team team
     * @param leagueID id of the league
     * @param wonMatches number of won matches
     * @param tiedMatches number of tied matches
     * @param lostMatches number of lost matches
     */
    public TeamRanking(String id, String teamID, Team team, String leagueID, int wonMatches, int tiedMatches, int lostMatches, int points, ArrayList<Integer> pointsPerMatch) {
        this.id = id;
        this.teamID = teamID;
        this.team = team;
        this.leagueID = leagueID;
        this.wonMatches = wonMatches;
        this.tiedMatches = tiedMatches;
        this.lostMatches = lostMatches;
        this.points = points;
        this.pointsPerMatch = pointsPerMatch;
    }

    public TeamRanking() { }

    /**
     * Gets the id of the team ranking.
     * @return id of the team ranking (String)
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id of the team ranking.
     * @param id id of the team ranking
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the id of the team.
     * @return id of the team (String)
     */
    public String getTeamID() {
        return teamID;
    }

    /**
     * Sets the id of the team.
     * @param teamID id of the team
     */
    public void setTeamID(String teamID) {
        this.teamID = teamID;
    }

    /**
     * Gets the team.
     * @return team (Team)
     */
    public Team getTeam() {
        return team;
    }

    /**
     * Sets the team.
     * @param team team
     */
    public void setTeam(Team team) {
        this.team = team;
    }

    /**
     * Gets the id of the league.
     * @return id of the league (String)
     */
    public String getLeagueID() {
        return leagueID;
    }

    /**
     * Sets the id of the league.
     * @param leagueID id of the league
     */
    public void setLeagueID(String leagueID) {
        this.leagueID = leagueID;
    }

    /**
     * Gets the number of won matches.
     * @return number of won matches (int)
     */
    public int getWonMatches() {
        return wonMatches;
    }

    /**
     * Sets the number of won matches.
     * @param wonMatches number of won matches
     */
    public void setWonMatches(int wonMatches) {
        this.wonMatches = wonMatches;
    }

    /**
     * Gets the number of tied matches.
     * @return number of tied matches (int)
     */
    public int getTiedMatches() {
        return tiedMatches;
    }

    /**
     * Sets the number of tied matches.
     * @param tiedMatches number of tied matches
     */
    public void setTiedMatches(int tiedMatches) {
        this.tiedMatches = tiedMatches;
    }

    /**
     * Gets the number of lost matches.
     * @return number of lost matches (int)
     */
    public int getLostMatches() {
        return lostMatches;
    }

    /**
     * Sets the number of lost matches.
     * @param lostMatches number of lost matches
     */
    public void setLostMatches(int lostMatches) {
        this.lostMatches = lostMatches;
    }

    /**
     * Gets the points.
     * @return points (int)
     */
    public int getPoints() {
        return points;
    }

    /**
     * Sets the points.
     * @param points points
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * Gets the points per match.
     * @return points per match (ArrayList<Integer>)
     */
    public ArrayList<Integer> getPointsPerMatch() {
        return pointsPerMatch;
    }

    /**
     * Sets the points per match.
     * @param pointsPerMatch points per match
     */
    public void setPointsPerMatch(ArrayList<Integer> pointsPerMatch) {
        this.pointsPerMatch = pointsPerMatch;
    }
}

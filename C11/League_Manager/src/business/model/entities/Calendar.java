package business.model.entities;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * This class represents a calendar of a league.
 */
public class Calendar {

    // Attributes
    private String id;
    private transient List<Team> teams;
    private transient List<List<Match>> matches;    // matches = numJourneys * (numTeams / 2)
    private ArrayList<String> journeysId;
    private String leagueID;
    private transient int numJourneys;

    /**
     * Constructor of Calendar.
     * @param id id of the calendar
     * @param leagueID id of the league
     * @param teams teams of the league
     * @param matches matches of the league
     */
    public Calendar(String id, String leagueID, List<Team> teams, List<List<Match>> matches, int numJourneys) {
        this.id = id;
        this.leagueID = leagueID;
        this.teams = teams;
        this.matches = matches;
        this.numJourneys = numJourneys;
    }

    /**
     * Empty constructor of Calendar.
     */
    public Calendar() {}

    /**
     * Gets the id of the calendar.
     * @return id of the calendar (String)
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id of the calendar.
     * @param id id of the calendar
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the teams of the league.
     * @return teams of the league (List<Team>)
     */
    public List<Team> getTeams() {
        return teams;
    }

    /**
     * Sets the teams of the league.
     * @param teams teams of the league
     */
    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    /**
     * Gets the matches of the league.
     * @return matches of the league (List<List<Match>>)
     */
    public List<List<Match>> getMatches() {
        return matches;
    }

    /**
     * Sets the matches of the league.
     * @param matches matches of the league
     */
    public void setMatches(List<List<Match>> matches) {
        this.matches = matches;
    }


    /**
     * Gets the id of the journeys.
     * @return id of the journeys (ArrayList<String>)
     */
    public ArrayList<String> getJourneysId() {
        return journeysId;
    }

    /**
     * Sets the id of the journeys.
     * @param journeysId id of the journeys
     */
    public void setJourneysId(ArrayList<String> journeysId) {
        this.journeysId = journeysId;
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
     * Gets the number of journeys.
     * @return number of journeys (int)
     */
    public int getNumJourneys() {
        return numJourneys;
    }
}

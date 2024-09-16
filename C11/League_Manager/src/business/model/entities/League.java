package business.model.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This class represents a league.
 */
public class League {

    // Attributes
    private String id;
    private String name;
    private String date;
    private String startHour;
    private transient List<Team> teams;
    private transient List<String> teamsName;
    private List<String> teamsID;
    private String calendarID;
    private transient Calendar calendar;
    private String journey = String.valueOf(0);
    private transient List<TeamRanking> rankings;
    private String imagePath;
    private int numJourneys;

    /**
     * Constructor of League.
     * @param name name of the league
     * @param date date of the league
     * @param teamsName teams of the league
     * @param calendarID calendar of the league
     */
    public League(String name, String date, String startHour, List<String> teamsName, List<Team> teams,
                  String calendarID, String imagePath) {
        id = UUID.randomUUID().toString();
        this.name = name;
        this.date = date;
        this.startHour = startHour;
        this.teamsName = teamsName;
        this.teams = teams;
        this.calendarID = calendarID;
        this.imagePath = imagePath;

        teamsID = new ArrayList<>();
        for (Team team : teams) {
            teamsID.add(team.getId());
        }
    }

    /**
     * Empty constructor of League.
     */
    public League() {}

    /**
     * Method that gets the calendar id of the league.
     * @return calendar id of the league (String)
     */
    public String getCalendarID() {
        return calendarID;
    }

    /**
     * Method that sets the calendar of the league.
     */
    public void setCalendarID(String calendarID) {
        this.calendarID = calendarID;
    }

    /**
     * Method that gets the id of the league.
     * @return id of the league (String)
     */
    public String getId() {
        return id;
    }

    /**
     * Method that sets the id of the league.
     * @param id id of the league
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Method that sets the league's name.
     * @param name name of the league
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Method that sets the league's date.
     * @param date date of the league
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Method that gets the teams of the league.
     * @return teams of the league (List<Team>)
     */
    public List<Team> getTeams() {
        return teams;
    }

    /**
     * Method that sets the teams of the league.
     * @param teams teams of the league
     */
    public void setTeams(ArrayList<Team> teams) {
        this.teams = teams;
    }

    /**
     * Method that gets the teams id of the league.
     * @return teams id of the league (List<String>)
     */
    public List<String> getTeamsID() {
        return teamsID;
    }

    /**
     * Method that sets the teams id of the league.
     * @param teamsID teams id of the league
     */
    public void setTeamsID(ArrayList<String> teamsID) {
        this.teamsID = teamsID;
    }

    /**
     * Method that gets the calendar of the league.
     * @return calendar of the league (Calendar)
     */
    public Calendar getCalendar() {
        return calendar;
    }

    /**
     * Method that sets the calendar of the league.
     * @param calendar calendar of the league
     */
    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    /**
     * Method that gets the name of the league.
     * @return name of the league (String)
     */
    public String getName() {
        return name;
    }

    /**
     * Method that gets the date of the league.
     * @return date of the league (String)
     */
    public String getDate() {
        return date;
    }

    /**
     * Method that gets the journey of the league.
     * @return journey of the league (int)
     */
    public String getJourney() {
        return journey;
    }

    /**
     * Method that sets the journey of the league.
     * @param journey journey of the league
     */
    public void setJourney(String journey) {
        this.journey = journey;
    }

    /**
     * Method that gets the rankings of the league.
     * @return rankings of the league (List<TeamRanking>)
     */
    public List<TeamRanking> getRankings() {
        return rankings;
    }

    /**
     * Method that sets the rankings of the league.
     * @param rankings rankings of the league
     */
    public void setRankings(List<TeamRanking> rankings) {
        this.rankings = rankings;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getStartHour() {
        return startHour;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public void setTeamsName(List<String> teamsName) {
        this.teamsName = teamsName;
    }

    public int getNumJourneys() {
        return numJourneys;
    }

    public void setNumJourneys(int numJourneys) {
        this.numJourneys = numJourneys;
    }
}

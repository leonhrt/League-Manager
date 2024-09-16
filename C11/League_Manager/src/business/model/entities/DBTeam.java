package business.model.entities;

import java.util.ArrayList;

/**
 * This class represents a team that will be uploaded to DB or download from DB.
 */
public class DBTeam {

    // Attributes
    private ArrayList<String> players = new ArrayList<>();
    private String teamName;
    private String id;
    private String imagePath;

    /**
     * Constructor of Team.
     * @param id id of the team
     * @param teamName name of the team
     */
    public DBTeam(String id, String teamName, String imagePath) {
        this.id = id;
        this.teamName = teamName;
        this.imagePath = imagePath;
    }

    /**
     * Empty constructor of Team for firebase.
     */
    public DBTeam() { }

    /**
     * Method to add a player to the team.
     * @param playerID id of the player
     */
    public void addPlayer(String playerID) {
        players.add(playerID);
    }

    /**
     * Method that gets the players of the team.
     * @return players of the team (ArrayList<String>)
     */
    public ArrayList<String> getPlayers() {
        return players;
    }

    /**
     * Method that sets the players to the team.
     * @param playersID players of the team
     */
    public void setPlayers(ArrayList<String> playersID) {
        this.players = playersID;
    }

    /**
     * Method that gets the name of the team.
     * @return name of the team (String)
     */
    public String getName() {
        return teamName;
    }

    /**
     * Method that sets the name of the team.
     * @param teamName name of the team
     */
    public void setName(String teamName) {
        this.teamName = teamName;
    }


    /**
     * Method that gets the id of the team.
     * @return id of the team (String)
     */
    public String getId() {
        return id;
    }

    /**
     * Method that sets the id of the team.
     * @param id id of the team (String)
     */
    public void setId(String id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}

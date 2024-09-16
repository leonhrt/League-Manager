package business.model.entities;

import business.model.entities.Player;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * This class represents a team.
 */
public class Team {

    // Attributes
    @SerializedName("team_name")
    @Expose
    private final String name;
    @SerializedName("players")
    @Expose
    private Player[] players;

    @SerializedName("image_path")
    private String imagePath;

    @Expose(serialize = false)
    private String id;


    /**
     * Constructor of Team.
     * @param name name of the team
     * @param players players of the team
     */
    public Team(String name, Player[] players, String imagePath) {
        this.name = name;
        this.players = players;
        this.imagePath = imagePath;
    }

    /**
     * Constructor of Team.
     * @param id id of the team
     * @param name name of the team
     */
    public Team(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Method that gets the name of the team.
     * @return name of the team (String)
     */
    public String getName() {
        return name;
    }

    /**
     * Method that gets the players of the team.
     * @return players of the team (Player[])
     */
    public Player[] getPlayers() {
        return players;
    }

    /**
     * Method that sets the players to the team.
     * @param players players of the team
     */
    public void setPlayers(Player[] players) {
        this.players = players;
    }


    /**
     * Method that gets the id of the team.
     * @return id of the team (String)
     */
    public String getId() {
        return id;
    }

    /**
     * Method that sets the id to the team.
     * @param id id of the team
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

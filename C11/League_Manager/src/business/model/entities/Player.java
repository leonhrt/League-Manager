package business.model.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * This class represents a player.
 */
public class Player {

    // Attributes
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("name")
    @Expose
    private String username;
    @SerializedName("email")
    @Expose
    private String email ;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("number")
    @Expose
    private int number;
    @SerializedName("dni")
    @Expose
    private String dni;
    @Expose(serialize = false)
    private ArrayList<String> teams = new ArrayList<>();
    @Expose(serialize = false)
    private boolean isAdmin = false;
    @Expose(serialize = false)
    private String id;

    /**
     * Constructor of Player.
     * @param password password of the player
     * @param username username of the player
     * @param email email of the player
     * @param phone phone of the player
     * @param number number of the player
     * @param dni dni of the player
     */
    // Aquesta funció ha de ser capaç de crear un jugador amb el nom que li passem per paràmetre.
    public Player(String password, String username, String email, String phone, int number, String dni) {
        this.password = password;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.number = number;
        this.dni = dni;
    }

    /**
     * Constructor of Player.
     */
    public Player() { } //Empty constructor for firebase

    /**
     * Constructor of Player.
     * @param username username of the player
     * @param password password of the player
     */
    public Player(String username, String password) {
        this.username = username;
        this.password = password;
        this.email = null;
        this.phone = null;
        this.number = -1;
        this.dni = null;
        this.isAdmin = true;
    }

    /**
     * Method that gets the name of the teams of the player.
     * @return teams of the player (ArrayList<String>)
     */
    public ArrayList<String> getTeams() {
        return teams;
    }

    /**
     * Method that sets the teams of the player.
     * @param teams teams of the player
     */
    public void setTeams(ArrayList<String> teams) {
        this.teams = teams;
    }

    /**
     * Method that adds a team to the player.
     * @param team team of the player
     */
    public void addTeam(String team) {
        teams.add(team);
    }

    /**
     * Method that gets the username of the player.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Method that gets the email of the player.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Method that gets the phone of the player.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Method that gets the number of the player.
     */
    public int getNumber() {
        return number;
    }

    /**
     * Method that gets the dni of the player.
     */
    public String getDni() {
        return dni;
    }

    /**
     * Method that sets the password of the player.
     * @param password password of the player
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Method that checks if the player is admin.
     * @return true if the player is admin, false otherwise (boolean)
     */
    public boolean isAdmin() {
        return isAdmin;
    }

    /**
     * Method that sets if the player is admin.
     * @param b true if the player is admin, false otherwise
     */
    public void setAdmin(boolean b) {
        this.isAdmin = b;
    }

    /**
     * Method that sets the id of the player.
     * @param id id of the player
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Method that gets the id of the player.
     * @return id of the player (String)
     */
    public String getId() {
        return id;
    }

    /**
     * Method that gets the password of the player.
     * @return password of the player (String)
     */
    public String getPassword() {
        return password;
    }

    /**
     * Method that sets the username of the player.
     * @param username username of the player
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Method that sets the email of the player.
     * @param email email of the player
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Method that sets the phone of the player.
     * @param phone phone of the player
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Method that sets the number of the player.
     * @param number number of the player
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * Method that sets the dni of the player.
     * @param dni dni of the player
     */
    public void setDni(String dni) {
        this.dni = dni;
    }
}

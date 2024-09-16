package business.model;

import business.model.entities.League;
import business.model.entities.Player;
import business.model.entities.Team;
import business.model.exceptions.*;
import persistence.LeagueDAO;
import persistence.PlayerDAO;
import persistence.TeamDAO;
import persistence.filesDAO.ConfigDAO;

import java.util.ArrayList;

/**
 * Class that manages the users of the application
 */
public class UserManager {

    // Constants
    private final String ADMIN_LOGIN = "admin";

    // Components
    private PlayerDAO playerDAO;
    private TeamDAO teamDAO;
    private LeagueDAO leagueDAO;
    Player playerLogged = new Player();
    private ConfigDAO config;
    private boolean isAdmin = false;


    /**
     * Constructor of the class UserManager
     * @param playerDAO player DAO
     * @param teamDAO team DAO
     */
    public UserManager(PlayerDAO playerDAO, TeamDAO teamDAO, ConfigDAO config, LeagueDAO leagueDAO) {
        this.playerDAO = playerDAO;
        this.teamDAO = teamDAO;
        this.config = config;
        this.leagueDAO = leagueDAO;
    }

    /**
     * Method that is able to check if this player already
     * exists, and if exists this method will generate a
     * random number and add it to the username.
     * @param username the username of the player
     * @return the username of the player or the username with a random number (String)
     */
    private String checkUsernameIfExists(String username) {
        String usernameCopy = username;
        if (playerDAO.getPlayerByOneField(username, "username") == null) {
            return username;
        }
        int number = 0;
        while (playerDAO.getPlayerByOneField(username, "username") != null) {
            number = generateRandomNumber();
            usernameCopy = username + number;
        }
        return usernameCopy;
    }

    /**
     * Method that generates a random number
     * @return random number (int)
     */
    private int generateRandomNumber() {
        // Aquesta funció ha de ser capaç de generar una contrasenya per a un nou jugador.
        return (int) (Math.random() * 1000);
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    /**
     * Method that is able to check if this email already exists
     * and starts the session of the player
     * @param username_dni the username or the DNI of the player
     * @param password the password of the player
     * @return the player that has started the session (Player)
     * @throws PlayerNotFoundException if the player is not found
     * the program will throw this exception with the message:
     * "Invalid username or password."
     * @throws InvalidAdminException if the admin is not valid
     * the program will throw this exception with the message:
     * "The admin password is not valid."
     */
    public Player playerLogin(String username_dni, String password) throws PlayerNotFoundException, InvalidAdminException {
        Player player;
        if (username_dni.equals(ADMIN_LOGIN)) {
            if (password.equals(config.getAdminPassword())) {
                isAdmin = true;
                player = new Player(username_dni, password);
                return player;
            } else {
                throw new InvalidAdminException();
            }
        } else if (playerDAO.getPlayerByLogin(username_dni, password) != null) {
            player = playerDAO.getPlayerByLogin(username_dni, password);
            playerLogged = player;
        } else {
            throw new PlayerNotFoundException();
        }

        return player;
    }

    /**
     * Method that checks if the password is valid
     * @param password the password of the player
     * @return true if the password is valid or false otherwise (boolean)
     */
    public boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false;
        }

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasNumber = false;

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isUpperCase(c)) {
                hasUpper = true;
            } else if (Character.isLowerCase(c)) {
                hasLower = true;
            } else if (Character.isDigit(c)) {
                hasNumber = true;
            }
        }

        return hasUpper && hasLower && hasNumber;
    }

    /**
     * Delete player from the database
     * @return true if the player has been deleted or false otherwise (boolean)
     */
    public boolean deleteUserInfoFromDb() {
        //Busquem el jugador a la bd
        Player player = playerDAO.getExistentPlayer(playerLogged.getId());

        //Si perteneix almenys a un equip
        if (!player.getTeams().isEmpty()) {
            //Per cada equip eliminem el jugador del equip al qual pertanyia
            for (String team : player.getTeams()) {
                if (teamDAO.deletePlayerFromTeam(team, player)) {
                    //continue
                } else {
                    return false; //Si ha un error retornem que no s'ha pogut eliminar correctament el jugador
                }
            }
        }

        if(playerDAO.deleteFromDatabase(playerLogged.getId())) {
            logoutPlayer(); //Eliminem el jugador de la memoria RAM
            return true; //Retornem que s'ha pogut eliminar correctament el jugador
        } else {
            return false; //Si ha un error retornem que no s'ha pogut eliminar correctament el jugador
        }
    }

    /**
     * Method that delete Player from RAMs memory.
     */
    public void logoutPlayer() {
        playerLogged = new Player(); // Player that is actually running the program, or has just logged in.
        isAdmin = false;
        //... en un futur poder cal eliminar més dades
    }

    /**
     * Check if the number of the player is correct
     * @param number the number of the player
     * @return true if the number is correct or false otherwise (boolean)
     */
    public boolean checkCorrectNumber(int number) throws PlayerWithInvalidNumberException {
        if (number <= 0) {
            throw new PlayerWithInvalidNumberException();
        } else {
            return true;
        }
    }

    /**
     * Check if the email is correct and if the email already exists
     * in the database
     * @param email the email of the player
     * @return true if the email is correct or false otherwise (boolean)
     */
    public boolean checkCorrectEmail(String email, String username) throws EmailNotCorrectException, ExistsUserSameEmailException {
        if (playerDAO.getPlayerByOneField(email, "email") == null) { //Si no existeix
            if (email.contains("@")) { //Comprovem que el format de l'emai sigui correcte
                return true;
            } else {
                throw new EmailNotCorrectException();
            }
        } else { //Si ja hi ha un player a la DB amb aquest emai, comprovem si coincideix amb player que estem intentant crear
            if (playerDAO.getPlayerByOneField(username, "username") != null) {
                if (playerDAO.getPlayerByOneField(email, "email").getId().equals(playerDAO.getPlayerByOneField(username, "username").getId())) {
                    return true;
                } else {
                    throw new ExistsUserSameEmailException();
                }
            } else {
                throw new ExistsUserSameEmailException();
            }
        }
    }

    /**
     * Method that check if the dni is valid
     * @param dni the dni of the player
     * @return true if the dni is valid or false otherwise (boolean)
     */
    public static boolean isValidDNI(String dni) {
        String regex = "^[0-9]{8}[a-zA-Z]$";
        return dni.matches(regex);
    }

    /**
     * Method that checks if the DNI is correct and if the DNI already exists
     * @param dni
     * @return true si el DNI és correcte o false altrament (boolean)
     */
    public boolean checkCorrectDNI(String dni, String username) throws DniNotCorrectException, ExistsUserSameDniException {
        if (playerDAO.getPlayerByOneField(dni, "dni") == null) { //Si no existeix un player amb aquest ID
            if (isValidDNI(dni)) { //Comprovem que el DNI sigui valid
                return true;
            } else {
                throw new DniNotCorrectException();
            }
        } else { //Si ja existeix un player amb aquest DNI, comprovem si és el DNI del player que tenim al JSON
            if (playerDAO.getPlayerByOneField(username, "username") != null) {
                if (playerDAO.getPlayerByOneField(dni, "dni").getId().equals(playerDAO.getPlayerByOneField(username, "username").getId())) {
                    return true;
                } else {
                    throw new ExistsUserSameDniException();
                }
            } else {
                throw new ExistsUserSameDniException();
            }
        }
    }

    /**
     * Getter de l'atribut ADMIN_LOGIN
     * @return ADMIN_LOGIN (String)
     */
    public String getADMIN_LOGIN() {
        return ADMIN_LOGIN;
    }

    /**
     * Delete all users from the database
     */
    public void deleteAllUsersFromDB() {
        playerDAO.deleteAllUsersFromDB();
    }

    /**
     * Method that update the password of the player
     * @param password the new password of the player
     * @param email_dni email or dni of the player
     * @throws InvalidPasswordException if the password is not valid
     * the program will throw this exception with the message:
     * "La nova contrasenya té un format no vàlid."
     * @throws PlayerNotFoundException if the player is not found
     * the program will throw this exception with the message:
     * "Invalid username or password."
     */
    public void changePassword(String password, String email_dni) throws InvalidPasswordException, InvalidChangePassword {
        if(isValidPassword(password)) { //Si la nova contrasenya es valida
            Player tempPlayer = playerDAO.getPlayerByOneField(email_dni, "email"); //Si busquem per l'email
            if(tempPlayer == null) { //Si es null (no l'ha trobat)
                tempPlayer = playerDAO.getPlayerByOneField(email_dni, "dni");  //El busquem per dni
                if(tempPlayer != null) { //Si no es null (l'ha trobat), l'actualitzem
                    tempPlayer.setPassword(password);
                    playerDAO.updatePlayerToDB(tempPlayer);
                    Player aux = playerDAO.getPlayerByOneField(email_dni, "dni");
                    System.out.println(aux.getPassword());
                } else { //Si no l'ha trobat ni per l'email ni pel dni llançem la exception
                    throw new InvalidChangePassword();
                }
            } else { //Si l'ha trobat l'actualitzem
                tempPlayer.setPassword(password);
                playerDAO.updatePlayerToDB(tempPlayer);
            }
        } else { //Si la nova contrasenya no es valida
            throw new InvalidPasswordException(); //Llançem la exception
        }
    }

    /**
     * Method get the player loggedr
     * @return playerLogged (Player)
     */
    public Player getPlayerLogged() {
        return playerLogged;
    }


    /**
     * Method that checks if the player is playing the league given
     * @param leagueID the id of the league
     * @return true if the player is playing the league or false otherwise (boolean)
     */
    public boolean checkIfPlayingLeague(String leagueID) {
        League league = leagueDAO.getLeagueFromID(leagueID);
        ArrayList<Team> teams = leagueDAO.getTeamsBasedOnLeague(league.getName());

        for (Team team: teams) { //Per els teams de la lliga
            for (Player player: team.getPlayers()) { //Per cada player del team
                if (player.getId().equals(playerLogged.getId())) { //Si el player es troba en algun dels teams
                    return true;
                }
            }
        }
        return false;
    }
}

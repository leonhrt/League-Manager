package persistence;

import business.model.entities.Player;
import business.model.exceptions.PlayerNotFoundException;

import java.util.ArrayList;

/**
 * Interface that defines the methods that a PlayerDAO must implement
 */
public interface PlayerDAO {

    /**
     * Method that export the players selected to the DB
     * @param player player to export
     * @return true if the player has been exported, false otherwise
     */
    boolean exportDataToDB(Player player);

    /**
     * Method that get the players from the DB that match with the email or dni
     * and password
     * @param email_dni email or dni of the player
     * @param password password of the player
     * @return player
     * @throws PlayerNotFoundException if the player is not found
     * a message will be shown to the user in the GUI the message will be
     * "Invalid username or password."
     */
    Player getPlayerByLogin(String email_dni, String password) throws PlayerNotFoundException;

    /**
     * Method that get the player selected from the DB
     * @param fieldValue value of the field
     * @param field field to search
     * @return player
     */
    Player getPlayerByOneField(String fieldValue, String field);

    /**
     * Method that delete the player selected from the DB
     * @param name name of the player
     * @return true if the player has been deleted, false otherwise (boolean)
     */
    boolean deleteFromDatabase(String name);

    /**
     * Method that update the player selected from the DB
     * @param player player to update
     * @return true if the player has been updated, false otherwise (boolean)
     */
    boolean updatePlayerToDB(Player player);

    /**
     * Method that delete all the players from the DB
     */
    void deleteAllUsersFromDB();

    /**
     * Method that gets a player from the DB
     * @return player found in the database (Player)
     */
    Player getExistentPlayer(String dni);

    /**
     * Method that deletes a team from the players
     * @param teamID id of the team
     * @return true if the team has been deleted, false otherwise (boolean)
     */
    boolean deleteTeamFromPlayers(String teamID);

}

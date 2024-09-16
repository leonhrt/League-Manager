package persistence;

import business.model.entities.DBTeam;
import business.model.entities.Player;
import business.model.entities.Team;

import java.util.ArrayList;

/**
 * Interface that defines the methods that a TeamDAO must implement
 */
public interface TeamDAO {
    //TODO: need to add some methods

    /**
     * Method that exports a team to the DB
     * @param team team to export
     * @return true if the team has been exported, false otherwise (boolean)
     */
    boolean exportDataToDB(DBTeam team);

    /**
     * Method that get a teamDB searching by the name from the DB
     * @param teamName name of the team
     * @return team (DBTeam)
     */
    DBTeam getTeamByName(String teamName);

    /**
     * Method that get a team searching by the id from the DB
     * @param teamId id of the team
     * @return team (Team)
     */
    Team getTeamById(String teamId);

    /**
     * Method that deletes a player from a specific team from the DB
     * @param teamId id of the team
     * @param playerToRemove player to remove
     * @return true if the player has been deleted, false otherwise (boolean)
     */
    boolean deletePlayerFromTeam(String teamId, Player playerToRemove);

    /**
     * Method that gets all the teams from the DB
     * @return list of teams (ArrayList<DBTeam>)
     */
    ArrayList<DBTeam> getAllTeams();

    /**
     * Method that updates a team in the DB
     * @param team team to update
     * @return true if the team has been updated, false otherwise (boolean)
     */
    boolean updateTeamInDataBase(Team team);

    /**
     * Method that deletes a team searching by the name from the DB
     * @param teamId id of the team
     * @return true if the team has been deleted, false otherwise (boolean)
     */
    boolean deleteFromDatabase(String teamId);

    /**
     * Method that gets the teams that plays the player selected
     * with a specific value and specific field
     * @param fieldValue value of the field
     * @param field field to search
     * @return list of players (ArrayList<Player>)
     */
    ArrayList<Team> getPlayerTeams(String fieldValue, String field);


    /**
     * Method that converts a DBTeam to a Team
     * @param dbTeam team to convert
     * @return team (Team)
     */
    Team convertToTeam(DBTeam dbTeam);

    /**
     * Method that gets all the players of a specific team
     * @param teamName name of the team
     * @return list of players (ArrayList<Player>)
     */
    ArrayList<Player> getPlayersOfTeam(String teamName);

    /**
     * Method that gets all the teams of a specific player
     * @param teamId id of the team
     * @return list of teams (ArrayList<DBTeam>)
     */
    ArrayList<DBTeam> getDBTeamsFromPlayer(String teamId);
}

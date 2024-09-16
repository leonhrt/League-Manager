package persistence;

import business.model.entities.League;
import business.model.entities.Team;

import java.util.ArrayList;

/**
 * Interface that defines the methods that a LeagueDAO must implement
 */
public interface LeagueDAO {
    //TODO: need to add some methods

    //TODO: AFEGIR AQUEST METODE A LA INTERFICIE
    //int getGameDuration(String leagueId); --> esta a config DAO

    /**
     * Method that exports a league to the DB
     * @param league league to export
     * @return true if the league has been exported, false otherwise (boolean)
     */
    boolean exportLeagueToDB(League league);

    League getLeagueFromID(String id);

    /**
     * Method that gets a league from the DB
     * @param name name of the league
     * @return league (League)
     */
    League getLeagueByName(String name);

    /**
     * Gets all the leagues from the DB
     * @return list of leagues (ArrayList<League>)
     */
    ArrayList<League> getAllLeaguesFromDB();

    /**
     * Deletes a league from the DB
     * @param leagueId id of the league
     * @return true if the league has been deleted, false otherwise (boolean)
     */
    boolean deleteFromDatabase(String leagueId);

    /**
     * Method that deletes a team from a league
     * @param teamId id of the team
     * @return true if the team has been deleted, false otherwise (boolean)
     */
    boolean deleteTeamFromLeagues(String teamId);

    /**
     * Method that updates a league to the DB
     * @param league league to update
     * @return true if the league has been updated, false otherwise (boolean)
     */
    boolean updateLeague(League league);

    /**
     * Method that gets all the teams from a league
     * @param name name of the league
     * @return list of teams (ArrayList<Team>)
     */
    ArrayList<Team> getTeamsBasedOnLeague(String name);

    /**
     * Method that gets all the leagues of a player
     * @param id id of the player
     * @return list of leagues (ArrayList<League>)
     */
    ArrayList<League> getLeaguesOfPlayer(String id);
}

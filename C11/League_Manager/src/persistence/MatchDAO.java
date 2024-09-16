package persistence;

import business.model.entities.Match;
import business.model.entities.MatchInfo;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Interface that defines the methods that a MatchDAO must implement
 */
public interface MatchDAO {
    /**
     * Method that get a match from the DB
     * @param id id of the match
     * @return match (Match)
     */
    Match getMatchFromID(String id);

    /**
     * Method that update a match to the DB
     * @param match match to update
     * @return true if the match has been updated, false otherwise (boolean)
     */
    boolean updateRanking(Match match);

    /**
     * Method that get all the matches from the DB
     * @return list of matches (ArrayList<Match>)
     */
    ArrayList<Match> getAllMatchesFromDB();

    /**
     * Method that deletes a match from the DB
     * @param matchId id of the match to delete
     * @return true if the match has been deleted, false otherwise (boolean)
     */
    boolean deleteMatchesFromLeague(String matchId);

    /**
     * Method that gets the matches from a league
     * @param leagueId id of the league
     * @return list of matches (ArrayList<String>)
     */
    ArrayList<String> getMatchesFromLeague(String leagueId);

    /**
     * Method that exports a match to the DB
     * @param match match to export
     * @return true if the match has been exported, false otherwise (boolean)
     */
    boolean exportMatchToDB(Match match);

    /**
     * Method that deletes all the matches from a team
     * @param teamId id of the team
     * @return true if the matches has been deleted, false otherwise (boolean)
     */
    boolean deleteMatchesFromTeam(String teamId);

    /**
     * Method that updates the status of a match
     * @param match match to update
     * @return true if the match has been updated, false otherwise (boolean)
     */
    boolean updateMatchStatus(Match match);

    /**
     * Method that deletes a match from the DB
     * @param matchID id of the match
     * @return true if the match has been deleted, false otherwise (boolean)
     */
    boolean deleteMatchFromDB(String matchID);

    /**
     * Method that gets the matches played
     * @return list of matches (ArrayList<Match>)
     */
    ArrayList<Match> getMatchesPlayed();

    /**
     * Method that gets the matches played by a team
     * @param id id of the team
     * @return list of matches (ArrayList<Match>)
     */
    ArrayList<Match> getMatchesPlayedByTeam(String id);

    /**
     * Method that removes the played matches
     */
    void deletePlayedMatches();

    /**
     * Method that checks if a match exists
     * @param match match to check
     * @return true if the match exists, false otherwise (boolean)
     */
    boolean matchDoesnExist(Match match);

}

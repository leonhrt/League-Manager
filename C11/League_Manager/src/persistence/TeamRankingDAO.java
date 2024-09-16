package persistence;

import business.model.entities.Match;
import business.model.entities.TeamRanking;

import java.util.ArrayList;

/**
 * Interface that defines the methods that a TeamRankingDAO must implement
 */
public interface TeamRankingDAO {

    /**
     * Method that exports some team rankings to the DB
     * @param rankings rankings to export
     * @return true if the ranking has been exported, false otherwise (boolean)
     */
    boolean exportDataToDB(ArrayList<TeamRanking> rankings);

    /**
     * Method that updates the team rankings in the DB
     * @param ranking ranking to update
     * @return true if the rankings has been updated, false otherwise (boolean)
     */
    boolean updateTeamRanking(TeamRanking ranking);

    /**
     * Method that gets a team ranking searching by the team id and the league id from the DB
     * @param teamId id of the team
     * @param leagueId id of the league
     * @return team ranking (TeamRanking)
     */
    TeamRanking getRankingByTeam(String teamId, String leagueId);

    /**
     * Method that gets all the team rankings from the DB
     * @return list of team rankings (ArrayList<TeamRanking>)
     */
    ArrayList<TeamRanking> getRankingsByLeague(String leageId);

    /**
     * Method that deletes all the team rankings from a specific league from the DB
     * @param leagueId id of the league
     * @return true if the rankings has been deleted, false otherwise (boolean)
     */
    boolean deleteRankingsByLeage(String leagueId);

    /**
     * Method that deletes a team ranking from the DB
     * @param id id of the team ranking
     * @return true if the rankings has been deleted, false otherwise (boolean)
     */
    boolean deleteRanking(String id);

    /**
     * Method that deletes all the team rankings from a specific team from the DB
     * @param teamId id of the team
     * @return true if the rankings has been deleted, false otherwise (boolean)
     */
    boolean deleteRankingsByTeam(String teamId);

    /**
     * Method that updates the team rankings from a match
     * @param match match to update
     * @return true if the rankings has been updated, false otherwise (boolean)
     */
    boolean updateRankingFromMatch(Match match);
}

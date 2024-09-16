package business.model;

import business.model.entities.Match;
import business.model.entities.MatchInfo;
import business.model.entities.Team;
import org.checkerframework.checker.units.qual.C;
import persistence.LeagueDAO;
import persistence.MatchDAO;
import persistence.TeamDAO;
import persistence.filesDAO.ConfigDAO;

import java.util.ArrayList;

/**
 * Class that manages the matches
 */
public class MatchManager {
    private int timeMatch;
    private ConfigDAO configDAO;
    private MatchDAO matchDAO;
    private TeamDAO teamDAO;
    private UserManager userManager;

    /**
     * Constructor of the MatchManager class
     * @param configDAO configDAO
     * @param matchDAO matchDAO
     * @param teamDAO teamDAO
     * @param userManager userManager
     */
    public MatchManager(ConfigDAO configDAO, MatchDAO matchDAO, TeamDAO teamDAO, UserManager userManager){
        this.configDAO = configDAO;
        timeMatch = configDAO.getGameDuration();
        this.matchDAO = matchDAO;
        this.teamDAO = teamDAO;
        this.userManager = userManager;

    }

    /**
     * Getter of the time of the match
     * @return time of the match
     */
    public int getTimeMatch(){
        return timeMatch;
    }

    /**
     * Method that returns the match info of all the matches that are playing
     * @return the match info of all the matches that are playing
     */
    public ArrayList<MatchInfo> getMatchesPlaying() {
        if(userManager.isAdmin()){
            return allMatchesPlaying();
        }
        return allMatchesPlayingFromPlayer(userManager.playerLogged.getId());
    }

    /**
     * Method that returns all the matches playing from a player
     * @param id id of the player
     * @return all the matches playing from a player
     */
    private ArrayList<MatchInfo> allMatchesPlayingFromPlayer(String id) {
        ArrayList<Team> teams = teamDAO.getPlayerTeams("id", id);
        ArrayList<MatchInfo> info = new ArrayList<>();

        for (Team team : teams){
            for (Match match : matchDAO.getMatchesPlayedByTeam(team.getId())){
                info.add(getMatchInfo(match));
            }
        }

        return info;
    }

    /**
     * Method that returns all the matches playing
     * @return all the matches playing
     */
    private ArrayList<MatchInfo> allMatchesPlaying() {
        ArrayList<Match> matches = matchDAO.getMatchesPlayed();
        ArrayList<MatchInfo> info = new ArrayList<>();

        for (Match match: matches) {
            info.add(getMatchInfo(match));
        }

        return info;
    }

    /**
     * Method that returns the info of a match
     * @param match match
     * @return info of the match
     */
    public MatchInfo getMatchInfo(Match match) {
        Team temp = teamDAO.getTeamById(match.getTeam1());
        Team temp2 = teamDAO.getTeamById(match.getTeam2());
        return new MatchInfo(match.getIdMatch(), temp.getImagePath(), temp2.getImagePath(), temp.getName(), temp2.getName(), match.getTeam1Score(), match.getTeam2Score(), match.getMatchEvents());
    }

    /**
     * Method that returns the info of a match
     * @param idMatch id of the match
     * @return info of the match
     */
    public MatchInfo getInfoFromMatch(String idMatch) {
        Match match = matchDAO.getMatchFromID(idMatch);
        return getMatchInfo(match);
    }
}

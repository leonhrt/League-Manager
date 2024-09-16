package persistence.dbDAO;

import business.model.entities.DBTeam;
import business.model.entities.League;
import business.model.entities.Match;
import business.model.entities.Team;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import persistence.*;

import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Class that implements the LeagueDAO interface
 */
public class LeagueDbDAO implements LeagueDAO {
    // Constants
    private static final String TABLE = "LEAGUE";

    // Attributes
    private static Firestore bd;
    private TeamDAO teamDAO;
    private CalendarDAO calendarDAO;
    private MatchDAO matchDAO;
    private TeamRankingDAO teamRankingDAO;

    /**
     * Constructor of the LeagueDbDAO class
     * @param bd database
     * @param teamDAO teamDAO
     * @param calendarDAO calendarDAO
     * @param matchDAO matchDAO
     * @param teamRankingDAO teamRankingDAO
     */
    public LeagueDbDAO(Firestore bd, TeamDAO teamDAO, CalendarDAO calendarDAO, MatchDAO matchDAO, TeamRankingDAO teamRankingDAO) {
        LeagueDbDAO.bd = bd;
        this.teamDAO = teamDAO;
        this.calendarDAO = calendarDAO;
        this.matchDAO = matchDAO;
        this.teamRankingDAO = teamRankingDAO;
    }

    /**
     * Method that import a league from the DB
     * @param id id of the league
     * @param data data of the league
     * @return true if the league has been imported, false otherwise
     */
    private boolean importDataToDB (String id, Map<String,Object> data){
        try{
            DocumentReference docRef = bd.collection(TABLE).document(id);
            ApiFuture<WriteResult> result = docRef.set(data);
            System.out.println("Update time : " +result.get().getUpdateTime());
            return true;
        } catch (ExecutionException | InterruptedException e) {
            return false;
        }
    }

    /**
     * Method that get a list of leagues from the DB
     * @return league
     */
    @Override
    public ArrayList<League> getAllLeaguesFromDB() {
        ApiFuture<QuerySnapshot> future = bd.collection(TABLE).get();
        List<QueryDocumentSnapshot> documents = null;

        try {
            documents = future.get().getDocuments();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        ArrayList<League> leagues = new ArrayList<>();

        for (QueryDocumentSnapshot document : documents) {
            League temp = document.toObject(League.class);
            leagues.add(getLeagueByName(temp.getName()));
        }

        return leagues;
    }

    /**
     * Method that export a league from the DB
     * @param league league to export
     * @return true if the league has been exported, false otherwise
     */
    @Override
    public boolean exportLeagueToDB(League league) {
        //L'escrivim de nou modificat
        Map<String, Object> data = new HashMap<>();
        data.put("name", league.getName());
        data.put("date", league.getDate());
        data.put("teamsID", league.getTeamsID());
        data.put("id", league.getId()); //Tots els documents han de tenir un id
        data.put("calendarID", league.getCalendarID());
        data.put("startHour", league.getStartHour());
        data.put("imagePath", league.getImagePath());
        data.put("journey", league.getJourney());
        data.put("numJourneys", league.getNumJourneys());

        return importDataToDB(league.getId(), data);
    }


    /**
     * Method that gets a league by the id from the DB
     * @param id id of the league
     * @return league if the league has been found, null otherwise
     */
    @Override
    public League getLeagueFromID(String id) {
        ApiFuture<DocumentSnapshot> future = bd.collection(TABLE).document(id).get();
        DocumentSnapshot document = null;

        try {
            document = future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        if(!document.exists()) {
            return null;
        }

        League temp = document.toObject(League.class);

        if(temp.getTeamsID() != null) {
            ArrayList<String> teamsId = new ArrayList<>();
            for (String teamID : temp.getTeamsID()) {
                teamsId.add(teamID);
            }
            temp.setTeamsID(teamsId);
        }

        temp.setCalendar(calendarDAO.getCalendarFromDB(temp.getCalendarID()));

        temp.setRankings(teamRankingDAO.getRankingsByLeague(temp.getId()));

        return temp;
    }

    /**
     * Method that gets a league by the name from the DB
     * @param name name of the league
     * @return league
     */
    @Override
    public League getLeagueByName(String name) {
        ApiFuture<QuerySnapshot> future = bd.collection(TABLE).whereEqualTo("name", name).get();
        List<QueryDocumentSnapshot> documents = null;

        try {
            documents = future.get().getDocuments();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        if(documents.isEmpty()) {
            return null;
        }

        League temp = documents.get(0).toObject(League.class);

        if(temp.getTeamsID() != null) {
            ArrayList<String> teamsId = new ArrayList<>();
            ArrayList<Team> teams = new ArrayList<>();
            for (String teamID : temp.getTeamsID()) {
                teamsId.add(teamID);
                if(teamDAO.getTeamById(teamID) != null) {
                    teams.add(teamDAO.getTeamById(teamID));
                }
            }
            temp.setTeamsID(teamsId);
            temp.setTeams(teams);
        }

        temp.setCalendar(calendarDAO.getCalendarFromDB(temp.getCalendarID()));

        temp.setRankings(teamRankingDAO.getRankingsByLeague(temp.getId()));

        return temp;
    }

    /**
     * Method that delete a league from the DB
     * @param leagueId id of the league to delete
     * @return true if the league has been deleted, false otherwise
     */
    @Override
    public boolean deleteFromDatabase(String leagueId) {
        try {
            // asynchronously delete a document
            ApiFuture<WriteResult> writeResult = bd.collection(TABLE).document(leagueId).delete();
            System.out.println("Update time : " + writeResult.get().getUpdateTime());
            return true;
        } catch (InterruptedException | ExecutionException e) {
            return false;
        }
    }

    /**
     * Method that delete a team from the leagues
     * @param teamId id of the team
     * @return true if the team has been deleted, false otherwise
     */
    @Override
    public boolean deleteTeamFromLeagues(String teamId) {
        ArrayList<League> leagues = getAllLeaguesFromDB();

        for (League league: leagues) {
            if (league.getTeamsID().contains(teamId)) {
                ArrayList<String> tmp = (ArrayList<String>) league.getTeamsID();
                tmp.remove(teamId);
                league.setTeamsID(tmp);
                if(!updateLeague(league)) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * Method that update a league from the DB
     * @param league league to update
     * @return true if the league has been updated, false otherwise
     */
    @Override
    public boolean updateLeague(League league) {
        if(deleteFromDatabase(league.getId())) {
            return exportLeagueToDB(league);
        }
        return false;
    }

    /**
     * Method that gets all the teams from the DB based on the league name
     * @param name name of the league to get the teams
     * @return list of teams of the league
     */
    @Override
    public ArrayList<Team> getTeamsBasedOnLeague(String name) {
        League league = getLeagueByName(name);
        ArrayList<Team> teams = new ArrayList<>();
        for (String teamId: league.getTeamsID()) {
            teams.add(teamDAO.getTeamById(teamId));
        }
        return teams;
    }

    /**
     * Method that gets all the leagues from the DB based on the player id
     * @return list of leagues of the player
     */
    @Override
    public ArrayList<League> getLeaguesOfPlayer(String id) {
        ArrayList<League> leagues = getAllLeaguesFromDB();
        ArrayList<League> leaguesOfPlayer = new ArrayList<>();

        ArrayList<Team> teams = teamDAO.getPlayerTeams("id", id);

        for (League league: leagues) {
            for (Team team: teams) {
                if (league.getTeamsID().contains(team.getId())) {
                    if(!leaguesOfPlayer.contains(league)) {
                        leaguesOfPlayer.add(league);
                    }
                }
            }
        }

        return leaguesOfPlayer;
    }



}


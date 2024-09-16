package persistence.dbDAO;

import business.model.entities.*;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import persistence.TeamDAO;
import persistence.TeamRankingDAO;

import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * This class is responsible for creating the TeamRanking objects and exporting them to the database
 */
public class TeamRankingDbDAO implements TeamRankingDAO {

    // Constants
    private final String TABLE = "RANKING";

    // Components
    private static Firestore bd;
    private TeamDAO teamDAO;


    /**
     * Constructor method
     * @param teamDAO team DAO
     */
    public TeamRankingDbDAO(TeamDAO teamDAO, Firestore bd) {
        this.teamDAO = teamDAO;
        TeamRankingDbDAO.bd = bd;
    }

    /**
     * Method that imports data to the DB
     * @param id id of the document to import
     * @param data data to import
     * @return true if the data has been imported, false otherwise (boolean)
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
     * Method that exports some team rankings to the DB
     * @param rankings rankings to export
     * @return true if the ranking has been exported, false otherwise (boolean)
     */
    @Override
    public boolean exportDataToDB(ArrayList<TeamRanking> rankings) {
        for (TeamRanking ranking: rankings) {
            Map<String, Object> data = new HashMap<>();
            data.put("teamID", ranking.getTeamID());
            data.put("leagueID", ranking.getLeagueID());
            data.put("wonMatches", ranking.getWonMatches());
            data.put("tiedMatches", ranking.getTiedMatches()); //Tots els documents han de tenir un id
            data.put("lostMatches", ranking.getLostMatches());
            data.put("id", ranking.getId());
            data.put("points", ranking.getPoints());
            data.put("pointsPerMatch", ranking.getPointsPerMatch());

            if(!importDataToDB(ranking.getId(), data)) { return false; }
        }

        return true;
    }

    /**
     * Method that updates the team rankings in the DB
     * @return true if the rankings has been updated, false otherwise (boolean)
     */
    @Override
    public boolean updateTeamRanking(TeamRanking ranking) {
        if(deleteRanking(ranking.getId())) {
            if(exportDataToDB(new ArrayList<TeamRanking>(){{add(ranking);}})) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Method that gets all the team rankings from the DB
     * @return list of team rankings (ArrayList<TeamRanking>)
     */
    private ArrayList<TeamRanking> getAllTeamRankings() {
        // asynchronously retrieve multiple documents
        ApiFuture<QuerySnapshot> future = bd.collection(TABLE).get();
        // future.get() blocks on response
        List<QueryDocumentSnapshot> documents = null;
        try {
            documents = future.get().getDocuments();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        ArrayList<TeamRanking> rankings = new ArrayList<>();

        for (QueryDocumentSnapshot document : documents) {
            rankings.add(document.toObject(TeamRanking.class));
        }

        return rankings;
    }

    /**
     * Method that gets a team ranking from the DB
     * @param teamId id of the team
     * @param leagueId id of the league
     * @return team ranking (TeamRanking)
     */
    @Override
    public TeamRanking getRankingByTeam(String teamId, String leagueId) {
        CollectionReference collection = bd.collection(TABLE);

        ApiFuture<QuerySnapshot> future = collection
                .whereEqualTo("teamID", teamId).whereEqualTo("leagueID", leagueId)
                .get();

        List<QueryDocumentSnapshot> documents = null;
        try {
            documents = future.get().getDocuments();
            if (!documents.isEmpty()) {
                return documents.get(0).toObject(TeamRanking.class);
            } else {
                return null;
            }
        } catch (InterruptedException | ExecutionException ignored) {
            return null;
        }
    }

    /**
     * Method that gets all the team rankings from a League
     * that are stored in the FirebaseDB
     * @return list of team rankings (ArrayList<TeamRanking>)
     */
    @Override
    public ArrayList<TeamRanking> getRankingsByLeague(String leagueId) {
        ArrayList<TeamRanking> rankings = getAllTeamRankings();
        ArrayList<TeamRanking> rankingsToReturn = new ArrayList<>();

        for (TeamRanking ranking: rankings) {
            if(Objects.equals(ranking.getLeagueID(), leagueId)) {
                ranking.setTeam(teamDAO.getTeamById(ranking.getTeamID()));
                rankingsToReturn.add(ranking);
            }
        }
        return rankingsToReturn;
    }

    /**
     * Method that deletes a team ranking from the DB
     * @param leagueId id of the league
     * @return true if the ranking has been deleted, false otherwise (boolean)
     */
    @Override
    public boolean deleteRankingsByLeage(String leagueId) {
        ArrayList<TeamRanking> rankings = getAllTeamRankings();

        for (TeamRanking ranking: rankings) {
            if(Objects.equals(ranking.getLeagueID(), leagueId)) {
                if(!deleteRanking(ranking.getId())) { return false; }
            }
        }
        return true;
    }

    /**
     * Method that deletes a team ranking from the DB
     * @param id id of the ranking
     * @return true if the ranking has been deleted, false otherwise (boolean)
     */
    @Override
    public boolean deleteRanking(String id) {
        try{
            ApiFuture<WriteResult> writeResult = bd.collection(TABLE).document(id).delete();
            System.out.println("Update time : " +writeResult.get().getUpdateTime());
            return true;
        } catch (ExecutionException | InterruptedException e) {
            return false;
        }
    }

    /**
     * Method that deletes a team ranking from the DB
     * @param teamId id of the team
     * @return true if the ranking has been deleted, false otherwise (boolean)
     */
    @Override
    public boolean deleteRankingsByTeam(String teamId) {
        ArrayList<TeamRanking> rankings = getAllTeamRankings();

        for (TeamRanking ranking: rankings) {
            if(Objects.equals(ranking.getTeamID(), teamId)) {
                if(!deleteRanking(ranking.getId())) { return false; }
            }
        }
        return true;
    }

    /**
     * Method that updates the team rankings from a match
     * @param match match to update the rankings
     * @return true if the rankings has been updated, false otherwise (boolean)
     */
    @Override
    public boolean updateRankingFromMatch(Match match) {
        if(match.getTeam1Score() > match.getTeam2Score()) { //Si ha guanyat l'equip 1
            return updateTeamRankingPoints(getRankingByTeam(match.getTeam1(), match.getLeague()), 3) &&
                    updateTeamRankingWinnedMatches(getRankingByTeam(match.getTeam1(), match.getLeague()), 1) &&
                    updateTeamRankingPoints(getRankingByTeam(match.getTeam2(), match.getLeague()), 0) &&
                    updateTeamRankingLostMatches(getRankingByTeam(match.getTeam2(), match.getLeague()), 1);
        } else if (match.getTeam1Score() < match.getTeam2Score()) { //Si ha guanyat l'equip 2
            return updateTeamRankingPoints(getRankingByTeam(match.getTeam1(), match.getLeague()), 0) &&
                    updateTeamRankingLostMatches(getRankingByTeam(match.getTeam1(), match.getLeague()), 1) &&
                    updateTeamRankingPoints(getRankingByTeam(match.getTeam2(), match.getLeague()), 3) &&
                    updateTeamRankingWinnedMatches(getRankingByTeam(match.getTeam2(), match.getLeague()), 1);
        } else if (match.getTeam1Score() == match.getTeam2Score()) { //Si han empatat
            return updateTeamRankingPoints(getRankingByTeam(match.getTeam1(), match.getLeague()), 1) &&
                    updateTeamRankingTiedMatches(getRankingByTeam(match.getTeam1(), match.getLeague()), 1) &&
                    updateTeamRankingPoints(getRankingByTeam(match.getTeam2(), match.getLeague()), 1) &&
                    updateTeamRankingTiedMatches(getRankingByTeam(match.getTeam2(), match.getLeague()), 1);
        }
        return false;
    }

    /**
     * Method that updates the team rankings from a match
     * @param rankingByTeam ranking to update
     * @param i number of tied matches to add to the ranking
     * @return
     */
    private boolean updateTeamRankingTiedMatches(TeamRanking rankingByTeam, int i) {
        rankingByTeam.setTiedMatches(rankingByTeam.getTiedMatches() + i);
        return updateTeamRanking(rankingByTeam);
    }

    /**
     * Method that updates the team rankings from a match
     * @param rankingByTeam ranking to update
     * @param i number of lost matches to add to the ranking
     * @return true if the rankings has been updated, false otherwise (boolean)
     */
    private boolean updateTeamRankingLostMatches(TeamRanking rankingByTeam, int i) {
        rankingByTeam.setLostMatches(rankingByTeam.getLostMatches() + i);
        return updateTeamRanking(rankingByTeam);
    }

    /**
     * Method that updates the team rankings from a match
     * @param rankingByTeam ranking to update
     * @param i number of winned matches to add to the ranking
     * @return true if the rankings has been updated, false otherwise (boolean)
     */
    private boolean updateTeamRankingWinnedMatches(TeamRanking rankingByTeam, int i) {
        rankingByTeam.setWonMatches(rankingByTeam.getWonMatches() + i);
        return updateTeamRanking(rankingByTeam);
    }

    /**
     * Method that updates the team rankings from a match
     * @param rankingByTeam ranking to update
     * @param points points to add to the ranking
     * @return true if the rankings has been updated, false otherwise (boolean)
     */
    private boolean updateTeamRankingPoints(TeamRanking rankingByTeam, int points) {
        //Posem els punts que pertoca
        rankingByTeam.setPoints(rankingByTeam.getPoints() + points);
        ArrayList<Integer> temp = rankingByTeam.getPointsPerMatch();
        temp.add(points);
        rankingByTeam.setPointsPerMatch(temp);

        return updateTeamRanking(rankingByTeam); //Acualitzem a la base de dades
    }

}

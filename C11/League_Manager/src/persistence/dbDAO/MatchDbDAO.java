package persistence.dbDAO;

import business.model.entities.Calendar;
import business.model.entities.JourneyForDB;
import business.model.entities.Match;
import business.model.entities.MatchDB;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import persistence.MatchDAO;
import persistence.TeamRankingDAO;
import persistence.filesDAO.ConfigDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Class that implements the MatchDAO interface
 */
public class MatchDbDAO implements MatchDAO {

    // Attributes
    private static Firestore bd;
    private  final String TABLE = "MATCH";
    private ConfigDAO configDAO;
    private TeamRankingDAO teamRankingDAO;

    public MatchDbDAO(Firestore bd, ConfigDAO configDAO, TeamRankingDAO teamRankingDAO) {
        MatchDbDAO.bd = bd;
        this.configDAO = configDAO;
        this.teamRankingDAO = teamRankingDAO;
    }


    /**
     * Method that imports match to the DB
     * @param id id of the match
     * @param data data of the match
     * @return true if the match has been imported, false otherwise
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
     * Method that gets all the matches from the DB
     * @return all the matches from the DB (ArrayList<Match>)
     */
    @Override
    public Match getMatchFromID(String id) {
        try{
            DocumentReference docRef = bd.collection(TABLE).document(id);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                MatchDB match = document.toObject(MatchDB.class);
                return new Match(match.getTeam1(), match.getTeam2(), match.getTeam1score(), match.getTeam2score(), match.getId(), match.getLeague(), match.getDate(), match.getPlayed(), configDAO.getGameDuration(), match.getStartHour(), match.getMatchEvents());
            } else {
                return null;
            }
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }

    /**
     * Method that exports a match to the DB
     * @return match to export (Match)
     */
    @Override
    public boolean exportMatchToDB(Match match) {
        Map<String, Object> data = new HashMap<>();
        data.put("league",match.getLeague());
        data.put("team1", match.getTeam1());
        data.put("team2", match.getTeam2());
        data.put("team1score", match.getTeam1Score());
        data.put("team2score", match.getTeam2Score());
        data.put("id", match.getIdMatch()); //Tots els documents han de tenir un id
        data.put("date", match.getDate());
        data.put("startHour", match.getStartHour());
        data.put("played", match.getPlayed());
        data.put("matchEvents", match.getMatchEvents());

        //... altres atributs a ficar

        if(importDataToDB(match.getIdMatch(), data)){
            return true;
        } else{
            return false;
        }
    }

    /**
     * Method that removes a match from the DB based on the league id
     * @param leagueId id of the league
     * @return true if the matches have been deleted, false otherwise (boolean)
     */
    @Override
    public boolean deleteMatchesFromLeague(String leagueId) {
        ArrayList<Match> matches = getAllMatchesFromDB();

        for (Match match: matches) {
            System.out.println("lliga del match"+match.getLeague());
            System.out.println("id de la lliga"+leagueId);
            if (match.getLeague().equals(leagueId)) {
                if(!deleteMatchFromDB(match.getIdMatch())) { return false; }
            }
        }
        return true;
    }

    /**
     * Deletes all the matches from a team
     * @param teamId id of the team
     * @return true if the matches have been deleted, false otherwise
     */
    @Override
    public boolean deleteMatchesFromTeam(String teamId) {
       ArrayList<Match> matches = getAllMatchesFromDB();

        for (Match match: matches) {
             if (match.getTeam1().equals(teamId) || match.getTeam2().equals(teamId)) {
                 if(deleteMatchFromJourney(match.getIdMatch())) {
                     if (!deleteMatchFromDB(match.getIdMatch())) {
                         return false;
                     }
                 } else {
                     return false;
                 }
             }
        }
        return true;
    }

    /**
     * Method that deletes a match from a journey given a match id
     * @param idMatch id of the match
     * @return true if the match has been deleted, false otherwise (boolean)
     */
    private boolean deleteMatchFromJourney(String idMatch) {
        ArrayList<JourneyForDB> journeys = getAllJourneysFromDB();

        for (JourneyForDB journey : journeys) {
            if (journey.getMatchesID().contains(idMatch)) {
                ArrayList<String> matches = journey.getMatchesID();
                matches.remove(idMatch);
                journey.setMatchesID(matches);
                if (!updateJourney(journey)) {
                        return false;
                }
            }
        }

        return true;
    }

    /**
     * Method that updates a journey from the DB
     * @param journey journey to update (Journey)
     * @return true if the journey has been updated, false otherwise (boolean)
     */
    private boolean updateJourney(JourneyForDB journey) {
        if(deleteJourneyFromDB(journey.getId())) {
            return exportJourneyToDB(journey);
        }
        return false;
    }

    /**
     * Method that exports a journey to the DB
     * @param journey journey to export (Journey)
     * @return true if the journey has been exported, false otherwise (boolean)
     */
    private boolean exportJourneyToDB(JourneyForDB journey) {
        Map<String, Object> data = new HashMap<>();
        data.put("id", journey.getId());
        data.put("matchesID", journey.getMatchesID());

        try{
            DocumentReference docRef = bd.collection("JOURNEYS").document(journey.getId());
            ApiFuture<WriteResult> result = docRef.set(data);
            System.out.println("Update time : " +result.get().getUpdateTime());
            return true;
        } catch (ExecutionException | InterruptedException e) {
            return false;
        }
    }

    /**
     * Method that removes a journey from the DB based on the journey id
     * @param id id of the journey
     * @return true if the journey has been deleted, false otherwise (boolean)
     */
    private boolean deleteJourneyFromDB(String id) {
        try{
            ApiFuture<WriteResult> writeResult = bd.collection("JOURNEYS").document(id).delete();
            System.out.println("Update time : " +writeResult.get().getUpdateTime());
            return true;
        } catch (ExecutionException | InterruptedException e) {
            return false;
        }
    }


    /**
     * Method that gets all the journeys from the DB
     * @return all the journeys from the DB (ArrayList<JourneyForDB>)
     */
    private ArrayList<JourneyForDB> getAllJourneysFromDB() {
        ArrayList<JourneyForDB> journeys = new ArrayList<>();
        try{
            ApiFuture<QuerySnapshot> future = bd.collection("JOURNEYS").get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                JourneyForDB journey = document.toObject(JourneyForDB.class);
                journeys.add(journey);
            }
            return journeys;
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }

    /**
     * Method that deletes a match from the DB
     * @param mathID id of the match
     * @return true if the match has been deleted, false otherwise
     */
    @Override
    public boolean deleteMatchFromDB(String mathID) {
        try{
            ApiFuture<WriteResult> writeResult = bd.collection(TABLE).document(mathID).delete();
            System.out.println("Update time : " +writeResult.get().getUpdateTime());
            return true;
        } catch (ExecutionException | InterruptedException e) {
            return false;
        }
    }

    /**
     * Method that updates a ranking in the DB and deletes the match
     * @param match match to update
     * @return true if the match has been updated, false otherwise
     */
    @Override
    public boolean updateRanking(Match match) {
        //Actualitzem el resultat del match al team ranking
        if(!teamRankingDAO.updateRankingFromMatch(match)) { return false; }

        if(deleteMatchFromDB(match.getIdMatch())) { //eliminem
           return true;
        }
        return false;
    }

    /**
     * Method that updates a match in the DB
     * @param match match to update
     * @return true if the match has been updated, false otherwise
     */
    @Override
    public boolean updateMatchStatus(Match match) {
        if(deleteMatchFromDB(match.getIdMatch())) {
            return exportMatchToDB(match);
        }
        return false;
    }

    /**
     * Method that gets all the matches from the DB
     * @return list of matches (ArrayList<Match>)
     */
    @Override
    public ArrayList<String> getMatchesFromLeague(String leagueId) {
        ArrayList<Match> allMatches = getAllMatchesFromDB();
        ArrayList<String> matchesToReturn = new ArrayList<>();

        for (Match match: allMatches) {
            if(match.getLeague().equals(leagueId)){
                matchesToReturn.add(match.getIdMatch());
            }
        }

        return matchesToReturn;
    }

    /**
     * Method that gets all the matches from the DB
     * @return list of matches (ArrayList<Match>)
     */
    @Override
    public ArrayList<Match> getAllMatchesFromDB() {
        ApiFuture<QuerySnapshot> future = bd.collection(TABLE).get();
        List<QueryDocumentSnapshot> documents = null;
        try {
            documents = future.get().getDocuments();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        ArrayList<MatchDB> matches = new ArrayList<>(); //Agafem tots els partits de la base de dades
        for (QueryDocumentSnapshot document : documents) {
            matches.add(document.toObject(MatchDB.class));
        }

        ArrayList<Match> matchesToReturn = new ArrayList<>(); //passar de matchDB a matches i returnar matches com a tal
        //passar de matchDB a matches i returnar matches com a tal
        for (MatchDB match: matches) {
            matchesToReturn.add(new Match(match.getTeam1(), match.getTeam2(), match.getTeam1score(), match.getTeam2score(), match.getId(), match.getLeague(), match.getDate(), match.getPlayed(), configDAO.getGameDuration(), match.getStartHour(), match.getMatchEvents()));
        }

        return matchesToReturn;
    }

    /**
     * Method that gets all the matches played
     * @return list of matches (ArrayList<Match>)
     */
    @Override
    public ArrayList<Match> getMatchesPlayed() {
        ArrayList<Match> matches = getAllMatchesFromDB();
        ArrayList<Match> matchesToReturn = new ArrayList<>();

        for(Match match : matches) {
            if(match.getPlayed() == 1) {
                matchesToReturn.add(match);
            }
        }

        return matchesToReturn;
    }

    /**
     * Method that gets all the matches played given a team ID
     * @param id team ID
     * @return list of matches (ArrayList<Match>)
     */
    @Override
    public ArrayList<Match> getMatchesPlayedByTeam(String id) {
        ArrayList<Match> matches = getAllMatchesFromDB();
        ArrayList<Match> matchesToReturn = new ArrayList<>();

        for(Match match : matches) {
            if(match.getPlayed() == 1 && match.getTeam1().equals(id) || match.getTeam2().equals(id)) {
                matchesToReturn.add(match);
            }
        }

        return matchesToReturn;
    }

    /**
     * Method that deletes all the played matches from the DB
     */
    @Override
    public void deletePlayedMatches() {
        ApiFuture<QuerySnapshot> future = bd.collection(TABLE).whereEqualTo("played", 1).get();
        List<QueryDocumentSnapshot> documents = null;
        try {
            documents = future.get().getDocuments();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        for (QueryDocumentSnapshot document : documents) {
            deleteMatchFromDB(document.toObject(MatchDB.class).getId());
        }
    }

    @Override
    public boolean matchDoesnExist(Match match) {
        ApiFuture<QuerySnapshot> future = bd.collection(TABLE).whereEqualTo("id", match.getIdMatch()).get();

        try {
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            return documents.size() == 0;
        } catch (InterruptedException | ExecutionException e) {
            return true;
        }
    }

}

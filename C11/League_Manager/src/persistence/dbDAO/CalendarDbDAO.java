package persistence.dbDAO;

import business.model.entities.Calendar;
import business.model.entities.JourneyForDB;
import business.model.entities.League;
import business.model.entities.Match;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import persistence.CalendarDAO;
import persistence.MatchDAO;
import persistence.PlayerDAO;

import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Class that implements the CalendarDAO interface
 */
public class CalendarDbDAO implements CalendarDAO {
    private static Firestore bd;
    private MatchDAO matchDAO;
    private static final String TABLE = "CALENDAR";

    public CalendarDbDAO(Firestore bd, MatchDAO matchDAO) {
        this.bd = bd;
        this.matchDAO = matchDAO;
    }

    /**
     * Method that imports data to the DB
     * @param id calendar ID
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
     * Method that gets a calendar from the DB
     * @param calendar calendar to export
     * @return the calendar (Calendar)
     */
    @Override
    public boolean exportCalendarToDB(Calendar calendar) {
        ArrayList<String> journeysID = new ArrayList<>();

        //creem la journey i buidem la llista de llistes de partits
        journeysID = createJourneys(calendar);

        //L'escrivim de nou modificat
        Map<String, Object> data = new HashMap<>();
        data.put("journeysId", journeysID); //Posem la llista de llistes de partits
        data.put("id", calendar.getId()); //Tots els documents han de tenir un id
        data.put("leagueID", calendar.getLeagueID());

        return importDataToDB(calendar.getId(), data);
    }

    /**
     * Method that creates the journeys of the calendar
     * @param calendar calendar to create the journeys
     * @return the journeys ID (ArrayList<String>)
     */
    private ArrayList<String> createJourneys(Calendar calendar) {
        ArrayList<String> matchesID = new ArrayList<>();
        ArrayList<String> journeysID = new ArrayList<>();
        for (List<Match> journeys: calendar.getMatches()) {
            for (Match match: journeys) {
                matchesID.add(match.getIdMatch());
                matchDAO.exportMatchToDB(match);
            }
            JourneyForDB journeyForDB = new JourneyForDB(matchesID);
            if(!exportJourneyToDB(journeyForDB)) {
                throw new RuntimeException("Error exporting journey to the database");
            }

            journeysID.add(journeyForDB.getId());
            matchesID = new ArrayList<>();
        }

        return journeysID;
    }

    /**
     * Method that exports a journey to the DB
     * @param journeyForDB journey to export
     * @return true if the journey has been exported, false otherwise (boolean)
     */
    private boolean exportJourneyToDB(JourneyForDB journeyForDB) {
        //L'escrivim de nou modificat
        Map<String, Object> data = new HashMap<>();
        data.put("matchesID", journeyForDB.getMatchesID());
        data.put("id", journeyForDB.getId()); //Posem la llista de llistes de partits

        try{
            DocumentReference docRef = bd.collection("JOURNEYS").document(journeyForDB.getId());
            ApiFuture<WriteResult> result = docRef.set(data);
            System.out.println("Update time : " +result.get().getUpdateTime());
            return true;
        } catch (ExecutionException | InterruptedException e) {
            return false;
        }
    }

    /**
     * @param calendarID
     * @return
     */
    @Override
    public List<List<Match>> getMatchesFromCalendar(String calendarID) {
        // asynchronously retrieve multiple documents
        ApiFuture<QuerySnapshot> future = bd.collection(TABLE).whereEqualTo("id", calendarID).get();
        List<QueryDocumentSnapshot> documents = null;
        try {
            documents = future.get().getDocuments();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        Calendar temp = documents.get(0).toObject(Calendar.class);

        List<List<Match>> lists = new ArrayList<>();

        for (String journeyID: temp.getJourneysId()) {
            JourneyForDB journeyForDB = getJourneyFromDB(journeyID);
            ArrayList<String> matchesID = journeyForDB.getMatchesID();
            List<Match> aux = new ArrayList<>();
            for (String matchID: matchesID) {
                Match match = matchDAO.getMatchFromID(matchID);
                aux.add(match);
            }
            lists.add(aux);
        }


        return lists;
    }

    /**
     * Method that gets a calendar from the DB
     * @param calendarID calendar ID to get
     * @return the calendar (Calendar)
     */
    public Calendar getCalendarFromDB(String calendarID) {
        //Procés invers al crear una lliga
        // asynchronously retrieve multiple documents
        ApiFuture<QuerySnapshot> future = bd.collection(TABLE).whereEqualTo("id", calendarID).get();
        List<QueryDocumentSnapshot> documents = null;
        try {
            documents = future.get().getDocuments();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        Calendar temp = documents.get(0).toObject(Calendar.class);
        ArrayList<String> journeysID = temp.getJourneysId();
        List<List<Match>> lists = new ArrayList<>();

        for (String journeyID: temp.getJourneysId()) {
            JourneyForDB journeyForDB = getJourneyFromDB(journeyID);
            ArrayList<String> matchesID = journeyForDB.getMatchesID();
            List<Match> aux = new ArrayList<>();
            for (String matchID: matchesID) {
                Match match = matchDAO.getMatchFromID(matchID);
                aux.add(match);
            }
            lists.add(aux);
        }

        temp.setMatches(lists);

        return temp;
    }

    /**
     * Method that deletes a calendar from a league
     * @param leagueID id of the league to delete
     * @return true if the calendar has been deleted, false otherwise (boolean)
     */
    @Override
    public boolean deleteCalendarFromALeague(String leagueID) {
        Calendar calendar = getCalendarFromLeague(leagueID);

        for (String journeyID : calendar.getJourneysId()) {
            if(!deleteJourney(journeyID)) {
                return false;
            }
        }

        return deleteCalendar(calendar.getId());
    }

    /**
     * Method that deletes a journey from the DB
     * @param journeyID journey ID to delete
     * @return true if the journey has been deleted, false otherwise (boolean)
     */
    private boolean deleteJourney(String journeyID) {
        try {
            bd.collection("JOURNEYS").document(journeyID).delete();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Method that deletes a calendar from the DB
     * @param id calendar ID to delete
     * @return true if the calendar has been deleted, false otherwise (boolean)
     */
    private boolean deleteCalendar(String id) {
        try {
            bd.collection(TABLE).document(id).delete();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Method that gets a calendar from a league
     * @param leagueID league ID to get
     * @return the calendar (Calendar)
     */
    private Calendar getCalendarFromLeague(String leagueID) {
        // asynchronously retrieve multiple documents
        ApiFuture<QuerySnapshot> future = bd.collection(TABLE).whereEqualTo("leagueID", leagueID).get();
        // future.get() blocks on response
        List<QueryDocumentSnapshot> documents = null;
        try {
            documents = future.get().getDocuments();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        return documents.get(0).toObject(Calendar.class);
    }

    /**
     * Method that gets a journey from the DB
     * @param journeyID journey ID to get
     * @return the journey
     */
    private JourneyForDB getJourneyFromDB(String journeyID) {
        // asynchronously retrieve multiple documents
        ApiFuture<QuerySnapshot> future = bd.collection("JOURNEYS").whereEqualTo("id", journeyID).get();
        // future.get() blocks on response
        List<QueryDocumentSnapshot> documents = null;
        try {
            documents = future.get().getDocuments();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        return documents.get(0).toObject(JourneyForDB.class);
    }
}

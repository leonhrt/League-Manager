package business.model.entities;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

/**
 * This class represents a journey that will be uploaded to DB or download from DB.
 */
public class JourneyForDB {

    // Attributes
    private ArrayList<String> matchesID;
    private String id;

    public JourneyForDB() {}
    /**
     * Constructor of Journey.
     * @param matchesID matches of the journey
     */
    public JourneyForDB(ArrayList<String> matchesID) {
        this.matchesID = matchesID;
        id = UUID.randomUUID().toString();
    }

    /**
     * Method that gets the matches of the journey.
     * @return matches of the journey (ArrayList<String>)
     */
    public ArrayList<String> getMatchesID() {
        return matchesID;
    }

    /**
     * Method that sets the matches to the journey.
     * @param matchesID matches of the journey
     */
    public void setMatchesID(ArrayList<String> matchesID) {
        this.matchesID = matchesID;
    }

    /**
     * Method that get the id of the journey.
     * @return id of the journey (String)
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}

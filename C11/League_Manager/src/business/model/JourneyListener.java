package business.model;

/**
 * Interface that allows the communication between the MatchController and the MatchView
 */
public interface JourneyListener {

    /**
     * Method that notifies the change of journey
     * @param leagueId id of the league
     * @param newJourney new journey
     */
    void journeyChanged(String leagueId, String newJourney);
}

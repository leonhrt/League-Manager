package business.model;

import business.model.entities.Match;
import business.model.entities.MatchInfo;

import java.util.ArrayList;

/**
 * Interface that allows the communication between the MatchController and the MatchView
 */
public interface MatchListener {
    //void notifyMatchUpdate(ArrayList<MatchInfo> matches);
    /**
     * Method that notifies the total time of the match
     * @param totalTimeMatch total time of the match
     */
    //void notifyTotalTimeMatch(int totalTimeMatch);

    /**
     * Method that notifies the start of the match
     * @param info match info
     */
    void notifyStartMatch(MatchInfo info);

    /**
     * Method that notifies the change of possession
     * @param playingTeam playing team of the match
     */
    //void notifyChangePossession(int playingTeam);

    /**
     * Method that notifies the score of the team
     * @param info match info
     */
    void notifyScoreUpdated(MatchInfo info);

    /**
     * Method that notifies the end of the match
     */
    void notifyEndMatch(Match info);
}

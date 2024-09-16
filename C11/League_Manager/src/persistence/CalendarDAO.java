package persistence;


import business.model.entities.Calendar;
import business.model.entities.Match;

import java.util.List;
import java.util.Queue;

/**
 * Interface that defines the methods that a CalendarDAO must implement
 */
public interface CalendarDAO {
    // TODO: Add calendar to DB

    /**
     * Method that exports a calendar to the DB
     * @param calendar calendar to export
     * @return true if the calendar has been exported, false otherwise (boolean)
     */
    boolean exportCalendarToDB(Calendar calendar);

    /**
     * Method that gets the matches from the calendar
     * @param calendarID id of the calendar
     * @return list of matches (List<List<Match>>)
     */
    List<List<Match>> getMatchesFromCalendar(String calendarID); //gets the matches from the calendar.

    /**
     * Method that imports a calendar from the DB
     * @param calendarID id of the calendar to import
     * @return calendar imported (Calendar)
     */
    Calendar getCalendarFromDB(String calendarID);

    /**
     * Method that deletes a calendar from the DB based on the league id
     * @param leagueID id of the league to delete
     * @return true if the calendar has been deleted, false otherwise (boolean)
     */
    boolean deleteCalendarFromALeague(String leagueID);
}

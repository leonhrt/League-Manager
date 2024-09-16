package business.model;

import business.model.entities.*;
import business.model.entities.Calendar;
import business.model.exceptions.CouldntDeleteLeagueException;
import business.model.exceptions.DateOrHourInvalidException;
import business.model.exceptions.LeagueNameAlreadyExists;
import persistence.*;
import persistence.filesDAO.ConfigDAO;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Class that manages the leagues
 */
public class LeagueManager {

    // Components
    private final LeagueDAO leagueDAO;
    private final TeamDAO teamDAO;
    private final CalendarDAO calendarDAO;
    private final TeamRankingDAO teamRankingDAO;
    private final MatchDAO matchDAO;
    private final UserManager userManager;
    private final ConfigDAO configDAO;
    private final int MINIMUM_DAY = -1;
    private final int MAXIMUM_DAY = 31;
    private final int MINIMUM_MONTH = -1;
    private final int MAXIMUM_MONTH = 12;
    private final int MINIMUM_YEAR = 2023;
    private final int MINIMUM_HOUR = 0;
    private final int MAXIMUM_HOUR = 23;
    private final int MINIMUM_MINUTE = 0;
    private final int MAXIMUM_MINUTE = 59;
    private final String DATE_SEPARATOR = "/";
    private final String HOUR_SEPARATOR = ":";
    private final String DATETIME_FORMAT = "dd/MM/yyyy HH:mm";
    private final String DUMMY_TEAM = "-1";
    private final String DECIMAL_FORMAT = "00";


    /**
     * Constructor de LeagueManager
     * @param leagueDAO league DAO
     * @param teamDAO team DAO
     * @param userManager user Manager
     * @param calendarDAO calendar DAO
     */
    public LeagueManager(LeagueDAO leagueDAO, TeamDAO teamDAO, UserManager userManager, CalendarDAO calendarDAO,
                         TeamRankingDAO teamRankingDAO, MatchDAO matchDAO, ConfigDAO configDAO) {
        this.leagueDAO = leagueDAO;
        this.teamDAO = teamDAO;
        this.userManager = userManager;
        this.calendarDAO = calendarDAO;
        this.teamRankingDAO = teamRankingDAO;
        this.matchDAO = matchDAO;
        this.configDAO = configDAO;
    }

    /**
     * Method that checks if the league name and the date are valid
     * @param leagueName league name
     * @param date date
     * @param startHour start hour
     * @throws LeagueNameAlreadyExists in case the league name already exists
     * @throws DateOrHourInvalidException in case the date or the hour are invalid
     */
    public void checkLeagueNameAndDate(String leagueName, String date, String startHour) throws LeagueNameAlreadyExists,
            DateOrHourInvalidException {
        if (null != leagueDAO.getLeagueByName(leagueName)) {
            throw new LeagueNameAlreadyExists();
        }

        if (!validDate(date, startHour)) {
            throw new DateOrHourInvalidException();
        }
    }

    /**
     * Method that checks if the date is valid
     * @param date date
     * @param startHour start hour
     * @return true if the date is valid, false otherwise
     */
    private boolean validDate(String date, String startHour) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATETIME_FORMAT);
        LocalDateTime currentDateTime = LocalDateTime.now();

        String[] dateStr = date.split(DATE_SEPARATOR);
        String[] startHourStr = startHour.split(HOUR_SEPARATOR);

        try{
            int day = Integer.parseInt(dateStr[0]);
            int month = Integer.parseInt(dateStr[1]);
            int year = Integer.parseInt(dateStr[2]);


            int hour = Integer.parseInt(startHourStr[0]);
            int minute = Integer.parseInt(startHourStr[1]);

            if ((day >= MINIMUM_DAY && day <= MAXIMUM_DAY)
                    && (month >= MINIMUM_MONTH && month <= MAXIMUM_MONTH)
                    && (year >= MINIMUM_YEAR)
                    && (hour >= MINIMUM_HOUR && hour <= MAXIMUM_HOUR)
                    && (minute >= MINIMUM_MINUTE && minute <= MAXIMUM_MINUTE)) {
                LocalDateTime newDate = LocalDateTime.parse(date + " " + startHour, formatter);

                return currentDateTime.isBefore(newDate);
            }
        }
        catch(ArrayIndexOutOfBoundsException | NumberFormatException e){
            return false;
        }

        return false;
    }

    /**
     * Method that add the teams to the league, create the calendar and the ranking
     * @param leagueName league name
     * @param date date
     * @param teams teams
     */
    public League createLeague(String leagueName, String date, String startHour, ArrayList<String> teams,
                             String imagePath) {
        League league = new League(leagueName, date, startHour, teams, getTeams(teams), UUID.randomUUID().toString(),
                imagePath);
        Calendar calendar = createCalendar(league);
        league.setNumJourneys(calendar.getNumJourneys());
        calendarDAO.exportCalendarToDB(calendar);
        leagueDAO.exportLeagueToDB(league);

        teamRankingDAO.exportDataToDB(createRanking(league));

        return league;
    }

    /**
     * Method that gets the teams from the database and converts them to Team objects
     * @param teamsName teams name
     * @return ArrayList of Team objects
     */
    private ArrayList<Team> getTeams(ArrayList<String> teamsName) {
        ArrayList<Team> teams = new ArrayList<>();
        for (String team : teamsName) {
            teams.add(teamDAO.convertToTeam(teamDAO.getTeamByName(team)));
        }

        return teams;
    }

    /**
     * Method that creates a ranking for a league
     * @param league league
     * @return ranking
     */
    private ArrayList<TeamRanking> createRanking(League league) {
        ArrayList<TeamRanking> listTeamRanking = new ArrayList<>();

        for (int i = 0; i < league.getTeams().size(); i++) {
            TeamRanking teamRanking = new TeamRanking(UUID.randomUUID().toString(), league.getTeamsID().get(i),
                    league.getTeams().get(i), league.getId(), 0, 0, 0, 0 ,
                    new ArrayList<>());
            listTeamRanking.add(teamRanking);
        }

        return listTeamRanking;
    }

    /**
     * Method that generates next matches date for a league
     * @param startDate start date
     * @param startHour start hour
     * @param journey journey
     *
     * @return next matches date (LocalDateTime)
     */
    private LocalDateTime generateNextMatchesDate(String startDate, String startHour, int journey) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATETIME_FORMAT);
        LocalDateTime dateAndHour = LocalDateTime.parse(startDate + " " + startHour, formatter);

        // First journey
        if (journey == 0) {
            return dateAndHour.plusMinutes(1);
        }

        // All other journeys
        return dateAndHour.plusMinutes((configDAO.getGameDuration() + 2) * journey);
    }

    /**
     * Method that creates a calendar for a league
     * @param league league
     * @return calendar of the league (Calendar)
     */
    private Calendar createCalendar(League league) {
        List<Team> listTeams = new ArrayList<>(league.getTeams());

        Team dummyTeam = new Team(DUMMY_TEAM, "");
        if (listTeams.size() % 2 != 0) {
            listTeams.add(dummyTeam);
        }

        int numJourneys = listTeams.size() - 1;
        int halfSize = listTeams.size() / 2;

        List<Team> teams = new ArrayList<>();

        teams.addAll(listTeams);
        teams.remove(0);

        int teamsSize = teams.size();

        DecimalFormat formatter = new DecimalFormat(DECIMAL_FORMAT);

        List<List<Match>> matchesAndJourneys = new ArrayList<>();
        for (int journey = 0; journey < numJourneys; journey++) {
            List<Match> matches = new ArrayList<>();
            int teamIndex = journey % teamsSize;

            LocalDateTime dateAndHour = generateNextMatchesDate(league.getDate(), league.getStartHour(), journey);
            Match match;

            if (!teams.get(teamIndex).getId().equals(DUMMY_TEAM) && !listTeams.get(0).getId().equals(DUMMY_TEAM)) {
                String date = formatter.format(dateAndHour.getDayOfMonth()) + DATE_SEPARATOR
                        + formatter.format(dateAndHour.getMonthValue()) + DATE_SEPARATOR + dateAndHour.getYear();

                String hour = formatter.format(dateAndHour.getHour()) + HOUR_SEPARATOR
                        + formatter.format(dateAndHour.getMinute());

                match = new Match(teams.get(teamIndex).getId(), listTeams.get(0).getId(), league.getId(),
                        UUID.randomUUID().toString(), date, hour);
                matches.add(match);
            }

            for (int index = 1; index < halfSize; index++) {
                int firstTeam = (journey + index) % teamsSize;
                int secondTeam = (journey + teamsSize - index) % teamsSize;
                if (!teams.get(firstTeam).getId().equals(DUMMY_TEAM)
                        && !teams.get(secondTeam).getId().equals(DUMMY_TEAM)) {
                    String date = formatter.format(dateAndHour.getDayOfMonth()) + DATE_SEPARATOR
                            + formatter.format(dateAndHour.getMonthValue()) + DATE_SEPARATOR + dateAndHour.getYear();

                    String hour = formatter.format(dateAndHour.getHour()) + HOUR_SEPARATOR
                            + formatter.format(dateAndHour.getMinute());

                    match = new Match(teams.get(firstTeam).getId(), teams.get(secondTeam).getId(), league.getId(),
                            UUID.randomUUID().toString(), date, hour);
                    matches.add(match);
                }
            }
            matchesAndJourneys.add(matches);
        }

        int size = matchesAndJourneys.size();
        for (int i = 0; i < size; i++) {
            List<Match> matches = new ArrayList<>();
            LocalDateTime dateAndHour = generateNextMatchesDate(league.getDate(), league.getStartHour(), size + i);

            for (int j = 0; j < matchesAndJourneys.get(i).size(); j++) {
                Match match = new Match(matchesAndJourneys.get(i).get(j));
                match.setId(UUID.randomUUID().toString());

                String date = formatter.format(dateAndHour.getDayOfMonth()) + DATE_SEPARATOR
                        + formatter.format(dateAndHour.getMonthValue()) + DATE_SEPARATOR + dateAndHour.getYear();
                String hour = formatter.format(dateAndHour.getHour()) + HOUR_SEPARATOR
                        + formatter.format(dateAndHour.getMinute());
                match.setDate(date);
                match.setStartHour(hour);

                matches.add(match);
            }
            matchesAndJourneys.add(matches);
        }

        if (listTeams.size() % 2 != 0) {
            listTeams.remove(dummyTeam);
        }

        return new Calendar(league.getCalendarID(), league.getId(), listTeams, matchesAndJourneys,
                matchesAndJourneys.size());
    }

    /**
     * Method that deletes leagues from the database based on the league names
     * @param leagueNames league names to delete
     */
    public void deleteLeagues(List<String> leagueNames) throws CouldntDeleteLeagueException {
        for (String leagueName : leagueNames) {
            League league = leagueDAO.getLeagueByName(leagueName);
            if(leagueDAO.deleteFromDatabase(league.getId()) && matchDAO.deleteMatchesFromLeague(league.getId())
               && teamRankingDAO.deleteRankingsByLeage(league.getId()) && calendarDAO.deleteCalendarFromALeague(league.getId()) ) {
            } else {
                throw new CouldntDeleteLeagueException();
            }
        }
    }

    /**
     * Method that updates a league
     * @param league league to update
     */
    public void updateLeague(League league) {
        leagueDAO.updateLeague(league);
    }

    /**
     * Method that returns a list of leagues
     * @return list of leagues
     */
    public ArrayList<League> getAllLeagues() {
        if (userManager.isAdmin()) {
            return leagueDAO.getAllLeaguesFromDB();
        }

        return leagueDAO.getLeaguesOfPlayer(userManager.getPlayerLogged().getId());
    }

    /**
     * Method that returns a list of team rankings
     * @param leagueId id of the league
     * @return list of team rankings
     */
    public ArrayList<TeamRanking> listRankingDetails(String leagueId) {
        ArrayList<TeamRanking> teamRankings = teamRankingDAO.getRankingsByLeague(leagueId);

        mergeSort(teamRankings, 0, teamRankings.size() - 1);

        return teamRankings;
    }

    /**
     * Method that returns a league from its id
     * @param id id of the league
     * @return league
     */
    public League getLeagueById(String id){
        return leagueDAO.getLeagueFromID(id);
    }

    /**
     * Method that sorts a list of team rankings
     * @param rankingList list to sort
     * @param i first index
     * @param j last index
     */
    private void mergeSort(List<TeamRanking> rankingList, int i, int j) {
        if (i < j) {
            int mid = (i + j) / 2;
            mergeSort(rankingList, i, mid);
            mergeSort(rankingList, mid + 1, j);
            merge(rankingList, i, mid, j);
        }
    }

    /**
     * Method that merges two lists
     * @param rankingList list to merge
     * @param i first index
     * @param mid middle index
     * @param j last index
     */
    private void merge(List<TeamRanking> rankingList, int i, int mid, int j) {
        List<TeamRanking> b = new ArrayList<>();

        for (int x = 0; x < rankingList.size(); x++) {
            b.add(new TeamRanking());
        }

        int l = i;
        int r = mid + 1;
        int cursor = i;

        while (l <= mid && r <= j) {
            if (rankingList.get(l).getPoints() > rankingList.get(r).getPoints()) {
                b.set(cursor, rankingList.get(l));
                l++;
            } else {
                b.set(cursor, rankingList.get(r));
                r++;
            }
            cursor++;
        }

        while (l <= mid) {
            b.set(cursor, rankingList.get(l));
            l++;
            cursor++;
        }

        while (r <= j) {
            b.set(cursor, rankingList.get(r));
            r++;
            cursor++;
        }

        int k = i;
        while (k <= j) {
            rankingList.set(k, b.get(k));
            k++;
        }
    }
}

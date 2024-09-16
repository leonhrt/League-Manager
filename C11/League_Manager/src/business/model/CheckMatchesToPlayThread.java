package business.model;

import business.model.entities.League;
import business.model.entities.Match;
import persistence.MatchDAO;
import presentation.controller.AdminOptionController;
import presentation.controller.UserOptionController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Class that checks if there are matches to play
 */
public class CheckMatchesToPlayThread extends Thread {
    // Components
    private MatchDAO matchDAO;
    private MatchManager matchManager;
    private ArrayList<Match> matchesPlaying;
    private MatchListener matchListener;
    private MatchListener matchListenerAdminOption;
    private LeagueManager leagueManager;
    private JourneyListener journeyListener;
    private UserManager userManager;
    private MatchListener matchListenerUserMenu;
    private MatchListener matchListenerUserOption;
    private JourneyListener journeyListenerUserMenu;

    /**
     * Constructor of the class
     * @param matchDAO match DAO
     */
    public CheckMatchesToPlayThread(MatchDAO matchDAO, MatchManager matchManager, MatchListener matchListener, LeagueManager leagueManager, AdminOptionController listener, UserManager userManager, MatchListener matchListenerUserMenu, UserOptionController listenerUserOption) {
        this.matchDAO = matchDAO;
        this.matchManager = matchManager;
        matchesPlaying = new ArrayList<>();
        this.matchListener = matchListener;
        this.leagueManager = leagueManager;
        this.journeyListener = listener;
        this.journeyListenerUserMenu = listenerUserOption;
        this.matchListenerAdminOption = listener;
        this.userManager = userManager;
        this.matchListenerUserMenu = matchListenerUserMenu;
        this.matchListenerUserOption = listenerUserOption;

        matchDAO.deletePlayedMatches();
    }

    /**
     * Executes the thread
     */
    @Override
    public void run() {
        while (true) {
            ArrayList<String> leaguesUpdated = new ArrayList<>();

            ArrayList<Match> matches = matchDAO.getAllMatchesFromDB();
            ArrayList<Match> matchesToPlay = new ArrayList<>();
            for (Match match: matches) {
                //Si s'ha de jugar i no s'està jugant i no s'ha jugat ja
                if(toPlay(match.getDate(), match.getStartHour()) && (match.getPlayed() != 1)){
                    if(!leaguesUpdated.contains(match.getLeague())) { //Si la lliga no s'ha actualitzat
                        League tempLeague = leagueManager.getLeagueById(match.getLeague());
                        if(!tempLeague.getJourney().equals("ENDED")) {
                            tempLeague.setJourney(checkJourneys(Integer.parseInt(tempLeague.getJourney()) + 1, tempLeague.getNumJourneys()));
                            leagueManager.updateLeague(tempLeague);
                            leaguesUpdated.add(match.getLeague()); //Afegeix la lliga a la llista de lligues actualitzades
                            //Actualitzar journey de la lliga amb listener
                            if (userManager.isAdmin()) { //Si es admin el player logged
                                journeyListener.journeyChanged(match.getLeague(), tempLeague.getJourney());
                                journeyListenerUserMenu.journeyChanged(match.getLeague(), tempLeague.getJourney());
                            } else if (userManager.checkIfPlayingLeague(tempLeague.getId())) { //Si el player logged juga a aquella lliga
                                journeyListener.journeyChanged(match.getLeague(), tempLeague.getJourney());
                                journeyListenerUserMenu.journeyChanged(match.getLeague(), tempLeague.getJourney());
                            }
                        }
                    }
                    matchesToPlay.add(match);
                    match.setPlayed(1); //Actualitzem el match ja no cal començar el thread
                    matchDAO.updateMatchStatus(match);
                }
            }

            startMatches(matchesToPlay);

            try {
                Thread.sleep(30000); //Esperem 45 segons a tornar a fer la petició a la base de dades

            } catch (InterruptedException ignored) { }

        }
    }

    /**
     * Checks if the league has ended
     * @param journey journey of the league
     * @param maxJourneys number of journeys of the league
     * @return "ENDED" if the league has ended, the journey otherwise
     */
    private String checkJourneys(int journey, int maxJourneys) {
        if (journey >= maxJourneys) {
            return "ENDED";
        } else {
            return String.valueOf(journey);
        }
    }


    /**
     * Checks if the match has to be played
     * @param date time of the match
     * @param startHour start hour of the match
     */
    private boolean toPlay(String date, String startHour) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime targetDateTime = LocalDateTime.parse(date + " " + startHour, formatter);

        if (!currentDateTime.isBefore(targetDateTime)) { //Si s'ha de jugar
            return true;
        }

        // Si no s'ha de jugar
        return false;
    }

    /**
     * Starts the matches
     * @param matchesToPlay matches to play
     */
    private void startMatches(ArrayList<Match> matchesToPlay) {
        matchesPlaying.addAll(matchesToPlay);

        for (Match match: matchesToPlay) {
            if(!match.isAlive()) {//Si no s'està jugant l'iniciem (el thread no esta anant)
                System.out.println(match.getStartHour() + " " + match.getDate() + " started");
                match.setMatchDAO(matchDAO);
                match.startMatch(matchListener, matchManager.getTimeMatch(), matchManager.getMatchInfo(match), leagueManager, matchListenerAdminOption, userManager, matchListenerUserOption, matchListenerUserMenu); //Iniciem el thread, el thread un cop acabi cridara a la funcio updateMatch o cada x segons
            }
        }
    }



}

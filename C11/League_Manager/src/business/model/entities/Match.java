package business.model.entities;

import business.model.LeagueManager;
import business.model.MatchListener;
import business.model.UserManager;
import persistence.MatchDAO;

import java.util.ArrayList;
import java.util.Random;

/**
 * This class represents a match of a league.
 */
public class Match extends Thread{

    // Attributes
    private final String team1;
    private final String team2;
    private int team1Score = 0;
    private int team2Score = 0;
    private String id;
    private String league;
    private String date;
    private String startHour;
    private int isPlayed;
    private int gameDuration;
    private MatchInfo info;
    private int playingTeam = firstTeamStartMatch();
    private int timePlayingTeam = 0;
    private int totalTimeMatch = 0;
    private MatchListener matchListenerAdminMenu;
    private MatchListener matchListenerAdminOption;
    private MatchListener matchListenerUserMenu;
    private MatchListener matchListenerUserOption;

    private ArrayList<String> matchEvents;
    private MatchDAO matchDAO;
    private LeagueManager leagueManager;
    private UserManager userManager;
    private boolean needsToNotify;


    public void setMatchDAO(MatchDAO matchDAO) {
        this.matchDAO = matchDAO;
    }

    /**
     * Constructor of Match used to clone a match.
     * @param that match to clone
     */
    public Match(Match that) {
        this.team1 = that.team1;
        this.team2 = that.team2;
        this.team1Score = that.team1Score;
        this.team2Score = that.team2Score;
        this.id = that.id;
        this.league = that.league;
    }

    /**
     * Constructor of Match.
     * @param team1
     * @param team2
     * @param team1Score
     * @param team2Score
     * @param id
     * @param league
     * @param date
     * @param isPlayed
     * @param gameDuration
     * @param startHour
     */
    public Match(String team1, String team2, int team1Score, int team2Score, String id, String league, String date, int isPlayed, int gameDuration, String startHour, ArrayList<String> events) {
        this.team1 = team1;
        this.team2 = team2;
        this.team1Score = team1Score;
        this.team2Score = team2Score;
        this.id = id;
        this.league = league;
        this.date = date;
        this.isPlayed = isPlayed;
        this.gameDuration = gameDuration;
        this.startHour = startHour;
        matchEvents = events;
    }

    /**
     * Constructor of Match.
     * @param team1         first team of the match
     * @param team2         second team of the match
     * @param league        league of the match
     * @param id            id of the match
     * @param matchListenerAdminMenu
     */
    public Match(String team1, String team2, String league, String id, MatchListener matchListenerAdminMenu) {
        this.team1 = team1;
        this.team2 = team2;
        this.league = league;
        this.id = id;
        this.matchListenerAdminMenu = matchListenerAdminMenu;
        matchEvents = new ArrayList<>();
    }

    public Match(String name, String name1, String league, String id, String date, String hour) {
        this.team1 = name;
        this.team2 = name1;
        this.league = league;
        this.id = id;
        this.date = date;
        this.startHour = hour;
        matchEvents = new ArrayList<>();
    }

    public void startMatch(MatchListener matchListener, int gameDuration, MatchInfo info, LeagueManager leagueManager, MatchListener matchListenerAdminMenu, UserManager userManager, MatchListener matchListenerUserOption, MatchListener matchListenerUserMenu) {
        this.matchListenerAdminMenu = matchListener;
        this.gameDuration = gameDuration * 60000;
        this.info = info;
        this.leagueManager = leagueManager;
        this.matchListenerAdminOption = matchListenerAdminMenu;
        this.matchListenerUserMenu = matchListenerUserMenu;
        this.matchListenerUserOption = matchListenerUserOption;
        this.userManager = userManager;
        if(!userManager.isAdmin()) { //Si no es admin, comprovar si el user esta jugant la lliga
            needsToNotify = userManager.checkIfPlayingLeague(this.league); //Ens guardem el atribut per saber si hem de notificar o no
        }
        this.start();
    }

    /**
     * Thread that simulates a match.
     */
    @Override
    public void run() {
        matchEvents = new ArrayList<>();
        // One team starts with the ball 50% chance each team
        int auxPlayingTeam = getPlayingTeam();

        matchEvents.add(startMatchToString());
        matchEvents.add(startsTeamToString());
        updateInDatabase();

        info.setEvents(matchEvents);
        if (userManager.isAdmin()) {
            matchListenerAdminMenu.notifyStartMatch(info);
            matchListenerAdminOption.notifyStartMatch(info);
            matchListenerUserOption.notifyStartMatch(info);
            matchListenerUserMenu.notifyStartMatch(info);
        } else if (needsToNotify) {
            matchListenerAdminMenu.notifyStartMatch(info);
            matchListenerAdminOption.notifyStartMatch(info);
            matchListenerUserOption.notifyStartMatch(info);
            matchListenerUserMenu.notifyStartMatch(info);
        }

        do {
            if(matchDAO.matchDoesnExist(this)) {
                matchListenerAdminOption.notifyEndMatch(this);
                matchListenerAdminMenu.notifyEndMatch(this);
                matchListenerUserOption.notifyEndMatch(this);
                matchListenerUserMenu.notifyEndMatch(this);
                return;
            }

            //Player plays with the ball a random time between 0 and 1 second
            try {
                int timePlayedWithBall = timePlayedWithBall();
                timePlayingTeam += timePlayedWithBall;
                totalTimeMatch += timePlayedWithBall;
                //matchListener.notifyTotalTimeMatch(getTotalTimeMatch());
                //matchEvents.add();

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            //Player have a 10% chance of losing the ball
            playingTeam = possessionChanged(playingTeam);

            //1 if the team that has the ball is the same as the team that started with the ball
            //0 if the team that has the ball is the other team that started with the ball
            if (!ballSameTeam(auxPlayingTeam, playingTeam)) {
                //If the ball changed team, the time that the team had the ball is reset
                timePlayingTeam = 0;
                //matchListener.notifyChangePossession(playingTeam);
                //matchEvents.add(changePossesionToString());
                //updateInDatabase();
            }

            //If the team has the ball for more than 10 seconds, the team can try to score
            //If a team scores, the other team starts with the ball
            scorePhase(timePlayingTeam, playingTeam);

            //The other team starts with the ball because the team scored or because the team lost the ball trying to scre
            playingTeam = (playingTeam + 1) % 2;
            auxPlayingTeam = playingTeam;

        } while(!matchEnded(totalTimeMatch));

        if(matchDAO.matchDoesnExist(this)) {
            matchListenerAdminOption.notifyEndMatch(this);
            matchListenerAdminMenu.notifyEndMatch(this);
            matchListenerUserOption.notifyEndMatch(this);
            matchListenerUserMenu.notifyEndMatch(this);
            return;
        }
        updateRankingToDatabase();

    }



    private synchronized void updateRankingToDatabase() {
        matchDAO.updateRanking(this); //update ranking i eliminem match
        //Si el partit s'ha de notificar (en el cas d'admin sempre, en el cas d'usuari només els de les seves lligues)
        if(userManager.isAdmin()) { //Si es admin el usuari logejat
            matchEvents.add(endMatchToString());
            matchEvents.add(winnerToString());
            matchListenerAdminMenu.notifyEndMatch(this);
            matchListenerAdminOption.notifyEndMatch(this);
            matchListenerUserOption.notifyEndMatch(this);
            matchListenerUserMenu.notifyEndMatch(this);
        } else if (needsToNotify) { //Si el partit es tracta d'un partit de la lliga del jugador logejat
            matchEvents.add(endMatchToString());
            matchEvents.add(winnerToString());
            matchListenerAdminMenu.notifyEndMatch(this);
            matchListenerAdminOption.notifyEndMatch(this);
            matchListenerUserOption.notifyEndMatch(this);
            matchListenerUserMenu.notifyEndMatch(this);
        }
    }

    private String endMatchToString() {
        return "Match ended: " + team1Score + "-" + team2Score;
    }
    private String winnerToString(){
        if(team1Score>team2Score){
            return info.getTeam1Name() + " wins the match. They get 3 points!";
        }
        else if(team1Score<team2Score){
            return info.getTeam2Name() + " wins the match. They get 3 points!";
        }
        else{
            return "There is a draw. Both get 1 point!";

        }
    }

    private String changePossesionToString() {
        String team = playingTeam == 0? info.getTeam1Name():info.getTeam2Name();
        return "Possession changed. Now playing : " + team;
    }

    private String startMatchToString() {
        return "Match started: " + info.getTeam1Name() + " vs " + info.getTeam2Name();
    }
    private String startsTeamToString() {
        String team = playingTeam == 0? info.getTeam1Name():info.getTeam2Name();
        return  "Starts playing: " + team;
    }
    private String teamScoredToString(String team) {
        return "GOOOOOOOOOOOOAAAAAAAL!!! "+ team + " scored. " + team1Score + " - " + team2Score ;
    }


    /**
     * Time played with the ball.
     * @return time played with the ball
     * @throws InterruptedException exception
     */
    private int timePlayedWithBall() throws InterruptedException {
        int play;
        //Player plays with the ball a random time between 0 and 1 second
        Random random = new Random();
        play = random.nextInt(1000);
        if (totalTimeMatch + play > gameDuration) {
            play = gameDuration - totalTimeMatch;
        }
        Thread.sleep(play);
        return play;
    }

    /**
     * Calculate if the possession of the ball changed.
     * @param playingTeam
     * @return
     */
    private int possessionChanged(int playingTeam) {
        //Player passes the ball to another player
        if (!passBall()) {
            return (playingTeam + 1) % 2;
        }
        return playingTeam;
    }

    /**
     * Heuristic to decide is the ball is passed.
     * @return True if the ball is passed, false otherwise
     */
    private boolean passBall() {
        Random random = new Random();
        int pass = random.nextInt(10);
        return pass != 0;
    }

    /**
     * Heuristic to decide if the team score or not.
     * @param timePlaying time that the team has the ball
     * @param playingTeam team that has the ball
     */
    private void scorePhase(int timePlaying, int playingTeam){
        if (timePlaying >= 5000) {
            //If the team has the ball for more than 10 seconds, the team can score
            //The team has a 10% chance of scoring
            if(matchDAO.matchDoesnExist(this)) {
                matchListenerAdminOption.notifyEndMatch(this);
                matchListenerAdminMenu.notifyEndMatch(this);
                matchListenerUserOption.notifyEndMatch(this);
                matchListenerUserMenu.notifyEndMatch(this);
                return;
            }

            if (tryScore()) {
                if (playingTeam == 0) {
                    team1Score++;
                    matchEvents.add(teamScoredToString(info.getTeam1Name()));
                } else {
                    team2Score++;
                    matchEvents.add(teamScoredToString(info.getTeam2Name()));
                }
                info.setScoreTeam1(team1Score);
                info.setScoreTeam2(team2Score);
                if(userManager.isAdmin()) { //Si es admin el usuari logejat
                    matchListenerAdminMenu.notifyScoreUpdated(info);
                    matchListenerAdminOption.notifyScoreUpdated(info);
                    matchListenerUserOption.notifyScoreUpdated(info);
                    matchListenerUserMenu.notifyScoreUpdated(info);
                } else if (needsToNotify) { //Si el partit es tracta d'un partit de la lliga del jugador logejat
                    matchListenerAdminMenu.notifyScoreUpdated(info);
                    matchListenerAdminOption.notifyScoreUpdated(info);
                    matchListenerUserOption.notifyScoreUpdated(info);
                    matchListenerUserMenu.notifyScoreUpdated(info);
                }
                updateInDatabase(); //actualitzem el match cada vegada que hi ha un gol
            }
        }
    }


    private synchronized void updateInDatabase() {
        matchDAO.updateMatchStatus(this);
    }

    /**
     * Random number between 0 and 1 to decide if the team scores.
     * @return True if the team scores, false otherwise
     */
    private boolean tryScore() {
        Random random = new Random();
        int score = random.nextInt(10);
        return score == 0;
    }

    /**
     * Sets the time that the team has the ball.
     * @param auxPlayingTeam team that started with the ball
     * @param playingTeam team that has the ball
     * @return True if the ball is in the same team, false otherwise
     */
    private boolean ballSameTeam(int auxPlayingTeam, int playingTeam) {
        return auxPlayingTeam == playingTeam;
    }

    private boolean matchEnded(int totalTimeMatch) {
        return totalTimeMatch >= gameDuration;
    }


    private int firstTeamStartMatch() {
        Random random = new Random();
        return random.nextInt(2);
    }

    private synchronized int getPlayingTeam() {
        return playingTeam;
    }

    public String getTeam1() {
        return team1;
    }

    public String getTeam2() {
        return team2;
    }

    public int getTeam1Score() {
        return team1Score;
    }

    public int getTeam2Score() {
        return team2Score;
    }

    public String getIdMatch() {
        return id;
    }

    /**
     * Sets the id of the match.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the league of the match.
     * @return league of the match (String)
     */
    public String getLeague() {
        return league;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPlayed() {
        return isPlayed;
    }

    public void setPlayed(int played) {
        isPlayed = played;
    }

    public String getStartHour() {
        return startHour;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public ArrayList<String> getMatchEvents() {
        return matchEvents;
    }
}



package presentation.view.pantalles;

import business.model.entities.*;
import presentation.controller.AdminOptionController;
import presentation.view.ui_elements.CreateLeagueDialog;
import presentation.view.ui_elements.JImagePanel;
import presentation.view.ui_elements.MatchButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class that creates the panel that allows the admin to choose what to manage
 */
public class AdminOptionView extends JImagePanel {

    // Attributes

    // Constants
    public final static String CARD_ADMIN_OPTION = "CARD_ADMIN_OPTION";
    public final static String LOGOUT_PRESSED = "LOGOUT_PRESSED";
    public final static String MENU_PRESSED = "MENU_PRESSED";
    public static final String MATCH_PRESSED = "MATCH_PRESSED_";

    // Components
    private AdminManage adminManage;
    private AdminShowTeamsDelete adminShowTeamsDelete;
    private AdminShowLeaguesDelete adminShowLeaguesDelete;
    private AdminAddTeamsToLeagues adminAddTeamsToLeagues;
    private CreateLeagueDialog createLeagueDialog;
    private ViewLeagues viewLeagues;
    private ViewMatches viewMatches;
    private InfoLeague infoLeague;
    private  MatchDetail matchDetail;
    private SpecificTeam specificTeam;

    private CardLayout cardLayoutAdminManage;
    private JPanel jpCardLayout;

    private JPanel jPanelMatches;
    private JMenuItem jmiLogout;
    private JButton jbMenu;
    private HashMap<String,MatchButton> matches;

    private String leagueCreationName;
    private String leagueCreationStartDate;
    private String leagueCreationStartHour;


    /**
     * Constructor method
     * Creates the panel with the buttons to manage teams and leagues
     *
     * @param
     * @param adminManage  panel that allows the admin to manage teams and leagues
     * @param specificTeam
     */
    public AdminOptionView(AdminManage adminManage, AdminShowTeamsDelete adminShowTeamsDelete, AdminShowLeaguesDelete adminShowLeaguesDelete,
                           AdminAddTeamsToLeagues adminAddTeamsToLeagues, CreateLeagueDialog createLeagueDialog, ViewLeagues viewLeagues,
                           ViewMatches viewMatches, InfoLeague infoLeague, MatchDetail matchDetail, SpecificTeam specificTeam) {
        super("resources/general/fonsEstadi.png");
        matches=new HashMap<>();
        setLayout(new BorderLayout());

        cardLayoutAdminManage = new CardLayout();
        jpCardLayout = new JPanel(cardLayoutAdminManage);
        jpCardLayout.setOpaque(false);

        this.adminManage = adminManage;
        jpCardLayout.add(adminManage, AdminManage.CARD_MANAGE);

        this.adminShowTeamsDelete = adminShowTeamsDelete;
        jpCardLayout.add(adminShowTeamsDelete, AdminShowTeamsDelete.CARD_TEAMS_DELETE);

        this.adminShowLeaguesDelete = adminShowLeaguesDelete;
        jpCardLayout.add(adminShowLeaguesDelete, AdminShowLeaguesDelete.CARD_LEAGUES_DELETE);

        this.adminAddTeamsToLeagues = adminAddTeamsToLeagues;
        jpCardLayout.add(adminAddTeamsToLeagues, AdminAddTeamsToLeagues.CARD_CREATE_LEAGUE);

        this.viewLeagues = viewLeagues;
        jpCardLayout.add(viewLeagues, ViewLeagues.CARD_VIEW_LEAGUES);

        this.viewMatches = viewMatches;
        jpCardLayout.add(viewMatches, ViewMatches.CARD_VIEW_MATCHES);

        this.createLeagueDialog = createLeagueDialog;

        this.infoLeague = infoLeague;
        jpCardLayout.add(infoLeague,InfoLeague.CARD_INFO_LEAGUE);

        this.matchDetail = matchDetail;
        jpCardLayout.add(matchDetail,MatchDetail.CARD_MATCH_DETAIL);

        this.specificTeam = specificTeam;
        jpCardLayout.add(specificTeam,SpecificTeam.CARD_SPECIFIC_TEAM);

        add(jpCardLayout, BorderLayout.CENTER);

        // WEST LAYOUT (logo)
        JPanel jpWest = new JPanel();
        jpWest.setOpaque(false);
        jpWest.setLayout(new BorderLayout());
        add(jpWest, BorderLayout.WEST);

        jbMenu = new JButton();
        JImagePanel jiLogo = new JImagePanel("resources/general/logoPages.png");
        jiLogo.setPreferredSize(new Dimension(300,500));
        jiLogo.setOpaque(false);
        jbMenu.add(jiLogo);
        jbMenu.setPreferredSize(new Dimension(130,100));
        jbMenu.setOpaque(false);
        jbMenu.setContentAreaFilled(false);
        jbMenu.setBorderPainted(false);
        jbMenu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        jpWest.add(jbMenu, BorderLayout.NORTH);

        // EAST LAYOUT (profile)
        JPanel jpEast = new JPanel();
        jpEast.setBackground(Color.pink);
        add(jpEast, BorderLayout.EAST);

        jpEast.setOpaque(false);
        jpEast.setLayout(new BorderLayout());

        ImageIcon jiProfile = new ImageIcon("resources/general/profile.png");
        Image img = jiProfile.getImage();

        Image newimg = img.getScaledInstance(110, 100,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon newIcon = new ImageIcon(newimg);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu();
        menu.setIcon(newIcon);
        menuBar.setPreferredSize(new Dimension(130,100));
        menuBar.setOpaque(false);
        menuBar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        jmiLogout = new JMenuItem("Logout");
        jmiLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        menu.add(jmiLogout);
        menuBar.add(menu);

        jpEast.add(menuBar, BorderLayout.NORTH);

        createSouthMatches();

        setActionCommands();
    }

    /**
     * Method that sets the action commands for the buttons
     */
    private void setActionCommands() {
        jmiLogout.setActionCommand(LOGOUT_PRESSED);
        jbMenu.setActionCommand(MENU_PRESSED);
    }



    /**
     * Method that creates the panel that contains the matches
     * It is a scroll panel
     */
    private void createSouthMatches(){
        jPanelMatches = new JPanel();
        jPanelMatches.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.5f));
        jPanelMatches.setOpaque(false);
        jPanelMatches.setBorder(BorderFactory.createEmptyBorder(5,0,0,0));

        JScrollPane jspMatches = new JScrollPane(jPanelMatches, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jspMatches.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
        jspMatches.setOpaque(false);
        jspMatches.getViewport().setOpaque(false);
        add(jspMatches, BorderLayout.SOUTH);

    }

    /**
     * Method that returns the matches size
     * @return matches size (int)
     */
    public int getMatchesSize(){
        return matches.size();
    }

    /**
     * Method that registers the controller
     * @param l controller listener
     */
    public void registerController(AdminOptionController l) {
        jmiLogout.addActionListener(l);
        adminManage.registerController(l);
        jbMenu.addActionListener(l);
        adminShowTeamsDelete.registerController(l);
        adminShowLeaguesDelete.registerController(l);
        createLeagueDialog.registerController(l);
        adminAddTeamsToLeagues.registerController(l);
        infoLeague.registerController(l);

    }

    /**
     * Method that is uset to switch between the card layout
     * @param id card layout id
     */
    public void switchCard(String id) {
        cardLayoutAdminManage.show(jpCardLayout,id);
    }

    /**
     * Method that gets the selected teams to delete
     * @return teams to delete (arraylist)
     */
    public ArrayList<String> getTeamsToDelete() {
        return adminShowTeamsDelete.getSelectedTeams();
    }

    /**
     * Method that gets the selected leagues to delete
     * @return leagues to delete (arraylist)
     */
    public ArrayList<String> getLeaguesToDelete() {
        return adminShowLeaguesDelete.getSelectedLeagues();
    }

    /**
     * Method that gets the league name
     * @return name of the league (string)
     */
    public String getLeagueName(){
        return leagueCreationName;
    }

    /**
     * Method that gets the league start date
     * @return start date of the league (string)
     */
    public String getStartDate(){
        return leagueCreationStartDate;
    }

    /**
     * Method that gets the league start hour
     * @return start hour of the league (string)
     */
    public String getStartHour() {
        return leagueCreationStartHour;
    }

    /**
     * Method that saves the league creation data
     */
    public void saveLeagueCreationData() {
        leagueCreationName = createLeagueDialog.getLeagueName();
        leagueCreationStartDate = createLeagueDialog.getStartDate();
        leagueCreationStartHour = createLeagueDialog.getStartHour();
    }

    /**
     * Method that sets the error message
     * @param message error message
     */
    public void setErrorCreation(String message) {
        createLeagueDialog.setError(message);
    }

    /**
     * Method that closes the league form
     */
    public void closeLeagueForm() {
        createLeagueDialog.dispose();
    }

    /**
     * Method that give to the user the option to select a file
     * @return id of the option selected (int)
     */
    public int selectFile() {
        return createLeagueDialog.selectFile();
    }

    /**
     * Method that gets the selected file
     * @return selected file (file)
     */
    public File getFile() {
        return createLeagueDialog.getFile();
    }

    /**
     * Method that sets the league image
     * @param path path of the image
     */
    public void setLeagueImagePath(String path) {
        createLeagueDialog.setImage(path);
    }

    /**
     * Method that gets the league image path
     * @return path of the image (string)
     */
    public String getLeagueImagePath(){
        return createLeagueDialog.getImagePath();
    }

    /**
     * Method that notifies the user that not all fields are filled
     * @return
     */
    public boolean notAllFieldsFilled() {
        return createLeagueDialog.notAllFilled();
    }

    /**
     * Method that gets the selected teams to add to the league
     * @return teams to add to the league (arraylist)
     */
    public ArrayList<String> getTeamsToLeague() {
        return adminAddTeamsToLeagues.getSelectedTeams();
    }

    /**
     * Method that update the league journey
     * @param leagueId league id
     * @param newJourney new journey
     */
    public void updateLeagueJourney(String leagueId, String newJourney) {
        viewLeagues.updateLeagueJourney(leagueId,newJourney);
    }

    /**
     * Method that add a match to the view
     * @param l controller listener
     * @param matchInfo match info
     */
    public void addMatch(ActionListener l, MatchInfo matchInfo) {
        MatchButton matchButton = new MatchButton(matchInfo.getTeam1Path(), matchInfo.getTeam2Path(), matchInfo.getScoreTeam1(), matchInfo.getScoreTeam2());
        matchButton.setPreferredSize(new Dimension(200, 70));
        matchButton.setBorder(BorderFactory.createEmptyBorder(0,5,0,5));
        matchButton.setActionCommand(MATCH_PRESSED + matchInfo.getId());
        matchButton.addActionListener(l);
        matches.put(matchInfo.getId(), matchButton);
        jPanelMatches.add(matchButton);
        viewMatches.addMatch(matchInfo,l);
        matchDetail.addEvents(matchInfo.getMatchEvents(),matchInfo.getId());
        revalidate();
    }

    /**
     * Method that updates the match info
     * @param info match info
     */
    public void updateMatchInfo(MatchInfo info) {
        matches.get(info.getId()).updateScore(info.getScoreTeam1(), info.getScoreTeam2());
        viewMatches.updateScore(info);
        matchDetail.updateScore(info);
        matchDetail.addEvents(info.getMatchEvents(),info.getId());
        revalidate();
    }

    /**
     * Method that removes a match
     * @param info match info
     */
    public void removeMatch(Match info) {
        jPanelMatches.remove(matches.get(info.getIdMatch()));
        viewMatches.deleteMatch(info.getIdMatch());
        matchDetail.addEvents(info.getMatchEvents(),info.getIdMatch());
        revalidate();
    }

    /**
     * Method that loads the statistics of the league
     * @param ranking ranking of the league
     */
    public void loadStadistics(ArrayList<TeamRanking> ranking) {
        infoLeague.loadRanking(ranking);

        ArrayList<String> teamNames = new ArrayList<>();
        ArrayList<ArrayList<Integer>> points = new ArrayList<>();
        int max_matches = 0;
        for (TeamRanking rank :ranking) {
            teamNames.add(rank.getTeam().getName());
            points.add(rank.getPointsPerMatch());
            if (max_matches<rank.getPointsPerMatch().size()){
                max_matches = rank.getPointsPerMatch().size();
            }
        }
        infoLeague.addStatisticsGraph(max_matches,points,teamNames);

    }

    /**
     * Method that loads the teams from the database
     * @param allTeams all teams from the database
     */
    public void loadTeams(ArrayList<DBTeam> allTeams) {
        adminShowTeamsDelete.loadTeams(allTeams);
        adminAddTeamsToLeagues.loadTeams(allTeams);
    }

    /**
     * Method that loads the leagues from the database
     * @param allLeagues all leagues from the database
     */
    public void loadLeagues(ArrayList<League> allLeagues,ActionListener l) {
        adminShowLeaguesDelete.loadLeagues(allLeagues);
        viewLeagues.loadLeagues(allLeagues,l);
    }

    /**
     * Method that loads the team from the database
     * @param newTeam new team
     */
    public void loadNewTeam(DBTeam newTeam){
        adminShowTeamsDelete.loadNewTeam(newTeam);
        adminAddTeamsToLeagues.loadNewTeam(newTeam);
    }

    /**
     * Method that loads the league from the database
     * @param new_league new league
     * @param l controller listener
     */
    public void loadNewLeague(League new_league, ActionListener l){
        adminShowLeaguesDelete.loadNewLeague(new_league);
        viewLeagues.loadNewLeague(new_league, l);

    }

    /**ç
     * Method that removes the leagues from the view
     * @param leagues leagues to remove
     */
    public void removeLeagues(ArrayList<String> leagues){
        for (String league : leagues) {
            viewLeagues.removeLeague(league);
        }
    }

    /**
     * Method that set a starting match to the view
     * @param info match info
     */
    public void openMatchDetails(MatchInfo info) {
        matchDetail.setMatchData(info.getId(),info.getTeam1Path(),info.getTeam1Name(),info.getScoreTeam1(),info.getTeam2Path(),info.getTeam2Name(),info.getScoreTeam2());
        matchDetail.addEvents(info.getMatchEvents(),info.getId());
        switchCard(MatchDetail.CARD_MATCH_DETAIL);
    }

    /**
     * Method that load the players to the team
     * @param playersFromATeam players from a team
     * @param team_pressed team pressed
     */
    public void loadPlayersInTeam(ArrayList<Player> playersFromATeam, String team_pressed) {
        specificTeam.loadPlayers(playersFromATeam,team_pressed);
    }

    /**
     * Method that deletes a team from the league creation
     * @param teamsToDelete teams to delete
     */
    public void deleteTeamsFromLeagueCreation(ArrayList<String> teamsToDelete) {
        for(String team: teamsToDelete){
            adminAddTeamsToLeagues.removeTeams(team);

        }
    }

    public void removeAllLeagues() {
        viewLeagues.removeLeagues();
    }
}

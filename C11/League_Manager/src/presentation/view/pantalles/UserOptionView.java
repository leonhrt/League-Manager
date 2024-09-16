package presentation.view.pantalles;

import business.model.entities.*;
import presentation.controller.UserOptionController;
import presentation.view.ui_elements.JImagePanel;
import presentation.view.ui_elements.MatchButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class that creates the panel that allows the user option view
 */
public class UserOptionView extends JImagePanel {
    public final static String CARD_USER_OPTION = "CARD_USER_OPTION";
    private ViewLeagues viewLeagues;
    private MatchDetail matchDetail;
    private HashMap<String, MatchButton> matches;
    private CardLayout cardLayout;
    private JPanel jpCardLayout;
    private JMenuItem jmiLogout;
    private JMenuItem jmiChangePassword;
    private JMenuItem jmiDeleteAccount;
    private JButton jbMenu;
    private JPanel jPanelMatches;
    private ViewMatches viewMatches;
    private InfoLeague infoLeague;
    private SpecificTeam specificTeam;

    /**
     * Constructor method
     * @param viewLeagues ViewLeagues
     * @param matchDetail MatchDetail
     * @param viewMatches ViewMatches
     */
    public UserOptionView(ViewLeagues viewLeagues,MatchDetail matchDetail,ViewMatches viewMatches,
                          InfoLeague infoLeague,SpecificTeam specificTeam) {
        super("resources/general/fonsEstadi.png");
        matches=new HashMap<>();
        setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        jpCardLayout = new JPanel(cardLayout);
        jpCardLayout.setOpaque(false);

        this.viewLeagues = viewLeagues;
        jpCardLayout.add(viewLeagues,ViewLeagues.CARD_VIEW_LEAGUES);

        this.matchDetail = matchDetail;
        jpCardLayout.add(matchDetail,MatchDetail.CARD_MATCH_DETAIL);

        this.viewMatches = viewMatches;
        jpCardLayout.add(viewMatches,ViewMatches.CARD_VIEW_MATCHES);

        this.infoLeague = infoLeague;
        jpCardLayout.add(infoLeague,InfoLeague.CARD_INFO_LEAGUE);

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

        jmiChangePassword = new JMenuItem("Change Password");
        jmiChangePassword.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        jmiDeleteAccount = new JMenuItem("Delete Account");
        jmiDeleteAccount.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        menu.add(jmiChangePassword);
        menu.add(jmiLogout);
        menu.add(jmiDeleteAccount);

        menu.add(jmiLogout);
        menuBar.add(menu);

        jpEast.add(menuBar, BorderLayout.NORTH);

        createSouthMatches();

        setActionCommands();
    }

    /**
     * Method that sets the action commands
     */
    private void setActionCommands() {
        jmiLogout.setActionCommand(UserMenuView.LOGOUT_PRESSED);
        jmiChangePassword.setActionCommand(UserMenuView.CHANGE_PASSWORD_PRESSED);
        jmiDeleteAccount.setActionCommand(UserMenuView.DELETE_ACCOUNT_PRESSED);
        jbMenu.setActionCommand(AdminOptionView.MENU_PRESSED);
    }

    /**
     * Method that registers the controller
     * @param l ActionListener
     */
    public void registerController(ActionListener l){
        jmiLogout.addActionListener(l);
        jmiChangePassword.addActionListener(l);
        jmiDeleteAccount.addActionListener(l);
        jbMenu.addActionListener(l);
    }

    /**
     * Method that creates the south matches panel
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
     * Method that changes between the cards of the card layout
     * @param id
     */
    public void changeCard(String id) {
        cardLayout.show(jpCardLayout,id);
    }

    /**
     * Method that loads the matches of the league
     * @param allLeagues all the leagues
     * @param l action listener
     */
    public void loadLeagues(ArrayList<League> allLeagues, ActionListener l) {
        viewLeagues.loadLeagues(allLeagues,l);
    }

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

    public void loadPlayersInTeam(ArrayList<Player> playersFromATeam, String team_pressed) {
        specificTeam.loadPlayers(playersFromATeam,team_pressed);

    }

    public void updateLeagueJourney(String leagueId, String newJourney) {
        viewLeagues.updateLeagueJourney(leagueId,newJourney);
    }

    public void addMatch(ActionListener l, MatchInfo matchInfo) {
        MatchButton matchButton = new MatchButton(matchInfo.getTeam1Path(), matchInfo.getTeam2Path(), matchInfo.getScoreTeam1(), matchInfo.getScoreTeam2());
        matchButton.setPreferredSize(new Dimension(200, 70));
        matchButton.setBorder(BorderFactory.createEmptyBorder(0,5,0,5));
        matchButton.setActionCommand(UserMenuView.MATCH_PRESSED + matchInfo.getId());
        matchButton.addActionListener(l);
        matches.put(matchInfo.getId(), matchButton);
        jPanelMatches.add(matchButton);
        viewMatches.addMatch(matchInfo,l);
        matchDetail.addEvents(matchInfo.getMatchEvents(),matchInfo.getId());
        revalidate();
    }

    public void updateMatchInfo(MatchInfo info) {
        matches.get(info.getId()).updateScore(info.getScoreTeam1(), info.getScoreTeam2());
        viewMatches.updateScore(info);
        matchDetail.updateScore(info);
        matchDetail.addEvents(info.getMatchEvents(),info.getId());
        revalidate();

    }

    public void removeMatch(Match info) {
        jPanelMatches.remove(matches.get(info.getIdMatch()));
        viewMatches.deleteMatch(info.getIdMatch());
        matchDetail.addEvents(info.getMatchEvents(),info.getIdMatch());
        revalidate();
    }

    public void removeLeagues() {
        viewLeagues.removeLeagues();
    }

    public void openMatchDetails(MatchInfo info) {
        matchDetail.setMatchData(info.getId(),info.getTeam1Path(),info.getTeam1Name(),info.getScoreTeam1(),info.getTeam2Path(),info.getTeam2Name(),info.getScoreTeam2());
        matchDetail.addEvents(info.getMatchEvents(),info.getId());
        changeCard(MatchDetail.CARD_MATCH_DETAIL);
    }
}

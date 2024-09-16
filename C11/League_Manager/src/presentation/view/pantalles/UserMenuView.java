package presentation.view.pantalles;

import business.model.entities.Match;
import business.model.entities.MatchInfo;
import com.sun.jdi.PrimitiveValue;
import presentation.view.ui_elements.JImagePanel;
import presentation.view.ui_elements.MatchButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class that creates the panel that allows the user move across the different options
 */
public class UserMenuView extends JImagePanel {

    // Components
    private JMenuItem jmiLogout;
    private JMenuItem jmiChangePassword;
    private JMenuItem jmiDeleteAccount;
    private JButton jbViewLeagues;
    private JButton jbMatches;
    private JPanel jPanelMatches;
    private HashMap<String,MatchButton> matches;


    // Constants
    public static final String MATCH_PRESSED = "MATCH_PRESSED_";
    public static final String CARD_MENU_PLAYER = "CARD_MENU_PLAYER";
    public static final String CHANGE_PASSWORD_PRESSED = "CHANGE_PASSWORD_PRESSED";
    public static final String LOGOUT_PRESSED = "LOGOUT_PRESSED";
    public static final String DELETE_ACCOUNT_PRESSED = "DELETE_ACCOUNT_PRESSED";
    public static final String VIEW_LEAGUES_PRESSED = "VIEW_LEAGUES_PRESSED";
    public static final String MATCHES_PRESSED = "MATCHES_PRESSED";

    /**
     * Constructor method
     * Creates the panel with the buttons to move across the different options
     * @param path String that represents the path to the image background
     */
    public UserMenuView(String path) {
        super(path);
        setLayout(new BorderLayout());
        createEastMenu();
        createSouthMatches();
        createCenterOptions();
        matches = new HashMap<>();

        setActionCommands();
    }

    /**
     * Method that registers the controller as listener of the buttons
     * @param l ActionListener that represents the controller
     */
    public void registerController(ActionListener l){

        jbViewLeagues.addActionListener(l);
        jbMatches.addActionListener(l);
        jmiLogout.addActionListener(l);
        jmiChangePassword.addActionListener(l);
        jmiDeleteAccount.addActionListener(l);
    }

    /**
     * Method that add the action commands to the buttons
     */
    private void setActionCommands() {
        jbViewLeagues.setActionCommand(VIEW_LEAGUES_PRESSED);
        jbMatches.setActionCommand(MATCHES_PRESSED);
        jmiLogout.setActionCommand(LOGOUT_PRESSED);
        jmiChangePassword.setActionCommand(CHANGE_PASSWORD_PRESSED);
        jmiDeleteAccount.setActionCommand(DELETE_ACCOUNT_PRESSED);
    }

    /**
     * Method that creates the east panel of the menu
     */
    private void createEastMenu() {
        JPanel jpEast = new JPanel();
        jpEast.setOpaque(false);
        jpEast.setLayout(new BorderLayout());

        ImageIcon image = new ImageIcon("resources/general/profile.png");
        Image img = image.getImage();

        Image newimg = img.getScaledInstance(110, 100,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon newIcon = new ImageIcon(newimg);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu();
        menu.setIcon(newIcon);
        menuBar.setPreferredSize(new Dimension(130,100));
        menuBar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        jmiLogout = new JMenuItem("Logout");
        jmiLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        jmiChangePassword = new JMenuItem("Change Password");
        jmiChangePassword.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        jmiDeleteAccount = new JMenuItem("Delete Account");
        jmiDeleteAccount.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        menu.add(jmiChangePassword);
        menu.add(jmiDeleteAccount);
        menu.add(jmiLogout);

        menuBar.add(menu);
        menuBar.setOpaque(false);

        jpEast.add(menuBar, BorderLayout.NORTH);

        add(jpEast, BorderLayout.EAST);

    }

    /**
     * Method that creates the south panel of the menu
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
     * Method that creates the center panel of the menu
     */
    private void createCenterOptions() {
        // JPanel al center
        JPanel jpcenter = new JPanel();
        jpcenter.setLayout(new GridLayout(1,2));
        jpcenter.setOpaque(false);

        // PANEL esquerra on aniran els títols
        JPanel jpleft = new JPanel();
        jpleft.setOpaque(false);
        jpleft.setLayout(new BoxLayout(jpleft, BoxLayout.Y_AXIS));
        jpleft.setBorder(BorderFactory.createEmptyBorder(20,10,0,600));
        jpcenter.add(jpleft);

        // Panels per cada títol
        jbViewLeagues = new JButton();
        jbViewLeagues.setLayout(new BoxLayout(jbViewLeagues,BoxLayout.X_AXIS));
        jbViewLeagues.setOpaque(false);
        jbViewLeagues.setBorder(BorderFactory.createEmptyBorder(0,10,5,40));
        jbViewLeagues.setOpaque(false);
        jbViewLeagues.setContentAreaFilled(false);
        jbViewLeagues.setBorderPainted(false);
        jbViewLeagues.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));


        jbMatches = new JButton();
        jbMatches.setLayout(new BoxLayout(jbMatches,BoxLayout.X_AXIS));
        jbMatches.setOpaque(false);
        jbMatches.setBorder(BorderFactory.createEmptyBorder(25,0,5,70));
        jbMatches.setOpaque(false);
        jbMatches.setContentAreaFilled(false);
        jbMatches.setBorderPainted(false);
        jbMatches.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JImagePanel jiViewLeagues = new JImagePanel("resources/player_menu/playerViewLeagues.png");
        JImagePanel jiMatches = new JImagePanel("resources/player_menu/playerMatches.png");

        jiViewLeagues.setOpaque(false);
        jiMatches.setOpaque(false);

        jbViewLeagues.add(jiViewLeagues);
        jbMatches.add(jiMatches);

        jpleft.add(jbViewLeagues);
        jpleft.add(jbMatches);
        jpleft.add(Box.createRigidArea(new Dimension(0,500)));
        add(jpcenter, BorderLayout.CENTER);
    }

    /**
     * Method that add a match to the menu
     * @param l ActionListener
     * @param matchInfo MatchInfo
     */
    public void addMatch(ActionListener l, MatchInfo matchInfo) {
        MatchButton matchButton = new MatchButton(matchInfo.getTeam1Path(), matchInfo.getTeam2Path(), matchInfo.getScoreTeam1(), matchInfo.getScoreTeam2());
        matchButton.setPreferredSize(new Dimension(200, 70));
        matchButton.setBorder(BorderFactory.createEmptyBorder(0,5,0,5));
        matchButton.setActionCommand(MATCH_PRESSED + matchInfo.getId());
        matchButton.addActionListener(l);
        this.matches.put(matchInfo.getId(), matchButton);
        jPanelMatches.add(matchButton);
        revalidate();
    }

    /**
     * Method that updates the match info
     * @param info MatchInfo
     */
    public void updateMatchInfo(MatchInfo info) {
        MatchButton matchButton = matches.get(info.getId());
        matchButton.updateScore(info.getScoreTeam1(), info.getScoreTeam2());
        revalidate();
    }

    /**
     * Method that removes a match from the menu
     * @param id String
     */
    public void removeMatch(String id) {
        jPanelMatches.remove(matches.get(id));
        matches.remove(id);
        revalidate();
    }
}

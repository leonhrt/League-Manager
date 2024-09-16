package presentation.view.pantalles;

import business.model.entities.Match;
import business.model.entities.MatchInfo;
import business.model.entities.Player;
import presentation.view.ui_elements.CreateLeagueDialog;
import presentation.view.ui_elements.FileDialog;
import presentation.view.ui_elements.JImagePanel;
import presentation.view.ui_elements.MatchButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class that creates the admin menu view
 */
public class AdminMenuView extends JImagePanel {

    // Constants
    public static final String CARD_MENU_ADMIN = "CARD_MENU_ADMIN";
    public static final String LOGOUT_PRESSED = "LOGOUT_PRESSED";
    public static final String CREATE_TEAM_PRESSED = "CREATE_TEAM_PRESSED";
    public static final String MANAGE_PRESSED = "MANAGE_PRESSED";
    public static final String CREATE_LEAGUE_PRESSED = "CREATE_LEAGUE_PRESSED";
    public static final String VIEW_LEAGUES_PRESSED = "VIEW_LEAGUES_PRESSED";
    public static final String VIEW_MATCHES_PRESSED = "VIEW_MATCHES_PRESSED";
    public static final String MATCH_PRESSED = "MATCH_PRESSED_";

    // Components
    private JButton jbCreateTeam;
    private JButton jbManage;
    private JButton jbCreateLeague;
    private JButton jbViewLeagues;
    private JButton jbMatches;
    private JMenuItem jmiLogout;
    private JPanel jPanelMatches;
    private FileDialog fileDialog;
    private CreateLeagueDialog createLeagueDialog;
    private HashMap<String, MatchButton> matches;

    /**
     * Constructor method
     * Creates the panel with the file dialog and the buttons to manage teams and leagues
     * @param path path to the background image
     * @param fileDialog file dialog to choose the image for the team
     */
    public AdminMenuView(String path, FileDialog fileDialog, CreateLeagueDialog createLeagueDialog) {
        super(path);
        setLayout(new BorderLayout());
        this.fileDialog = fileDialog;
        this.createLeagueDialog = createLeagueDialog;
        createEastMenu();
        createSouthMatches();
        createCenterOptions();

        setActionCommands();

        matches = new HashMap<>();

    }

    /**
     * Method that creates the center panel
     */
    private void createCenterOptions() {

        JPanel jpCenter = new JPanel();
        jpCenter.setLayout(new GridLayout(1,2));
        jpCenter.setOpaque(false);

        // PANEL esquerra on aniran els títols
        JPanel jpCenterLeft = new JPanel();
        jpCenterLeft.setOpaque(false);
        jpCenterLeft.setLayout(new BoxLayout(jpCenterLeft, BoxLayout.Y_AXIS));
        jpCenterLeft.setBorder(BorderFactory.createEmptyBorder(20,10,0,520));
        jpCenter.add(jpCenterLeft);

        // Panels per cada títol
        jbCreateTeam = new JButton();
        jbCreateTeam.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jbCreateTeam.setLayout(new BoxLayout(jbCreateTeam,BoxLayout.X_AXIS));
        jbCreateTeam.setOpaque(false);
        jbCreateTeam.setBorder(BorderFactory.createEmptyBorder(0,10,5,100));
        jbCreateTeam.setContentAreaFilled(false);
        jbCreateTeam.setBorderPainted(false);

        jbManage = new JButton();
        jbManage.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jbManage.setLayout(new BoxLayout(jbManage,BoxLayout.X_AXIS));
        jbManage.setOpaque(false);
        jbManage.setBorder(BorderFactory.createEmptyBorder(25,0,5,70));
        jbManage.setContentAreaFilled(false);
        jbManage.setBorderPainted(false);

        jbCreateLeague = new JButton();
        jbCreateLeague.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jbCreateLeague.setLayout(new BoxLayout(jbCreateLeague,BoxLayout.X_AXIS));
        jbCreateLeague.setOpaque(false);
        jbCreateLeague.setBorder(BorderFactory.createEmptyBorder(25,0,15,70));
        jbCreateLeague.setContentAreaFilled(false);
        jbCreateLeague.setBorderPainted(false);

        jbViewLeagues = new JButton();
        jbViewLeagues.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jbViewLeagues.setLayout(new BoxLayout(jbViewLeagues,BoxLayout.X_AXIS));
        jbViewLeagues.setOpaque(false);
        jbViewLeagues.setBorder(BorderFactory.createEmptyBorder(25,0,5,100));
        jbViewLeagues.setContentAreaFilled(false);
        jbViewLeagues.setBorderPainted(false);

        jbMatches = new JButton();
        jbMatches.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jbMatches.setLayout(new BoxLayout(jbMatches,BoxLayout.X_AXIS));
        jbMatches.setOpaque(false);
        jbMatches.setBorder(BorderFactory.createEmptyBorder(25,0,4,180));
        jbMatches.setContentAreaFilled(false);
        jbMatches.setBorderPainted(false);

        JImagePanel jiCreateTeam = new JImagePanel("resources/admin_menu/adminCreateTeam.png");
        JImagePanel jiManage = new JImagePanel("resources/admin_menu/adminManage.png");
        JImagePanel jiCreateLeague = new JImagePanel("resources/admin_menu/adminCreateLeague.png");
        JImagePanel jiViewLeagues = new JImagePanel("resources/admin_menu/adminAllLeagues.png");
        JImagePanel jiMatches = new JImagePanel("resources/admin_menu/adminMatches.png");

        jiCreateTeam.setOpaque(false);
        jiManage.setOpaque(false);
        jiCreateLeague.setOpaque(false);
        jiViewLeagues.setOpaque(false);
        jiMatches.setOpaque(false);

        jbCreateTeam.add(jiCreateTeam);
        jbManage.add(jiManage);
        jbCreateLeague.add(jiCreateLeague);
        jbViewLeagues.add(jiViewLeagues);
        jbMatches.add(jiMatches);

        jpCenterLeft.add(jbCreateTeam);
        jpCenterLeft.add(jbManage);
        jpCenterLeft.add(jbCreateLeague);
        jpCenterLeft.add(jbViewLeagues);
        jpCenterLeft.add(jbMatches);
        jpCenterLeft.add(Box.createRigidArea(new Dimension(0,150)));


        add(jpCenter, BorderLayout.CENTER);


    }


    /**
     * Method that create the East panel
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

        menu.add(jmiLogout);
        menuBar.add(menu);
        menuBar.setOpaque(false);

        jpEast.add(menuBar, BorderLayout.NORTH);

        add(jpEast, BorderLayout.EAST);


    }

    /**
     * Method that creates the south panel
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
     * Method that set the action commands
     */
    private void setActionCommands() {
        jbCreateTeam.setActionCommand(CREATE_TEAM_PRESSED);
        jbManage.setActionCommand(MANAGE_PRESSED);
        jbCreateLeague.setActionCommand(CREATE_LEAGUE_PRESSED);
        jbViewLeagues.setActionCommand(VIEW_LEAGUES_PRESSED);
        jbMatches.setActionCommand(VIEW_MATCHES_PRESSED);
        jmiLogout.setActionCommand(LOGOUT_PRESSED);
    }

    /**
     * Method that register the controller
     * @param l controller
     */
    public void registerController(ActionListener l){
        //loadMatches(null,l);

        jbCreateTeam.addActionListener(l);
        jbManage.addActionListener(l);
        jbCreateLeague.addActionListener(l);
        jbViewLeagues.addActionListener(l);
        jbMatches.addActionListener(l);
        jmiLogout.addActionListener(l);
        fileDialog.registerController(l);
    }

    /**
     * Method that add the matches info to the panel
     * @param l controller
     * @param matchInfo match info
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
     * Method that update the match info
     * @param info match info
     */
    public void updateMatchInfo(MatchInfo info) {
        MatchButton matchButton = matches.get(info.getId());
        matchButton.updateScore(info.getScoreTeam1(), info.getScoreTeam2());
        revalidate();
        //matchButton.repaint();
    }

    /**
     * Method that remove a match from the panel
     * @param id match id
     */
    public void removeMatch(String id) {
        jPanelMatches.remove(matches.get(id));
        matches.remove(id);
        revalidate();
    }

    /**
     * Method that show the file dialog
     */
    public void showFileDialog(){
        fileDialog.setVisible();
    }

    /**
     * Method that close the file dialog
     */
    public void closeFileDialog() {
        fileDialog.dispose();
    }

    /**
     * Method that ask for a file to the user
     * @return the file id (int)
     */
    public int askFile() {
        return fileDialog.selectFile();
    }

    /**
     * Method that return the selected file
     * @return the file (File)
     */
    public File getSelectedFile() {
        return fileDialog.getFile();
    }

    /**
     * Method that set the message of the file dialog
     * @param text message
     * @param isError true if is an error, false otherwise
     */
    public void setMessageFile(String text, boolean isError){
        fileDialog.setText(text,isError);
    }

    /**
     * Method that show the new players
     * @param new_players list of new players
     */
    public void showNewPlayers(ArrayList<Player> new_players) {
        fileDialog.showNewPlayers(new_players);
    }

    /**
     * Method that return the size of the matches
     * @return the size of the matches (int)
     */

    public void showLeagueForm(){
        createLeagueDialog.setVisible();
    }

    /**
     * Method that reset the league form
     */
    public void resetLeagueForm() {
        createLeagueDialog.resetTextFields();
    }


}

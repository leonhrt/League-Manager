package presentation.view.pantalles;

import presentation.view.ui_elements.JImagePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Class that creates the panel that allows the admin to choose what to manage
 */
public class AdminManage extends JPanel {

    //Components
    private JButton jbTeams;
    private JButton jbLeagues;

    //Constants
    public static final String CARD_MANAGE = "CARD_MANAGE";
    public static final String TEAMS_PRESSED = "TEAMS_PRESSED";
    public static final String LEAGUES_PRESSED = "LEAGUES_PRESSED";


    /**
     * Constructor method
     * Creates the panel with the buttons to manage teams and leagues
     */
    public AdminManage() {
        setBackground(Color.orange);
        setBorder(BorderFactory.createEmptyBorder(180, 70, 140, 70));
        setLayout(new BorderLayout());
        setOpaque(false);

        JLabel jlQuestion = new JLabel("WHAT WOULD YOU LIKE TO MANAGE?", JLabel.CENTER);
        jlQuestion.setFont(new Font("Inter", Font.BOLD, 37));
        jlQuestion.setForeground(Color.WHITE);
        jlQuestion.setBorder(BorderFactory.createEmptyBorder(0, 0, 60, 0));
        add(jlQuestion, BorderLayout.NORTH);

        JPanel jpManage = new JPanel();
        jpManage.setOpaque(false);
        jpManage.setLayout(new GridLayout(1, 2, 50, 0));
        add(jpManage, BorderLayout.CENTER);

        jbTeams = new JButton();
        jbTeams.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.5f));
        jbTeams.setRolloverEnabled(false);
        jbTeams.setLayout(new BorderLayout());
        jpManage.add(jbTeams);
        jbTeams.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));
        jbTeams.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));


        JLabel jlTeams = new JLabel("TEAMS", JLabel.CENTER);
        jlTeams.setFont(new Font("Inter", Font.BOLD, 30));
        jlTeams.setForeground(Color.WHITE);
        jlTeams.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jbTeams.add(jlTeams, BorderLayout.NORTH);

        JImagePanel jiTeams = new JImagePanel("resources/admin_menu/teams.png");
        jiTeams.setOpaque(false);
        jbTeams.add(jiTeams, BorderLayout.CENTER);

        jbLeagues = new JButton();
        jbLeagues.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.5f));
        jbLeagues.setRolloverEnabled(false);
        jbLeagues.setLayout(new BorderLayout());
        jpManage.add(jbLeagues);
        jbLeagues.setBorder(BorderFactory.createEmptyBorder(10, 40, 20, 15));
        jbLeagues.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));


        JLabel jlLeagues = new JLabel("LEAGUES", JLabel.CENTER);
        jlLeagues.setFont(new Font("Inter", Font.BOLD, 30));
        jlLeagues.setForeground(Color.WHITE);
        jlLeagues.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jbLeagues.add(jlLeagues, BorderLayout.NORTH);

        JImagePanel jiLeagues = new JImagePanel("resources/admin_menu/leagues.png");
        jiLeagues.setOpaque(false);
        jbLeagues.add(jiLeagues, BorderLayout.CENTER);

        setActionCommands();
    }

    /**
     * Method that sets the action commands for the buttons
     */
    private void setActionCommands() {
        jbTeams.setActionCommand(TEAMS_PRESSED);
        jbLeagues.setActionCommand(LEAGUES_PRESSED);
    }

    /**
     * Method that registers the controller to the buttons
     * @param l controller to be registered
     */
    public void registerController(ActionListener l){
        jbTeams.addActionListener(l);
        jbLeagues.addActionListener(l);
    }
}

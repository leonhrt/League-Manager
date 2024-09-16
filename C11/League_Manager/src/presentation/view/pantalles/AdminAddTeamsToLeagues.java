package presentation.view.pantalles;

import business.model.entities.DBTeam;
import presentation.view.ui_elements.ElementCheck;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * This class is a JPanel that contains a scrollPane with all the teams in the database.
 */
public class AdminAddTeamsToLeagues extends JPanel {

    //Constants
    public static final String CARD_CREATE_LEAGUE = "CARD_CREATE_LEAGUE";
    public static final String CREATE_LEAGUE_PRESSED = "CREATE_LEAGUE_PRESSED";

    //Components
    private JPanel jpScrollTeams;
    private ArrayList<ElementCheck> checks;
    private JButton jbCreateLeague;

    /**
     * Constructor of the class. It creates the panel.
     */
    public AdminAddTeamsToLeagues() {
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(100,0,50,0));

        JPanel jpNorth = new JPanel();
        jpNorth.setLayout(new BoxLayout(jpNorth,BoxLayout.X_AXIS));
        jpNorth.setOpaque(false);

        JLabel jlSelect = new JLabel("SELECT TEAMS IN LEAGUE (MIN 2 TEAMS)");
        jlSelect.setFont(new Font("Arial", Font.BOLD, 30));
        jlSelect.setForeground(new Color(27,34,76));
        jlSelect.setBorder(BorderFactory.createEmptyBorder(0,0,20,0));

        jbCreateLeague = new JButton("CREATE LEAGUE");
        jbCreateLeague.setBackground(new Color(240,184,67));
        jbCreateLeague.setForeground(Color.WHITE);
        jbCreateLeague.setBorder(BorderFactory.createLineBorder(new Color(27,34,76), 2));
        jbCreateLeague.setActionCommand(CREATE_LEAGUE_PRESSED);

        jpNorth.add(jlSelect);
        jpNorth.add(Box.createHorizontalGlue());
        jpNorth.add(jbCreateLeague);

        add(jpNorth, BorderLayout.NORTH);

        scrollPaneTeams();
    }

    /**
     * This method creates the scrollPane with all the teams in the database.
     */
    private void scrollPaneTeams() {
        checks = new ArrayList<>();

        jpScrollTeams = new JPanel();
        jpScrollTeams.setOpaque(false);


        JScrollPane jspTeams = new JScrollPane(jpScrollTeams, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jspTeams.setOpaque(false);
        jspTeams.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        jspTeams.getViewport().setOpaque(false);

        jpScrollTeams.setLayout(new BoxLayout(jpScrollTeams, BoxLayout.Y_AXIS));
        add(jspTeams,BorderLayout.CENTER);

    }

    /**
     * This method loads the teams in the scrollPane.
     * @param teams ArrayList of teams to load.
     */
    public void loadTeams(ArrayList<DBTeam> teams){
        jpScrollTeams.removeAll();
        if(teams.size() != 0) {
            jbCreateLeague.setVisible(true);
            checks = new ArrayList<>();
            for (DBTeam team : teams) {
                ElementCheck check = new ElementCheck(team.getImagePath(), team.getName());
                check.setPreferredSize(new Dimension(900, 150));
                checks.add(check);
                jpScrollTeams.add(check);
                jpScrollTeams.add(Box.createRigidArea(new Dimension(0, 20)));
            }
            for (int i = teams.size(); i < 4; i++) {

                jpScrollTeams.add(Box.createRigidArea(new Dimension(0, 150)));
            }
        }
        else{
            jbCreateLeague.setVisible(false);
            JLabel jlNoTeams = new JLabel("No teams in database");
            jlNoTeams.setFont(new Font("Arial", Font.BOLD, 25));
            jlNoTeams.setForeground(Color.black);
            jpScrollTeams.add(jlNoTeams);
        }

        revalidate();

    }

    /**
     * This method registers the controller to the buttons.
     * @param l Controller to register.
     */
    public void registerController(ActionListener l){
        jbCreateLeague.addActionListener(l);
    }

    /**
     * This method returns the teams selected by the user.
     * @return ArrayList of teams selected by the user.
     */
    public ArrayList<String> getSelectedTeams() {
        ArrayList<String> teamsToDelete = new ArrayList<>();
        for (int i = 0; i < checks.size(); i++) {
            if (checks.get(i).isSelected()){
                teamsToDelete.add(checks.get(i).getName());
            }
        }
        return teamsToDelete;
    }

    /**
     * This method loads a new team in the scrollPane.
     * @param team Team to load.
     */
    public void loadNewTeam(DBTeam team) {
        jpScrollTeams.removeAll();
        for (int i = 0; i < checks.size(); i++) {
            jpScrollTeams.add(checks.get(i));
            jpScrollTeams.add(Box.createRigidArea(new Dimension(0, 20)));

        }

        ElementCheck check = new ElementCheck(team.getImagePath(), team.getName());
        check.setPreferredSize(new Dimension(900, 150));
        checks.add(check);
        jpScrollTeams.add(check);
        jpScrollTeams.add(Box.createRigidArea(new Dimension(0, 20)));

        for (int i = checks.size(); i < 4; i++) {
            jpScrollTeams.add(Box.createRigidArea(new Dimension(0, 150)));
        }

        revalidate();


    }

    /**
     * This method removes a team from the scrollPane.
     * @param teamName Name of the team to remove.
     */
    public void removeTeams(String teamName) {
        for (int i = 0; i <checks.size() ; i++) {
            if (checks.get(i).getName().equals(teamName)){
                jpScrollTeams.remove(checks.get(i));
                checks.remove(checks.get(i));
            }
        }


        jpScrollTeams.removeAll();
        for (ElementCheck team : checks) {
            jpScrollTeams.add(team);
            jpScrollTeams.add(Box.createRigidArea(new Dimension(0, 20)));

        }
        revalidate();

    }
}

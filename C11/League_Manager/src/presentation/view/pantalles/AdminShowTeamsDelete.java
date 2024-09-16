package presentation.view.pantalles;

import business.model.entities.DBTeam;
import org.checkerframework.checker.units.qual.C;
import presentation.view.ui_elements.ElementCheck;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * This class is a JPanel that is only used by the admin. It contains a scrollPane with all the teams in the database
 * that can be deleted.
 */
public class AdminShowTeamsDelete extends JPanel {

    //Constants
    public static final String CARD_TEAMS_DELETE = "CARD_TEAMS_DELETE";
    public static final String DELETE_TEAMS_PRESSED = "DELETE_TEAMS_PRESSED";

    //Components
    private JPanel jpScrollTeams;
    private JButton jbDelete;
    private ArrayList<ElementCheck> checks;
    private JLabel jlNoTeams;

    /**
     * Constructor of the class. It creates the panel.
     */
    public AdminShowTeamsDelete() {
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(100,0,50,0));

        JPanel jpNorth = new JPanel();
        jpNorth.setLayout(new BoxLayout(jpNorth,BoxLayout.X_AXIS));
        jpNorth.setOpaque(false);

        JLabel jlSelect = new JLabel("SELECT TEAMS TO DELETE");
        jlSelect.setFont(new Font("Arial", Font.BOLD, 40));
        jlSelect.setForeground(new Color(27,34,76));
        jlSelect.setBorder(BorderFactory.createEmptyBorder(0,0,20,0));

        jbDelete = new JButton("DELETE");
        jbDelete.setBackground(new Color(240,184,67));
        jbDelete.setForeground(Color.WHITE);
        jbDelete.setBorder(BorderFactory.createLineBorder(new Color(27,34,76), 2));
        jbDelete.setActionCommand(DELETE_TEAMS_PRESSED);

        jpNorth.add(jlSelect);
        jpNorth.add(Box.createHorizontalGlue());
        jpNorth.add(jbDelete);

        add(jpNorth, BorderLayout.NORTH);

        scrollPaneTeams();
    }

    /**
     * This method creates the scrollPane that contains all the teams in the database.
     */
    private void scrollPaneTeams() {
        checks = new ArrayList<>();

        jpScrollTeams = new JPanel();
        jpScrollTeams.setOpaque(false);
        jlNoTeams = new JLabel("No teams in database");
        jlNoTeams.setFont(new Font("Arial", Font.BOLD, 25));
        jlNoTeams.setAlignmentX(CENTER_ALIGNMENT);
        jlNoTeams.setForeground(Color.black);

        JScrollPane jspTeams = new JScrollPane(jpScrollTeams, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jspTeams.setOpaque(false);
        jspTeams.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        jspTeams.getViewport().setOpaque(false);

        jpScrollTeams.setLayout(new BoxLayout(jpScrollTeams, BoxLayout.Y_AXIS));
        add(jspTeams,BorderLayout.CENTER);

    }

    /**
     * This method loads all the teams in the database in the scrollPane.
     * @param teams ArrayList of DBTeam that contains all the teams in the database.
     */
    public void loadTeams(ArrayList<DBTeam> teams){
        jpScrollTeams.removeAll();
        checks = new ArrayList<>();
        for (DBTeam team : teams) {
            ElementCheck check = new ElementCheck(team.getImagePath(), team.getName());
            check.setPreferredSize(new Dimension(900, 150));
            checks.add(check);
            jpScrollTeams.add(check);
            jpScrollTeams.add(Box.createRigidArea(new Dimension(0, 20)));
        }
        checkNumTeams();
        for (int i = teams.size(); i < 4; i++) {

            jpScrollTeams.add(Box.createRigidArea(new Dimension(0, 150)));
        }

        revalidate();

    }

    /**
     * This method registers the controller of the panel.
     * @param l ActionListener that controls the panel.
     */
    public void registerController(ActionListener l){
        jbDelete.addActionListener(l);
    }

    /**
     * Method that return the name of the teams that are selected to be deleted.
     * @return ArrayList of String that contains the name of the teams that are selected to be deleted.
     */
    public ArrayList<String> getSelectedTeams() {
        ArrayList<String> teamsToDelete = new ArrayList<>();
        ArrayList<ElementCheck> deletedChecks = new ArrayList<>();
        for (ElementCheck team : checks) {
            if (team.isSelected()) {
                teamsToDelete.add(team.getName());
                deletedChecks.add(team);
                jpScrollTeams.remove(team);
            }
        }
        for (ElementCheck deletedCheck : deletedChecks) {
            checks.remove(deletedCheck);
        }
        revalidate();
        jpScrollTeams.removeAll();
        for (ElementCheck check : checks) {
            jpScrollTeams.add(check);
            jpScrollTeams.add(Box.createRigidArea(new Dimension(0, 20)));
            revalidate();
        }
        checkNumTeams();
        for (int i = checks.size(); i < 4; i++) {
            jpScrollTeams.add(Box.createRigidArea(new Dimension(0, 150)));
        }
        revalidate();
        return teamsToDelete;
    }

    /**
     * This method loads a new team in the scrollPane.
     * @param team DBTeam that contains the team to be loaded.
     */
    public void loadNewTeam(DBTeam team) {
        jpScrollTeams.removeAll();

        for (ElementCheck check : checks) {
            jpScrollTeams.add(check);
            jpScrollTeams.add(Box.createRigidArea(new Dimension(0, 20)));

        }
        ElementCheck check = new ElementCheck(team.getImagePath(), team.getName());
        check.setPreferredSize(new Dimension(900, 150));
        checks.add(check);
        jpScrollTeams.add(check);
        jpScrollTeams.add(Box.createRigidArea(new Dimension(0, 20)));

        checkNumTeams();
        for (int i = checks.size(); i < 4; i++) {
            jpScrollTeams.add(Box.createRigidArea(new Dimension(0, 150)));
        }

        revalidate();
    }

    /**
     * This method checks if there are teams in the database. If there are no teams, it shows a message.
     */
    public void checkNumTeams() {
        if(checks.isEmpty()){
            jbDelete.setVisible(false);
            jpScrollTeams.add(jlNoTeams);
        }
        else{
            jbDelete.setVisible(true);
            if(jpScrollTeams.isAncestorOf(jlNoTeams)){
                jpScrollTeams.remove(jlNoTeams);
            }
        }
    }

}

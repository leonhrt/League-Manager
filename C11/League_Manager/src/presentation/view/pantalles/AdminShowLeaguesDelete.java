package presentation.view.pantalles;

import business.model.entities.League;
import presentation.view.ui_elements.ElementCheck;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;


/**
 * This class is a JPanel that is only used by the admin. It contains a scrollPane with all the leagues in the database
 * that can be deleted.
 */
public class AdminShowLeaguesDelete extends JPanel {

    //Constants
    public static final String CARD_LEAGUES_DELETE = "CARD_LEAGUES_DELETE";
    public static final String DELETE_LEAGUES_PRESSED = "DELETE_LEAGUES_PRESSED";

    //Components
    private JPanel jpScrollLeagues;
    private ArrayList<ElementCheck> checks;
    private JButton jbDelete;
    private JLabel jlNoLeagues;

    /**
     * Constructor of the class. It creates the panel.
     */
    public AdminShowLeaguesDelete() {
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(100,0,50,0));

        JPanel jpNorth = new JPanel();
        jpNorth.setLayout(new BoxLayout(jpNorth,BoxLayout.X_AXIS));
        jpNorth.setOpaque(false);

        JLabel jlSelect = new JLabel("SELECT LEAGUES TO DELETE");
        jlSelect.setFont(new Font("Arial", Font.BOLD, 40));
        jlSelect.setForeground(new Color(27,34,76));
        jlSelect.setBorder(BorderFactory.createEmptyBorder(0,0,20,0));

        jbDelete = new JButton("DELETE");
        jbDelete.setBackground(new Color(240,184,67));
        jbDelete.setForeground(Color.WHITE);
        jbDelete.setBorder(BorderFactory.createLineBorder(new Color(27,34,76), 2));
        jbDelete.setActionCommand(DELETE_LEAGUES_PRESSED);

        jpNorth.add(jlSelect);
        jpNorth.add(Box.createHorizontalGlue());
        jpNorth.add(jbDelete);

        add(jpNorth, BorderLayout.NORTH);
        scrollPaneLeagues();
    }

    /**
     * This method creates the scrollPane with all the leagues in the database.
     */
    private void scrollPaneLeagues() {
        jlNoLeagues = new JLabel("No leagues in database");
        jlNoLeagues.setAlignmentX(CENTER_ALIGNMENT);
        jlNoLeagues.setFont(new Font("Arial", Font.BOLD, 25));
        jlNoLeagues.setForeground(Color.black);

        checks = new ArrayList<>();

        jpScrollLeagues = new JPanel();
        jpScrollLeagues.setOpaque(false);

        JScrollPane jspTeams = new JScrollPane(jpScrollLeagues, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jspTeams.setOpaque(false);
        jspTeams.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        jspTeams.getViewport().setOpaque(false);

        jpScrollLeagues.setLayout(new BoxLayout(jpScrollLeagues, BoxLayout.Y_AXIS));
        add(jspTeams,BorderLayout.CENTER);

    }

    /**
     * This method loads all the leagues in the database in the scrollPane.
     * @param leagues ArrayList of leagues in the database.
     */
    public void loadLeagues(ArrayList<League> leagues){
        jpScrollLeagues.removeAll();
        checks = new ArrayList<>();
        for (League league : leagues) {
            ElementCheck check = new ElementCheck(league.getImagePath(), league.getName());
            check.setPreferredSize(new Dimension(900, 150));
            checks.add(check);
            jpScrollLeagues.add(check);
            jpScrollLeagues.add(Box.createRigidArea(new Dimension(0, 20)));
        }
        checkNumLeagues();
        for (int i = leagues.size(); i < 4; i++) {
            jpScrollLeagues.add(Box.createRigidArea(new Dimension(0,150)));
        }
        revalidate();

    }

    /**
     * This method registers the controller of the panel.
     * @param l Controller of the panel.
     */
    public void registerController(ActionListener l){
        jbDelete.addActionListener(l);
    }

    /**
     * This method returns the leagues selected to be deleted.
     * @return ArrayList of leagues selected to be deleted.
     */
    public ArrayList<String> getSelectedLeagues() {
        ArrayList<String> teamsToDelete = new ArrayList<>();
        ArrayList<ElementCheck> deletedChecks = new ArrayList<>();
        for (ElementCheck team : checks) {
            if (team.isSelected()) {
                teamsToDelete.add(team.getName());
                deletedChecks.add(team);
                jpScrollLeagues.remove(team);
            }
        }
        for (ElementCheck deletedCheck : deletedChecks) {
            checks.remove(deletedCheck);
        }
        revalidate();
        jpScrollLeagues.removeAll();
        for (ElementCheck check : checks) {
            jpScrollLeagues.add(check);
            jpScrollLeagues.add(Box.createRigidArea(new Dimension(0, 20)));
            revalidate();
        }
        checkNumLeagues();
        for (int i = checks.size(); i < 4; i++) {
            jpScrollLeagues.add(Box.createRigidArea(new Dimension(0, 150)));
        }
        revalidate();
        return teamsToDelete;
    }

    /**
     * This method loads a new league in the scrollPane.
     * @param league League to be loaded.
     */
    public void loadNewLeague(League league) {
        jpScrollLeagues.removeAll();

        for (ElementCheck check : checks) {
            jpScrollLeagues.add(check);
            jpScrollLeagues.add(Box.createRigidArea(new Dimension(0, 20)));
        }
        ElementCheck check = new ElementCheck(league.getImagePath(), league.getName());
        check.setPreferredSize(new Dimension(900, 150));
        checks.add(check);
        jpScrollLeagues.add(check);
        jpScrollLeagues.add(Box.createRigidArea(new Dimension(0, 20)));

        checkNumLeagues();

        for (int i = checks.size(); i < 4; i++) {
            jpScrollLeagues.add(Box.createRigidArea(new Dimension(0, 150)));
        }

        revalidate();
    }

    /**
     * Method that checks if there are leagues in the database. If there are not, it shows a message.
     */
    private void checkNumLeagues() {
        if(checks.isEmpty()){
            jbDelete.setVisible(false);
            jpScrollLeagues.add(jlNoLeagues);
        }
        else{
            jbDelete.setVisible(true);
            if(jpScrollLeagues.isAncestorOf(jlNoLeagues)){
                jpScrollLeagues.remove(jlNoLeagues);
            }
        }
    }
}

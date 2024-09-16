package presentation.view.pantalles;

import business.model.entities.League;
import presentation.view.ui_elements.LeagueBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Class that contains the panel that allows the user to view the leagues
 */
public class ViewLeagues extends JPanel {

    // Constants
    public static final String CARD_VIEW_LEAGUES = "CARD_VIEW_LEAGUES";
    public static final String LEAGUE_PRESSED_ = "LEAGUE_PRESSED_";

    // Attributes
    private JPanel jpScrollLeaguesHorizontal;
    private HashMap<String, LeagueBox> leagueBoxes;
    private JLabel jlNoLeagues;

    /**
     * Constructor method
     */
    public ViewLeagues() {
        leagueBoxes = new HashMap<>();
        setLayout(new BorderLayout());
        setOpaque(false);
        JPanel jPanelLeagues = new JPanel(new BorderLayout());
        jPanelLeagues.setOpaque(false);
        add(jPanelLeagues, BorderLayout.CENTER);
        leagueBoxes = new HashMap<>();
        scrollLeaguesHorizontal();

    }

    /**
     * Method that creates the horizontal scroll panel that contains the leagues
     */
    private void scrollLeaguesHorizontal(){
        jpScrollLeaguesHorizontal = new JPanel();
        jpScrollLeaguesHorizontal.setOpaque(false);
        jpScrollLeaguesHorizontal.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.5f));
        jpScrollLeaguesHorizontal.setBorder(BorderFactory.createEmptyBorder(5,0,0,10));

        jlNoLeagues = new JLabel("No leagues in database");
        jlNoLeagues.setFont(new Font("Arial", Font.BOLD, 25));
        jlNoLeagues.setForeground(Color.black);

        JScrollPane jspMatches = new JScrollPane(jpScrollLeaguesHorizontal, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jspMatches.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
        jspMatches.setOpaque(false);
        jspMatches.getViewport().setOpaque(false);
        jspMatches.setBorder(BorderFactory.createEmptyBorder(200,0,0,0));

        add(jspMatches,BorderLayout.CENTER);
    }

    /**
     * Method that loads the leagues in the horizontal scroll panel
     * @param leagues is the list of leagues
     * @param l is the action listener
     */
    public void loadLeagues(ArrayList<League> leagues,ActionListener l){
        jpScrollLeaguesHorizontal.removeAll();
        for (League league : leagues) {
            LeagueBox leagueBox = new LeagueBox(league.getImagePath(), league.getName(), league.getJourney(), league.getTeamsID().size());
            leagueBox.setRolloverEnabled(false);
            leagueBox.setActionCommand(LEAGUE_PRESSED_+league.getId());
            leagueBox.addActionListener(l);
            leagueBoxes.put(league.getId(),leagueBox);
            jpScrollLeaguesHorizontal.add(leagueBox);
            jpScrollLeaguesHorizontal.add(Box.createRigidArea(new Dimension(15,5)));
        }

        checkNumLeagues();

        for (int i = leagues.size(); i < 4; i++) {
            jpScrollLeaguesHorizontal.add(Box.createRigidArea(new Dimension(0,150)));
        }

        revalidate();
    }

    /**
     * Method that updates the journey of a league
     * @param leagueId is the id of the league
     * @param newJourney is the new journey
     */
    public void updateLeagueJourney(String leagueId, String newJourney) {
        if(!leagueBoxes.isEmpty()) {
            leagueBoxes.get(leagueId).setJourney(newJourney);
        }
    }

    /**
     * Method that loads a new league in the horizontal scroll panel
     * @param league is the league
     * @param l is the action listener
     */
    public void loadNewLeague(League league,ActionListener l) {
        LeagueBox leagueBox = new LeagueBox(league.getImagePath(), league.getName(), league.getJourney(), league.getTeamsID().size());
        leagueBox.setRolloverEnabled(false);
        leagueBox.setActionCommand(LEAGUE_PRESSED_+league.getId());
        leagueBox.addActionListener(l);
        leagueBoxes.put(league.getId(),leagueBox);
        jpScrollLeaguesHorizontal.add(leagueBox);
        jpScrollLeaguesHorizontal.add(Box.createRigidArea(new Dimension(15,5)));
        checkNumLeagues();
        revalidate();
    }

    /**
     * Method that removes a league from the horizontal scroll panel
     * @param name is the name of the league
     */
    public void removeLeague(String name){
        Iterator<Map.Entry<String, LeagueBox>> iterator = leagueBoxes.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, LeagueBox> entry = iterator.next();
            LeagueBox league = entry.getValue();
            if (league.getTeamName().equals(name)) {
                iterator.remove();
                jpScrollLeaguesHorizontal.remove(league);
                revalidate();
            }
        }
        checkNumLeagues();
        revalidate();

    }

    /**
     * Method that checks if there are leagues in the horizontal scroll panel
     */
    private void checkNumLeagues() {
        if(leagueBoxes.isEmpty()){
            jpScrollLeaguesHorizontal.add(jlNoLeagues);
        }
        else{
            if(jpScrollLeaguesHorizontal.isAncestorOf(jlNoLeagues)){
                jpScrollLeaguesHorizontal.remove(jlNoLeagues);
            }
        }
    }

    public void removeLeagues() {
        jpScrollLeaguesHorizontal.removeAll();
    }
}

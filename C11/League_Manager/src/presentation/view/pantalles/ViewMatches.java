package presentation.view.pantalles;

import business.model.entities.MatchInfo;
import presentation.view.ui_elements.ElementCheck;
import presentation.view.ui_elements.MatchBox;
import presentation.view.ui_elements.MatchButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;

/**
 * Class that creates the panel that allows the user view the matches
 */
public class ViewMatches extends JPanel {

    // Constants
    public static final String CARD_VIEW_MATCHES = "CARD_VIEW_MATCHES";

    // Attributes
    private HashMap<String, MatchBox> matches;
    private JPanel jpScrollMatches;

    /**
     * Constructor method of the class
     */
    public ViewMatches() {
        matches = new HashMap<>();
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(100,0,50,0));

        ScrollPaneTeams();
    }

    /**
     * Method that creates the scroll pane that contains the teams
     */
    private void ScrollPaneTeams() {
        jpScrollMatches = new JPanel();
        jpScrollMatches.setOpaque(false);
        jpScrollMatches.setLayout(new BoxLayout(jpScrollMatches, BoxLayout.Y_AXIS));

        JScrollPane jspTeams = new JScrollPane(jpScrollMatches, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jspTeams.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        jspTeams.setOpaque(false);
        jspTeams.getViewport().setOpaque(false);

        add(jspTeams, BorderLayout.CENTER);
    }

    /**
     * Method that adds a match to the panel
     * @param matchInfo MatchInfo object that contains the information of the match
     * @param l ActionListener object that will be added to the match
     */
    public void addMatch(MatchInfo matchInfo, ActionListener l){
        jpScrollMatches.removeAll();

        for(MatchBox match:matches.values()){
            jpScrollMatches.add(match);
            jpScrollMatches.add(Box.createRigidArea(new Dimension(0,20)));
        }
        MatchBox box = new MatchBox(matchInfo.getTeam1Path(),matchInfo.getTeam1Name(),matchInfo.getScoreTeam1(), matchInfo.getTeam2Path(),matchInfo.getTeam2Name(),matchInfo.getScoreTeam2());
        box.setActionCommand(AdminMenuView.MATCH_PRESSED+matchInfo.getId());
        box.addActionListener(l);
        box.setPreferredSize(new Dimension(900,150));
        matches.put(matchInfo.getId(),box);
        jpScrollMatches.add(box);
        jpScrollMatches.add(Box.createRigidArea(new Dimension(0, 20)));

        for (int i = matches.size(); i < 4; i++) {
            jpScrollMatches.add(Box.createRigidArea(new Dimension(0, 150)));
        }
        revalidate();
    }

    /**
     * Method that deletes a match from the panel
     * @param id String that contains the id of the match
     */
    public void deleteMatch(String id){
        jpScrollMatches.remove(matches.get(id));
        matches.remove(id);

        if(matches.size() == 0){
            jpScrollMatches.removeAll();
        }

        revalidate();
    }

    /**
     * Method that updates the score of a match
     * @param info MatchInfo object that contains the information of the match
     */
    public void updateScore(MatchInfo info) {
        matches.get(info.getId()).setScore(info.getScoreTeam1(),info.getScoreTeam2());
    }
}

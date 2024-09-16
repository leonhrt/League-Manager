package presentation.view.pantalles;

import business.model.entities.MatchInfo;
import presentation.view.ui_elements.MatchBox;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

/**
 * This class is a JPanel that is used to show the details of a match.
 */
public class MatchDetail extends JPanel {

    //Constants
    public static final String CARD_MATCH_DETAIL = "CARD_MATCH_DETAIL";

    //Components
    private MatchBox matchBox;
    private JPanel jpScrollMatches;
    private String currentId;
    private JScrollBar jsbEvents;

    /**
     * Constructor of the class. It creates the panel.
     */
    public MatchDetail(){
        setOpaque(false);
        setLayout(new BorderLayout());
        matchBox = new MatchBox("resources/added_files/imgonline-com-ua-ReplaceColor-2wDSH5R275ib-removebg-preview.png", "A", 0, "resources/added_files/imgonline-com-ua-ReplaceColor-2wDSH5R275ib-removebg-preview.png", "B", 7);
        add(matchBox,BorderLayout.NORTH);

        jpScrollMatches = new JPanel();
        jpScrollMatches.setBackground(Color.LIGHT_GRAY);
        JScrollPane jScrollPane = new JScrollPane(jpScrollMatches, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane.setOpaque(false);
        jScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        jScrollPane.getViewport().setOpaque(false);
        jsbEvents = jScrollPane.getVerticalScrollBar();
        jScrollPane.setBorder(BorderFactory.createEmptyBorder(30,0,0,0));

        jpScrollMatches.setLayout(new BoxLayout(jpScrollMatches, BoxLayout.Y_AXIS));
        add(jScrollPane,BorderLayout.CENTER);

    }

    /**
     * This method updates the score of the match.
     * @param matchInfo MatchInfo object with the new score.
     */
    public void updateScore(MatchInfo matchInfo){
        if(Objects.equals(matchInfo.getId(), currentId)){
            matchBox.setScore(matchInfo.getScoreTeam1(),matchInfo.getScoreTeam2());
        }
        revalidate();
    }

    /**
     * This method adds the events of the match to the panel.
     * @param events ArrayList of Strings with the events.
     * @param id String with the id of the match.
     */
    public void addEvents(ArrayList<String> events,String id){
        if(Objects.equals(id, currentId)) {
            jpScrollMatches.removeAll();
            for (String event : events) {
                JLabel jlEvent = new JLabel(event, SwingConstants.CENTER);
                jlEvent.setFont(new Font("Arial", Font.PLAIN, 18));
                jpScrollMatches.add(jlEvent);
                jpScrollMatches.add(Box.createRigidArea(new Dimension(0, 40)));
                revalidate();
                jsbEvents.setValue(jsbEvents.getMaximum());
            }
        }
    }

    /**
     * This method sets the data of the match.
     * @param id String with the id of the match.
     * @param path1 String with the path of the image of the first team.
     * @param team1 String with the name of the first team.
     * @param score1 int with the score of the first team.
     * @param path2 String with the path of the image of the second team.
     * @param team2 String with the name of the second team.
     * @param score2 int with the score of the second team.
     */
    public void setMatchData(String id ,String path1, String team1, int score1, String path2, String team2, int score2){
        remove(matchBox);
        matchBox = new MatchBox(path1, team1, score1, path2, team2, score2);
        currentId = id;
        add(matchBox,BorderLayout.NORTH);
        revalidate();

    }
}

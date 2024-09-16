package presentation.view.ui_elements;


import javax.swing.*;
import java.awt.*;

/**
 * Class that creates a match box
 */
public class MatchBox extends JButton {

    // Constants
    private JLabel jlScore;
    private JLabel jlTeam1;
    private JLabel jlTeam2;

    /**
     * Constructor method void
     */
    public MatchBox(){}

    /**
     * Constructor method
     * @param path1 path of the image of the first team
     * @param name1 name of the first team
     * @param score1 score of the first team
     * @param path2 path of the image of the second team
     * @param name2 name of the second team
     * @param score2 score of the second team
     */
    public MatchBox(String path1, String name1, int score1,String path2, String name2, int score2) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(new Color(240,184,67), 7));
        setOpaque(false);

        JImagePanel jiImage1 = new JImagePanel(path1);
        jiImage1.setSize(new Dimension(80,80));
        jiImage1.setBorder(BorderFactory.createEmptyBorder(30,30,30,100));
        add(jiImage1, BorderLayout.WEST);

        JPanel jPanelInfo = new JPanel();
        jPanelInfo.setLayout(new BorderLayout());
        jPanelInfo.setBackground(Color.LIGHT_GRAY);
        jPanelInfo.setBorder(BorderFactory.createEmptyBorder(0,40,0,100));

        jlTeam1 = new JLabel(name1);
        jlTeam1.setFont(new Font("Arial", Font.BOLD,25));
        jlTeam1.setForeground(Color.WHITE);
        jlTeam2 = new JLabel(name2);
        jlTeam2.setFont(new Font("Arial", Font.BOLD,25));
        jlTeam2.setForeground(Color.WHITE);
        jlScore = new JLabel(score1 + " - " + score2, SwingConstants.CENTER);
        jlScore.setFont(new Font("Arial", Font.BOLD,18));
        jlScore.setForeground(Color.WHITE);


        jPanelInfo.add(jlTeam1, BorderLayout.WEST);
        jPanelInfo.add(jlTeam2, BorderLayout.EAST);
        jPanelInfo.add(jlScore, BorderLayout.SOUTH);

        add(jPanelInfo, BorderLayout.CENTER);

        JImagePanel jiImage2 = new JImagePanel(path2);
        jiImage2.setSize(new Dimension(80,80));
        jiImage2.setBorder(BorderFactory.createEmptyBorder(30,30,30,100));
        add(jiImage2, BorderLayout.EAST);
    }

    /**
     * Method that sets the score of the match
     * @param score1 score of the first team
     * @param score2 score of the second team
     */
    public void setScore(int score1, int score2){
        jlScore.setText(score1 + " - " + score2);
    }

}

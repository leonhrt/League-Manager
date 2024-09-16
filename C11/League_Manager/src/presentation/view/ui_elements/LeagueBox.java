package presentation.view.ui_elements;

import javax.swing.*;
import java.awt.*;

/**
 * Class that creates a box with the information of a league
 */
public class LeagueBox extends JButton {

    // Components
    private JLabel jLabelJourney;
    private JLabel jlName;
    private String journey;

    /**
     * Constructor method
     * @param path String that contains the path of the image
     * @param name String that contains the name of the league
     * @param journey String that contains the journey of the league
     * @param teams int that contains the number of teams of the league
     */
    public LeagueBox(String path, String name, String journey, int teams) {
        this.journey = journey;

        setBackground(new Color(1.0f, 1.0f, 1.0f, 0.5f));
        setPreferredSize(new Dimension(250, 350));
        setLayout(new BorderLayout());

        jlName = new JLabel(name, SwingConstants.CENTER);
        jlName.setFont(new Font("Inter", Font.BOLD, 25));
        //jlName.setBorder(BorderFactory.createEmptyBorder(0,20,0,0));
        add(jlName, BorderLayout.NORTH);

        JImagePanel jiLogo = new JImagePanel(path);
        add(jiLogo, BorderLayout.CENTER);
        jiLogo.setOpaque(false);


        JPanel jpInfo = new JPanel(new BorderLayout());
        jpInfo.setOpaque(false);
        jpInfo.setBorder(BorderFactory.createEmptyBorder(20,0,0,0));
        add(jpInfo, BorderLayout.SOUTH);

        jLabelJourney = new JLabel("J." + journey, JLabel.CENTER);
        jLabelJourney.setFont(new Font("Inter", Font.BOLD, 17));

        jpInfo.add(jLabelJourney, BorderLayout.CENTER);

        JLabel jLabelPlayers = new JLabel(teams + " teams", JLabel.CENTER);
        jLabelPlayers.setFont(new Font("Inter", Font.BOLD, 17));
        jLabelPlayers.setBorder(BorderFactory.createEmptyBorder(0,5,35,5));
        jpInfo.add(jLabelPlayers, BorderLayout.SOUTH);

    }

    /**
     * Method that sets the journey of the league
     * @param new_journey String that contains the new journey of the league
     */
    public void setJourney(String new_journey) {
        journey = new_journey;
        jLabelJourney.setText("J." + journey);
    }

    /**
     * Method that returns the journey of the league
     * @return String that contains the journey of the league
     */
    public String getTeamName() {
        return jlName.getText();
    }
}

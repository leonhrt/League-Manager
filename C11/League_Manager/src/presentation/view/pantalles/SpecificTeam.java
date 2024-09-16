package presentation.view.pantalles;

import business.model.entities.Player;
import presentation.view.ui_elements.JImagePanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * This class is to show the information of a specific team.
 */
public class SpecificTeam extends JPanel {

    // Constants
    public static final String CARD_SPECIFIC_TEAM = "CARD_SPECIFIC_TEAM";

    // Components
    private JPanel jpPlayersInfo;
    private JScrollPane jspPlayers;
    private JScrollBar jsbPosition;
    private JLabel jlTeamName;

    /**
     * Constructor of the class.
     */
    public SpecificTeam() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(70,0,70,0));
        setOpaque(false);

        jpPlayersInfo = new JPanel();
        jpPlayersInfo.setOpaque(false);

        jspPlayers = new JScrollPane(jpPlayersInfo, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jspPlayers.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
        jspPlayers.getViewport().setOpaque(false);
        jspPlayers.setOpaque(false);
        jsbPosition = jspPlayers.getHorizontalScrollBar();

        jlTeamName = new JLabel("", SwingConstants.CENTER);
        jlTeamName.setFont(new Font("Arial", Font.BOLD, 40));
        jlTeamName.setForeground(new Color(27,34,76));
        jlTeamName.setBorder(BorderFactory.createEmptyBorder(0,0,30,0));
        add(jlTeamName, BorderLayout.NORTH);

        add(jspPlayers, BorderLayout.CENTER);
    }

    /**
     * This method is to load the players of a team.
     * @param team is the team.
     * @param teamName is the name of the team.
     */
    public void loadPlayers(ArrayList<Player> team,String teamName){
        jpPlayersInfo.removeAll();
        jlTeamName.setText("Team: " + teamName);
        jpPlayersInfo.setLayout(new GridLayout(2,team.size()/2, 25, 25));
        for (Player player : team) {
            JPanel jpPlayers = new JPanel();
            jpPlayers.setPreferredSize(new Dimension(465, 90));
            jpPlayers.setOpaque(false);
            jpPlayers.setLayout(new BorderLayout());
            jpPlayers.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

            JImagePanel jiSamarretes = new JImagePanel("resources/general/samarreta.png");
            jiSamarretes.setLayout(new BorderLayout());

            JLabel numero = new JLabel(Integer.toString(player.getNumber()));
            numero.setFont(new Font("Arial", Font.BOLD, 30));
            numero.setForeground(Color.white);
            numero.setBorder(BorderFactory.createEmptyBorder(0, 70, 0, 0));
            jiSamarretes.add(numero, BorderLayout.CENTER);

            jiSamarretes.setOpaque(false);
            jpPlayers.add(jiSamarretes, BorderLayout.CENTER);

            JPanel jpInfoPlayer = new JPanel();
            jpInfoPlayer.setPreferredSize(new Dimension(230, 90));
            jpInfoPlayer.setLayout(new GridLayout(2, 1));
            JLabel jl = new JLabel(player.getUsername() + " - " + player.getNumber());
            jl.setFont(new Font("Arial", Font.BOLD, 15));
            JTextArea textArea = new JTextArea();
            textArea.append("Email: " + player.getEmail() + "\n");
            textArea.append("DNI: " + player.getDni() + "\n");
            textArea.append("Phone number: " +player.getPhone() + "\n");
            textArea.setEditable(false);
            textArea.setOpaque(false);
            textArea.setFont(new Font("Arial", Font.PLAIN, 15));
            jpInfoPlayer.add(jl);
            jpInfoPlayer.add(textArea);
            jpInfoPlayer.setOpaque(false);

            jpPlayers.add(jpInfoPlayer, BorderLayout.EAST);

            jpPlayersInfo.add(jpPlayers);
        }
        jsbPosition.setValue(jsbPosition.getMinimum());

    }
}

package presentation.view.ui_elements;

import presentation.view.ui_elements.JImagePanel;

import javax.swing.*;
import java.awt.*;

/**
 * Class that creates a button that represents a match
 */
public class MatchButton extends JButton {

    private final JLabel jlScore;

    public MatchButton(String teamA, String teamB, int scoreA, int scoreB) {
        JPanel jp = new JPanel();
        jp.setBackground(new Color(209,209,211));
        jp.setLayout(new BorderLayout());

        JImagePanel ji1 = new JImagePanel(teamA);
        JImagePanel ji2 = new JImagePanel(teamB);

        ji1.setPreferredSize(new Dimension(70,70));
        ji2.setPreferredSize(new Dimension(70,70));

        jlScore = new JLabel(scoreA + "  -  " + scoreB);
        updateScore(scoreA, scoreB);
        jlScore.setHorizontalAlignment(JTextField.CENTER);
        jlScore.setFont(new Font("Serif", Font.PLAIN, 14));
        jp.add(ji1, BorderLayout.WEST);
        jp.add(ji2, BorderLayout.EAST);
        jp.add(jlScore, BorderLayout.CENTER);

        Dimension size = getSize();
        jp.setSize(size);
        add(jp);
        setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));

        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));


    }

    public void updateScore(int scoreA, int scoreB) {
        jlScore.setText(scoreA + "  -  " + scoreB);
    }


}

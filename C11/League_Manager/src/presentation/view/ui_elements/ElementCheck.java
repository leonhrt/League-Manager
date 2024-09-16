package presentation.view.ui_elements;

import javax.swing.*;
import java.awt.*;

/**
 * Class that creates a check element
 */
public class ElementCheck extends JPanel {

    // Components
    private JCheckBox jcbCheck;
    private JLabel jlName;

    /**
     * Constructor method
     * @param path String that contains the path of the image
     * @param name String that contains the name of the element
     */
    public ElementCheck(String path, String name) {

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(new Color(240,184,67), 7));

        JImagePanel jiImage = new JImagePanel(path);
        jiImage.setSize(new Dimension(80,80));
        jiImage.setBorder(BorderFactory.createEmptyBorder(30,30,30,100));
        add(jiImage, BorderLayout.WEST);

        jlName = new JLabel(name);
        jlName.setFont(new Font("Arial", Font.BOLD, 30));
        jlName.setBorder(BorderFactory.createEmptyBorder(0,40,0,100));
        add(jlName, BorderLayout.CENTER);

        jcbCheck = new JCheckBox();
        jcbCheck.setBorder(BorderFactory.createEmptyBorder(0,0,0,100));
        add(jcbCheck, BorderLayout.EAST);

        //setSize(new Dimension(400,130));
    }

    /**
     * Method that returns if the element is selected
     * @return boolean that indicates if the element is selected
     */
    public boolean isSelected(){
        return jcbCheck.isSelected();
    }

    /**
     * Method that returns the name of the element
     * @return String that contains the name of the element
     */
    public String getName(){
        return jlName.getText();
    }
}

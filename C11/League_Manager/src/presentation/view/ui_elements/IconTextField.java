package presentation.view.ui_elements;

import javax.swing.*;
import java.awt.*;

/**
 * Class that creates a text field with an icon
 */
public class IconTextField extends JPanel {

    // Components
    private HintTextField field;

    /**
     * Constructor method
     * @param path String that contains the path to the icon
     * @param hint String that contains the hint
     */
    public IconTextField(String path, String hint){
        setBorder(BorderFactory.createLineBorder(Color.BLACK,5));
        setLayout(new BoxLayout(this,BoxLayout.X_AXIS));

        JImagePanel icon = new JImagePanel(path);
        icon.setPreferredSize(new Dimension(40,30));
        icon.setBackground(Color.decode("#E7853A"));

        field = new HintTextField(hint);
        field.setFont(new Font("Serif",Font.PLAIN,20));
        field.setBorder(BorderFactory.createEmptyBorder(0,10,0,0));
        add(icon);
        add(field);
    }

    /**
     * Method that returns the text
     * @return String that contains the text
     */
    public String getText(){
        return field.getText();
    }

    /**
     * Method that resets the text field
     */
    public void resetTextField() {
        field.resetText();
    }
}

package presentation.view.ui_elements;

import javax.swing.*;
import java.awt.*;

/**
 * Class that creates a password field with a hint
 */
public class IconPasswordField extends JPanel{

    // Components
    private HintPasswordField field;

    /**
     * Constructor method
     * @param path String that contains the path to the icon
     * @param hint String that contains the hint
     */
    public IconPasswordField(String path, String hint){
        setBorder(BorderFactory.createLineBorder(Color.BLACK,5));
        setLayout(new BoxLayout(this,BoxLayout.X_AXIS));

        JImagePanel icon = new JImagePanel(path);
        icon.setPreferredSize(new Dimension(40,30));
        icon.setBackground(Color.decode("#E7853A"));

        field = new HintPasswordField(hint);
        field.setFont(new Font("Serif",Font.PLAIN,20));
        field.setBorder(BorderFactory.createEmptyBorder(0,10,0,0));
        add(icon);
        add(field);
    }

    /**
     * Method that returns the password
     * @return String that contains the password
     */
    public String getPassword(){
        return String.valueOf(field.getPassword());
    }

    /**
     * Method that resets the text field
     */
    public void resetTextField() {
        field.resetText();
    }
}

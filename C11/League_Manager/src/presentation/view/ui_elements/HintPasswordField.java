package presentation.view.ui_elements;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * Class that creates a password field with a hint
 */
public class HintPasswordField extends JPasswordField {

    // Constants
    private final String hint;
    private static final char DEFAULT_ECHO_CHAR = '•';
    private static final Font normalFont = new Font("Serif", Font.PLAIN, 20);
    private static final Font hintFont = new Font("Serif", Font.ITALIC, 20);

    /**
     * Constructor method
     * @param hint String that contains the hint
     */
    public HintPasswordField(final String hint) {
        this.hint = hint;
        setText(hint);
        setFont(hintFont);
        setForeground(Color.GRAY);
        setEchoChar((char) 0);

        this.addFocusListener(new FocusAdapter() {

            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(getPassword()).equals(hint)) {
                    setEchoChar(DEFAULT_ECHO_CHAR);
                    setText("");
                    setFont(normalFont);
                } else {
                    setEchoChar(DEFAULT_ECHO_CHAR);
                    setText(String.valueOf(getPassword()));
                    setFont(normalFont);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (String.valueOf(getPassword()).equals(hint)|| String.valueOf(getPassword()).length()==0) {
                    setText(hint);
                    setEchoChar((char) 0);
                    setFont(hintFont);
                    setForeground(Color.GRAY);
                } else {
                    setEchoChar(DEFAULT_ECHO_CHAR);
                    setText(String.valueOf(getPassword()));
                    setFont(normalFont);
                    setForeground(Color.BLACK);
                }
            }
        });

    }

    /**
     * Method that resets the text of the password field
     */
    public void resetText() {
            setEchoChar((char) 0);
            setText(hint);
            setFont(hintFont);
            setForeground(Color.GRAY);
    }
}

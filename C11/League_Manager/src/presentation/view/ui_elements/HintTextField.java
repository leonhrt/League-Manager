package presentation.view.ui_elements;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * Class that creates a text field with a hint
 */
public class HintTextField extends JTextField {

    // Constants
    private final String hint;
    private static final Font normalFont = new Font("Serif", Font.PLAIN, 20);
    private static final Font hintFont = new Font("Serif", Font.ITALIC, 20);

    /**
     * Constructor method
     * @param hint String that contains the hint
     */
    public HintTextField(final String hint) {
        this.hint = hint;
        setText(hint);
        setFont(hintFont);
        setForeground(Color.GRAY);

        this.addFocusListener(new FocusAdapter() {

            @Override
            public void focusGained(FocusEvent e) {
                if (getText().equals(hint)) {
                    setText("");
                    setFont(normalFont);
                } else {
                    setText(getText());
                    setFont(normalFont);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().equals(hint)|| getText().length()==0) {
                    setText(hint);
                    setFont(hintFont);
                    setForeground(Color.GRAY);
                } else {
                    setText(getText());
                    setFont(normalFont);
                    setForeground(Color.BLACK);
                }
            }
        });

    }

    /**
     * Method that resets the text of the text field
     */
    public void resetText(){
        setText(hint);
        setFont(hintFont);
        setForeground(Color.GRAY);
    }
}
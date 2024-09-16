package presentation.view.pantalles;

import presentation.view.ui_elements.IconPasswordField;
import presentation.view.ui_elements.IconTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Class that creates the panel that allows the user to recover his password
 */
public class RecoverPasswordPanel extends JPanel {
    public static final String DEFAULT_ERROR = "Please fill the form:" ;

    // Components
    private IconTextField itfUsername;
    private IconPasswordField ipfPassword;
    private IconPasswordField ipfPasswordRepeat;
    private JButton jbRecover;
    private JLabel jlError;

    /**
     * Constructor method
     * Creates the panel with the buttons and text fields to recover the password
     */
    public  RecoverPasswordPanel(){
        setLayout(new BorderLayout());
        setOpaque(false);

        JPanel jpSupeiror = new JPanel();
        jpSupeiror.setOpaque(false);
        JPanel jpCentre= new JPanel();
        jpCentre.setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(0,65,0,65));
        jpCentre.setLayout(new BoxLayout(jpCentre,BoxLayout.Y_AXIS));

        JLabel jlTitle = new JLabel("RECOVER PASSWORD");
        jlTitle.setForeground(Color.decode("#F0B843"));
        jlTitle.setFont(new Font("Inter", Font.BOLD, 20));

        jlError = new JLabel(DEFAULT_ERROR);
        jlError.setAlignmentX(RIGHT_ALIGNMENT);

        itfUsername = new IconTextField("resources/login/user_icon.png","Email Address / DNI");
        itfUsername.setAlignmentX(RIGHT_ALIGNMENT);

        ipfPassword = new IconPasswordField("resources/login/password_icon.png","New Password");
        ipfPassword.setAlignmentX(RIGHT_ALIGNMENT);

        ipfPasswordRepeat = new IconPasswordField("resources/login/password_icon.png","Repeat New Password");
        ipfPasswordRepeat.setAlignmentX(RIGHT_ALIGNMENT);

        jbRecover = new JButton("RECOVER");
        jbRecover.setAlignmentX(RIGHT_ALIGNMENT);
        jbRecover.setBorder(BorderFactory.createLineBorder(Color.BLACK,3));
        jbRecover.setBackground(Color.decode("#E7853A"));
        jbRecover.setForeground(Color.WHITE);
        jbRecover.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));


        jpSupeiror.add(jlTitle);
        jpCentre.add(jlError);
        jpCentre.add(Box.createRigidArea(new Dimension(0, 10)));
        jpCentre.add(itfUsername);
        jpCentre.add(Box.createRigidArea(new Dimension(0, 10)));
        jpCentre.add(ipfPassword);
        jpCentre.add(Box.createRigidArea(new Dimension(0, 10)));
        jpCentre.add(ipfPasswordRepeat);
        jpCentre.add(Box.createRigidArea(new Dimension(0, 10)));
        jpCentre.add(jbRecover);
        jpCentre.add(Box.createRigidArea(new Dimension(0, 120)));

        add(jpSupeiror, BorderLayout.NORTH);
        add(jpCentre, BorderLayout.CENTER);
        setActionCommands();
    }

    /**
     * Method that registers the controller as listener of the buttons
     * @param listener controller
     */
    public void registerController(ActionListener listener){
        jbRecover.addActionListener(listener);
    }

    /**
     * Method that set action commands to the buttons
     */
    private void setActionCommands(){
        jbRecover.setActionCommand(LoginView.UPDATE_PASSWORD_PRESSED);
    }

    /**
     * Method that returns the email or DNI of the user
     * @return email or DNI of the user
     */
    public String getEmailDNI() {
        return itfUsername.getText();
    }

    /**
     * Method that returns the password of the user (first time)
     * @return password of the user
     */
    public String getPassword1() {
        return ipfPassword.getPassword();
    }

    /**
     * Method that returns the password of the user (second time)
     * @return password of the user
     */
    public String getPassword2() {
        return ipfPasswordRepeat.getPassword();
    }

    /**
     * Method that sets the error message
     * @param s error message
     * @param isError boolean that indicates if the error is an error or a success
     */
    public void setErrror(String s, boolean isError){
        if(isError){
            jlError.setForeground(Color.RED);
        }
        else {
            jlError.setForeground(Color.GREEN);
        }
        jlError.setText(s);
    }

    /**
     * Method that sets the error message to the default one
     */
    public void noErrorText() {
        jlError.setText(DEFAULT_ERROR);
    }

    /**
     * Method that resets the text fields
     */
    public void resetTextFields() {
        itfUsername.resetTextField();
        ipfPasswordRepeat.resetTextField();
        ipfPassword.resetTextField();
    }
}

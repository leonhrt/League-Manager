package presentation.view.pantalles;

import presentation.view.ui_elements.IconPasswordField;
import presentation.view.ui_elements.IconTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Class that creates the panel that allows the user or admin to login
 */
public class LoginPanel extends JPanel {

    // Components
    private IconTextField itfUsernameLogin;
    private IconPasswordField icfPasswordLogin;
    private JButton jbLogin;
    private JButton jbForgotLogin;
    private JLabel jlError;
    private static final String NO_ERROR = "Enter credentials: ";

    /**
     * Constructor method
     * Creates the panel with the fields to enter the credentials and the login button
     */
    public LoginPanel(){
        setBorder(BorderFactory.createEmptyBorder(0,65,0,65));
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        setOpaque(false);

        jlError = new JLabel();
        noErrorText();
        jlError.setAlignmentX(Component.RIGHT_ALIGNMENT);

        itfUsernameLogin = new IconTextField("resources/login/user_icon.png","Email Address / DNI");
        icfPasswordLogin = new IconPasswordField("resources/login/password_icon.png","Password");
        itfUsernameLogin.setAlignmentX(Component.RIGHT_ALIGNMENT);
        icfPasswordLogin.setAlignmentX(Component.RIGHT_ALIGNMENT);

        jbLogin= new JButton("Login");
        jbLogin.setBorder(BorderFactory.createLineBorder(Color.BLACK,3));
        jbLogin.setBackground(Color.decode("#E7853A"));
        jbLogin.setForeground(Color.WHITE);
        jbLogin.setAlignmentX(Component.RIGHT_ALIGNMENT);
        jbLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        jbForgotLogin = new JButton( "<HTML><U>Forgot your password?</U></HTML>");
        jbForgotLogin.setAlignmentX(Component.RIGHT_ALIGNMENT);
        jbForgotLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jbForgotLogin.setBorderPainted(false);
        jbForgotLogin.setContentAreaFilled(false);
        jbForgotLogin.setFocusPainted(false);
        jbForgotLogin.setOpaque(false);

        JPanel jpanelTextForgot = new JPanel();
        jpanelTextForgot.setOpaque(false);
        jpanelTextForgot.add(jbForgotLogin);
        jpanelTextForgot.setAlignmentX(Box.RIGHT_ALIGNMENT);

        add(jlError);
        add(Box.createRigidArea(new Dimension(0, 25)));
        add(itfUsernameLogin);
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(icfPasswordLogin);
        add(Box.createRigidArea(new Dimension(0, 4)));
        add(jbLogin);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(jpanelTextForgot);
        add(Box.createRigidArea(new Dimension(0, 170)));

        addActionCommands();
    }

    /**
     * Method that sets the NON error text
     */
    public void noErrorText(){
        jlError.setForeground(Color.BLACK);
        jlError.setText(NO_ERROR);
    }

    /**
     * Method adds the action commands to the buttons
     */
    private void addActionCommands(){
        jbLogin.setActionCommand(LoginView.LOGIN_PRESSED);
        jbForgotLogin.setActionCommand(LoginView.FORGOT_PASSWORD_PRESSED);
    }

    /**
     * Method that registers the controller to the buttons
     * @param listener controller
     */
    public void registerController(ActionListener listener){
        jbLogin.addActionListener(listener);
        jbForgotLogin.addActionListener(listener);

    }

    /**
     * Method that returns the username entered
     * @return username or email address (String)
     */
    public String getUsername() {
        return itfUsernameLogin.getText();
    }

    /**
     * Method that returns the password entered
     * @return password
     */
    public String getPassword() {
        return icfPasswordLogin.getPassword();
    }

    /**
     * Method that sets the error text
     * @param error text
     * @param isError boolean that indicates if the text is an error or not
     */
    public void setErrorText(String error, boolean isError) {
        if(isError){
            jlError.setForeground(Color.RED);
        }
        else{
            jlError.setForeground(Color.BLACK);
        }
        jlError.setText(error);
        jlError.setVisible(true);

    }

    /**
     * Method that resets the text fields
     */
    public void resetTextFields() {
        itfUsernameLogin.resetTextField();
        icfPasswordLogin.resetTextField();
    }
}

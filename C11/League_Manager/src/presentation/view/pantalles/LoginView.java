package presentation.view.pantalles;

import presentation.view.ui_elements.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Class that creates the panel that allows the user to login
 */
public class LoginView extends JImagePanel {

    // Constants
    public static final String CARD_LOGIN = "CARD_LOGIN";
    public static final String SUBCARD_LOGIN = "SUBCARD_LOGIN";
    public static final String SUBCARD_RECOVER_PASSWORD = "SUBCARD_RECOVER_PASSWORD";
    public static final String LOGIN_PRESSED = "LOGIN_PRESSED";
    public static final String FORGOT_PASSWORD_PRESSED = "FORGOT_PASSWORD_PRESSED";
    public static final String UPDATE_PASSWORD_PRESSED = "UPDATE_PASSWORD_PRESSED";
    public static final String ERROR_AUTHENTICATION = "Authentication failed. Check credentials.";

    // Components
    private CardLayout cl;
    private JPanel jpCardLayout;
    private LoginPanel loginPanel;
    private RecoverPasswordPanel recoverPasswordPanel;

    /**
     * Constructor method
     * Creates the panel with the buttons to login
     */
    public LoginView(String path,LoginPanel loginPanel,RecoverPasswordPanel recoverPasswordPanel) {
        super(path);
        cl = new CardLayout();
        jpCardLayout = new JPanel(cl);
        jpCardLayout.setOpaque(false);
        this.loginPanel = loginPanel;
        this.recoverPasswordPanel = recoverPasswordPanel;
        resetAll();

        jpCardLayout.add(loginPanel,SUBCARD_LOGIN);
        jpCardLayout.add(recoverPasswordPanel, SUBCARD_RECOVER_PASSWORD);

        createLoginSection();

    }

    /**
     * Method that creates the login section
     */
    private void createLoginSection() {
        setLayout(new GridLayout(1,3));
        JPanel jpCenter = new JPanel();
        jpCenter.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        jpCenter.setLayout(new GridLayout(2,1,0,20));
        jpCenter.setOpaque(false);

        add(MainView.createInvisiblePanel());
        addLogo(jpCenter);
        jpCenter.add(jpCardLayout);
        add(jpCenter);
        add(MainView.createInvisiblePanel());
    }

    /**
     * Method that adds the logo to the panel
     * @param jpCenter panel where the logo will be added
     */
    private void addLogo(JPanel jpCenter) {
        JImagePanel logo = new JImagePanel("resources/general/logo.png");
        logo.setOpaque(false);
        jpCenter.add(logo);
    }

    /**
     * Method that gets the username introduced by the user
     * @return username introduced by the user (String)
     */
    public String getUsernameLogin(){
        return loginPanel.getUsername();
    }

    /**
     * Method that gets the password introduced by the user
     * @return password introduced by the user (String)
     */
    public String getPasswordLogin(){
        return loginPanel.getPassword();
    }

    /**
     * Method that set error text
     * @param error error text
     * @param isError boolean that indicates if the text is an error or not
     */
    public void setError(String error,boolean isError){
        loginPanel.setErrorText(error,isError);
    }



    /**
     * Method that changes the card
     * @param id id of the card
     */
    public void changeCard(String id){
        resetAll();
        cl.show(jpCardLayout,id);
    }

    /**
     * Method that resets all the text fields
     */
    public void resetAll() {
        loginPanel.resetTextFields();
        loginPanel.noErrorText();
        recoverPasswordPanel.noErrorText();
        recoverPasswordPanel.resetTextFields();
    }



    /**
     * Method that registers the controller
     * @param l controller
     */
    public void registerController(ActionListener l){
        loginPanel.registerController(l);
        recoverPasswordPanel.registerController(l);
    }

    /**
     * Method that gets the email introduced by the user
     * @return email introduced by the user (String)
     */
    public String getRecoverEmailDNI() {
        return recoverPasswordPanel.getEmailDNI();
    }

    /**
     * Method that gets the new password introduced by the user
     * @return new password introduced by the user (String)
     */
    public String getNewPassword() {
        return recoverPasswordPanel.getPassword1();
    }

    /**
     * Method that gets the new password introduced by the user (for the second time)
     * @return new password introduced by the user (String)
     */
    public String getNewPasswordRepeat() {
        return recoverPasswordPanel.getPassword2();
    }

    /**
     * Method that sets the error text
     * @param message error text
     * @param isError boolean that indicates if the text is an error or not
     */
    public void setRecoverError(String message, boolean isError) {
        recoverPasswordPanel.setErrror(message,isError);
    }
}

package presentation.controller;
import business.model.UserManager;
import business.model.entities.Player;
import business.model.exceptions.InvalidAdminException;
import business.model.exceptions.InvalidChangePassword;
import business.model.exceptions.InvalidPasswordException;
import business.model.exceptions.PlayerNotFoundException;
import presentation.view.pantalles.AdminMenuView;
import presentation.view.pantalles.LoginView;
import presentation.view.pantalles.UserMenuView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * LoginController.java
 * Controller for the login view
 */
public class LoginController implements ActionListener {

    // Attributes
    private LoginView loginView;
    private MainController mainController;
    private UserManager userManager;

    /**
     * Constructor
     * @param loginView LoginView
     * @param mainController MainController
     * @param userManager UserManager
     */
    public LoginController(LoginView loginView, MainController mainController, UserManager userManager) {
        this.loginView = loginView;
        this.mainController = mainController;
        this.userManager = userManager;
    }

    /**
     * Processes the events
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case LoginView.LOGIN_PRESSED:
                loginView.setError("Checking credentials...", false);
                String username = loginView.getUsernameLogin();
                String password = loginView.getPasswordLogin();
                login(username, password);
                break;

            case LoginView.FORGOT_PASSWORD_PRESSED:
                loginView.changeCard(LoginView.SUBCARD_RECOVER_PASSWORD);
                break;

            case LoginView.UPDATE_PASSWORD_PRESSED:
                updatePassword();

                break;
        }
    }

    /**
     * Method that supervises the file dialog
     */
    public void login(String username, String password) {
        try {
            Player logged = userManager.playerLogin(username,password);
            System.out.println(logged.getUsername());

            if(logged.isAdmin()){
                mainController.switchView(AdminMenuView.CARD_MENU_ADMIN);

            }
            else{
                mainController.switchView(UserMenuView.CARD_MENU_PLAYER);
            }

            new Thread(() -> {
                mainController.loadAll();
                }).start();

        } catch (PlayerNotFoundException ex) {
            loginView.setError(LoginView.ERROR_AUTHENTICATION, true);
        } catch (InvalidAdminException ex) {
            loginView.setError(ex.getMessage(),true);

        }
    }

    /**
     * Method that updates the password
     */
    private void updatePassword() {
        String emailDNI = loginView.getRecoverEmailDNI();
        String newPassword = loginView.getNewPassword();
        String newPasswordRepeat = loginView.getNewPasswordRepeat();

        System.out.println(emailDNI);
        if(newPassword.equals(newPasswordRepeat)){
            try {
                userManager.changePassword(newPassword, emailDNI);
                loginView.changeCard(LoginView.SUBCARD_LOGIN);
            } catch (InvalidPasswordException | InvalidChangePassword e) {
                loginView.setRecoverError(e.getMessage(),true);
            }
        }
        else {
            loginView.setRecoverError("The two passwords don't match.",true);
        }


    }

}

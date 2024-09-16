package presentation.view.pantalles;

import javax.swing.*;
import java.awt.*;

/**
 * Class that creates the main view
 */
public class MainView extends JFrame {

    // Constants
    private CardLayout cl;
    private JPanel jpCardLayout;
    private LoginView loginView;
    private AdminMenuView adminMenuView;
    private AdminOptionView adminOptionView;
    private UserMenuView userMenuView;
    private UserOptionView userOptionView;

    /**
     * Constructor method
     * Creates the main view
     */
    public MainView() {
        setTitle("Football Manager");
        setSize(1229, 860);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cl = new CardLayout();
        jpCardLayout = new JPanel(cl);

        getContentPane().add(jpCardLayout);
    }

    /**
     * Method that adds the cards to the main view
     * @param loginView LoginView object that will be added to the main view
     * @param adminMenuView AdminMenuView object that will be added to the main view
     * @param userMenuView UserMenuView object that will be added to the main view
     * @param adminOptionView AdminOptionView object that will be added to the main view
     */
    public void addCards(LoginView loginView, AdminMenuView adminMenuView, UserMenuView userMenuView,AdminOptionView adminOptionView,UserOptionView userOptionView){
        this.loginView = loginView;
        this.adminMenuView = adminMenuView;
        this.userMenuView = userMenuView;
        this.adminOptionView = adminOptionView;
        this.userOptionView = userOptionView;

        jpCardLayout.add(loginView,LoginView.CARD_LOGIN);
        jpCardLayout.add(adminMenuView, AdminMenuView.CARD_MENU_ADMIN);
        jpCardLayout.add(userMenuView, UserMenuView.CARD_MENU_PLAYER);
        jpCardLayout.add(adminOptionView,AdminOptionView.CARD_ADMIN_OPTION);
        jpCardLayout.add(userOptionView,UserOptionView.CARD_USER_OPTION);
    }

    /**
     * Method that creates an invisible panel
     * @return invisible panel (JPanel)
     */
    public static JPanel createInvisiblePanel(){
        JPanel jPanel = new JPanel();
        jPanel.setOpaque(false);
        return jPanel;
    }

    /**
     * Method that changes the card layout to the one selected by the user
     * @param id id of the card layout that will be shown
     */
    public void changeCard(String id){
        loginView.resetAll();
        cl.show(jpCardLayout,id);
    }

    /**
     * Method that makes the main view visible
     */
    public void start() {
        this.setVisible(true);
        jpCardLayout.requestFocusInWindow();
    }

    /*public void setLoginErrorText(String errorAuthentication) {
        loginView.setError(errorAuthentication, true);
    }*/

    /**
     * Method that opens the team file dialog to select the team file
     */
    public void openTeamFile(){
        adminMenuView.showFileDialog();
    }

    /**
     * Method that closes the team file dialog
     */
    public void closeFileDialog() {
        adminMenuView.closeFileDialog();
    }

    /**
     * Method that switches the login card to the one selected by the user
     * @param id id of the card layout that will be shown
     */
    public void switchLoginCard(String id) {
        loginView.changeCard(id);
    }


}
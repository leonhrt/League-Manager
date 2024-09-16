package presentation.controller;
import business.model.*;
import business.model.entities.Match;
import business.model.entities.MatchInfo;
import presentation.view.pantalles.*;
import presentation.view.ui_elements.FileDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * UserMenuController.java
 * Controller for the user menu view
 */
public class UserMenuController implements ActionListener, MatchListener {

    // Attributes
    private UserMenuView userMenuView;
    private MainController mainController;
    private UserManager userManager;
    private UserOptionView userOptionView;
    private MatchManager matchManager;
    private TeamManager teamManager;
    private LeagueManager leagueManager;

    /**
     * Constructor
     * @param userMenuView UserMenuView
     * @param mainController MainController
     * @param teamManager TeamManager
     * @param leagueManager LeagueManager
     * @param userManager UserManager
     * @param userOptionView UserOptionView
     */
    public UserMenuController(UserMenuView userMenuView, MainController mainController, TeamManager teamManager, LeagueManager leagueManager, UserManager userManager, UserOptionView userOptionView, MatchManager matchManager) {
        this.userMenuView = userMenuView;
        this.mainController = mainController;
        this.teamManager = teamManager;
        this.leagueManager = leagueManager;
        this.userManager = userManager;
        this.userOptionView = userOptionView;
        this.matchManager = matchManager;
    }

    /**
     * Processes the events
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case UserMenuView.CHANGE_PASSWORD_PRESSED -> {
                    userManager.logoutPlayer();
                    mainController.switchView(LoginView.CARD_LOGIN);
                    mainController.switchLoginView(LoginView.SUBCARD_RECOVER_PASSWORD);}

                case UserMenuView.LOGOUT_PRESSED -> {
                    userManager.logoutPlayer();
                    mainController.switchView(LoginView.CARD_LOGIN);
                    mainController.switchLoginView(LoginView.SUBCARD_LOGIN);}

            case UserMenuView.DELETE_ACCOUNT_PRESSED -> {
                int resposta = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete your account?", "Deletion confirmation", JOptionPane.YES_NO_OPTION);

                if (resposta == JOptionPane.YES_OPTION) {
                    new Thread(() -> {
                        userManager.deleteUserInfoFromDb();
                    }).start();
                    mainController.switchView(LoginView.CARD_LOGIN);
                }

            }
            case UserMenuView.VIEW_LEAGUES_PRESSED -> {
                mainController.switchView(UserOptionView.CARD_USER_OPTION);
                userOptionView.changeCard(ViewLeagues.CARD_VIEW_LEAGUES);
            }
            case UserMenuView.MATCHES_PRESSED -> {
                mainController.switchView(UserOptionView.CARD_USER_OPTION);
                userOptionView.changeCard(ViewMatches.CARD_VIEW_MATCHES);

            }
        }
        if(e.getActionCommand().startsWith(AdminOptionView.MATCH_PRESSED)){
            MatchInfo info = matchManager.getInfoFromMatch(e.getActionCommand().substring(AdminOptionView.MATCH_PRESSED.length()));
            mainController.switchView(UserOptionView.CARD_USER_OPTION);
            userOptionView.openMatchDetails(info);
        }
    }

    @Override
    public void notifyStartMatch(MatchInfo info) {
        userMenuView.addMatch(this, info);

    }

    @Override
    public void notifyScoreUpdated(MatchInfo info) {
        userMenuView.updateMatchInfo(info);
    }

    @Override
    public void notifyEndMatch(Match info) {
        userMenuView.removeMatch(info.getIdMatch());
    }


}

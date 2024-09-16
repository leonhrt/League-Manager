package presentation.controller;

import business.model.*;
import business.model.entities.Match;
import business.model.entities.MatchInfo;
import business.model.entities.TeamRanking;
import presentation.view.pantalles.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * UserMenuController.java
 * Controller for the user menu view
 */
public class UserOptionController implements ActionListener, MatchListener, JourneyListener,TableListener {
    private final UserOptionView userOptionView;
    private MainController mainController;
    private LeagueManager leagueManager;
    private UserManager userManager;
    private MatchManager matchManager;
    private TeamManager teamManager;

    public UserOptionController(UserOptionView userOptionView, MainController mainController, LeagueManager leagueManager, UserManager userManager, MatchManager matchManager,TeamManager teamManager) {
        this.userOptionView = userOptionView;
        this.mainController = mainController;
        this.leagueManager = leagueManager;
        this.userManager = userManager;
        this.matchManager = matchManager;
        this.teamManager = teamManager;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()) {
            case UserMenuView.CHANGE_PASSWORD_PRESSED -> {
                System.out.println("A");

                userManager.logoutPlayer();
                mainController.switchView(LoginView.CARD_LOGIN);
                mainController.switchLoginView(LoginView.SUBCARD_RECOVER_PASSWORD);
            }

            case UserMenuView.LOGOUT_PRESSED -> {
                System.out.println("A");

                userManager.logoutPlayer();
                mainController.switchView(LoginView.CARD_LOGIN);
                mainController.switchLoginView(LoginView.SUBCARD_LOGIN);
            }

            case UserMenuView.DELETE_ACCOUNT_PRESSED -> {
                System.out.println("A");

                int resposta = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete your account?", "Deletion confirmation", JOptionPane.YES_NO_OPTION);

                if (resposta == JOptionPane.YES_OPTION) {
                    new Thread(() -> {
                        userManager.deleteUserInfoFromDb();
                    }).start();
                    mainController.switchView(LoginView.CARD_LOGIN);
                }
            }
            case AdminOptionView.MENU_PRESSED -> {
                System.out.println("A");
                mainController.switchView(UserMenuView.CARD_MENU_PLAYER);
            }
        }
        if(e.getActionCommand().startsWith(ViewLeagues.LEAGUE_PRESSED_)){
            ArrayList<TeamRanking> ranking = leagueManager.listRankingDetails(e.getActionCommand().substring(ViewLeagues.LEAGUE_PRESSED_.length()));
            userOptionView.loadStadistics(ranking);
            userOptionView.changeCard(InfoLeague.CARD_INFO_LEAGUE);
        }
        if(e.getActionCommand().startsWith(AdminOptionView.MATCH_PRESSED)){
            MatchInfo info = matchManager.getInfoFromMatch(e.getActionCommand().substring(AdminOptionView.MATCH_PRESSED.length()));
            mainController.switchView(UserOptionView.CARD_USER_OPTION);
            userOptionView.openMatchDetails(info);
        }
    }

    @Override
    public void journeyChanged(String leagueId, String newJourney) {
        userOptionView.updateLeagueJourney(leagueId,newJourney);

    }

    @Override
    public void notifyStartMatch(MatchInfo info) {
        userOptionView.addMatch(this, info);

    }

    @Override
    public void notifyScoreUpdated(MatchInfo info) {
        userOptionView.updateMatchInfo(info);
    }

    @Override
    public void notifyEndMatch(Match info) {
        userOptionView.removeMatch(info);
        userOptionView.loadStadistics(leagueManager.listRankingDetails(info.getLeague()));
    }

    public void loadAll() {
        userOptionView.removeLeagues();
        userOptionView.loadLeagues(leagueManager.getAllLeagues(),this);

    }

    @Override
    public void teamPressedInTable(String team_pressed) {
        userOptionView.loadPlayersInTeam(teamManager.getPlayersFromATeam(team_pressed),team_pressed);
        userOptionView.changeCard(SpecificTeam.CARD_SPECIFIC_TEAM);
    }
}

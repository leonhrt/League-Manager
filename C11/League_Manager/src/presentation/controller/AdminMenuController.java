package presentation.controller;

import business.model.*;
import business.model.entities.DBTeam;
import business.model.entities.Match;
import business.model.entities.MatchInfo;
import business.model.entities.Player;
import business.model.exceptions.*;
import presentation.view.pantalles.*;
import presentation.view.ui_elements.FileDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * AdminMenuController.java
 * Controller for the admin menu view
 */
public class AdminMenuController implements ActionListener, MatchListener {

    // Attributes
    private AdminMenuView adminMenuView;
    private MainController mainController;
    private AdminOptionController adminOptionController;
    private UserManager userManager;
    private TeamManager teamManager;
    private LeagueManager leagueManager;
    private MatchManager matchManager;
    private AdminOptionView adminOptionView;

    /**
     * Constructor
     * @param adminMenuView AdminMenuView
     * @param mainController MainController
     * @param teamManager TeamManager
     * @param leagueManager LeagueManager
     * @param userManager UserManager
     * @param adminOptionView AdminOptionView
     * @param adminOptionController AdminOptionController
     * @param matchManager MatchManager
     */
    public AdminMenuController(AdminMenuView adminMenuView, MainController mainController, TeamManager teamManager, LeagueManager leagueManager, UserManager userManager, AdminOptionView adminOptionView,AdminOptionController adminOptionController,MatchManager matchManager) {
        this.adminOptionController = adminOptionController;
        this.adminMenuView = adminMenuView;
        this.mainController = mainController;
        this.teamManager = teamManager;
        this.leagueManager = leagueManager;
        this.userManager = userManager;
        this.adminOptionView = adminOptionView;
        this.matchManager = matchManager;
    }

    /**
     * Processes the events
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case FileDialog.UPLOAD_FILE_PRESSED -> manageFileDialog();
            case FileDialog.CANCEL_FILE_PRESSED -> adminMenuView.closeFileDialog();
            case AdminMenuView.LOGOUT_PRESSED -> {
                userManager.logoutPlayer();
                mainController.switchView(LoginView.CARD_LOGIN);
                mainController.switchLoginView("login");}

            case AdminMenuView.CREATE_TEAM_PRESSED ->mainController.askTeamFile();
            case AdminMenuView.MANAGE_PRESSED -> {
                mainController.switchView(AdminOptionView.CARD_ADMIN_OPTION);
                adminOptionView.switchCard(AdminManage.CARD_MANAGE);}
            case AdminMenuView.CREATE_LEAGUE_PRESSED ->{
                adminMenuView.resetLeagueForm();
                adminMenuView.showLeagueForm();
            }

            case AdminMenuView.VIEW_LEAGUES_PRESSED -> {
                mainController.switchView(AdminOptionView.CARD_ADMIN_OPTION);
                adminOptionView.switchCard(ViewLeagues.CARD_VIEW_LEAGUES);}

            case AdminMenuView.VIEW_MATCHES_PRESSED -> {
                mainController.switchView(AdminOptionView.CARD_ADMIN_OPTION);
                adminOptionView.switchCard(ViewMatches.CARD_VIEW_MATCHES);
            }
        }

        if(e.getActionCommand().startsWith(AdminOptionView.MATCH_PRESSED)){
            MatchInfo info = matchManager.getInfoFromMatch(e.getActionCommand().substring(AdminOptionView.MATCH_PRESSED.length()));
            mainController.switchView(AdminOptionView.CARD_ADMIN_OPTION);
            adminOptionView.openMatchDetails(info);
        }
    }

    /**
     * Method that manages the file dialog
     */
    private void manageFileDialog(){
        int return_value = adminMenuView.askFile();
        if(return_value == JFileChooser.APPROVE_OPTION){
            File file = adminMenuView.getSelectedFile();
            String filename = file.getAbsolutePath();
            try {
                DBTeam newTeam = teamManager.createTeamFromJSON(filename);
                adminOptionController.addNewTeam(newTeam);
                adminMenuView.setMessageFile("Team was created successfully!", false);
                ArrayList<Player> new_players = teamManager.getPlayersRegistered();
                if(!new_players.isEmpty()){
                    adminMenuView.showNewPlayers(new_players);
                }

            } catch (BusinessStoppingException | FileNotFoundException ex) {
                adminMenuView.setMessageFile(ex.getMessage(), true);
            }
        }
        else{
            adminMenuView.setMessageFile("You didn't choose a file to upload", true);
        }
    }


    /**
     * Method that notifies the start of a match
     * @param info MatchInfo
     */
    @Override
    public void notifyStartMatch(MatchInfo info) {
        adminMenuView.addMatch(this, info);
    }

    /**
     * Method that notifies the update of a match
     * @param info MatchInfo
     */
    @Override
    public void notifyScoreUpdated(MatchInfo info) {
        adminMenuView.updateMatchInfo(info);

    }

    /**
     * Method that notifies the end of a match
     * @param info MatchInfo
     */
    @Override
    public void notifyEndMatch(Match info) {
        adminMenuView.removeMatch(info.getIdMatch());
    }

}


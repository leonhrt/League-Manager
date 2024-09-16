package presentation.controller;


import business.model.*;
import business.model.entities.*;
import business.model.exceptions.CouldntDeleteLeagueException;
import business.model.exceptions.CouldntRemoveTeamException;
import business.model.exceptions.DateOrHourInvalidException;
import business.model.exceptions.LeagueNameAlreadyExists;
import presentation.view.pantalles.*;
import presentation.view.ui_elements.CreateLeagueDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class AdminOptionController implements ActionListener, JourneyListener, MatchListener, TableListener {
/**
 * AdminOptionController.java
 * Controller for the admin option view
 */
    // Attributes
    private MainController mainController;
    private AdminOptionView adminOptionView;
    private TeamManager teamManager;
    private LeagueManager leagueManager;
    private MatchManager matchManager;

    // Constants
    private final String DEFAULT_PHOT_PATH =  "resources/added_files";

    /**
     * Constructor
     * @param adminOptionView AdminOptionView
     * @param mainController MainController
     * @param teamManager TeamManager
     * @param leagueManager LeagueManager
     * @param matchManager MatchManager
     */
    public  AdminOptionController(AdminOptionView adminOptionView, MainController mainController, TeamManager teamManager, LeagueManager leagueManager,MatchManager matchManager){
        this.adminOptionView = adminOptionView;
        this.mainController = mainController;
        this.teamManager = teamManager;
        this.leagueManager = leagueManager;
        this.matchManager = matchManager;
    }

    /**
     * Processes the events
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()) {
            case AdminOptionView.LOGOUT_PRESSED -> {
                mainController.switchView(LoginView.CARD_LOGIN);
                adminOptionView.switchCard(AdminManage.CARD_MANAGE);
            }

            case AdminOptionView.MENU_PRESSED -> {
                mainController.switchView(AdminMenuView.CARD_MENU_ADMIN);
                adminOptionView.switchCard(AdminManage.CARD_MANAGE);
            }
            case AdminManage.TEAMS_PRESSED -> {
                adminOptionView.switchCard(AdminShowTeamsDelete.CARD_TEAMS_DELETE);
            }
            case AdminManage.LEAGUES_PRESSED -> {
                adminOptionView.switchCard(AdminShowLeaguesDelete.CARD_LEAGUES_DELETE);
            }
            case AdminShowTeamsDelete.DELETE_TEAMS_PRESSED -> {
                ArrayList<String> teamsToDelete = adminOptionView.getTeamsToDelete();
                adminOptionView.deleteTeamsFromLeagueCreation(teamsToDelete);
                new Thread(() -> {
                    try {
                        teamManager.removeTeams(teamsToDelete);
                    } catch (CouldntRemoveTeamException ex) {
                        System.out.println(ex.getMessage());
                    }
                }).start();

            }
            case AdminShowLeaguesDelete.DELETE_LEAGUES_PRESSED -> {
                ArrayList<String> leaguesToDelete = adminOptionView.getLeaguesToDelete();
                adminOptionView.removeLeagues(leaguesToDelete);
                new Thread(() -> {
                    try {
                        leagueManager.deleteLeagues(leaguesToDelete);
                    } catch (CouldntDeleteLeagueException ex) {
                        throw new RuntimeException(ex); //TODO: enviar missatge d'error a la vista
                    }
                }).start();
            }
            case CreateLeagueDialog.NEXT_PRESSED -> nextPressed();
            case CreateLeagueDialog.CANCEL_PRESSED ->adminOptionView.closeLeagueForm();
            case CreateLeagueDialog.UPLOAD_PHOTO_PRESSED -> selectImage();
            case AdminAddTeamsToLeagues.CREATE_LEAGUE_PRESSED -> createLeague();
        }
        if(e.getActionCommand().startsWith(ViewLeagues.LEAGUE_PRESSED_)){
            ArrayList<TeamRanking> ranking = leagueManager.listRankingDetails(e.getActionCommand().substring(ViewLeagues.LEAGUE_PRESSED_.length()));
            adminOptionView.loadStadistics(ranking);
            adminOptionView.switchCard(InfoLeague.CARD_INFO_LEAGUE);
            System.out.println(e.getActionCommand().substring(ViewLeagues.LEAGUE_PRESSED_.length()));
        }
        if(e.getActionCommand().startsWith(AdminOptionView.MATCH_PRESSED)){
            MatchInfo info = matchManager.getInfoFromMatch(e.getActionCommand().substring(AdminOptionView.MATCH_PRESSED.length()));
            adminOptionView.openMatchDetails(info);
        }
    }

    /**
     * Selects the image to upload
     */
    private void selectImage() {
        adminOptionView.setLeagueImagePath(null);
        int return_value = adminOptionView.selectFile();
        if(return_value == JFileChooser.APPROVE_OPTION) {
            File file = adminOptionView.getFile();

            String path = DEFAULT_PHOT_PATH;
            try {
                Path destinationPath = Path.of(path, file.getName());
                Files.copy(file.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
                adminOptionView.setLeagueImagePath(path+ "/" + file.getName());
            } catch (IOException e) {
                adminOptionView.setErrorCreation("There was a problem uploading the image. Try again.");
            }
        }
        else{
            adminOptionView.setErrorCreation("You didn't choose a file to upload");
        }

    }

    /**
     * Method that controls the button to create a league
     */
    private void nextPressed() {
        if(adminOptionView.notAllFieldsFilled()){
            adminOptionView.setErrorCreation("Please fill all the fields before continuing");
        }
        else if(adminOptionView.getLeagueImagePath() == null){
            adminOptionView.setErrorCreation("Please choose a league image before continuing.");

        }
        else{
            try {
                adminOptionView.saveLeagueCreationData();
                leagueManager.checkLeagueNameAndDate(adminOptionView.getLeagueName(), adminOptionView.getStartDate(), adminOptionView.getStartHour());
                adminOptionView.closeLeagueForm();
                mainController.switchView(AdminOptionView.CARD_ADMIN_OPTION);
                adminOptionView.switchCard(AdminAddTeamsToLeagues.CARD_CREATE_LEAGUE);
            } catch (LeagueNameAlreadyExists | DateOrHourInvalidException ex) {
                adminOptionView.setErrorCreation(ex.getMessage());
            }
        }
    }

    /**
     * Method that creates a league
     */
    private void createLeague() {
        ArrayList<String> teams = adminOptionView.getTeamsToLeague();
        if(teams.size() >1){
            new Thread(() -> {
                League new_league = leagueManager.createLeague(adminOptionView.getLeagueName(), adminOptionView.getStartDate(),
                        adminOptionView.getStartHour(),teams,adminOptionView.getLeagueImagePath());
                adminOptionView.loadNewLeague(new_league, this);
            }).start();
            mainController.switchView(AdminMenuView.CARD_MENU_ADMIN);
        }
        else{
            JOptionPane.showMessageDialog(null, "Choose at least 2 teams.");
        }
    }

    /**
     * Method that updates the journey of a league
     * @param leagueId id of the league
     * @param newJourney new journey
     */
    @Override
    public void journeyChanged(String leagueId, String newJourney) {
        adminOptionView.updateLeagueJourney(leagueId,newJourney);
    }

    /**
     * Method that notifies the start of a match
     * @param info info of the match
     */
    @Override
    public void notifyStartMatch(MatchInfo info) {
        adminOptionView.addMatch(this, info);

    }

    /**
     * Method that notifies the update of a score
     * @param info info of the match
     */
    @Override
    public void notifyScoreUpdated(MatchInfo info) {
        adminOptionView.updateMatchInfo(info);

    }

    /**
     * Method that notifies the end of a match
     * @param info info of the match
     */
    @Override
    public void notifyEndMatch(Match info) {
        adminOptionView.removeMatch(info);
        adminOptionView.loadStadistics(leagueManager.listRankingDetails(info.getLeague()));

    }

    /**
     * Method that loads all the teams and leagues
     */
    public void loadAll() {
        adminOptionView.removeAllLeagues();
        adminOptionView.loadTeams(teamManager.getAllTeams());
        adminOptionView.loadLeagues(leagueManager.getAllLeagues(),this);
    }

    /**
     * Method that adds a new team
     * @param newTeam new team
     */
    public void addNewTeam(DBTeam newTeam) {
        adminOptionView.loadNewTeam(newTeam);
    }

    /**
     * Method that puts the players of a team in a table
     * @param team_pressed team pressed
     */
    @Override
    public void teamPressedInTable(String team_pressed) {
        adminOptionView.loadPlayersInTeam(teamManager.getPlayersFromATeam(team_pressed),team_pressed);
        adminOptionView.switchCard(SpecificTeam.CARD_SPECIFIC_TEAM);
    }
}

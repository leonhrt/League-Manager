import business.model.*;
import business.model.entities.League;
import business.model.entities.Match;
import business.model.exceptions.*;
import persistence.dbDAO.*;
import persistence.filesDAO.ConfigDAO;
import presentation.controller.*;
import presentation.view.pantalles.*;
import presentation.view.ui_elements.CreateLeagueDialog;
import presentation.view.ui_elements.FileDialog;
import presentation.view.ui_elements.LeagueBox;

import javax.swing.text.View;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


/**
 * Main class
 * @author Andrea Ballester Griful
 * @author Leonardo Ruben Edenak Chouev
 * @author Pol Guarch Bosom
 * @author Jan Piñol Castuera
 * @author Oriol Rebordosa Cots
 * @author Joan Tarragó Pina
 */
public class Main {

    /**
     * Main method
     * @param args arguments
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws ConfigFileNotFoundException
     */
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException, ConfigFileNotFoundException, CouldntDeleteLeagueException {
        //1. DATABASE
        ConfigDAO config = new ConfigDAO();
        config = config.readConfig();
        DatabaseDAO database = new DatabaseDAO();
        database.createDB();

        //2. DBDAO
        PlayerDbDAO playerDbDAO = new PlayerDbDAO(database.getBd());
        TeamDbDAO teamDbDAO = new TeamDbDAO(database.getBd(), playerDbDAO);
        TeamRankingDbDAO teamRankingDbDAO = new TeamRankingDbDAO(teamDbDAO, database.getBd());
        MatchDbDAO matchDbDAO = new MatchDbDAO(database.getBd(), config, teamRankingDbDAO);
        CalendarDbDAO calendarDbDAO = new CalendarDbDAO(database.getBd(), matchDbDAO);
        LeagueDbDAO leagueDbDAO = new LeagueDbDAO(database.getBd(), teamDbDAO, calendarDbDAO, matchDbDAO, teamRankingDbDAO);

        //3. MANAGER
        UserManager userManager = new UserManager(playerDbDAO, teamDbDAO, config, leagueDbDAO);
        TeamManager teamManager = new TeamManager(userManager, teamDbDAO, playerDbDAO, teamRankingDbDAO, leagueDbDAO, matchDbDAO);
        LeagueManager leagueManager = new LeagueManager(leagueDbDAO, teamDbDAO,userManager, calendarDbDAO, teamRankingDbDAO, matchDbDAO, config);
        MatchManager matchManager = new MatchManager(config, matchDbDAO, teamDbDAO, userManager);

        //4. MAIN VIEW i MAIN CONTROLLER
        MainView mainView = new MainView();
        MainController mainController = new MainController(mainView);

        //5. LOGIN VIEW i LOGIN CONTROLLER
        LoginPanel loginPanel = new LoginPanel();
        RecoverPasswordPanel recoverPasswordPanel = new RecoverPasswordPanel();
        LoginView loginView = new LoginView("resources/login/fons.png",loginPanel,recoverPasswordPanel);

        LoginController loginController =new LoginController(loginView,mainController,userManager);
        loginView.registerController(loginController);

        //6. USER MENU VIEW i USER MENU CONTROLLER
        FileDialog fileDialog = new FileDialog(mainView);
        CreateLeagueDialog createLeagueDialog = new CreateLeagueDialog(mainView);
        UserMenuView userMenuView = new UserMenuView("resources/player_menu/playerFons.png");


        //7. ADMIN OPTION VIEW i ADMIN OPTION CONTROLLER
        AdminManage adminManage = new AdminManage();
        AdminShowTeamsDelete adminShowTeamsDelete = new AdminShowTeamsDelete();
        AdminShowLeaguesDelete adminShowLeaguesDelete = new AdminShowLeaguesDelete();
        AdminAddTeamsToLeagues adminAddTeamsToLeagues = new AdminAddTeamsToLeagues();
        ViewLeagues viewLeagues = new ViewLeagues();
        ViewMatches viewMatches = new ViewMatches();
        InfoLeague infoLeague = new InfoLeague();
        MatchDetail matchDetail = new MatchDetail();
        SpecificTeam specificTeam = new SpecificTeam();
        AdminOptionView adminOptionView = new AdminOptionView(adminManage,adminShowTeamsDelete,
                adminShowLeaguesDelete,adminAddTeamsToLeagues,createLeagueDialog,viewLeagues,
                viewMatches,infoLeague,matchDetail,specificTeam);

        AdminOptionController adminOptionController = new AdminOptionController(adminOptionView,mainController,teamManager,leagueManager,matchManager);
        adminOptionView.registerController(adminOptionController);

        //8. ADMIN MENU VIEW i ADMIN MENU CONTROLLER
        AdminMenuView adminMenuView = new AdminMenuView("resources/admin_menu/adminFons.png",fileDialog,createLeagueDialog);
        AdminMenuController adminMenuController = new AdminMenuController(adminMenuView,mainController,teamManager,leagueManager,userManager, adminOptionView,adminOptionController,matchManager);
        adminMenuView.registerController(adminMenuController);

        ViewLeagues viewLeagues2 = new ViewLeagues();
        MatchDetail matchDetail2 = new MatchDetail();
        ViewMatches viewMatches2 = new ViewMatches();
        InfoLeague infoLeague2 = new InfoLeague();
        SpecificTeam specificTeam2 = new SpecificTeam();
        UserOptionView userOptionView =new UserOptionView(viewLeagues2,matchDetail2,viewMatches2,infoLeague2,specificTeam2);
        UserOptionController userOptionController = new UserOptionController(userOptionView,mainController,leagueManager,userManager,matchManager,teamManager);
        userOptionView.registerController(userOptionController);
        infoLeague2.registerController(userOptionController);

        UserMenuController userMenuController = new UserMenuController(userMenuView, mainController,teamManager,leagueManager,userManager,userOptionView,matchManager);
        userMenuView.registerController(userMenuController);

        //9. THREAD MATCH START DETECTOR
        CheckMatchesToPlayThread  checkMatchesToPlayThread = new CheckMatchesToPlayThread(matchDbDAO,matchManager, adminMenuController, leagueManager, adminOptionController, userManager,userMenuController,userOptionController);
        Thread threadPlayMatches = new Thread(checkMatchesToPlayThread);
        threadPlayMatches.start();

        //10. MAIN VIEW START i CONTROLLERS UNION
        mainController.setControllers(adminMenuController,adminOptionController,userOptionController,userManager);

        mainView.addCards(loginView,adminMenuView, userMenuView,adminOptionView,userOptionView);
        mainView.start();

    }
}
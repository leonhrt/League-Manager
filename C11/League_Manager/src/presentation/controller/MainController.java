package presentation.controller;
import business.model.UserManager;
import presentation.view.pantalles.AdminMenuView;
import presentation.view.pantalles.MainView;

/**
 * MainController.java
 * Controller for the main view
 */
public class MainController {

    // Attributes
    private AdminMenuController adminMenuController;
    private AdminOptionController adminOptionController;
    private UserOptionController userOptionController;
    private UserManager userManager;

    private MainView mainView;

    /**
     * Constructor
     * @param mainView MainView
     */
    public MainController(MainView mainView) {
        this.mainView = mainView;
    }

    /**
     * Sets the controllers
     * @param adminMenuController AdminMenuController
     * @param adminOptionController AdminOptionController
     * @param userOptionController UserOptionController
     */
    public void setControllers(AdminMenuController adminMenuController, AdminOptionController adminOptionController,UserOptionController userOptionController,UserManager userManager){
        this.adminMenuController = adminMenuController;
        this.adminOptionController = adminOptionController;
        this.userOptionController = userOptionController;
        this.userManager = userManager;
    }

    /**
     * Method that switches between views
     * @param id the id of the view to be switched to
     */
    public void switchView(String id) {
        mainView.changeCard(id);

    }

    /**
     * Method that asks the user for a file that contains the teams
     */
    public void askTeamFile() {
        mainView.openTeamFile();
    }

    /**
     * Method that closes the file dialog
     */
    public void closeFileDialog() {
        mainView.closeFileDialog();
    }

    /**
     * Method that switches the login view
     * @param id the id of the view to be switched to
     */
    public void switchLoginView(String id) {
        mainView.switchLoginCard(id);
    }


    public void loadAll() {
        if (userManager.isAdmin()){
            adminOptionController.loadAll();
        }
        else{
            userOptionController.loadAll();
        }
    }
}




package business.model;

import business.model.entities.DBTeam;
import business.model.entities.Player;
import business.model.entities.Team;
import business.model.exceptions.*;
import persistence.*;
import persistence.filesDAO.TeamJsonDAO;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

/**
 * Class that manages the teams
 */
public class TeamManager {

    // Components
    private UserManager userManager;
    private TeamDAO teamDAO;
    private PlayerDAO playerDAO;
    private TeamRankingDAO teamRankingDAO;
    private LeagueDAO leagueDAO;
    private MatchDAO matchDAO;

    // Attributes
    private ArrayList<Player> playersRegistered = new ArrayList<>();


    /**
     * Constructor method for the TeamManager class
     * @param userManager the user manager
     * @param teamDAO the team DAO
     * @param playerDAO the player DAO
     * @param teamRankingDAO the team ranking DAO
     * @param leagueDAO the league DAO
     * @param matchDAO the match DAO
     */
    public TeamManager(UserManager userManager, TeamDAO teamDAO, PlayerDAO playerDAO, TeamRankingDAO teamRankingDAO, LeagueDAO leagueDAO, MatchDAO matchDAO) {
        this.userManager = userManager;
        this.teamDAO = teamDAO;
        this.playerDAO = playerDAO;
        this.teamRankingDAO = teamRankingDAO;
        this.leagueDAO = leagueDAO;
        this.matchDAO = matchDAO;
    }

    /**
     * Method that creates a team and registers it in the DB
     * @param filename the name of the file
     * @throws TeamAlreadyExistsException if the team already exists
     * the program will throw this exception with the message:
     * "You have tried to create a team that already exists!"
     * @throws PlayerWithInvalidNumberException if the player has an invalid number
     * the program will throw this exception with the message:
     * "Tried to register a player with an invalid team number, it must be grater than 0!"
     * @throws ExistsUserSameDniException if the player has the same DNI as another player
     * the program will throw this exception with the message:
     * "There is already a user with the same DNI."
     * @throws DniNotCorrectException if the player has an invalid DNI
     * the program will throw this exception with the message:
     * "The DNI is not correct."
     * @throws ExistsUserSameEmailException if the player has the same email as another player
     * the program will throw this exception with the message:
     * "There is already a user with the same email."
     * @throws EmailNotCorrectException if the player has an invalid email
     * the program will throw this exception with the message:
     * "The email is not correct."
     * @throws PlayerNotFoundException if the player is not found
     * the program will throw this exception with the message:
     * "The player is not found."
     * @throws java.io.FileNotFoundException if the file is not found
     * the program will throw this exception with the message:
     * "The file is not found."
     */
    public DBTeam createTeamFromJSON(String filename) throws TeamAlreadyExistsException, PlayerWithInvalidNumberException, ExistsUserSameDniException, DniNotCorrectException, ExistsUserSameEmailException, EmailNotCorrectException, PlayerNotFoundException, java.io.FileNotFoundException, FormatNotExpectedException {
        ArrayList<Player> playersToRegister = new ArrayList<>(); //Creem una llista de jugadors a registrar

        Team team;
        TeamJsonDAO teamJsonDAO = new TeamJsonDAO();

        playersRegistered = new ArrayList<>(); //Reiniciem la llista de jugadors registrats
        team = teamJsonDAO.fromJsonFile(filename); //Llegim el JSON

        if(teamDAO.getTeamByName(team.getName()) == null) { //Si estem creant un team el qual no existeix
            //Si tenim jugadors els quals no es troben a la db els creem
            team.setId(UUID.randomUUID().toString()); //Li setejem el ID (random) per a la firebase
            for (Player player: team.getPlayers()) {
                //Si te el DNI en un format correcte o no existent a la DB, l'email en un format correcte o no existent a la DB
                // i el numero no es negatiu
                if(userManager.checkCorrectDNI(player.getDni(), player.getUsername()) && userManager.checkCorrectEmail(player.getEmail(), player.getUsername()) && userManager.checkCorrectNumber(player.getNumber())) {
                    if(playerDAO.getPlayerByOneField(player.getDni(), "dni") == null) { //Si el jugador no existeix, el registrem
                        playersToRegister.add(player);
                    } else {
                        //Busquem el jugador a la DB
                        Player tempPlayer = playerDAO.getPlayerByOneField(player.getDni(), "dni");
                        //Afegim el equip a la llista d'equips als quals juga
                        tempPlayer.addTeam(team.getId());
                        //Actualitzem el jugador a la db
                        playerDAO.updatePlayerToDB(tempPlayer);
                    }
                }
            }

            for (Player player: playersToRegister) {
                registerPlayerInDB(player, team);
            }

            DBTeam tempTeam = createTeamForDB(team); //Passem el team llegit a un team per a la base de dades.
            teamDAO.exportDataToDB(tempTeam); //Creem el team a la base de dades
            return tempTeam;

        } else {
            throw new TeamAlreadyExistsException();
        }
    }

    /**
     * Method that creates a team and registers it in the DB
     * @param team the team to create
     * @return the team created in the DB (DBTeam)
     */
    private DBTeam createTeamForDB(Team team) {
        DBTeam dbTeam = new DBTeam(team.getId(), team.getName(), team.getImagePath());

        for (Player player: team.getPlayers()) {
            dbTeam.addPlayer(playerDAO.getPlayerByOneField(player.getDni(), "dni").getId());
        }

        //De moment no li fiquem cap lliga, pero hem de ficar un valor per defecte.

        return dbTeam;
    }

    /**
     * Method that registers a player inside one team in the DB
     * @param player the player to register
     * @param team the team that the player will be registered
     */
    private void registerPlayerInDB(Player player, Team team) {
        player.addTeam(team.getId());

        player.setId(UUID.randomUUID().toString()); //Li setejem el ID (random) per a la firebase
        player.setPassword(createRandomPassword()); //Li creem una password random
        player.setAdmin(false);

        playerDAO.exportDataToDB(player);
        playersRegistered.add(player); //Afegim el jugador a la llista de jugadors registrats
    }

    /**
     * Generates a random password
     * @return the random password generated (String)
     */
    private String createRandomPassword() {
        String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; //Ni Ñ ni Ç
        String lowercase = "abcdefghijklmnopqrstuvwxyz"; //Ni Ñ ni Ç
        String number = "0123456789";

        StringBuilder password = new StringBuilder();
        Random random = new Random();

        do {
            password.append(lowercase.charAt(random.nextInt(lowercase.length())));
            password.append(uppercase.charAt(random.nextInt(uppercase.length())));
            password.append(number.charAt(random.nextInt(number.length())));

            for (int i = 0; i < 5; i++) {
                int type = random.nextInt(3) + 1;
                switch (type) {
                    case 1 -> password.append(lowercase.charAt(random.nextInt(lowercase.length())));
                    case 2 -> password.append(uppercase.charAt(random.nextInt(uppercase.length())));
                    case 3 -> password.append(number.charAt(random.nextInt(number.length())));
                }
            }
        }while (playerDAO.getPlayerByOneField(String.valueOf(password), "password") != null); //En el cas que haguem creat una password ja existent

        return password.toString();
    }

    /**
     * Method that returns all the teams registered in the DB
     * @return all the teams registered in the DB (ArrayList<DBTeam>)
     */
    public ArrayList<DBTeam> getAllTeams(){
        if(userManager.isAdmin()) {
            return teamDAO.getAllTeams();
        } else {
            return teamDAO.getDBTeamsFromPlayer(userManager.getPlayerLogged().getId()); //Si no es admin, nomes retornem els equips del jugador
        }
    }

    /**
     * Method that returns the players registered in the last team created
     * @return the players registered in the last team created (ArrayList<Player>)
     */
    public ArrayList<Player> getPlayersRegistered() {
        return playersRegistered;
    }


    /**
     * Method that deletes all teams from the database given an arraylist of them
     * @param teamsNames names of the teams we want to remove from de database
     * @throws CouldntRemoveTeamException in case it couldn't delete some team
     */
    public void removeTeams(ArrayList<String> teamsNames) throws CouldntRemoveTeamException {
        for (String teamName: teamsNames) {
            System.out.println(teamName);
            DBTeam team = teamDAO.getTeamByName(teamName);
            deleteTeam(team);
        }
    }

    /**
     * Method that deletes a team from the DB
     * @param teamToRemove the team to remove
     * @return true if the team has been deleted, false if not
     */
    private void deleteTeam(DBTeam teamToRemove) throws CouldntRemoveTeamException {
        //Borrar tots els teams rankings del team
        //Borrar tots els matches en els que juga el team
        //Borrar l'equip de les lligues a les que juga
        //Borrar l'equip de la DB
        if(
            playerDAO.deleteTeamFromPlayers(teamToRemove.getId()) && // (ES FA) Borrem l'equip dels jugadors
            teamDAO.deleteFromDatabase(teamToRemove.getId()) && // (ES FA) Borrem l'equip de la DB
            teamRankingDAO.deleteRankingsByTeam(teamToRemove.getId()) && //(ES FA) Borrem els rankings del equip
            matchDAO.deleteMatchesFromTeam(teamToRemove.getId()) &&  //TODO: (ES FA però no borra partits de les journeys) Borrem els partits del equip
            leagueDAO.deleteTeamFromLeagues(teamToRemove.getId()) // (ES FA) Borrem l'equip de les lligues
        ) {}
        else {
            throw new CouldntRemoveTeamException();
        }
    }

    /**
     * Method that gets all the players from a team
     * @param teamName the name of the team
     * @return ArrayList of players
     */
    public ArrayList<Player> getPlayersFromATeam(String teamName) {
        return teamDAO.getPlayersOfTeam(teamName);
    }
}

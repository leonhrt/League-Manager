package persistence.dbDAO;

import business.model.entities.DBTeam;
import business.model.entities.League;
import business.model.entities.Player;
import business.model.entities.Team;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import persistence.PlayerDAO;
import persistence.TeamDAO;

import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * This class is responsible for creating the Team objects and exporting them to the database
 */
public class TeamDbDAO implements TeamDAO {

    // Attributes
    private static Firestore bd;
    private final String TABLE = "TEAM";
    private PlayerDAO playerDAO;

    /**
     * Constructor method
     * @param bd Firestore object
     * @param playerDAO PlayerDAO object
     */
    public TeamDbDAO(Firestore bd, PlayerDAO playerDAO) {
        TeamDbDAO.bd = bd;
        this.playerDAO = playerDAO;
    }

    /**
     * Method that imports the data to the database
     * @param id Team id
     * @param data Map with the data
     * @return true if the data has been imported correctly, false otherwise (boolean)
     */
    private boolean importDataToDB (String id, Map<String,Object> data){
        try{
            DocumentReference docRef = bd.collection(TABLE).document(id);
            ApiFuture<WriteResult> result = docRef.set(data);
            System.out.println("Update time : " +result.get().getUpdateTime());
            return true;
        } catch (ExecutionException | InterruptedException e) {
            return false;
        }
        // Aquesta funció ha de ser capaç d'importar les dades la base de dades.

    }

    /**
     * Method that get all the teams from the database
     * @return list with all the teams from the database (ArrayList<DBTeam>)
     */
    @Override
    public ArrayList<DBTeam> getAllTeams() {
        // asynchronously retrieve all documents
        ApiFuture<QuerySnapshot> future = bd.collection(TABLE).get();
        // future.get() blocks on response
        List<QueryDocumentSnapshot> documents = null;
        try {
            documents = future.get().getDocuments();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        ArrayList<DBTeam> teams = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            teams.add(document.toObject(DBTeam.class));
        }

        return teams;
    }

    /**
     * Method that updates a team in the database
     * @param team team to update in the database
     * @return true if the team has been updated correctly, false otherwise (boolean)
     */
    @Override
    public boolean updateTeamInDataBase(Team team) {
        //Si s'ha esborrat correctament
        if(deleteFromDatabase(team.getId())) {
            //L'escrivim de nou modificat
            Map<String, Object> data = new HashMap<>();
            data.put("players",team.getPlayers());
            data.put("name", team.getName());
            data.put("id", team.getId()); //Tots els documents han de tenir un id
            data.put("imagePath", team.getImagePath());
            //... altres atributs a ficar

            return importDataToDB(team.getId(), data);
        } else{
            return false;
        }
    }

     /**
     * Method that gets a team by the id from the database
     * @param id id of the team
     * @return team from the database (Team)
     */
    @Override
    public Team getTeamById(String id) {
        // asynchronously retrieve multiple documents
        ApiFuture<QuerySnapshot> future = bd.collection(TABLE).whereEqualTo("id", id).get();
        // future.get() blocks on response
        List<QueryDocumentSnapshot> documents = null;
        DBTeam temp;
        try {
            documents = future.get().getDocuments();
            temp = documents.get(0).toObject(DBTeam.class);
        } catch (InterruptedException | ExecutionException | IndexOutOfBoundsException e) {
            return null;
        }


        ArrayList<Player> aux = new ArrayList<>();
        for (String playerId: temp.getPlayers()) {
            aux.add(playerDAO.getPlayerByOneField(playerId, "id"));
        }

        Player[] playersArray = aux.toArray(new Player[0]); // Convert ArrayList to Player[] array
        Team team2 = new Team(temp.getName(), playersArray, temp.getImagePath());
        team2.setId(temp.getId());

        return team2;
    }

    /**
     * Method that gets some teams by the names of each team from the database
     * @param teams list of teams
     * @return list of teams from the database (ArrayList<Team>)
     */
    public ArrayList<Team> getTeamsById(ArrayList<String> teams) {
        ArrayList<Team> temp = new ArrayList<>();

        for (String teamId: teams) {
            temp.add(getTeamById(teamId));
        }

        return temp;
    }

    /**
     * Method that delete a team from the database
     * @param teamId id of the team to delete
     * @return true if the team has been deleted correctly, false otherwise (boolean)
     */
    public boolean deleteFromDatabase(String teamId) {
        if(teamId == null) {
            return false;
        }

        try {
            ApiFuture<WriteResult> writeResult = bd.collection(TABLE).document(teamId).delete();
            System.out.println("Update time : " + writeResult.get().getUpdateTime());
            return true;
        } catch (InterruptedException | ExecutionException e) {
            return false;
        }
    }

    /**
     * Method that exports the data to the database
     * @param team team to export
     * @return true if the data has been exported correctly, false otherwise (boolean)
     */
    @Override
    public boolean exportDataToDB(DBTeam team) {
        //L'escrivim de nou modificat
        Map<String, Object> data = new HashMap<>();
        data.put("players",team.getPlayers());
        data.put("name", team.getName());
        data.put("id", team.getId()); //Tots els documents han de tenir un id
        data.put("imagePath", team.getImagePath()); //Tots els documents han de tenir un id
        //... altres atributs a ficar

        return importDataToDB(team.getId(), data);
    }

    /**
     * Method that get the data from the database
     * @param teamName name of the team
     * @return team from the database (DBTeam)
     */
    @Override
    public DBTeam getTeamByName(String teamName) {
        // asynchronously retrieve multiple documents
        ApiFuture<QuerySnapshot> future = bd.collection(TABLE).whereEqualTo("name",teamName).get();
        // future.get() blocks on response
        List<QueryDocumentSnapshot> documents = null;
        try {
            documents = future.get().getDocuments();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        if(documents.isEmpty()) {
            return null;
        }

        return documents.get(0).toObject(DBTeam.class);
    }

    /**
     * Method that delete a player from a team in the database
     * @param teamId id of the team
     * @param playerToRemove player to remove
     * @return true if the player has been deleted correctly, false otherwise (boolean)
     */
    @Override
    public boolean deletePlayerFromTeam(String teamId, Player playerToRemove) {
        Team aux = getTeamById(teamId);

        DBTeam temp = getTeamByName(aux.getName());

        ArrayList<String> players = new ArrayList<>();

        for (String player: temp.getPlayers()) {
            if(!player.equals(playerToRemove.getId())) {
                players.add(player);
            }
        }

        temp.setPlayers(players); //Canviem els jugadors.

        if(deleteFromDatabase(temp.getName())) {
            exportDataToDB(temp);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Method that get the teams of a player from the database
     * @param fieldValue value of the field
     * @param field field to search
     * @return list of teams of a player from the database (ArrayList<Team>)
     */
    @Override
    public ArrayList<Team> getPlayerTeams(String fieldValue, String field) {
        ApiFuture<QuerySnapshot> future = bd.collection("USER").whereEqualTo(fieldValue, field).get();
        // future.get() blocks on response
        List<QueryDocumentSnapshot> documents = null;
        try {
            documents = future.get().getDocuments();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        Player player = documents.get(0).toObject(Player.class);

        ArrayList<String> teams = new ArrayList<>();

        for (String team: player.getTeams()) {
            teams.add(team);
        }

        ArrayList<DBTeam> tempTeams = new ArrayList<>();
        for (String teamId: teams) {
            Team aux = getTeamById(teamId);
            tempTeams.add(getTeamByName(aux.getName()));
        }

        ArrayList<Team> teamsToReturn = new ArrayList<>();
        for (DBTeam team : tempTeams) {
            teamsToReturn.add(convertToTeam(team));
        }


        return teamsToReturn;
    }



    /**
     * Method that generate a Team from a DBTeam object
     * @param team DBTeam object
     * @return Team object (Team)
     */
    public Team convertToTeam(DBTeam team) {
        Team aux = new Team(team.getName(), team.getId());

        ArrayList<Player> players = new ArrayList<>();
        for (String player: team.getPlayers()) {
            players.add(playerDAO.getPlayerByOneField(player, "id"));
        }

        aux.setPlayers(players.toArray(new Player[players.size()]));
        aux.setImagePath(team.getImagePath());
        aux.setId(team.getId());

        return aux;
    }

    /**
     * Method that get the players of a team from the database
     * @param teamName name of the team
     * @return list of players of a team from the database (ArrayList<Player>)
     */
    @Override
    public ArrayList<Player> getPlayersOfTeam(String teamName) {
        DBTeam team = getTeamByName(teamName);
        ArrayList<Player> players = new ArrayList<>();
        for (String playersID: team.getPlayers()) {
            players.add(playerDAO.getPlayerByOneField(playersID, "id"));
        }

        return players;
    }

    /**
     * Method that get the teams of a player from the database
     * @param playerId id of the player
     * @return list of teams of a player from the database (ArrayList<DBTeam>)
     */
    @Override
    public ArrayList<DBTeam> getDBTeamsFromPlayer(String playerId) {
        ApiFuture<QuerySnapshot> future = bd.collection("USER").whereEqualTo("id", playerId).get();
        // future.get() blocks on response
        List<QueryDocumentSnapshot> documents = null;
        try {
            documents = future.get().getDocuments();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        Player player = documents.get(0).toObject(Player.class);

        ArrayList<String> teams = new ArrayList<>();

        for (String team: player.getTeams()) {
            teams.add(team);
        }

        ArrayList<DBTeam> tempTeams = new ArrayList<>();
        for (String teamId: teams) {
            Team aux = getTeamById(teamId);
            tempTeams.add(getTeamByName(aux.getName()));
        }

        return tempTeams;
    }
}

package persistence.dbDAO;

import business.model.entities.Player;
import business.model.entities.Team;
import business.model.exceptions.PlayerNotFoundException;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import persistence.PlayerDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Class that implements the PlayerDAO interface
 */
public class PlayerDbDAO implements PlayerDAO {
    private static Firestore bd;
    private static final String TABLE = "USER";

    public PlayerDbDAO(Firestore bd) {
        PlayerDbDAO.bd = bd;
    }

    /**
     * Method that imports a player to the DB
     * @param id id of the player
     * @param data data of the player
     * @return true if the player has been imported, false otherwise
     */
    public static boolean importDataToDB (String id, Map<String,Object> data){
        try{
            DocumentReference docRef = bd.collection(TABLE).document(id);
            ApiFuture<WriteResult> result = docRef.set(data);
            System.out.println("Update time : " +result.get().getUpdateTime());
            return true;
        } catch (ExecutionException | InterruptedException e) {
            return false;
        }
    }

    /**
     * Method that gets all the players from the DB
     * @return
     */
    private ArrayList<Player> getAllPlayersFromDB() {
        // asynchronously retrieve all documents
        ApiFuture<QuerySnapshot> future = bd.collection(TABLE).get();
        // future.get() blocks on response
        List<QueryDocumentSnapshot> documents = null;
        try {
            documents = future.get().getDocuments();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        ArrayList<Player> players = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            players.add(document.toObject(Player.class));
        }

        return players;
    }

    /**
     * Method that creates a player in the DB
     * @param player player to export
     * @return true if the player has been exported, false otherwise
     */
    @Override
    public boolean exportDataToDB(Player player) {
        //L'escrivim de nou modificat
        Map<String, Object> data = new HashMap<>();
        data.put("username",player.getUsername());
        data.put("password",player.getPassword());
        data.put("id", player.getId()); //Tots els documents han de tenir un id
        data.put("teams", player.getTeams());
        data.put("number", player.getNumber());
        data.put("phone", player.getPhone());
        data.put("dni", player.getDni());
        data.put("email", player.getEmail());

        //... altres atributs a ficar

        return importDataToDB(player.getId(), data);
    }

    /**
     * Method that gets the player from the DB that match with the email or dni
     * @param email_dni email or dni of the player
     * @param password password of the player
     * @return player that match with the email or dni or null if there is no player with that email or dni
     * @throws PlayerNotFoundException in case there is no player with that email or dni
     */
    @Override
    public Player getPlayerByLogin(String email_dni, String password) throws PlayerNotFoundException {
        if(searchPlayerByEmail(email_dni, password) != null) {
            return searchPlayerByEmail(email_dni, password);
        } else if (searchPlayerByDNI(email_dni, password) != null) {
            return searchPlayerByDNI(email_dni, password);
        } else {
            throw new PlayerNotFoundException();
        }
    }

    /**
     * Method that gets the player from the DB that match with the id given
     * @param id id of the player
     * @return player that match with the email or null if there is no player with that email
     */
    @Override
    public Player getExistentPlayer(String id) {
        CollectionReference collection = bd.collection(TABLE);

        ApiFuture<QuerySnapshot> future = collection
                .whereEqualTo("id", id)
                .get();

        List<QueryDocumentSnapshot> documents = null;

        try {
            documents = future.get().getDocuments();
            if (!documents.isEmpty()) {
                return documents.get(0).toObject(Player.class);
            } else {
                return null;
            }
        } catch (InterruptedException | ExecutionException ignored) {
            return null;
        }
    }

    /**
     * Method that deletes a team from the player
     * @param teamID id of the team to delete
     * @return true if the team has been deleted, false otherwise
     */
    @Override
    public boolean deleteTeamFromPlayers(String teamID) {
        ArrayList<Player> players = getAllPlayersFromDB();

        for (Player player: players) {
            if(!player.getTeams().isEmpty()) {
                for (String team: player.getTeams()) {
                    if(team.equals(teamID)) {
                        Player aux = new Player(player.getPassword(), player.getUsername(),player.getEmail(), player.getPhone(), player.getNumber(), player.getDni()); //Fer un nou player amb els mateixos atributs
                        ArrayList<String> newTeams = new ArrayList<>(player.getTeams());
                        newTeams.remove(teamID); //Eliminem el team vell
                        aux.setTeams(newTeams); //Posem tots els teams menys el team vell
                        aux.setId(player.getId());
                        if(deleteFromDatabase(player.getId())) { //Si hem pogut borrar el player a actualitzar
                            exportDataToDB(aux);   //Tornem a escriure el players sense el team eliminat
                        } else { return false; }
                    }
                }
            }
        }

        return true;
    }


    /**
     * Method that gets a player based on one field
     * @param value value of the field
     * @param field field to search
     * @return player that match with the field or null if there is no player with that field
     */
    @Override
    public Player getPlayerByOneField(String value, String field) {
        CollectionReference collection = bd.collection(TABLE);

        ApiFuture<QuerySnapshot> future = collection
                .whereEqualTo(field, value)
                .get();

        List<QueryDocumentSnapshot> documents = null;
        try {
            documents = future.get().getDocuments();
            if (!documents.isEmpty()) {
                return documents.get(0).toObject(Player.class);
            } else {
                return null;
            }
        } catch (InterruptedException | ExecutionException ignored) {
            return null;
        }
    }


    /**
     * Method that gets a player based on the email and password
     * @param email email of the player
     * @param password password of the player
     * @return player that match with the email and password or null if there is no player with that email and password
     */
    private Player searchPlayerByEmail(String email, String password) {
        CollectionReference collection = bd.collection(TABLE);

        ApiFuture<QuerySnapshot> future = collection
                .whereEqualTo("email", email)
                .whereEqualTo("password", password)
                .get();

        List<QueryDocumentSnapshot> documents = null;
        try {
            documents = future.get().getDocuments();
            if (!documents.isEmpty()) {
                return documents.get(0).toObject(Player.class);
            } else {
                return null;
            }
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
    }

    /**
     * Method that gets a player based on the dni and password
     * @param dni dni of the player
     * @param password password of the player
     * @return player that match with the dni and password or null if there is no player with that dni and password
     */
    private Player searchPlayerByDNI(String dni, String password) {
        CollectionReference collection = bd.collection(TABLE);

        ApiFuture<QuerySnapshot> future = collection
                .whereEqualTo("dni", dni)
                .whereEqualTo("password", password)
                .get();

        List<QueryDocumentSnapshot> documents = null;
        try {
            documents = future.get().getDocuments();
            if (!documents.isEmpty()) {
                return documents.get(0).toObject(Player.class);
            } else {
                return null;
            }
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
    }

    /**
     * Method that updates a player to the DB
     * @param player player to update
     * @return true if the player has been updated, false otherwise
     */
    @Override
    public boolean updatePlayerToDB(Player player) {
        //Si s'ha borrat correctament
        if(deleteFromDatabase(player.getId())) {
            //L'escrivim de nou modificat
            Map<String, Object> data = new HashMap<>();
            data.put("username",player.getUsername());
            data.put("password",player.getPassword());
            data.put("id", player.getId()); //Tots els documents han de tenir un id
            data.put("teams", player.getTeams());
            data.put("dni", player.getDni());
            data.put("number", player.getNumber());
            data.put("email", player.getEmail());
            data.put("phone", player.getPhone());
            //... altres atributs a ficar

            return importDataToDB(player.getId(), data);
        } else{
            return false;
        }
    }

    /**
     * Method that deletes all players from the DB
     */
    @Override
    public void deleteAllUsersFromDB() {
        // Query the documents in the collection
        ApiFuture<QuerySnapshot> future = bd.collection(TABLE).get();

        QuerySnapshot snapshot = null;
        try {
            snapshot = future.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        // Delete the documents
        List<WriteResult> deleteResults = new ArrayList<>();
        for (DocumentSnapshot document : snapshot.getDocuments()) {
            ApiFuture<WriteResult> deleteFuture = document.getReference().delete();
            try {
                deleteResults.add(deleteFuture.get());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

        // Wait for the deletion to complete
        for (WriteResult result : deleteResults) {
            result.getUpdateTime();
        }

        System.out.println("Tot borrat");
    }

    /**
     * Method that deletes a player from the DB
     * @param id name of the player
     * @return true if the player has been deleted, false otherwise
     */
    @Override
    public boolean deleteFromDatabase(String id) {
        try {
            // asynchronously delete a document
            ApiFuture<WriteResult> writeResult = bd.collection(TABLE).document(id).delete();
            System.out.println("Update time : " + writeResult.get().getUpdateTime());
            return true;
        } catch (InterruptedException | ExecutionException e) {
            return false;
        }
    }
}
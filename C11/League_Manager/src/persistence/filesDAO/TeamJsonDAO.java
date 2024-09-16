package persistence.filesDAO;

import business.model.entities.DBTeam;
import business.model.entities.Player;
import business.model.entities.Team;
import business.model.exceptions.ConfigFileNotFoundException;
import business.model.exceptions.FormatNotExpectedException;
import com.google.gson.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class is responsible for reading a JSON file and converting it to a Team object.
 */
public class TeamJsonDAO {

    /**
     * Method that reads a JSON file and converts it to a Team object.
     * @param fileName The name of the JSON file
     * @return Team object
     * @throws FileNotFoundException If the file is not found.
     */
    public Team fromJsonFile(String fileName) throws FileNotFoundException, FormatNotExpectedException {
        Gson gson = new Gson();
        Team team = null;

        try ( FileReader reader = new FileReader(fileName) ) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            String name = jsonObject.get("team_name").getAsString();
            String imagePath = jsonObject.get("image_path").getAsString();

            JsonArray jsonPlayers = jsonObject.getAsJsonArray("players");
            Player[] players = gson.fromJson(jsonPlayers, Player[].class);

            team = new Team(name, players, imagePath);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (JsonParseException | NullPointerException e) {
            throw new FormatNotExpectedException();
        }

        return team;
    }


}

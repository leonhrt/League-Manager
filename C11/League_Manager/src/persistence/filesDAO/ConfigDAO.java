package persistence.filesDAO;

import business.model.exceptions.ConfigFileNotFoundException;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Represents the configuration to set up the database.
 */
public class ConfigDAO {
    /**
     * Connection port with the database
     */
    private int dbPort;
    /**
     * IP address of the database server (usually "localhost")
     */
    private String dbIp;
    /**
     * Name of the database
     */
    private String dbName;
    /**
     * Database access user
     */
    private String dbUser;
    /**
     * Database access password
     */
    private String dbPassword;
    /**
     * Access password of the platform administrator
     */
    private String adminPassword;
    /**
     * Duration time (in minutes) of a game
     */
    private int gameDuration;

    /**
     * Constructs a new Config object with the specified database connection information and game duration.
     *
     * @param dbPort          the connection port with the database
     * @param dbIp            the IP address of the database server (usually "localhost")
     * @param dbName          the name of the database
     * @param dbUser          the database access user
     * @param dbPassword      the database access password
     * @param adminPassword   the access password of the platform administrator
     * @param gameDuration    the duration time (in minutes) of a game
     */

    /**
     * Returns the connection port with the database.
     *
     * @return the connection port with the database
     */
    public int getDbPort() {
        return dbPort;
    }

    /**
     * Sets the connection port with the database.
     *
     * @param dbPort the connection port with the database
     */
    public void setDbPort(int dbPort) {
        this.dbPort = dbPort;
    }

    /**
     * Returns the IP address of the database server.
     *
     * @return the IP address of the database server
     */
    public String getDbIp() {
        return dbIp;
    }

    /**
     * Sets the IP address of the database server.
     *
     * @param dbIp the IP address of the database server
     */
    public void setDbIp(String dbIp) {
        this.dbIp = dbIp;
    }

    /**
     * Returns the name of the database.
     *
     * @return the name of the database
     */
    public String getDbName() {
        return dbName;
    }

    /**
     * Sets the name of the database.
     *
     * @param dbName the name of the database
     */
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    /**
     * Returns the database access user.
     *
     * @return the database access user
     */
    public String getDbUser() {
        return dbUser;
    }

    /**
     * Sets the database access user.
     *
     * @param dbUser the database access user
     */
    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    /**
     * Returns the database access password.
     *
     * @return the database access password
     */
    public String getDbPassword() {
        return dbPassword;
    }

    /**
     * Sets the database access password.
     *
     * @param dbPassword the database access password
     */
    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    /**
     * Returns the administrator password for the platform.
     *
     * @return the administrator password for the platform
     */
    public String getAdminPassword() {
        return adminPassword;
    }

    /**
     * Sets the administrator password for the platform.
     *
     * @param adminPassword the new administrator password for the platform
     */
    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    /**
     * Returns the duration of a game in minutes.
     *
     * @return the duration of a game in minutes
     */
    public int getGameDuration() {
        return gameDuration;
    }

    /**
     * Sets the duration of a game in minutes.
     *
     * @param gameDuration the new duration of a game in minutes
     */
    public void setGameDuration(int gameDuration) {
        this.gameDuration = gameDuration;
    }

    /*
    A PERSISTENCE
     */

    /*
    AMB TRY CATCH
     */
    /**
     * Reads the configuration file in JSON format and returns a Config object
     * containing the configuration of the database.
     *
     * @return a Config object containing the configuration data read from the configuration file
     */
    public ConfigDAO readConfig() throws ConfigFileNotFoundException {
        Gson gson = new Gson();
        ConfigDAO config = null;
        try (FileReader reader = new FileReader("data/config.json"); ) {
            config = gson.fromJson(reader, ConfigDAO.class);
        } catch (Exception e) {
            throw new ConfigFileNotFoundException();
        }
        return config;
    }

    /*
    AMB EXCEPTION CAP AMUNT
     */
    /**
     * Reads the configuration file in JSON format and returns a Config object
     * containing the configuration of the database.
     *
     * @return a Config object containing the configuration data read from the configuration file
     * @throws FileNotFoundException if the configuration file is not found
     */
    /*
    public static ConfigDAO readConfig() throws FileNotFoundException {
        Gson gson = new Gson();
        FileReader reader = new FileReader("pathFitxer");

        return gson.fromJson(reader, ConfigDAO.class);

    }*/
}

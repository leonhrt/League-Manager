package business.model.exceptions;


/**
 * Exception that is thrown when the config file is not found
 */
public class ConfigFileNotFoundException extends BusinessStoppingException {

    /**
     * Constructor of ConfigFileNotFoundException
     * and puts the message "The config file (config.json) cannot be found for some reason."
     */
    public ConfigFileNotFoundException() {
        super("The config file (config.json) cannot be found for some reason.");
    }
}

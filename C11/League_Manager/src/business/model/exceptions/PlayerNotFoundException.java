package business.model.exceptions;

/**
 * Exception that is thrown when the player is not found
 */
public class PlayerNotFoundException extends BusinessStoppingException{

    /**
     * Constructor of PlayerNotFoundException
     * and puts the message "Invalid username or password."
     */
    public PlayerNotFoundException() {
        super("Invalid email/dni or password, please try again.");

    }
}

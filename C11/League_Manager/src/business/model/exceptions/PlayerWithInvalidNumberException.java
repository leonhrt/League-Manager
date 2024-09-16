package business.model.exceptions;

/**
 * Exception that is thrown when the player number is not valid
 */
public class PlayerWithInvalidNumberException extends BusinessStoppingException{

    /**
     * Constructor of PlayerWithInvalidNumberException
     * and puts the message "Tried to register a player with an invalid team number, it must be grater than 0!"
     */
    public PlayerWithInvalidNumberException() {
        super("Tried to register a player with an invalid team number, it must be grater than 0!");
    }
}

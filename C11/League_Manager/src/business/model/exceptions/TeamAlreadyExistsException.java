package business.model.exceptions;

/**
 * Exception that is thrown when the team already exists
 */
public class TeamAlreadyExistsException extends BusinessStoppingException{

    /**
     * Constructor of TeamAlreadyExistsException
     * and puts the message "You have tried to create a team that already exists!"
     */
    public TeamAlreadyExistsException() {
        super("You have tried to create a team that already exists!");
    }
}

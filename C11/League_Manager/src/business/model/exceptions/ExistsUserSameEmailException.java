package business.model.exceptions;

/**
 * Exception that is thrown when there is already a user with the same email
 */
public class ExistsUserSameEmailException extends BusinessStoppingException{

    /**
     * Constructor of ExistsUserSameEmailException
     * and puts the message "There is already a user with the same email."
     */
    public ExistsUserSameEmailException() {
        super("There is already a user with the same email.");
    }
}

package business.model.exceptions;

/**
 * Exception that is thrown when the password is not valid
 */
public class InvalidPasswordException extends BusinessStoppingException{

    /**
     * Constructor of InvalidPasswordException
     * and puts the message "The new password has an invalid format."
     */
    public InvalidPasswordException() {
        super("The new password has an invalid format.");
    }
}

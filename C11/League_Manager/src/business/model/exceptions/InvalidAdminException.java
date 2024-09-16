package business.model.exceptions;

/**
 * Exception that is thrown when the admin password is not valid
 */
public class InvalidAdminException extends BusinessStoppingException {

    /**
     * Constructor of InvalidAdminException
     * and puts the message "The admin password is not valid."
     */
    public InvalidAdminException() {
        super("The admin password is not valid.");
    }
}

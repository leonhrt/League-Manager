package business.model.exceptions;


/**
 * Exception that is thrown when the email is not correct
 */
public class EmailNotCorrectException extends BusinessStoppingException{
    /**
     * Constructor of EmailNotCorrectException
     * and puts the message "The email is not correct."
     */
    public EmailNotCorrectException() {
        super("The email is not correct.");
    }
}

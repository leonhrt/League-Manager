package business.model.exceptions;


/**
 * Exception that is thrown when there is already a user with the same DNI
 */
public class ExistsUserSameDniException extends BusinessStoppingException {

    /**
     * Constructor of ExistsUserSameDniException
     * and puts the message "There is already a user with the same DNI."
     */
    public ExistsUserSameDniException() {
        super("There is already a user with the same DNI.");
    }
}


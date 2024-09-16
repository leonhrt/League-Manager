package business.model.exceptions;

/**
 * Exception that is thrown when the DNI is not correct
 */
public class DniNotCorrectException extends BusinessStoppingException {

    /**
     * Constructor of DniNotCorrectException
     * and puts the message "The DNI is not correct."
     */
    public DniNotCorrectException() {
        super("The DNI is not correct.");
    }
}

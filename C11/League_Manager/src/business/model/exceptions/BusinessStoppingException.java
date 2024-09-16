package business.model.exceptions;

/**
 * Class that manages the exceptions that stop the business
 */
public class BusinessStoppingException extends Exception{

    /**
     * Constructor of BusinessStoppingException
     * @param message message
     */
    public BusinessStoppingException(String message) { super(message); }
}

package business.model.exceptions;

/**
 * This exception is thrown when the format of the file is not expected.
 */
public class FormatNotExpectedException extends BusinessStoppingException{
    /**
     * Constructor of BusinessStoppingException
     *
     */
    public FormatNotExpectedException() {
        super("The format of the file is not valid, plase check it.");
    }
}

package business.model.exceptions;

public class InvalidChangePassword extends BusinessStoppingException{
    /**
     * Constructor of BusinessStoppingException
     * and puts the message "Invalid email or dni, please try again."
     */
    public InvalidChangePassword() {
        super("Invalid email or dni, please try again.");
    }
}

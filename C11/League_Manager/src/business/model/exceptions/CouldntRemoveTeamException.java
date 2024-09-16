package business.model.exceptions;

public class CouldntRemoveTeamException extends BusinessStoppingException{
    /**
     * Constructor of BusinessStoppingException
     *
     * @param message message
     */
    public CouldntRemoveTeamException() {
        super("Sorry we couldn't remove the team.");
    }
}

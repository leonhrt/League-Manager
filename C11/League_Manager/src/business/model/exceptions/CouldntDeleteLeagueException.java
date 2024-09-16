package business.model.exceptions;

public class CouldntDeleteLeagueException extends BusinessStoppingException{
    /**
     * Constructor of BusinessStoppingException
     *
     */
    public CouldntDeleteLeagueException() {
        super("Sorry but we couldn't delete the league from the database.");
    }
}

package business.model.exceptions;

/**
 * Exception that is thrown when the league name already exists
 */
public class LeagueNameAlreadyExists extends BusinessStoppingException{

    /**
     * Constructor of LeagueNameAlreadyExists
     * and puts the message "El nom de la lliga ja existeix!"
     */
    public LeagueNameAlreadyExists() {
        super("The name of the league already exists.");
    }
}

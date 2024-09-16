package business.model.exceptions;

public class DateOrHourInvalidException extends BusinessStoppingException {
    public DateOrHourInvalidException() { super("The date or the hour is invalid, please check."); }
}

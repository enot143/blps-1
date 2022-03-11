package coursera.exceptions;

public class EmailExistsException extends Throwable{
    String message;
    public EmailExistsException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

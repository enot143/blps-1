package coursera.exceptions;

public class TestException extends Throwable {
    String message;
    public TestException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

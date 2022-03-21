package coursera.exceptions;

import org.springframework.http.HttpStatus;

public class TestException extends Throwable {
    String message;
    HttpStatus status;
    public TestException(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}

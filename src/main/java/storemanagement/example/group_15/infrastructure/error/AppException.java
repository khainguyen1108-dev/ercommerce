package storemanagement.example.group_15.infrastructure.error;

import org.springframework.http.HttpStatus;

public class AppException extends RuntimeException {
    private final HttpStatus code;
    private final String message;

    public AppException(HttpStatus code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public HttpStatus getCode() {
        return code;
    }

}

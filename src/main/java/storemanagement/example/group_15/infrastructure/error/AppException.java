package storemanagement.example.group_15.infrastructure.error;

import org.springframework.http.HttpStatus;

public class AppException extends RuntimeException {
    private HttpStatus code;

    public AppException(HttpStatus code, String message) {
        super(message);
        this.code = code;
    }

    public HttpStatus getCode() {
        return code;
    }

}

package storemanagement.example.group_15.infrastructure.error;

public class AppException extends RuntimeException{
    private  String code;
    public AppException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}

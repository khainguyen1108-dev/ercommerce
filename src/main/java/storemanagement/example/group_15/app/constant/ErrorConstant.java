package storemanagement.example.group_15.app.constant;

public class ErrorConstant {
    // HTTP Status Codes
    public static final int STATUS_BAD_REQUEST = 400;
    public static final int STATUS_UNAUTHORIZED = 401;
    public static final int STATUS_FORBIDDEN = 403;
    public static final int STATUS_NOT_FOUND = 404;
    public static final int STATUS_CONFLICT = 409;
    public static final int STATUS_INTERNAL_SERVER_ERROR = 500;

    // Error Messages
    public static final String ERR_INVALID_REQUEST = "Invalid request data";
    public static final String ERR_UNAUTHORIZED = "Unauthorized access";
    public static final String ERR_FORBIDDEN = "Access forbidden";
    public static final String ERR_NOT_FOUND = "Resource not found";
    public static final String ERR_USER_NOT_FOUND = "User not found";
    public static final String ERR_USERNAME_EXISTS = "Username already exists";
    public static final String ERR_EMAIL_EXISTS = "Email already exists";
    public static final String ERR_INVALID_CREDENTIALS = "Invalid username or password";
    public static final String ERR_SERVER_ERROR = "Internal server error";
}

package storemanagement.example.group_15.infrastructure.helper;

import org.springframework.http.HttpStatus;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import storemanagement.example.group_15.infrastructure.error.AppException;

public class AuthHelper {
  public static Long getUserIdFromRequest(HttpServletRequest request) {
    try {
      Claims claims = (Claims) request.getAttribute("claims");
      String sub = (String) claims.get("sub");
      return Long.parseLong(sub);
    } catch (Exception e) {
      throw new AppException(HttpStatus.UNAUTHORIZED, "Unauthorized");
    }
  }
}

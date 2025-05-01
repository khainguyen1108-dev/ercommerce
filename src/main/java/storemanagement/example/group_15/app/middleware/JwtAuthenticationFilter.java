package storemanagement.example.group_15.app.middleware;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import storemanagement.example.group_15.app.dto.response.common.ApiResponse;
import storemanagement.example.group_15.domain.auth.service.JwtService;

import java.io.IOException;


@Component
public class JwtAuthenticationFilter implements Filter {
    private static final String COOKIE = "Cookie";
    @Value("${jwt.secret-key}")
    private String SECRET_KEY;
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;
    private static String accessToken;
    private static String refreshToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        JwtAuthenticationFilter.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        JwtAuthenticationFilter.refreshToken = refreshToken;
    }

    @Autowired
    public JwtAuthenticationFilter(JwtService jwtService, ObjectMapper objectMapper) {
        this.jwtService = jwtService;
        this.objectMapper = objectMapper;
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        final Cookie[] cookies = httpRequest.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    this.setRefreshToken(cookie.getValue());
                } else if ("accessToken".equals(cookie.getName())) {
                    this.setAccessToken(cookie.getValue());
                }
            }
        }
        if (accessToken != null && jwtService.validateToken(accessToken)) {
            Claims claims = this.jwtService.getAllClaimsFromToken(accessToken);
            httpRequest.setAttribute("claims", claims);
            chain.doFilter(request, response);
            return;
        }
        if (refreshToken != null && jwtService.validateToken(refreshToken)) {
            try {
                String newAccessToken = jwtService.refreshAccessToken(refreshToken);
                if (newAccessToken != null) {
                    Cookie newAccessTokenCookie = new Cookie("access_token", newAccessToken);
                    newAccessTokenCookie.setHttpOnly(true);
                    newAccessTokenCookie.setPath("/");
                    newAccessTokenCookie.setMaxAge(10 * 60 * 60); // 10 giờ
                    httpResponse.addCookie(newAccessTokenCookie);
                    this.setAccessToken(newAccessToken);
                    Claims claims = this.jwtService.getAllClaimsFromToken(accessToken);
                    httpRequest.setAttribute("claims", claims);
                    chain.doFilter(request, response);
                    return;
                }
            } catch (Exception e) {
                sendErrorResponse(httpResponse, HttpStatus.UNAUTHORIZED.value(), "Invalid refresh token");
                return;
            }
        }
        this.sendErrorResponse(httpResponse, HttpStatus.UNAUTHORIZED.value(), "UNAUTHORIZED");
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ApiResponse<?> apiResponse = ApiResponse.error(message, status);
        objectMapper.writeValue(response.getOutputStream(), apiResponse);
    }
}



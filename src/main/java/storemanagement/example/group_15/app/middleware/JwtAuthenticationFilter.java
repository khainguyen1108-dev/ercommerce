package storemanagement.example.group_15.app.middleware;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import storemanagement.example.group_15.app.dto.response.common.ApiResponse;
import storemanagement.example.group_15.domain.auth.service.JwtService;

import java.io.IOException;
import java.util.Map;


@Component
public class JwtAuthenticationFilter implements Filter {
    private static final String COOKIE = "Cookie";
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;
    private String accessToken;
    private String refreshToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
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
                if ("refresh_token".equals(cookie.getName())) {
                    this.setRefreshToken(cookie.getValue());
                } else if ("access_token".equals(cookie.getName())) {
                    this.setAccessToken(cookie.getValue());
                }
            }
        }
        
        this.sendErrorResponse(httpResponse, HttpStatus.UNAUTHORIZED.value(), "UNAUTHORIZED");

    }


    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Sử dụng ApiResponse để chuẩn hóa response
        ApiResponse<?> apiResponse = ApiResponse.error(message, status);

        objectMapper.writeValue(response.getOutputStream(), apiResponse);
    }

}



package storemanagement.example.group_15.app.api;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import storemanagement.example.group_15.app.constant.SuccessConstant;
import storemanagement.example.group_15.app.dto.request.auth.AuthLoginRequestDTO;
import storemanagement.example.group_15.app.dto.response.auth.AuthLoginResponseDTO;
import storemanagement.example.group_15.app.dto.response.common.ApiResponse;
import storemanagement.example.group_15.domain.auth.service.AuthService;
import storemanagement.example.group_15.domain.auth.service.JwtService;

import java.util.Map;

@RestController
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private AuthService authService;
    @Autowired
    private JwtService jwtService;

    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponse<AuthLoginResponseDTO>> login(@RequestBody @Valid AuthLoginRequestDTO user, HttpServletResponse response) {
        try {
            Map<String, String> output = this.jwtService.generateTokens(user.getName());
            AuthLoginResponseDTO out = new AuthLoginResponseDTO(output.get("accessToken"), output.get("refreshToken"));
            Cookie accessTokenCookie = new Cookie("accessToken", output.get("accessToken"));
            accessTokenCookie.setHttpOnly(true);  // Không cho phép JavaScript truy cập vào cookie
            accessTokenCookie.setSecure(false);    // Chỉ gửi cookie qua kết nối HTTPS
            accessTokenCookie.setPath("/");      // Cookie có hiệu lực cho toàn bộ ứng dụng
            accessTokenCookie.setMaxAge((int) this.jwtService.getACCESS_TOKEN_VALIDITY());
            response.addCookie(accessTokenCookie);
//
            Cookie refreshTokenCookie = new Cookie("refreshToken", output.get("refreshToken"));
            refreshTokenCookie.setHttpOnly(true);  // Không cho phép JavaScript truy cập vào cookie
            refreshTokenCookie.setSecure(false);    // Chỉ gửi cookie qua kết nối HTTPS
            refreshTokenCookie.setPath("/");      // Cookie có hiệu lực cho toàn bộ ứng dụng
            refreshTokenCookie.setMaxAge((int) this.jwtService.getREFRESH_TOKEN_VALIDITY());
            response.addCookie(refreshTokenCookie);
            return ResponseEntity.status(SuccessConstant.CREATED)
                    .body(ApiResponse.success(SuccessConstant.SUCCESS, out, 201));

        } catch (Exception e) {
            log.error("login", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
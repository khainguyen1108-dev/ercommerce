package storemanagement.example.group_15.domain.auth.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import storemanagement.example.group_15.app.dto.request.auth.AuthLoginRequestDTO;
import storemanagement.example.group_15.app.dto.request.auth.AuthRegisterRequestDTO;
import storemanagement.example.group_15.app.dto.response.auth.AuthResponseDTO;
import storemanagement.example.group_15.domain.auth.entity.AuthEntity;
import storemanagement.example.group_15.domain.auth.repository.AuthRepository;
import storemanagement.example.group_15.infrastructure.error.AppException;

import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private AuthRepository authRepository;
    @Autowired
    private JwtService jwtService;

    public AuthResponseDTO login(AuthLoginRequestDTO input, HttpServletResponse response) {
        Optional<AuthEntity> existed = this.authRepository.findByEmail(input.getEmail());
        if (existed.isEmpty()) {
            throw new AppException(HttpStatus.BAD_REQUEST, "email.not_found");
        }
        if (!existed.get().getPassword().trim().equals(input.getPassword().trim())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "password.invalid");
        }
        Map<String, String> output = this.jwtService.generateTokens(existed.get().getId().toString());
        AuthResponseDTO out = new AuthResponseDTO(output.get("accessToken"), output.get("refreshToken"));
        Cookie accessTokenCookie = new Cookie("accessToken", output.get("accessToken"));
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(false);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge((int) this.jwtService.getACCESS_TOKEN_VALIDITY());
        response.addCookie(accessTokenCookie);
        Cookie refreshTokenCookie = new Cookie("refreshToken", output.get("refreshToken"));
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge((int) this.jwtService.getREFRESH_TOKEN_VALIDITY());
        response.addCookie(refreshTokenCookie);
        return out;
    }
//    public AuthResponseDTO register(AuthRegisterRequestDTO input, HttpServletResponse response){
//        AuthEntity
//    }
}

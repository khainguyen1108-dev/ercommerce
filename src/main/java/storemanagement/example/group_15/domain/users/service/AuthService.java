package storemanagement.example.group_15.domain.users.service;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import storemanagement.example.group_15.app.dto.request.auth.AuthLoginRequestDTO;
import storemanagement.example.group_15.app.dto.request.auth.AuthRegisterRequestDTO;
import storemanagement.example.group_15.app.dto.response.auth.AuthResponseDTO;
import storemanagement.example.group_15.domain.rules.entity.RuleEntity;
import storemanagement.example.group_15.domain.rules.repository.RuleRepository;
import storemanagement.example.group_15.domain.users.entity.AuthEntity;
import storemanagement.example.group_15.domain.users.repository.AuthRepository;
import storemanagement.example.group_15.infrastructure.error.AppException;
import storemanagement.example.group_15.infrastructure.helper.AuthHelper;
import storemanagement.example.group_15.infrastructure.helper.OtpEmailHelper;
import storemanagement.example.group_15.infrastructure.helper.PasswordHelper;
import storemanagement.example.group_15.infrastructure.helper.RedisHelper;

import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class AuthService {
    @Autowired
    private RuleRepository ruleRepository;
    @Autowired
    private AuthRepository authRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private OtpEmailHelper otpEmailHelper;
    @Autowired
    private RedisHelper redisHelper;

    public AuthResponseDTO login(AuthLoginRequestDTO input, HttpServletResponse response) {
        Optional<AuthEntity> existed = this.authRepository.findByEmail(input.getEmail());
        if (existed.isEmpty()) {
            throw new AppException(HttpStatus.BAD_REQUEST, "email.not_found");
        }
        // if (PasswordHelper.verifyPassword(input.getPassword(),
        // existed.get().getPassword())) {
        // throw new AppException(HttpStatus.BAD_REQUEST, "password.invalid");
        // }
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

    public AuthEntity getUser(Long id) {
        Optional<AuthEntity> output = this.authRepository.findById(id);
        if (output.isEmpty()) {
            throw new AppException(HttpStatus.BAD_REQUEST, "user_id.invalid");
        }
        return output.get();
    }

    public AuthResponseDTO register(AuthRegisterRequestDTO input, HttpServletResponse response) {
        Optional<AuthEntity> existed = this.authRepository.findByEmail(input.getEmail());
        if (existed.isPresent()) {
            throw new AppException(HttpStatus.BAD_REQUEST, "email.existed");
        }
        Optional<RuleEntity> rule = this.ruleRepository.findByName("user");
        if (rule.isEmpty()) {
            throw new AppException(HttpStatus.BAD_REQUEST, "something error when apply role for user");
        }
        AuthEntity user = new AuthEntity();
        user.setEmail(input.getEmail());
        user.setName(input.getName());
        user.setRole(rule.get());
        String hashPassword = PasswordHelper.hashPassword(input.getPassword());
        user.setPassword(hashPassword);
        this.authRepository.save(user);
        Map<String, String> output = this.jwtService.generateTokens(user.getId().toString());
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
        this.sendOtp(user.getId());
        return out;
    }

    public Object verify(String otp, Long id) {
        Optional<AuthEntity> user = this.authRepository.findById(id);
        if (user.isEmpty()) {
            throw new AppException(HttpStatus.BAD_REQUEST, "user_id.not_found");
        }
        Object otp_validate = this.redisHelper.get(user.get().getEmail());
        if (otp_validate.equals(otp)) {
            user.get().setIsVerify(true);
            this.authRepository.save(user.get());
            return "success";
        }
        throw new AppException(HttpStatus.BAD_REQUEST, "otp.invalid");
    }

    public Object sendOtp(Long id) {
        Optional<AuthEntity> user = this.authRepository.findById(id);
        if (user.isEmpty()) {
            throw new AppException(HttpStatus.BAD_REQUEST, "user_id.not_found");
        }
        if (user.get().getIsVerify().booleanValue()) {
            throw new AppException(HttpStatus.BAD_REQUEST, "user_id.authenticated");
        }
        String otp = AuthHelper.generateOTP();

        this.redisHelper.setWithExpiration(user.get().getEmail().trim(), otp, 1, TimeUnit.MINUTES);
        otpEmailHelper.sendOtpEmail(user.get().getEmail().trim(), user.get().getName(), otp);
        return "success";
    }
}

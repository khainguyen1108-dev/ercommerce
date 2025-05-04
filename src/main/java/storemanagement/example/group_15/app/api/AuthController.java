package storemanagement.example.group_15.app.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import storemanagement.example.group_15.app.constant.SuccessConstant;
import storemanagement.example.group_15.app.dto.request.auth.AuthLoginRequestDTO;
import storemanagement.example.group_15.app.dto.request.auth.AuthRegisterRequestDTO;
import storemanagement.example.group_15.app.dto.response.auth.AuthResponseDTO;
import storemanagement.example.group_15.app.dto.response.common.ApiResponse;
import storemanagement.example.group_15.domain.users.dto.UserDto;
import storemanagement.example.group_15.domain.users.entity.AuthEntity;
import storemanagement.example.group_15.domain.users.repository.AuthRepository;
import storemanagement.example.group_15.domain.users.service.AuthService;
import storemanagement.example.group_15.domain.users.service.JwtService;
import storemanagement.example.group_15.infrastructure.helper.AuthHelper;
import storemanagement.example.group_15.infrastructure.helper.RedisHelper;

import java.util.Map;
import java.util.Optional;

@RestController
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private AuthService authService;
    @Autowired
    private JwtService jwtService;

    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> login(@RequestBody @Valid AuthLoginRequestDTO user, HttpServletResponse response) {
        try {
            AuthResponseDTO output = this.authService.login(user, response);
            return ResponseEntity.status(SuccessConstant.CREATED)
                    .body(ApiResponse.success(SuccessConstant.SUCCESS, output, 201));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/users/getUser")
    public UserDto getUsers(HttpServletRequest request){
        try{
            Claims claims = (Claims) request.getAttribute("claims");
            String sub = (String) claims.get("sub");
            return this.authService.getUser(Long.parseLong(sub));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @PostMapping("/auth/register")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> register(@RequestBody @Valid AuthRegisterRequestDTO user, HttpServletResponse response) {
        try {
            AuthResponseDTO output = this.authService.register(user, response);
            return ResponseEntity.status(SuccessConstant.CREATED)
                    .body(ApiResponse.success(SuccessConstant.SUCCESS, output, 201));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @PostMapping("/auth/sendOtp")
    public ResponseEntity<ApiResponse<Object>> sendOtp( HttpServletRequest request) {
        try {
            Long id = AuthHelper.getUserIdFromRequest(request);
            Object output = this.authService.sendOtp(id);
            return ResponseEntity.status(SuccessConstant.CREATED)
                    .body(ApiResponse.success(SuccessConstant.SUCCESS, output, 201));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @PostMapping("/auth/verify")
    public ResponseEntity<ApiResponse<Object>> verify(@RequestBody Map<String,String > otp, HttpServletRequest request) {
        try {
            Long id = AuthHelper.getUserIdFromRequest(request);
            String otp_verify = otp.get("otp");
            Object output = this.authService.verify(otp_verify,id);
            return ResponseEntity.status(SuccessConstant.CREATED)
                    .body(ApiResponse.success(SuccessConstant.SUCCESS, output, 201));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @PostMapping("/auth/forgot-password")
    public ResponseEntity<ApiResponse<Object>> forgotPassword(@RequestBody Map<String, String> email){
        try {
            String input = email.get("email");
            Object output = this.authService.forgotPassword(input);
            return ResponseEntity.status(SuccessConstant.CREATED)
                    .body(ApiResponse.success(SuccessConstant.SUCCESS, output, 201));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @PostMapping("/auth/change-password")
    public ResponseEntity<ApiResponse<Object>> resetPassword(@RequestBody Map<String, String > reset, HttpServletRequest request){
        try {
            String current_password = reset.get("current_password");
            String new_password = reset.get("new_password");
            Long id = AuthHelper.getUserIdFromRequest(request);
            Object output = this.authService.changePassword(current_password, new_password, id);
            return ResponseEntity.status(SuccessConstant.OK)
                    .body(ApiResponse.success(SuccessConstant.SUCCESS, output, 200));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @PostMapping("/auth/role")
    public ResponseEntity<ApiResponse<Object>> role(@RequestBody Map<String, String > role, HttpServletRequest request){
        try {
            Long role_id = Long.parseLong(role.get("role_id"));
            Long id = AuthHelper.getUserIdFromRequest(request);
            Object output = this.authService.role(role_id, id);
            return ResponseEntity.status(SuccessConstant.OK)
                    .body(ApiResponse.success(SuccessConstant.SUCCESS, output, 200));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
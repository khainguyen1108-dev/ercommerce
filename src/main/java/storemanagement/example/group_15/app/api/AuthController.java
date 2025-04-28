package storemanagement.example.group_15.app.api;

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
import storemanagement.example.group_15.domain.users.service.AuthService;

@RestController
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private AuthService authService;

    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponse<AuthLoginResponseDTO>> login( @RequestBody @Valid AuthLoginRequestDTO user) {
        try {
            AuthLoginResponseDTO output = this.authService.login(user);
            return ResponseEntity.status(SuccessConstant.CREATED)
                    .body(ApiResponse.success(SuccessConstant.SUCCESS, output, 201));
        } catch (Exception e) {
            log.error("login",e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
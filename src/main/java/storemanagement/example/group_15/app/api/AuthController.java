package storemanagement.example.group_15.app.api;

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
import storemanagement.example.group_15.app.dto.request.auth.AuthRegisterRequestDTO;
import storemanagement.example.group_15.app.dto.response.auth.AuthResponseDTO;
import storemanagement.example.group_15.app.dto.response.common.ApiResponse;
import storemanagement.example.group_15.domain.auth.service.AuthService;
import storemanagement.example.group_15.domain.auth.service.JwtService;

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
            log.error("login", e.getMessage());
            throw new RuntimeException(e);
        }
    }

//    @PostMapping("/auth/register")
//    public ResponseEntity<ApiResponse<AuthResponseDTO>> register(@RequestBody @Valid AuthRegisterRequestDTO user, HttpServletResponse response) {
//        try {
//            AuthRegisterRequestDTO output = this.authService.register(user, response);
//            return ResponseEntity.status(SuccessConstant.CREATED)
//                    .body(ApiResponse.success(SuccessConstant.SUCCESS, output, 201));
//        } catch (Exception e) {
//            log.error("register", e.getMessage());
//            throw new RuntimeException(e);
//        }
//    }
}
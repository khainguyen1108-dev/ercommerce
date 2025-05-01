package storemanagement.example.group_15.app.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import storemanagement.example.group_15.app.constant.SuccessConstant;
import storemanagement.example.group_15.app.dto.request.user.UserResponseDTO;
import storemanagement.example.group_15.app.dto.response.common.ApiResponse;

@RestController
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/users/get-all")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getAll() {
        try {
            return ResponseEntity.status(SuccessConstant.CREATED)
                    .body(ApiResponse.error(SuccessConstant.SUCCESS, 201));
        } catch (Exception e) {
            log.error("get-all", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

package storemanagement.example.group_15.app.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import storemanagement.example.group_15.app.constant.SuccessConstant;
import storemanagement.example.group_15.app.dto.request.user.UserUpdateDTO;
import storemanagement.example.group_15.app.dto.response.common.ApiResponse;
import storemanagement.example.group_15.app.dto.response.user.UserResponseDTO;
import storemanagement.example.group_15.domain.users.service.UserService;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/users/get-all")
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getAll() {
        try {
            List<UserResponseDTO> users = userService.getAllUsers();
            return ResponseEntity.status(SuccessConstant.OK)
                    .body(ApiResponse.success(SuccessConstant.SUCCESS, users, 201));
        } catch (Exception e) {
            log.error("get-all", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUserById(@PathVariable Long id) {
        try {
            UserResponseDTO users = userService.getUserById(id);
            return ResponseEntity.status(SuccessConstant.OK)
                    .body(ApiResponse.success(SuccessConstant.SUCCESS, users, 201));
        } catch (Exception e) {
            log.error("get-all", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateUserById(
            @PathVariable Long id,
            @RequestBody UserUpdateDTO updateDTO) {
        try {
            UserResponseDTO updatedUser = userService.updateUser(id, updateDTO);
            return ResponseEntity.status(SuccessConstant.OK)
                    .body(ApiResponse.success(SuccessConstant.SUCCESS, updatedUser, 200));
        } catch (Exception e) {
            log.error("update", e.getMessage());
            throw new RuntimeException(e);
        }
    }

}

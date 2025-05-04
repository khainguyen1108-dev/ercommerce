package storemanagement.example.group_15.domain.users.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String address;
    private String phone;
    private LocalDateTime dob;
    private Boolean isBuy;
    private Boolean isVerify;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long roleId; // Thay vì trả nguyên đối tượng RoleEntity
}

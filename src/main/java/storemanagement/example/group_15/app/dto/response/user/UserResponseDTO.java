package storemanagement.example.group_15.app.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
  private Long id;
  private String email;
  private String name;
  private String phone;
  private String address;
  private Boolean isBuy;
}

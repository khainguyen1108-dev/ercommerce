package storemanagement.example.group_15.app.dto.request.user;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserUpdateDTO {
  private String name;
  private String email;
  private String password;
  private LocalDateTime dob;
  private String address;
  private String phone;
}

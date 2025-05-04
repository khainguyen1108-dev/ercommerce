package storemanagement.example.group_15.domain.users.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import storemanagement.example.group_15.app.dto.request.user.UserUpdateDTO;
import storemanagement.example.group_15.app.dto.response.user.UserResponseDTO;
import storemanagement.example.group_15.domain.users.entity.AuthEntity;
import storemanagement.example.group_15.domain.users.repository.AuthRepository;
import storemanagement.example.group_15.infrastructure.error.AppException;

@Service
public class UserService {
  @Autowired
  private AuthRepository authRepository;

  public List<UserResponseDTO> getAllUsers() {
    List<AuthEntity> users = authRepository.findAll();
    return users.stream().map(this::convertToResponseDTO).toList();
  }

  public UserResponseDTO getUserById(Long id) {
    AuthEntity user = authRepository.findById(id)
        .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "User not found"));
    return convertToResponseDTO(user);
  }

  public UserResponseDTO updateUser(Long id, UserUpdateDTO dto) {
    AuthEntity user = authRepository.findById(id)
        .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "User not found"));

    if (dto.getName() != null) {
      user.setName(dto.getName());
    }
    if (dto.getDob() != null) {
      user.setDob(dto.getDob());
    }
    if (dto.getAddress() != null) {
      user.setAddress(dto.getAddress());
    }
    if (dto.getPhone() != null) {
      user.setPhone(dto.getPhone());
    }

    authRepository.save(user);
    return convertToResponseDTO(user);
  }

  public UserResponseDTO convertToResponseDTO(AuthEntity user) {
    UserResponseDTO responseDTO = new UserResponseDTO();
    responseDTO.setId(user.getId());
    responseDTO.setEmail(user.getEmail());
    responseDTO.setName(user.getName());
    responseDTO.setPhone(user.getPhone());
    responseDTO.setAddress(user.getAddress());
    responseDTO.setIsBuy(user.getIsBuy());

    return responseDTO;
  }
}

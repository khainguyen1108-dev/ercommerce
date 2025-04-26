package storemanagement.example.group_15.domain.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import storemanagement.example.group_15.app.dto.request.auth.AuthLoginRequestDTO;
import storemanagement.example.group_15.app.dto.response.auth.AuthLoginResponseDTO;
import storemanagement.example.group_15.domain.users.constant.Role;
import storemanagement.example.group_15.domain.users.entity.AuthEntity;
import storemanagement.example.group_15.domain.users.repository.AuthRepository;
import storemanagement.example.group_15.infrastructure.error.AppException;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private  AuthRepository authRepository;
    public AuthLoginResponseDTO login(AuthLoginRequestDTO input) {
        //check email existed
        Optional<AuthEntity> existed = this.authRepository.findByEmail(input.getEmail());
        if (existed.isPresent()){
            throw new AppException("EMAIL_EXISTS", "Email already exists");
        }
        AuthLoginResponseDTO output = new AuthLoginResponseDTO("123","456",Role.ADMIN);
//        AuthEntity user_entity = new AuthEntity(input.getName(),input.getPassword(),input.getEmail(),input.getRole());
//        this.authRepository.save(user_entity);
        return output;
    }
}

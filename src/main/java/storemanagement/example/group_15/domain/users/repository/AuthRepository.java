package storemanagement.example.group_15.domain.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import storemanagement.example.group_15.domain.users.entity.AuthEntity;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<AuthEntity, Long> {
    Optional<AuthEntity> findByEmail(String email);
}

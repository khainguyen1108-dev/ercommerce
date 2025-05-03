package storemanagement.example.group_15.domain.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import io.lettuce.core.dynamic.annotation.Param;
import storemanagement.example.group_15.domain.users.entity.AuthEntity;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<AuthEntity, Long> {
    Optional<AuthEntity> findByEmail(String email);

    @Modifying
    @Query("UPDATE AuthEntity a SET a.isBuy = :isBuy WHERE a.id = :id")
    void updateIsBuyById(@Param("id") Long id, @Param("isBuy") Boolean isBuy);
}

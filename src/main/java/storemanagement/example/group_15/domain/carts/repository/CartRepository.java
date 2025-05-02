package storemanagement.example.group_15.domain.carts.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import storemanagement.example.group_15.domain.carts.entity.CartEntity;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Long> {
  Optional<CartEntity> findByCustomerId(Long customerId);
}

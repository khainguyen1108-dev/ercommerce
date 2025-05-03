package storemanagement.example.group_15.domain.favorites.repository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import storemanagement.example.group_15.domain.favorites.entity.FavoritesEntity;

import java.util.List;
import java.util.Optional;

public interface FavoritesRepository extends JpaRepository<FavoritesEntity, Long> {
    boolean existsByCustomerIdAndProductId(Long customerId, Long productId);
    List<FavoritesEntity> findAllByCustomerId(Long customerId);
    Optional<FavoritesEntity> findByProductIdAndCustomerId(Long productId, Long customerId);
}

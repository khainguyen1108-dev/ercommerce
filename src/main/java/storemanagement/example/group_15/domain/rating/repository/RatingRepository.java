package storemanagement.example.group_15.domain.rating.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import storemanagement.example.group_15.domain.rating.entity.RatingEntity;

import java.util.List;

public interface RatingRepository extends JpaRepository<RatingEntity, Long> {
    @Query(value = "SELECT * FROM rating WHERE product_id = :productId", nativeQuery = true)
    List<RatingEntity> findByProductId(@Param("productId") Long productId);
}

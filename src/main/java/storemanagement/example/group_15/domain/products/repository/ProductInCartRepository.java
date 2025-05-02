package storemanagement.example.group_15.domain.products.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import storemanagement.example.group_15.domain.products.entity.ProductInCartEntity;

@Repository
public interface ProductInCartRepository extends JpaRepository<ProductInCartEntity, Long> {
  Optional<ProductInCartEntity> findByCartIdAndProductId(Long cartId, Long productId);

  List<ProductInCartEntity> findAllByCartId(Long cartId);

  void deleteAllByCartId(Long cartId);

}

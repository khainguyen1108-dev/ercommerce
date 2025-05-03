package storemanagement.example.group_15.domain.orders.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.lettuce.core.dynamic.annotation.Param;
import storemanagement.example.group_15.domain.orders.entity.OrderEntity;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
        List<OrderEntity> findAllByCustomerId(Long customerId);

        Page<OrderEntity> findAllByCustomerId(Long customerId, org.springframework.data.domain.Pageable pageable);

        Page<OrderEntity> findByCustomerIdAndCreatedAtAfter(Long customerId, LocalDateTime startDate,
                        Pageable pageable);

        Page<OrderEntity> findByCustomerIdAndCreatedAtBefore(Long customerId, LocalDateTime endDate, Pageable pageable);

        Page<OrderEntity> findByCustomerIdAndCreatedAtBetween(Long customerId, LocalDateTime startDate,
                        LocalDateTime endDate,
                        Pageable pageable);

        @Query("SELECT SUM(o.totalPayment) FROM OrderEntity o WHERE o.createdAt BETWEEN :startDate AND :endDate")
        BigDecimal sumTotalPaymentBetween(@Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);

        long countByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

        @Query("SELECT o.products FROM OrderEntity o WHERE o.createdAt BETWEEN :startDate AND :endDate")
        List<String> findProductsOrderBetween(@Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);

}

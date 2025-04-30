package storemanagement.example.group_15.domain.products.entity;

import jakarta.persistence.*;
import lombok.*;
import storemanagement.example.group_15.domain.carts.entity.CartEntity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "products_in_cart")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductInCartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cart_id", nullable = false)
    private CartEntity cart;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @CreationTimestamp
    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;

    @UpdateTimestamp
    @Column(name = "update_time")
    private LocalDateTime updateTime;
}

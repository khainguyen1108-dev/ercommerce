package storemanagement.example.group_15.domain.favorites.entity;

import jakarta.persistence.*;
import lombok.Data;
import storemanagement.example.group_15.domain.products.entity.ProductEntity;
import storemanagement.example.group_15.domain.users.entity.AuthEntity;

@Entity
@Table(name = "favorites", uniqueConstraints = @UniqueConstraint(columnNames = { "product_id", "customer_id" }))
@Data
public class FavoritesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private AuthEntity customer;
}

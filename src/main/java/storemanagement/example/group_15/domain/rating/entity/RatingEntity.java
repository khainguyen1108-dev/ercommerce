package storemanagement.example.group_15.domain.rating.entity;

import jakarta.persistence.*;
import lombok.*;
import storemanagement.example.group_15.domain.products.entity.ProductEntity;
import storemanagement.example.group_15.domain.users.entity.AuthEntity;

@Entity
@Table(name = "rating")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private AuthEntity customer;

    @Column(name = "stars", nullable = false)
    private Integer stars;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}

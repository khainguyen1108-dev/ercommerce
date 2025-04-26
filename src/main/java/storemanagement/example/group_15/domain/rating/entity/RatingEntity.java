package storemanagement.example.group_15.domain.rating.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "stars", nullable = false)
    private Integer stars;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}

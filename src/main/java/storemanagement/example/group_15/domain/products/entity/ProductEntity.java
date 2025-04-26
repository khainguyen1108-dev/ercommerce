package storemanagement.example.group_15.domain.products.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "inventory", nullable = false)
    private Integer inventory;

    @Column(name = "sold", nullable = false)
    private Integer sold;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "price", nullable = false, precision = 15, scale = 2)
    private BigDecimal price;

    @Column(name = "img", length = 500)
    private String img;

    @Column(name = "collection_id")
    private Long collectionId;

    @Column(name = "vendor", length = 255)
    private String vendor;
}
